package model;

import controller.Subject;

import java.util.List;

public abstract class SearchAlbum implements controller.Observer,Albums {


    public void update(List<SoundClip> slist) {
        for (SoundClip s : slist) {
            updateSound(s);
        }
    }


    private void updateSound(SoundClip sound) {
        if (shouldContain(sound)) {
            add(sound);
        } else if (contains(sound)) {
            remove(sound);
        }

    }

    public abstract boolean shouldContain(SoundClip sound);
    public abstract boolean contains(SoundClip sound);
    public abstract void add(SoundClip sound);
    public abstract void remove(SoundClip sound);
    public abstract List<SoundClip> getList();

}
