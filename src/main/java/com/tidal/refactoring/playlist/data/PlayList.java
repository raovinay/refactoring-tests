package com.tidal.refactoring.playlist.data;

import com.tidal.refactoring.playlist.exception.PlaylistValidationException;
import lombok.Data;

import java.util.*;


/**
 * A very simplified version of TrackPlaylist
 */
@Data
public class PlayList {
    private Integer id;
    private String playListName;
    private LinkedHashSet<PlayListTrack> playListTracks;
    private Date registeredDate;
    private Date lastUpdated;
    private String uuid;
    private boolean deleted;

    public PlayList() {
        this.uuid = UUID.randomUUID().toString();
        Date d = new Date();
        this.registeredDate = d;
        this.lastUpdated = d;
        this.playListTracks = new LinkedHashSet<>();
    }

    public void setPlayListTracks(Set<PlayListTrack> tracks){
        playListTracks = new LinkedHashSet<>(new TreeSet<>(tracks));
    }

    public int getValidIndex(int intendedIndex) throws PlaylistValidationException {
        if (intendedIndex > this.playListTracks.size() || intendedIndex == -1) {
            intendedIndex = this.playListTracks.size();
        }
        if(!isValidIndex(intendedIndex)){
            throw new PlaylistValidationException();
        }
        return intendedIndex;
    }

    public boolean isValidIndex(int intendedIndex) {
        return intendedIndex >= 0 && intendedIndex <= this.playListTracks.size();
    }

    public List<PlayListTrack> addTracks(List<Track> tracksToAdd, int toIndex) throws PlaylistValidationException {
        // The index is out of bounds, put it in the end of the list.
        toIndex = this.getValidIndex(toIndex);

        List<PlayListTrack> added = new ArrayList<>(tracksToAdd.size());
        List<PlayListTrack> tracks = new LinkedList<>(this.playListTracks);
        for (Track track : tracksToAdd) {
            PlayListTrack playlistTrack = new PlayListTrack();
            playlistTrack.setTrack(track);
            playlistTrack.setPlaylist(this);
            playlistTrack.setDateAdded(new Date());
            tracks.add(toIndex, playlistTrack);
            added.add(playlistTrack);
            toIndex++;
        }

        reindex(tracks);

        resetPlayList(tracks);
        return added;
    }

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

    //derived fields:
    public int getNrOfTracks(){
        return this.playListTracks.size();
    }

    public float getDuration(){
        return (float) this.playListTracks.stream().mapToDouble(x -> x.getTrack().getDuration()).sum();
    }

    private void reindex(List<PlayListTrack> playListTracks) {
        int i = 0;
        for (PlayListTrack track : playListTracks) {
            track.setIndex(i++);
        }
    }
    private void resetPlayList(List<PlayListTrack> newList) {
        this.playListTracks.clear();
        this.playListTracks.addAll(newList);
    }

}
