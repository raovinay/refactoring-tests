package com.tidal.refactoring.playlist;

import com.tidal.refactoring.playlist.dao.PlaylistDaoBean;
import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;
import com.tidal.refactoring.playlist.exception.PlaylistException;
import com.tidal.refactoring.playlist.exception.PlaylistValidationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by raovinay on 19-06-2017.
 */
public class RefactoredPlayListBeanTest extends AbstractPlayListTest{
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
    public void happyTest() throws PlaylistValidationException {
        List<Track> trackList = getTracksForTest(2);

        PlayList mock = mock(PlayList.class);
        when(playlistDaoBean.getPlaylistByUUID(Mockito.anyString())).thenReturn(mock);
        when(mock.getNrOfTracks()).thenReturn(100);
        ArrayList dummyArray = new ArrayList();
        when(mock.addTracks(Mockito.anyList(), Mockito.anyInt())).thenReturn(dummyArray);

        List<PlayListTrack> addedTracks = playlistBusinessBean.addTracks("TEST", trackList, 5);

        assertThat(addedTracks, is(dummyArray));
    }

    @Test(expectedExceptions = PlaylistException.class)
    public void addMoreThanAllowedCapacityTest() throws PlaylistValidationException {
        List<Track> trackList = getTracksForTest(2);

        PlayList mock = mock(PlayList.class);
        when(playlistDaoBean.getPlaylistByUUID(Mockito.anyString())).thenReturn(mock);
        when(mock.getNrOfTracks()).thenReturn(500);

        playlistBusinessBean.addTracks("TEST", trackList, 5);
    }

    @Test
    public void addWithValidationExceptionTest() throws PlaylistValidationException {
        List<Track> trackList = getTracksForTest(2);

        PlayList mock = mock(PlayList.class);
        when(playlistDaoBean.getPlaylistByUUID(Mockito.anyString())).thenReturn(mock);
        when(mock.getNrOfTracks()).thenReturn(100);
        when(mock.addTracks(Mockito.anyList(), Mockito.anyInt())).thenThrow(new PlaylistValidationException());

        List<PlayListTrack> result = playlistBusinessBean.addTracks("TEST", trackList, 5);

        assertThat(result.size(), is(equalTo(0)));
    }

    @Test(expectedExceptions = PlaylistException.class)
    public void genericExceptionTest() throws PlaylistValidationException {
        List<Track> trackList = getTracksForTest(2);

        PlayList mock = mock(PlayList.class);
        when(playlistDaoBean.getPlaylistByUUID(Mockito.anyString())).thenReturn(mock);
        when(mock.getNrOfTracks()).thenReturn(100);
        when(mock.addTracks(Mockito.anyList(), Mockito.anyInt())).thenThrow(new RuntimeException());

        playlistBusinessBean.addTracks("TEST", trackList, 5);
    }

    @Test
    public void happyDelete() throws PlaylistValidationException {
        PlayList mock = mock(PlayList.class);
        when(playlistDaoBean.getPlaylistByUUID(Mockito.anyString())).thenReturn(mock);
        ArrayList<PlayListTrack> dummyArray = new ArrayList<>();
        when(mock.removeTracks(anyList())).thenReturn(dummyArray);

        List<PlayListTrack> result = playlistBusinessBean.removeTracks("TEST", Arrays.asList(1,2,3));

        assertThat(result, is(dummyArray));
    }

}
