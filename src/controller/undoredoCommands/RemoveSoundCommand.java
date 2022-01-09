package controller.undoredoCommands;

import controller.undoredoCommands.Command;
import model.Album;
import model.SoundClip;
import view.MusicOrganizerWindow;

import java.util.ArrayList;
import java.util.List;

public class RemoveSoundCommand implements Command {

    private List<SoundClip> selectedSound;
    private Album album;
    private MusicOrganizerWindow view;
    private List<Album> recentremoved;

    public RemoveSoundCommand(Album album, List<SoundClip> selectedSound, MusicOrganizerWindow view) {
        this.selectedSound = selectedSound;
        this.album = album;
        this.view = view;
        this.recentremoved = new ArrayList<>();
    }
    /** removes the selected sounds from the album and saves which albums they were removed from*/
    public void execute() {
        for (SoundClip s : selectedSound) {
            album.remove(s);
        }
        setRecentremoved(album);
        view.onClipsUpdated();

    }
    /** undos the execute command by adding to all albums added by the execute command*/
    public void undo() {
        for (Album recent : recentremoved) {
            for (SoundClip s : selectedSound) {
                recent.add(s);
            }
            recent.setRecentlymodified(false);
        }
        recentremoved.clear();
        view.onClipsUpdated();

    }
    /** help function to add all recently modified albums to recentremoved list*/
    private void setRecentremoved(Album album) {
        if (album.getRecentlymodified()) {
            recentremoved.add(album);
            album.setRecentlymodified(false);
            for (Album children : album.getChildren()) {
                setRecentremoved(children);
            }
        }

    }
}
