package controller.undoredoCommands;

import model.Album;
import view.MusicOrganizerWindow;


public class AddalbumCommand implements Command {

    private Album parent;
    private Album addedalbum;
    private MusicOrganizerWindow view;

    public AddalbumCommand(Album parent, Album addedalbum, MusicOrganizerWindow view) {
        this.parent = parent;
        this.addedalbum = addedalbum;
        this.view = view;
    }
    /** adds an album */
    public void execute() {
        parent.addchild(addedalbum);
        view.onAlbumAdded(parent, addedalbum, null);

    }
    /** undos the add album command */
    public void undo() {
        parent.removechild(addedalbum);
        view.onAlbumRemoved(addedalbum);
        view.onClipsUpdated();

    }

}
