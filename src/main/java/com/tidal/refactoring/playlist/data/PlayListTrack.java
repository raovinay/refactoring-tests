package com.tidal.refactoring.playlist.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Getter
public class PlayListTrack implements Serializable, Comparable<PlayListTrack> {

    private static final long serialVersionUID = 5464240796158432162L;

    private Integer id;
    private PlayList playlist;
    @Setter
    private int index;
    private Date dateAdded;
    @Setter
    private int trackId;

    private Track track;

    public PlayListTrack(Integer id, PlayList playList, Track track) {
        this.id=id;
        this.playlist=playList;
        this.track=track;
        dateAdded = new Date();
    }


    public int compareTo(PlayListTrack o) {
        return this.getIndex() - o.getIndex();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayListTrack that = (PlayListTrack) o;

        if (index != that.index) return false;
        if (trackId != that.trackId) return false;
        if (dateAdded != null ? !dateAdded.equals(that.dateAdded) : that.dateAdded != null) return false;
        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + index;
        result = 31 * result + (dateAdded != null ? dateAdded.hashCode() : 0);
        result = 31 * result + trackId;
        return result;
    }

    public String toString() {
        return "PlayListTrack id[" + getId() + "], trackId[" + getTrackId() + "]";
    }
}
