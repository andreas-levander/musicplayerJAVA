package controller;

import java.util.List;
import java.util.Set;

import controller.undoredoCommands.AddSoundCommand;
import controller.undoredoCommands.AddalbumCommand;
import controller.undoredoCommands.RemoveSoundCommand;
import controller.undoredoCommands.RemovealbumCommand;
import model.*;
import view.MusicOrganizerWindow;

public class MusicOrganizerController {

	private MusicOrganizerWindow view;
	private SoundClipBlockingQueue queue;
	private Album root;
	private UndoRedoController undoRedoController;
	private UpdateSearchAlbums updateSearchAlbums;
	
	public MusicOrganizerController() {

		// Create the root album for all sound clips
		root = new Album("All Sound Clips");
		
		// Create the blocking queue
		queue = new SoundClipBlockingQueue();

		// Create the UndoRedoContoller
		undoRedoController = new UndoRedoController();

		// Create Subject
		updateSearchAlbums = new UpdateSearchAlbums();

		// Create a separate thread for the sound clip player and start it
		
		(new Thread(new SoundClipPlayer(queue))).start();
	}
	
	/**
	 * Load the sound clips found in all subfolders of a path on disk. If path is not
	 * an actual folder on disk, has no effect.
	 */
	public Set<SoundClip> loadSoundClips(String path) {
		Set<SoundClip> clips = SoundClipLoader.loadSoundClips(path);
		// Add the loaded sound clips to the root album
		for (SoundClip s : clips) {
			root.add(s);
		}
		root.setRecentlymodified(false);

		return clips;
	}
	
	public void registerView(MusicOrganizerWindow view) {
		this.view = view;
	}

	public void updateSearchAlbums(List<SoundClip> soundClipList) {
		updateSearchAlbums.updateSearchAlbums(soundClipList);
	}
	/**
	 * Returns the root album
	 */
	public Album getRootAlbum(){
		return root;
	}

	/**
	 * Creates the Flagged Album
	 * @return flaggedalbum
	 */
	public FlaggedAlbum createFlaggedAlbum() {
		return new FlaggedAlbum(updateSearchAlbums);
	}

	/**
	 * Creates the RateAlbum
	 * @return RateAlbum
	 */
	public RateAlbum createRateAlbum() {
		return new RateAlbum(updateSearchAlbums);
	}

	/** Preforms the undo command and returns true or false depending on if the undo stack is empty or not */
	public boolean undo() {
		undoRedoController.onUndo();
		return undoRedoController.undoIsEmpty();
	}
	/** Preforms the redo command and returns true or false depending on if the redo stack is empty or not */
	public boolean redo() {
		undoRedoController.onRedo();
		return  undoRedoController.redoIsEmpty();
	}
	
	/**
	 * Adds an album to the Music Organizer
	 */
	public void addNewAlbum(Album selected, String name){ 
		if (selected != null) {
			Album newalbum = new Album(name);
			undoRedoController.onExecute(new AddalbumCommand(selected, newalbum, view));
		}

	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	public void deleteAlbum(Album selected){
		if (selected != null) {
			Album parent = selected.getParent();
			if (parent != null) {
				undoRedoController.onExecute(new RemovealbumCommand(parent,selected,view));

			}
		}
	}
	
	/**
	 * Adds sound clips to an album
	 */
	public void addSoundClips(Album selected, List<SoundClip> selectedSound){ 
		if (selected != null && !selectedSound.isEmpty() && selected != root) {
			undoRedoController.onExecute(new AddSoundCommand(selected,selectedSound,view));
		}
	}
	
	/**
	 * Removes sound clips from an album
	 */
	public void removeSoundClips(Album selected, List<SoundClip> selectedSound){ 
		if (selected != null && !selectedSound.isEmpty()) {
			undoRedoController.onExecute(new RemoveSoundCommand(selected,selectedSound,view));
		}
	}
	
	/**
	 * Puts the selected sound clips on the queue and lets
	 * the sound clip player thread play them. Essentially, when
	 * this method is called, the selected sound clips in the 
	 * SoundClipTable are played.
	 */
	public void playSoundClips(){
		List<SoundClip> l = view.getSelectedSoundClips();
		queue.enqueue(l);
		for(int i=0;i<l.size();i++) {
			view.displayMessage("Playing " + l.get(i));
		}
	}
}
