package controller;

import model.SoundClip;

import java.util.List;

public interface Observer {
    void update(List<SoundClip> slist);
}
