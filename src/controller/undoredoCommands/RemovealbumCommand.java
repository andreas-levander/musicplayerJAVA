package controller.undoredoCommands;

import model.Album;
import view.MusicOrganizerWindow;
import java.util.List;

public class RemovealbumCommand implements Command {
    private Album parent;
    private Album removealbum;
    private MusicOrganizerWindow view;
    private List<Album> children;

    public RemovealbumCommand(Album parent, Album removealbum, MusicOrganizerWindow view) {
        this.parent = parent;
        this.removealbum = removealbum;
        this.view = view;
        children = removealbum.getChildren();

    }
    /** removes an album */
    public void execute() {
        parent.removechild(removealbum);
        view.onAlbumRemoved(removealbum);
        view.onClipsUpdated();

    }
    /** undos the removed album*/
    public void undo() {
        parent.addchild(removealbum);
        children.forEach(x -> x.setParent(removealbum));
        view.onAlbumAdded(parent,removealbum,children);
        view.onClipsUpdated();

    }

}

