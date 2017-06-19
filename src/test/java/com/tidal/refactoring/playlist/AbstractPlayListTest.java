package com.tidal.refactoring.playlist;

import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;

import java.util.*;

import static com.tidal.refactoring.playlist.dao.PlaylistDaoBean.getTrack;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by raovinay on 09-06-2017.
 */
public class AbstractPlayListTest {

    //my own asserts
    void assertAddedTracks(List<PlayListTrack> addedTracks, int expectedSize, int expectedIndex) {
        assertThat(addedTracks.size(), is(equalTo(expectedSize)));
        for(PlayListTrack playListTrack : addedTracks) {
            assertThat(playListTrack.getIndex(), is(equalTo(expectedIndex++)));
        }
    }

    void assertFinalPlaylist(PlayList finalPlayList, int finalTrackCount, float finalDuration) {
        assertThat(finalPlayList.getNrOfTracks(), is(equalTo(finalTrackCount)));
        assertThat(finalPlayList.getPlayListTracks().size(), is(equalTo(finalTrackCount)));
        assertThat(finalPlayList.getDuration(), is(equalTo(finalDuration)));
    }

    void assertRemovedList(List<PlayListTrack> removedTracks, int removedCount, List<Integer> indices) {
        assertThat(removedTracks.size(), is(removedCount));
        for(int i =0 ; i<removedCount;i++){
            assertThat(removedTracks.get(i).getId(), is(equalTo(indices.get(i))));
        }
    }


    //private test helper methods
    protected PlayList getDefaultPlaylist(String uuid, int nrOfTracks) {
        if(nrOfTracks==0){
            nrOfTracks=376;
        }
        PlayList trackPlayList = new PlayList(49834, "Collection of great songs", uuid);
        trackPlayList.setPlayListTracks(getPlaylistTracks(nrOfTracks, trackPlayList));
        trackPlayList.setUuid(uuid);
        return trackPlayList;
    }

    private Set<PlayListTrack> getPlaylistTracks(int nrOfTracks, PlayList playList) {

        Set<PlayListTrack> playListTracks = new HashSet<PlayListTrack>();
        for (int i = 0; i < nrOfTracks; i++) {
            PlayListTrack playListTrack = new PlayListTrack(i+1, playList, getTrack());
            playListTrack.setIndex(i);
            playListTracks.add(playListTrack);
        }

        return playListTracks;
    }

    protected List<Track> getTracksForTest(int nrOfTracks) {
        List<Track> trackList = new ArrayList<Track>();

        for(int i=0;i<nrOfTracks;i++) {
            Track track = new Track(i, "A brand new track", 50, 4);
            trackList.add(track);
        }
        return trackList;
    }
}
