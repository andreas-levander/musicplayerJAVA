package controller;

import model.SoundClip;

import java.util.ArrayList;
import java.util.List;

public class UpdateSearchAlbums implements Subject {
    private List<Observer> observers;
    private List<SoundClip> soundclips;

    public UpdateSearchAlbums() {
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers) {
            o.update(soundclips);
        }
    }
    public void measurementsChanged() {
        this.notifyObservers();
    }

    public void updateSearchAlbums(List<SoundClip> slist) {
        soundclips = slist;
        measurementsChanged();
    }
}
