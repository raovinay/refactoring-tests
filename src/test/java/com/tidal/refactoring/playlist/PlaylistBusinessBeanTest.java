package com.tidal.refactoring.playlist;

import com.google.inject.Inject;
import com.tidal.refactoring.playlist.dao.PlaylistDaoBean;
import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@Guice(modules = TestBusinessModule.class)
public class PlaylistBusinessBeanTest {

    @Inject
    PlaylistBusinessBean playlistBusinessBean;

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddTracksBasic() throws Exception {
        List<Track> trackList = getTracksForTest();

        List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks(UUID.randomUUID().toString(), trackList, 5);

        assertEquals(2, playListTracks.size());
        PlayList playList = playListTracks.get(0).getTrackPlayList();
        assertEquals(378, playList.getNrOfTracks());
        assertEquals(378, playList.getPlayListTracks().size());
        assertEquals(5, playListTracks.get(0).getIndex());
        assertEquals(6, playListTracks.get(1).getIndex());
    }

    private List<Track> getTracksForTest() {
        List<Track> trackList = new ArrayList<Track>();

        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setId(76868);
        trackList.add(track);
        Track track2 = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track - acoustic :)");
        track.setId(76869);
        trackList.add(track2);
        return trackList;
    }
}