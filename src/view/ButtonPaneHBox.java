package view;


import controller.MusicOrganizerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import model.Album;
import model.SoundClip;

public class ButtonPaneHBox extends HBox {

	private MusicOrganizerController controller;
	private MusicOrganizerWindow view;
	
	private Button newAlbumButton;
	private Button deleteAlbumButton;
	private Button addSoundClipsButton;
	private Button removeSoundClipsButton;	
	private Button playButton;
	private Button undoButton;
	private Button redoButton;
	private Button rateButton;
	private Button flagButton;
	public static final int BUTTON_MIN_WIDTH = 150;

	
	
	public ButtonPaneHBox(MusicOrganizerController contr, MusicOrganizerWindow view) {
		super();
		this.controller = contr;
		this.view = view;
		
		newAlbumButton = createNewAlbumButton();
		this.getChildren().add(newAlbumButton);

		deleteAlbumButton = createDeleteAlbumButton();
		this.getChildren().add(deleteAlbumButton);
		
		addSoundClipsButton = createAddSoundClipsButton();
		this.getChildren().add(addSoundClipsButton);
		
		removeSoundClipsButton = createRemoveSoundClipsButton();
		this.getChildren().add(removeSoundClipsButton);
		
		playButton = createPlaySoundClipsButton();
		this.getChildren().add(playButton);

		undoButton = createUndoButton();
		this.getChildren().add(undoButton);

		redoButton = createRedoButton();
		this.getChildren().add(redoButton);

		rateButton = createRateButton();
		this.getChildren().add(rateButton);

		flagButton = createFlagButton();
		this.getChildren().add(flagButton);
		

	}
	
	/*
	 * Each method below creates a single button. The buttons are also linked
	 * with event handlers, so that they react to the user clicking on the buttons
	 * in the user interface
	 */

	private Button createNewAlbumButton() {
		Button button = new Button("New Album");
		button.setTooltip(new Tooltip("Create new sub-album to selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (view.getSelectedAlbum() != null && view.getSelectedAlbum() instanceof Album ) {
					String name = view.promptForAlbumName();
					if (name != null) {
						controller.addNewAlbum((Album)view.getSelectedAlbum(), name);
						setDisableUndoRedoButtons(false,true);
					}
				}
			}
			
		});
		return button;
	}
	
	private Button createDeleteAlbumButton() {
		Button button = new Button("Remove Album");
		button.setTooltip(new Tooltip("Remove selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (view.getSelectedAlbum() != null && view.getSelectedAlbum() != controller.getRootAlbum() && view.getSelectedAlbum() instanceof Album) {
					controller.deleteAlbum((Album) view.getSelectedAlbum());
					setDisableUndoRedoButtons(false,true);
				}
			}
			
		});
		return button;
	}
	
	private Button createAddSoundClipsButton() {
		Button button = new Button("Add Sound Clips");
		button.setTooltip(new Tooltip("Add selected sound clips to selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if(view.getSelectedAlbum() != null && view.getSelectedAlbum() != controller.getRootAlbum() && !view.getSelectedSoundClips().isEmpty() &&
						!view.getSelectedAlbum().getList().containsAll(view.getSelectedSoundClips()) && view.getSelectedAlbum() instanceof Album) {
					controller.addSoundClips((Album) view.getSelectedAlbum(), view.getSelectedSoundClips());
					setDisableUndoRedoButtons(false,true);
				}
			}
			
		});
		return button;
	}
	
	private Button createRemoveSoundClipsButton() {
		Button button = new Button("Remove Sound Clips");
		button.setTooltip(new Tooltip("Remove selected sound clips from selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (view.getSelectedAlbum() != null && view.getSelectedAlbum() != controller.getRootAlbum() && !view.getSelectedSoundClips().isEmpty()
						&& view.getSelectedAlbum() instanceof Album) {
					controller.removeSoundClips((Album) view.getSelectedAlbum(), view.getSelectedSoundClips());
					setDisableUndoRedoButtons(false,true);
				}
			}
			
		});
		return button;
	}
	
	private Button createPlaySoundClipsButton() {
		Button button = new Button("Play Sound Clips");
		button.setTooltip(new Tooltip("Play selected sound clips"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				controller.playSoundClips();
			}
			
		});
		return button;
	}

	private Button createUndoButton() {
		Button button = new Button("Undo");
		button.setTooltip(new Tooltip("Undo last action"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				boolean isempty = controller.undo();
				if (isempty) {
					button.setDisable(true);
				}
				redoButton.setDisable(false);
			}

		});
		return button;
	}
	private Button createRedoButton() {
		Button button = new Button("Redo");
		button.setTooltip(new Tooltip("Redo last action"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				boolean isempty = controller.redo();
				if(isempty) {
					button.setDisable(true);
				}
				undoButton.setDisable(false);
			}

		});
		return button;
	}
	private Button createRateButton() {
		Button button = new Button("Rate Soundclip");
		button.setTooltip(new Tooltip("Rate selected soundclip (0-5)"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if(!view.getSelectedSoundClips().isEmpty()) {
					Integer rating = view.promptForRating();
					if (rating != null) {
						for (SoundClip s : view.getSelectedSoundClips()) {
								s.setRating(rating);
						}
						controller.updateSearchAlbums(view.getSelectedSoundClips());
						view.onClipsUpdated();
					}
				}


			}

		});
		return button;
	}
	private Button createFlagButton() {
		Button button = new Button("Flag Soundclip");
		button.setTooltip(new Tooltip("Flag selected soundclip"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if(!view.getSelectedSoundClips().isEmpty()) {
					for (SoundClip s : view.getSelectedSoundClips()) {
						if (s.getFlagged()) {
							s.setFlagged(false);
						} else s.setFlagged(true);
					}
					controller.updateSearchAlbums(view.getSelectedSoundClips());
					view.onClipsUpdated();
				}

			}

		});
		return button;
	}

	/**
	 *
	 * Methods to enable/disable buttons
	 */

	public void setDisableUndoRedoButtons(boolean setUndo, boolean setRedo) {
		undoButton.setDisable(setUndo);
		redoButton.setDisable(setRedo);
	}
	public void setDisableAddRemoveSoundButtons(boolean setAdd, boolean setRemove) {
		addSoundClipsButton.setDisable(setAdd);
		removeSoundClipsButton.setDisable(setRemove);
	}
	public void setDisableRateFlagButtons(boolean setRate, boolean setFlag) {
		rateButton.setDisable(setRate);
		flagButton.setDisable(setFlag);
	}
	public void setDisablePlayButton(boolean setPlay) {
		playButton.setDisable(setPlay);
	}
	public void setDisableAddRemoveAlbumButtons(boolean setAdd, boolean setRemove) {
		newAlbumButton.setDisable(setAdd);
		deleteAlbumButton.setDisable(setRemove);
	}
}
