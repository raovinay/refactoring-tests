package com.tidal.refactoring.playlist.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Track {

    private int id;
    private String title;
    private float duration;
    private int artistId;

    }