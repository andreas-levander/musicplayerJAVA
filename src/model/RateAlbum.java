package model;

import controller.Subject;

import java.util.ArrayList;
import java.util.List;

public class RateAlbum extends SearchAlbum{

    private List<SoundClip> album;
    private Subject updateSearch;
    private String name;

    public RateAlbum(Subject s) {
        album = new ArrayList<>();
        updateSearch = s;
        updateSearch.registerObserver(this);
        name = "Great Sound Clips";
    }

    @Override
    public boolean shouldContain(SoundClip sound) {
        if (sound.getRating() != null) {
            return sound.getRating() >= 4 && sound.getRating() <= 5;
        } else return false;
    }

    @Override
    public void add(SoundClip sound) {
        if (!album.contains(sound)) {
            album.add(sound);
        }
    }

    @Override
    public void remove(SoundClip sound) {
        album.remove(sound);
    }

    @Override
    public List<SoundClip> getList() {
        return album;
    }

    @Override
    public String toString() {
        return name;
    }
    @Override
    public boolean contains(SoundClip sound) {
        return album.contains(sound);
    }
}
