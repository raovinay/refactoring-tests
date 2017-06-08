package com.tidal.refactoring.playlist;

import com.google.inject.Inject;
import com.tidal.refactoring.playlist.dao.PlaylistDaoBean;
import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;
import com.tidal.refactoring.playlist.exception.PlaylistException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.*;

import java.util.*;

import static com.tidal.refactoring.playlist.dao.PlaylistDaoBean.getTrack;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@Guice(modules = TestBusinessModule.class)
public class PlaylistBusinessBeanTest {

    @Inject
    @InjectMocks
    PlaylistBusinessBean playlistBusinessBean;

    @Mock
    PlaylistDaoBean playlistDaoBean;

    @BeforeSuite
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        Mockito.reset(playlistDaoBean);
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddTracksHapppy() throws Exception {
        List<Track> trackList = getTracksForTest(2);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid,376));
        List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks(uuid, trackList, 5);

        assertEquals(2, playListTracks.size());
        assertEquals(378, playlistDaoBean.getPlaylistByUUID(uuid).getNrOfTracks());
        assertEquals(378, playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().size());
        assertEquals(5, playListTracks.get(0).getIndex());
        assertEquals(6, playListTracks.get(1).getIndex());
        assertEquals(new Float(60*60*2+50*2), playlistDaoBean.getPlaylistByUUID(uuid).getDuration());
    }

    @Test
    public void testAddTracksLast() throws Exception {
        List<Track> trackList = getTracksForTest(2);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid,376));
        List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks(uuid, trackList, -1);

        assertEquals(2, playListTracks.size());
        assertEquals(378, playlistDaoBean.getPlaylistByUUID(uuid).getNrOfTracks());
        assertEquals(378, playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().size());
        assertEquals(376, playListTracks.get(0).getIndex());
        assertEquals(377, playListTracks.get(1).getIndex());
    }

    @Test
    public void testAddTracksLargerIndex() throws Exception {
        List<Track> trackList = getTracksForTest(2);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 376));
        List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks(uuid, trackList, 400);

        assertEquals(2, playListTracks.size());
        assertEquals(378, playlistDaoBean.getPlaylistByUUID(uuid).getNrOfTracks());
        assertEquals(378, playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().size());
        assertEquals(376, playListTracks.get(0).getIndex());
        assertEquals(377, playListTracks.get(1).getIndex());
    }

    @Test
    public void testAddToEmptyPlaylist() throws Exception {
        List<Track> trackList = getTracksForTest(2);
        when(playlistDaoBean.getPlaylistByUUID("TestUUID")).thenReturn(new PlayList());

        List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks("TestUUID", trackList, 5);

        assertEquals(2, playListTracks.size());
        assertEquals(2, playlistDaoBean.getPlaylistByUUID("TestUUID").getNrOfTracks());
        assertEquals(2, playlistDaoBean.getPlaylistByUUID("TestUUID").getPlayListTracks().size());
        assertEquals(0, playListTracks.get(0).getIndex());
        assertEquals(1, playListTracks.get(1).getIndex());
    }

    @Test
    public void testAddTrackNegativeIndex() throws Exception {
        when(playlistDaoBean.getPlaylistByUUID("TestUUID")).thenReturn(new PlayList());
        List<Track> trackList = getTracksForTest(2);

        List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks("TestUUID", trackList, -2);

        assertTrue(playListTracks.isEmpty());
    }

    @Test(expectedExceptions = PlaylistException.class)
    public void testAddToFullPlaylist() throws Exception {
        List<Track> trackList = getTracksForTest(200);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 376));

        playlistBusinessBean.addTracks(uuid, trackList, 5);
    }

    @Test
    public void testDuplicateTracks(){
        List<Track> trackList = new ArrayList<>();
        Track duplicateTrack = getTrack();
        trackList.add(duplicateTrack);
        trackList.add(duplicateTrack);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 376));

        List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks(uuid, trackList, 5);
        assertEquals(2, playListTracks.size());
        assertEquals(378, playlistDaoBean.getPlaylistByUUID(uuid).getNrOfTracks());
        assertEquals(378, playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().size());
        assertEquals(5, playListTracks.get(0).getIndex());
        assertEquals(6, playListTracks.get(1).getIndex());
    }

    //private test helper methods
    protected PlayList getDefaultPlaylist(String uuid, int nrOfTracks) {
        if(nrOfTracks==0){
            nrOfTracks=376;
        }
        PlayList trackPlayList = new PlayList();
        trackPlayList.setDeleted(false);
        trackPlayList.setDuration((float) (60 * 60 * 2));
        trackPlayList.setId(49834);
        trackPlayList.setLastUpdated(new Date());
        trackPlayList.setNrOfTracks(nrOfTracks);
        trackPlayList.setPlayListName("Collection of great songs");
        trackPlayList.setPlayListTracks(getPlaylistTracks(nrOfTracks));
        trackPlayList.setUuid(uuid);
        return trackPlayList;
    }

    private Set<PlayListTrack> getPlaylistTracks(int nrOfTracks) {

        Set<PlayListTrack> playListTracks = new HashSet<PlayListTrack>();
        for (int i = 0; i < nrOfTracks; i++) {
            PlayListTrack playListTrack = new PlayListTrack();
            playListTrack.setDateAdded(new Date());
            playListTrack.setId(i + 1);
            playListTrack.setIndex(i);
            playListTrack.setTrack(getTrack());

            playListTracks.add(playListTrack);
        }

        return playListTracks;
    }

    private List<Track> getTracksForTest(int nrOfTracks) {
        List<Track> trackList = new ArrayList<Track>();

        for(int i=0;i<nrOfTracks;i++) {
            Track track = new Track();
            track.setArtistId(4);
            track.setTitle("A brand new track");
            track.setId(i);
            track.setDuration(50);
            trackList.add(track);
        }
        return trackList;
    }
}