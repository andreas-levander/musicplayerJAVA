package controller.undoredoCommands;

import model.Album;
import model.SoundClip;
import view.MusicOrganizerWindow;

import java.util.List;

public class AddSoundCommand implements Command{

    private List<SoundClip> selectedSound;
    private Album album;
    private MusicOrganizerWindow view;
    private Album last;

    public AddSoundCommand(Album album, List<SoundClip> selectedSound, MusicOrganizerWindow view) {
        this.selectedSound = selectedSound;
        this.album = album;
        this.view = view;
        last = album;
    }
    /** adds the selected sounds to the album and saves the last parent which got sounds added to it*/
    public void execute() {
        for (SoundClip s : selectedSound) {
            album.add(s);
        }
        getLast(album);

        view.onClipsUpdated();

    }
    /** undos the add sound command by removing from the last parent*/
    public void undo() {
      for(SoundClip a : selectedSound) {
          last.remove(a);
      }
        view.onClipsUpdated();

    }
    /** help function to get the last added to parentalbum*/
    private void getLast(Album album) {
        if(album.getRecentlymodified()) {
            last = album;
            album.setRecentlymodified(false);
            getLast(last.getParent());
        }
    }
}
