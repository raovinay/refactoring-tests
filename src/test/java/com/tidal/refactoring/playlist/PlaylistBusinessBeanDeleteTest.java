package com.tidal.refactoring.playlist;

import com.tidal.refactoring.playlist.dao.PlaylistDaoBean;
import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;


/**
 * Created by raovinay on 08-06-2017.
 */
public class PlaylistBusinessBeanDeleteTest extends AbstractPlayListTest {
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

        PlayList finalPlayList = playlistDaoBean.getPlaylistByUUID(uuid);
        assertFinalPlaylist(finalPlayList, 1, 180f);
        assertEquals(new Integer(5), playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().iterator().next().getId());

        assertEquals(4, playListTracks.size());
        assertEquals(1, playListTracks.get(0).getId().intValue());
        assertEquals(2, playListTracks.get(1).getId().intValue());
    }

    @Test
    public void testDeleteTracksRandomOrder() throws Exception {
        List<Integer> tracksToDelete = Arrays.asList(4, 3, 1, 2);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 5));

        List<PlayListTrack> playListTracks = playlistBusinessBean.removeTracks(uuid, tracksToDelete);

        PlayList finalPlayList = playlistDaoBean.getPlaylistByUUID(uuid);
        assertFinalPlaylist(finalPlayList, 1, 180f);
        assertEquals(new Integer(5), playlistDaoBean.getPlaylistByUUID(uuid).getPlayListTracks().iterator().next().getId());

        assertEquals(4, playListTracks.size());
        assertEquals(new Integer(4), playListTracks.get(0).getId());
        assertEquals(new Integer(3), playListTracks.get(1).getId());
    }

    @Test
    public void testDeleteTracksOutOfBoundsIndex() throws Exception {
        List<Integer> tracksToDelete = Arrays.asList(6, 1, 3);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 5));

        List<PlayListTrack> playListTracks = playlistBusinessBean.removeTracks(uuid, tracksToDelete);

        PlayList finalPlayList = playlistDaoBean.getPlaylistByUUID(uuid);
        assertFinalPlaylist(finalPlayList, 3, 540f);

        assertEquals(2, playListTracks.size());
        assertEquals(new Integer(1), playListTracks.get(0).getId());
        assertEquals(new Integer(3), playListTracks.get(1).getId());
    }

    @Test
    public void testDeleteTracksMissingIndex() throws Exception {
        List<Integer> tracksToDelete = Arrays.asList(50, -1);
        String uuid = UUID.randomUUID().toString();
        when(playlistDaoBean.getPlaylistByUUID(uuid)).thenReturn(getDefaultPlaylist(uuid, 5));

        List<PlayListTrack> playListTracks = playlistBusinessBean.removeTracks(uuid, tracksToDelete);

        PlayList finalPlayList = playlistDaoBean.getPlaylistByUUID(uuid);
        assertFinalPlaylist(finalPlayList, 5, 900f);

        assertEquals(0, playListTracks.size());
    }


}
