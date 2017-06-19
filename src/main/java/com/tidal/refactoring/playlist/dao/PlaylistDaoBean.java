package com.tidal.refactoring.playlist.dao;

import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;

import java.util.*;

/**
 * Class faking the data layer, and returning fake playlists
 */
public class PlaylistDaoBean {

    private final Map<String, PlayList> playlists = new HashMap<String, PlayList>();

    public PlayList getPlaylistByUUID(String uuid) {

        PlayList playList = playlists.get(uuid);

        if (playList != null) {
            return playList;
        }

        //return default playlist
        return createPlayList(uuid);
    }

    private PlayList createPlayList(String uuid) {
        PlayList trackPlayList = new PlayList(49834, "Collection of great songs", uuid);
        trackPlayList.setPlayListTracks(getPlaylistTracks(trackPlayList));
        trackPlayList.setUuid(uuid);

        playlists.put(uuid, trackPlayList);

        return trackPlayList;
    }

    private static Set<PlayListTrack> getPlaylistTracks(PlayList playList) {

        Set<PlayListTrack> playListTracks = new HashSet<PlayListTrack>();
        for (int i = 0; i < 376; i++) {
            PlayListTrack playListTrack = new PlayListTrack(i + 1, playList, getTrack());
            playListTrack.setIndex(i);
            playListTracks.add(playListTrack);
        }

        return playListTracks;
    }

    public static Track getTrack() {
        Random randomGenerator = new Random();
        int trackNumber = randomGenerator.nextInt(15);
        Track track = new Track(trackNumber, "Track no: " + trackNumber, 60 * 3, randomGenerator.nextInt(10000));
        return track;
    }
}
