package com.tidal.refactoring.playlist;

import com.google.inject.Inject; 
import com.tidal.refactoring.playlist.dao.PlaylistDaoBean;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;
import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.exception.PlaylistException;
import com.tidal.refactoring.playlist.exception.PlaylistValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PlaylistBusinessBean {

    private static final int MAX_PLAYLIST_SIZE = 500;
    private PlaylistDaoBean playlistDaoBean;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistBusinessBean.class);

    @Inject
    public PlaylistBusinessBean(PlaylistDaoBean playlistDaoBean){
        this.playlistDaoBean = playlistDaoBean;
    }

    /**
     * Add tracks to the index
     */
    List<PlayListTrack> addTracks(final String uuid, final List<Track> tracksToAdd, final int toIndex) throws PlaylistException {

        try {

            PlayList playList = playlistDaoBean.getPlaylistByUUID(uuid);

            //We do not allow > 500 tracks in new playlists
            if (playList.getNrOfTracks() + tracksToAdd.size() > MAX_PLAYLIST_SIZE) {
                throw new PlaylistException("Playlist cannot have more than " + MAX_PLAYLIST_SIZE + " tracks");
            }
            //any method to invoke dao and save the final playlist should come here.
            return playList.addTracks(tracksToAdd, toIndex);

        }
        catch (PlaylistException e){
            LOGGER.error("System Exception. Cannot proceed. ", e);
            throw e;
        }
        catch (Exception e) {
            LOGGER.error("GENERIC EXCEPTION. Cannot proceed. ", e);
            throw new PlaylistException("Generic error");
        }
    }

    /**
	 * Remove the tracks from the playlist located at the sent indexes.
	 */
	List<PlayListTrack> removeTracks(String uuid, List<Integer> indexes) throws PlaylistException {
        PlayList playList = playlistDaoBean.getPlaylistByUUID(uuid);
        return playList.removeTracks(indexes);
	}
}
