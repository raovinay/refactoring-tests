package com.tidal.refactoring.playlist.data;

import lombok.Data;

@Data
public class Track {

    private String title;
    private float duration;

    private int artistId;
    private int id;

    }