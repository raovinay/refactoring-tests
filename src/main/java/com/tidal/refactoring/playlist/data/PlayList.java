package com.tidal.refactoring.playlist.data;

import com.tidal.refactoring.playlist.exception.PlaylistValidationException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * A very simplified version of TrackPlaylist
 */
@Getter
public class PlayList {
    private Integer id;
    @Setter
    private String playListName;
    private LinkedHashSet<PlayListTrack> playListTracks;
    private Date registeredDate;
    private Date lastUpdated;
    @Setter
    private String uuid;
    @Setter
    private boolean deleted;

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayList.class);

    public PlayList(Integer id, String playListName, String uuid) {
        this.uuid = uuid==null?UUID.randomUUID().toString():uuid;
        Date d = new Date();
        this.registeredDate = d;
        this.lastUpdated = d;
        this.playListTracks = new LinkedHashSet<>();
        this.id=id;
        this.playListName=playListName;
    }

    //derived fields:
    public int getNrOfTracks(){
        return this.playListTracks.size();
    }

    public float getDuration(){
        return (float) this.playListTracks.stream().mapToDouble(x -> x.getTrack().getDuration()).sum();
    }

    /**
     * Directly set the tracks to the playlist. This will override all existing tracks.
     * @param tracks
     */
    public void setPlayListTracks(Set<PlayListTrack> tracks){
        //no matter what order the tracks are sent in, we have to first sort by index and then add in the sorted order.
        playListTracks = new LinkedHashSet<>(new TreeSet<>(tracks));
    }

    /**
     * Add a list of Tracks to the given index.
     * If the index is beyond the size of the existing playlist - add to the end.
     * Return the list of PlayListTracks that got added to the PlayList.
     * @param tracksToAdd
     * @param toIndex
     * @return
     */
    public List<PlayListTrack> addTracks(List<Track> tracksToAdd, int toIndex) {
        // The index is out of bounds, put it in the end of the list.
        try {
            toIndex = this.getValidIndex(toIndex);
        }
        catch(PlaylistValidationException e){
            LOGGER.error("Validation error. Operation aborted. ", e);
            return Collections.EMPTY_LIST;
        }

        List<PlayListTrack> added = new ArrayList<>(tracksToAdd.size());
        List<PlayListTrack> tracks = new LinkedList<>(this.playListTracks);
        for (Track track : tracksToAdd) {
            PlayListTrack playlistTrack = new PlayListTrack(null, this, track);
            tracks.add(toIndex, playlistTrack);
            added.add(playlistTrack);
            toIndex++;
        }

        reindex(tracks);

        resetPlayList(tracks);
        return added;
    }

    /**
     * Remove tracks from the set of PlayListTracks based on index.
     * All invalid indices will be ignored.
     * Returns the tracks that were removed. Retains order.
     * @param indexes
     * @return
     */
    public List<PlayListTrack> removeTracks(List<Integer> indexes) {

        List<PlayListTrack> original = new LinkedList<>(this.playListTracks);
        List<PlayListTrack> removed = new ArrayList<>(indexes.size());
        //populate the remove list first, before deleting from the original list.
        //had to do this to retain the sequence.
        for(int idx:indexes){
            if(this.isValidIndex(idx)){
                removed.add(original.get(idx-1));
            }
        }
        if(removed.size()==0){
            return Collections.EMPTY_LIST;
        }
        //Reverse sort - so that indexes are not messed up while removing.
        indexes.sort(Collections.reverseOrder());
        for(int idx:indexes){
            if(this.isValidIndex(idx)) {
                original.remove(idx - 1);
            }
        }

        //3. reindex.
        reindex(original);
        //change the original playList object.
        resetPlayList(original);
        return removed;
    }

    /**
     * sets index to individual playListTrack elements based on their position in the list.
     * @param playListTracks
     */
    private void reindex(List<PlayListTrack> playListTracks) {
        int i = 0;
        for (PlayListTrack track : playListTracks) {
            track.setIndex(i++);
        }
    }

    /**
     * Adding the updated list of PlayListTracks back into the original set.
     * @param newList
     */
    private void resetPlayList(List<PlayListTrack> newList) {
        this.playListTracks.clear();
        this.playListTracks.addAll(newList);
    }

    /**
     * Checks if the index is valid and if not, try to set it to a valid value.
     * @param intendedIndex
     * @return
     * @throws PlaylistValidationException
     */
    private int getValidIndex(int intendedIndex) throws PlaylistValidationException {
        if (intendedIndex > this.playListTracks.size() || intendedIndex == -1) {
            intendedIndex = this.playListTracks.size();
        }
        if(!isValidIndex(intendedIndex)){
            throw new PlaylistValidationException();
        }
        return intendedIndex;
    }

    /**
     * Check if index is within the range.
     * @param intendedIndex
     * @return
     */
    private boolean isValidIndex(int intendedIndex) {
        return intendedIndex >= 0 && intendedIndex <= this.playListTracks.size();
    }


}
