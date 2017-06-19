package com.tidal.refactoring.playlist;

import com.tidal.refactoring.playlist.dao.PlaylistDaoBean;
import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;
import com.tidal.refactoring.playlist.exception.PlaylistException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tidal.refactoring.playlist.dao.PlaylistDaoBean.getTrack;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertTrue;

public class PlaylistBusinessBeanTest extends AbstractPlayListTest {
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
        if(playlistDaoBean!=null) {
            Mockito.reset(playlistDaoBean);
        }
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }


    @Test
    public void testAddTracksHapppy() throws Exception {
        List<Track> trackList = getTracksForTest(2);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid,376));

        List<PlayListTrack> addedTracks = playlistBusinessBean.addTracks(uuid, trackList, 5);

        PlayList finalPlayList = playlistDaoBean.getPlaylistByUUID(uuid);
        assertAddedTracks(addedTracks, 2, 5);
        assertFinalPlaylist(finalPlayList, 378, 67780f);
    }


    @Test
    public void testAddTracksLast() throws Exception {
        List<Track> trackList = getTracksForTest(2);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid,376));

        List<PlayListTrack> addedTracks = playlistBusinessBean.addTracks(uuid, trackList, -1);

        PlayList finalPlayList = playlistDaoBean.getPlaylistByUUID(uuid);
        assertAddedTracks(addedTracks, 2, 376);
        assertFinalPlaylist(finalPlayList, 378, 67780f);
    }

    @Test
    public void testAddTracksLargerIndex() throws Exception {
        List<Track> trackList = getTracksForTest(2);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 376));

        List<PlayListTrack> addedTracks = playlistBusinessBean.addTracks(uuid, trackList, 400);

        PlayList finalPlayList = playlistDaoBean.getPlaylistByUUID(uuid);
        assertAddedTracks(addedTracks, 2, 376);
        assertFinalPlaylist(finalPlayList, 378, 67780f);
    }

    @Test
    public void testAddToEmptyPlaylist() throws Exception {
        List<Track> trackList = getTracksForTest(2);
        when(playlistDaoBean.getPlaylistByUUID("TestUUID")).thenReturn(new PlayList(null, null, null));

        List<PlayListTrack> addedTracks = playlistBusinessBean.addTracks("TestUUID", trackList, 5);

        PlayList finalPlayList = playlistDaoBean.getPlaylistByUUID("TestUUID");
        assertAddedTracks(addedTracks, 2, 0);
        assertFinalPlaylist(finalPlayList, 2, 100);
    }

    @Test
    public void testAddTrackNegativeIndex() throws Exception {
        when(playlistDaoBean.getPlaylistByUUID("TestUUID")).thenReturn(new PlayList(null, null, null));
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

        List<PlayListTrack> addedTracks = playlistBusinessBean.addTracks(uuid, trackList, 5);

        PlayList finalPlayList = playlistDaoBean.getPlaylistByUUID(uuid);
        assertAddedTracks(addedTracks, 2, 5);
        assertFinalPlaylist(finalPlayList, 378, 68040f);
    }

    @Test(expectedExceptions = PlaylistException.class)
    public void testExceptions() throws Exception{
        List<Track> trackList = getTracksForTest(1);
        doThrow(new RuntimeException("TEST")).when(playlistDaoBean).getPlaylistByUUID(anyString());
        playlistBusinessBean.addTracks("TEST", trackList, 5);
    }

}