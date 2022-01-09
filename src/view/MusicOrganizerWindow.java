package view;
	
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import model.Album;
import controller.MusicOrganizerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Albums;
import model.SearchAlbum;
import model.SoundClip;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;



public class MusicOrganizerWindow extends Application {
	
	private BorderPane bord;
	private static MusicOrganizerController controller;
	private TreeItem<Albums> rootNode;
	private TreeItem<Albums> rateNode;
	private TreeItem<Albums> flaggedNode;
	private TreeView<Albums> tree;
	private ButtonPaneHBox buttons;
	private SoundClipListView soundClipTable;
	private TextArea messages;
	
	
	public static void main(String[] args) {
		controller = new MusicOrganizerController();
		if (args.length == 0) {
			controller.loadSoundClips("sample-sound");
		} else if (args.length == 1) {
			controller.loadSoundClips(args[0]);
		} else {
			System.err.println("too many command-line arguments");
			System.exit(0);
		}
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
				
		try {
			controller.registerView(this);
			primaryStage.setTitle("Music Organizer");
			
			bord = new BorderPane();
			
			// Create buttons in the top of the GUI
			buttons = new ButtonPaneHBox(controller, this);
			bord.setTop(buttons);

			// Create the tree in the left of the GUI
			tree = createTreeView();
			bord.setLeft(tree);

			
			// Create the list in the right of the GUI
			soundClipTable = createSoundClipListView();
			bord.setCenter(soundClipTable);
						
			// Create the text area in the bottom of the GUI
			bord.setBottom(createBottomTextArea());
			
			Scene scene = new Scene(bord);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent arg0) {
					Platform.exit();
					System.exit(0);
					
				}
				
			});

			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private TreeView<Albums> createTreeView(){
		rootNode = new TreeItem<>(controller.getRootAlbum());
		TreeItem<Albums> dummyNode = new TreeItem<>();
		TreeView<Albums> v = new TreeView<>(dummyNode);
		rateNode = new TreeItem<>(controller.createRateAlbum());
		dummyNode.getChildren().add(rateNode);
		flaggedNode = new TreeItem<>(controller.createFlaggedAlbum());
		dummyNode.getChildren().add(flaggedNode);
		dummyNode.getChildren().add(rootNode);
		v.setShowRoot(false);

		/** to stop default treeview double click expand/collapse behavior */
		v.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
			if (e.getClickCount() % 2 == 0 && e.getButton().equals(MouseButton.PRIMARY))
				e.consume();
		});
		
		v.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if(e.getClickCount()==2) {
					// This code gets invoked whenever the user double clicks in the TreeView
					onClipsUpdated();
					
				}
				
			}
			
		});
		/** Listner to check for changes in selected album and to enable/disable buttons */
		v.getSelectionModel().selectedItemProperty().addListener((observable, old_val, new_val) -> {
			if (new_val == rateNode || new_val == flaggedNode) {
				buttons.setDisableAddRemoveAlbumButtons(true,true);
				buttons.setDisableAddRemoveSoundButtons(true,true);
			} else if (new_val == rootNode) {
				buttons.setDisableAddRemoveAlbumButtons(false,true);
				buttons.setDisableAddRemoveSoundButtons(true,true);
			} else if (getSelectedSoundClips().isEmpty()) {
				buttons.setDisableAddRemoveAlbumButtons(false,false);
			} else {
				buttons.setDisableAddRemoveAlbumButtons(false,false);
				buttons.setDisableAddRemoveSoundButtons(false,false);

			}
		});

		
		return v;
	}

	
	private SoundClipListView createSoundClipListView() {
		SoundClipListView v = new SoundClipListView();
		v.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		v.display(controller.getRootAlbum());
		
		v.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if(e.getClickCount() == 2) {
					// This code gets invoked whenever the user double clicks in the sound clip table
					controller.playSoundClips();
				}
				
			}
			
		});
		/** Listner to check for changes in selected soundclip and to enable/disable buttons */
		v.getSelectionModel().selectedItemProperty().addListener((observableValue, soundClip, t1) -> {
			if (getSelectedTreeItem() == rootNode && t1 == null) {
				buttons.setDisablePlayButton(true);
				buttons.setDisableAddRemoveSoundButtons(true,true);
				buttons.setDisableRateFlagButtons(true,true);
			} else if (t1 != null && getSelectedTreeItem() == rootNode) {
				buttons.setDisablePlayButton(false);
				buttons.setDisableRateFlagButtons(false,false);
			} else if (t1 != null && getSelectedTreeItem() == null) {
				buttons.setDisablePlayButton(false);
				buttons.setDisableRateFlagButtons(false,false);
			} else if ((getSelectedTreeItem() == rateNode || getSelectedTreeItem() == flaggedNode) && t1 != null) {
				buttons.setDisablePlayButton(false);
				buttons.setDisableRateFlagButtons(false,false);
			} else if (t1 != null) {
				buttons.setDisableAddRemoveSoundButtons(false,false);
				buttons.setDisablePlayButton(false);
				buttons.setDisableRateFlagButtons(false,false);
			} else {
				buttons.setDisableAddRemoveSoundButtons(true,true);
				buttons.setDisablePlayButton(true);
				buttons.setDisableRateFlagButtons(true,true);
			}
		});
		
		return v;
	}
	
	private ScrollPane createBottomTextArea() {
		messages = new TextArea();
		messages.setPrefRowCount(3);
		messages.setWrapText(true);
		messages.prefWidthProperty().bind(bord.widthProperty());
		messages.setEditable(false); // don't allow user to edit this area
		
		// Wrap the TextArea in a ScrollPane, so that the user can scroll the 
		// text area up and down
		ScrollPane sp = new ScrollPane(messages);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		return sp;
	}
	
	/**
	 * Displays the message in the text area at the bottom of the GUI
	 * @param message the message to display
	 */
	public void displayMessage(String message) {
		messages.appendText(message + "\n");
	}
	
	public Albums getSelectedAlbum() {
		TreeItem<Albums> selectedItem = getSelectedTreeItem();
		return selectedItem == null ? null : selectedItem.getValue();
	}
	
	private TreeItem<Albums> getSelectedTreeItem(){
		return tree.getSelectionModel().getSelectedItem();
	}
	
	
	
	/**
	 * Pop up a dialog box prompting the user for a name for a new album.
	 * Returns the name, or null if the user pressed Cancel
	 */
	public String promptForAlbumName() {
		TextInputDialog dialog = new TextInputDialog();
		
		dialog.setTitle("Enter album name");
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter the name for the album");
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}
	/**
	 * Pop up a dialog box prompting the user for a rating for a Soundclip.
	 * Returns the number, or null if the user pressed Cancel
	 */
	public Integer promptForRating() {
		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("Rate Soundclip(s)");
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter the rating (0-5) for the selected soundclip(s)");
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			int input;
			try {
				input = Integer.parseInt(result.get());
				if (input >= 0 && input <= 5) {
					return input;
				}

			} catch (NumberFormatException e) {
				// Could not parse user input:
				System.err.println("Parse error " + e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * Return all the sound clips currently selected in the clip table.
	 */
	public List<SoundClip> getSelectedSoundClips(){
		return soundClipTable.getSelectedClips();
	}
	
	
	
	/**
	 * *****************************************************************
	 * Methods to be called in response to events in the Music Organizer
	 * *****************************************************************
	 */	
	
	
	
	/**
	 * Updates the album hierarchy with a new album
	 * @param newAlbum
	 */
	public void onAlbumAdded(Album parent, Album newAlbum, List<Album> children){

		TreeItem<Albums> root = rootNode;
		TreeItem<Albums> parentNode = findAlbumNode(parent, root);
		TreeItem<Albums> nAlbum = new TreeItem<>(newAlbum);


		parentNode.getChildren().add(nAlbum);
		if (children != null && !children.isEmpty()) {
			for (Album a : children) {
				TreeItem<Albums> b = findAlbumNode(a,root);
				parentNode.getChildren().remove(b);
				nAlbum.getChildren().add(b);
			}
			nAlbum.setExpanded(true);


		}
		parentNode.setExpanded(true); // automatically expand the parent node in the tree

	}
	
	/**
	 * Updates the album hierarchy by removing an album from it
	 */

	public void onAlbumRemoved(Album toRemove){

		TreeItem<Albums> root = rootNode;

		TreeItem<Albums> nodeToRemove = findAlbumNode(toRemove, root);
		TreeItem<Albums> parent = nodeToRemove.getParent();
		nodeToRemove.getChildren().forEach(x -> parent.getChildren().add(x));
		nodeToRemove.getParent().getChildren().remove(nodeToRemove);

	}
	
	/**
	 * Refreshes the clipTable in response to the event that clips have
	 * been modified in an album
	 */
	public void onClipsUpdated(){
		Albums a = getSelectedAlbum();
		soundClipTable.display(a);
	}

	private TreeItem<Albums> findAlbumNode(Album albumToFind, TreeItem<Albums> root) {

		// recursive method to locate a node that contains a specific album in the TreeView

		if(root.getValue().equals(albumToFind)) {
			return root;
		}

		for(TreeItem<Albums> node : root.getChildren()) {
			TreeItem<Albums> item = findAlbumNode(albumToFind, node);
			if(item != null)
				return item;
		}

		return null;
	}
	
}
