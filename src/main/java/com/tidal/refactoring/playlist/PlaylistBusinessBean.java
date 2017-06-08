package com.tidal.refactoring.playlist;

import com.google.inject.Inject; 
import com.tidal.refactoring.playlist.dao.PlaylistDaoBean;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;
import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.exception.PlaylistException;

import java.util.*;

public class PlaylistBusinessBean {

    private PlaylistDaoBean playlistDaoBean;

    @Inject
    public PlaylistBusinessBean(PlaylistDaoBean playlistDaoBean){
        this.playlistDaoBean = playlistDaoBean;
    }

    /**
     * Add tracks to the index
     */
    List<PlayListTrack> addTracks(String uuid, List<Track> tracksToAdd, int toIndex) throws PlaylistException {

        try {

            PlayList playList = playlistDaoBean.getPlaylistByUUID(uuid);

            //We do not allow > 500 tracks in new playlists
            if (playList.getNrOfTracks() + tracksToAdd.size() > 500) {
                throw new PlaylistException("Playlist cannot have more than " + 500 + " tracks");
            }

            // The index is out of bounds, put it in the end of the list.
            int size = playList.getPlayListTracks() == null ? 0 : playList.getPlayListTracks().size();
            if (toIndex > size || toIndex == -1) {
                toIndex = size;
            }

            if (!validateIndexes(toIndex, playList.getNrOfTracks())) {
                return Collections.EMPTY_LIST;
            }

            List<PlayListTrack> original = getPlayListTracksSorted(playList);

            List<PlayListTrack> added = new ArrayList<>(tracksToAdd.size());

            //core add logic
            for (Track track : tracksToAdd) {
                PlayListTrack playlistTrack = new PlayListTrack();
                playlistTrack.setTrack(track);
                playlistTrack.setTrackPlaylist(playList);
                playlistTrack.setDateAdded(new Date());
                playList.setDuration(addTrackDurationToPlaylist(playList, track));
                original.add(toIndex, playlistTrack);
                added.add(playlistTrack);
                toIndex++;
            }

            reindex(original);

            playList.getPlayListTracks().clear();
            playList.getPlayListTracks().addAll(original);
            playList.setNrOfTracks(original.size());

            return added;

        }
        catch (PlaylistException e){
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new PlaylistException("Generic error");
        }
    }

    private List<PlayListTrack> getPlayListTracksSorted(PlayList playList) {
        Set<PlayListTrack> originalSet = playList.getPlayListTracks();
        List<PlayListTrack> original;
        if (originalSet == null || originalSet.size() == 0) {
            original = new LinkedList<>();
        }
        else {
            original = new LinkedList<>(originalSet);
        }
        //consider making the set in the original collection as a treeset?
        Collections.sort(original);
        return original;
    }

    private void reindex(List<PlayListTrack> playListTracks) {
        int i = 0;
        for (PlayListTrack track : playListTracks) {
            track.setIndex(i++);
        }
    }

    /**
	 * Remove the tracks from the playlist located at the sent indexes
	 */
	List<PlayListTrack> removeTracks(String uuid, List<Integer> indexes) throws PlaylistException {
		// TODO
		return Collections.EMPTY_LIST;
	}

    private boolean validateIndexes(int toIndex, int length) {
        return toIndex >= 0 && toIndex <= length;
    }

    private float addTrackDurationToPlaylist(PlayList playList, Track track) {
        return (track != null ? track.getDuration() : 0)
                + (playList != null && playList.getDuration() != null ? playList.getDuration() : 0);
    }
}
