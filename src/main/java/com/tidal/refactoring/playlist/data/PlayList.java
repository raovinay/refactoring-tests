package com.tidal.refactoring.playlist.data;

import com.tidal.refactoring.playlist.exception.PlaylistValidationException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

    public void setPlayListTracks(Set<PlayListTrack> tracks){
        //no matter what order the tracks are sent in, we have to first sort by index and then add in the sorted order.
        playListTracks = new LinkedHashSet<>(new TreeSet<>(tracks));
    }


    public List<PlayListTrack> addTracks(List<Track> tracksToAdd, int toIndex) throws PlaylistValidationException {
        // The index is out of bounds, put it in the end of the list.
        toIndex = this.getValidIndex(toIndex);

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

    private int getValidIndex(int intendedIndex) throws PlaylistValidationException {
        if (intendedIndex > this.playListTracks.size() || intendedIndex == -1) {
            intendedIndex = this.playListTracks.size();
        }
        if(!isValidIndex(intendedIndex)){
            throw new PlaylistValidationException();
        }
        return intendedIndex;
    }

    private boolean isValidIndex(int intendedIndex) {
        return intendedIndex >= 0 && intendedIndex <= this.playListTracks.size();
    }


}
