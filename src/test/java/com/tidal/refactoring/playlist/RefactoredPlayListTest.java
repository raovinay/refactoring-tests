package com.tidal.refactoring.playlist;

import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;
import com.tidal.refactoring.playlist.exception.PlaylistValidationException;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by raovinay on 19-06-2017.
 */
public class RefactoredPlayListTest extends AbstractPlayListTest{

    private PlayList playList;

    @BeforeMethod
    public void init(){
        playList = getDefaultPlaylist("TEST", 50);
    }

    @Test
    public void addTracksTest() throws PlaylistValidationException {
        List<PlayListTrack> addedTracks = playList.addTracks(getTracksForTest(2), 5);

        assertFinalPlaylist(playList, 52, 9100f);
        assertAddedTracks(addedTracks, 2, 5);
    }

    @Test
    public void addTracksToTheEndTest() throws PlaylistValidationException {
        List<PlayListTrack> addedTracks = playList.addTracks(getTracksForTest(2), -1);

        assertFinalPlaylist(playList, 52, 9100f);
        assertAddedTracks(addedTracks, 2, 50);
    }

    @Test
    public void addTracksToTheEnd2Test() throws PlaylistValidationException {
        List<PlayListTrack> addedTracks = playList.addTracks(getTracksForTest(2), 500);

        assertFinalPlaylist(playList, 52, 9100f);
        assertAddedTracks(addedTracks, 2, 50);
    }


    @Test
    public void addWithValidationExceptionTest() throws PlaylistValidationException {
        List<PlayListTrack> addedTracks = playList.addTracks(getTracksForTest(2), -5);

        assertFinalPlaylist(playList, 50, 9000f);
        assertAddedTracks(addedTracks, 0, 0);
    }

    @Test
    public void removeTracksTest() throws PlaylistValidationException {
        List<PlayListTrack> removedTracks = playList.removeTracks(Arrays.asList(1,2,3));

        assertFinalPlaylist(playList, 47, 8460f);
        assertThat(removedTracks.size(), is(3));
        assertThat(removedTracks.get(0).getId(), is(equalTo(1)));
        assertThat(removedTracks.get(1).getId(), is(equalTo(2)));
    }

    @Test
    public void removeTracksRandomTest() throws PlaylistValidationException {
        List<PlayListTrack> removedTracks = playList.removeTracks(Arrays.asList(3,2,1));

        assertFinalPlaylist(playList, 47, 8460f);
        assertRemovedList(removedTracks, 3, Arrays.asList(3,2,1));
    }

    @Test
    public void removeTracksNonExistentTest() throws PlaylistValidationException {
        List<PlayListTrack> removedTracks = playList.removeTracks(Arrays.asList(501, 505));

        assertFinalPlaylist(playList, 50, 9000f);
        assertRemovedList(removedTracks, 0, new ArrayList<>());
    }
}
