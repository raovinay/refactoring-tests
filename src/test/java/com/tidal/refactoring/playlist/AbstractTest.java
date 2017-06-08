package com.tidal.refactoring.playlist;

import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;

import java.util.*;

import static com.tidal.refactoring.playlist.dao.PlaylistDaoBean.getTrack;

/**
 * Created by raovinay on 09-06-2017.
 */
public class AbstractTest {

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

    protected List<Track> getTracksForTest(int nrOfTracks) {
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
