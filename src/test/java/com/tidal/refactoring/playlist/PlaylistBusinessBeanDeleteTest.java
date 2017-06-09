package com.tidal.refactoring.playlist;

import com.google.inject.Inject;
import com.tidal.refactoring.playlist.dao.PlaylistDaoBean;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by raovinay on 08-06-2017.
 */
public class PlaylistBusinessBeanDeleteTest extends AbstractTest{
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
    public void testDeleteTracksHappy() throws Exception {
        List<Integer> tracksToDelete = Arrays.asList(1,2, 3, 4);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 5));

        List<PlayListTrack> playListTracks = playlistBusinessBean.removeTracks(uuid, tracksToDelete);

        assertEquals(4, playListTracks.size());
        assertEquals(new Integer(1), playListTracks.get(0).getId());
        assertEquals(new Integer(2), playListTracks.get(1).getId());
        assertEquals(1, playlistDaoBean.getPlaylistByUUID(uuid).getNrOfTracks());
        assertEquals(1, playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().size());
        assertEquals(new Integer(5), playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().iterator().next().getId());
        assertEquals(new Float(60*60*2-180*4), playlistDaoBean.getPlaylistByUUID(uuid).getDuration());
    }

    @Test
    public void testDeleteTracksRandomOrder() throws Exception {
        List<Integer> tracksToDelete = Arrays.asList(4, 3, 1, 2);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 5));

        List<PlayListTrack> playListTracks = playlistBusinessBean.removeTracks(uuid, tracksToDelete);

        assertEquals(4, playListTracks.size());
        assertEquals(new Integer(4), playListTracks.get(0).getId());
        assertEquals(new Integer(3), playListTracks.get(1).getId());
        assertEquals(1, playlistDaoBean.getPlaylistByUUID(uuid).getNrOfTracks());
        assertEquals(1, playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().size());
        assertEquals(new Integer(5), playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().iterator().next().getId());
        assertEquals(new Float(60*60*2-180*4), playlistDaoBean.getPlaylistByUUID(uuid).getDuration());
    }

    @Test
    public void testDeleteTracksOutOfBoundsIndex() throws Exception {
        List<Integer> tracksToDelete = Arrays.asList(6, 1, 3);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 5));

        List<PlayListTrack> playListTracks = playlistBusinessBean.removeTracks(uuid, tracksToDelete);

        assertEquals(2, playListTracks.size());
        assertEquals(new Integer(1), playListTracks.get(0).getId());
        assertEquals(new Integer(3), playListTracks.get(1).getId());
        assertEquals(3, playlistDaoBean.getPlaylistByUUID(uuid).getNrOfTracks());
        assertEquals(3, playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().size());
        assertEquals(new Float(60*60*2-180*2), playlistDaoBean.getPlaylistByUUID(uuid).getDuration());
    }

    @Test
    public void testDeleteTracksMissingIndex() throws Exception {
        List<Integer> tracksToDelete = Arrays.asList(50, -1);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 5));

        List<PlayListTrack> playListTracks = playlistBusinessBean.removeTracks(uuid, tracksToDelete);

        assertEquals(0, playListTracks.size());
        assertEquals(5, playlistDaoBean.getPlaylistByUUID(uuid).getNrOfTracks());
        assertEquals(5, playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().size());
        assertEquals(new Float(60*60*2), playlistDaoBean.getPlaylistByUUID(uuid).getDuration());
    }


}
