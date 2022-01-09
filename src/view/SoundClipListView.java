package view;


import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Album;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import model.Albums;
import model.SoundClip;

public class SoundClipListView extends TableView<SoundClip> {

	
	public SoundClipListView() {
		super();
		createTableView();

	}

	public SoundClipListView(ObservableList<SoundClip> arg0) {
		super(arg0);
		createTableView();
	}
	
	/**
	 * Displays the contents of the specified album
	 * @param album - the album which contents are to be displayed
	 */
	public void display(Albums album){
		this.getItems().clear();
		//
		// Add all SoundClips contained in the parameter album to 
		// the list of SoundClips 'clips' (the instance variable)
		if (album != null) {
			for (SoundClip s : album.getList()) {
				this.getItems().add(s);
			}
		}

	}

	public List<SoundClip> getSelectedClips(){
		ObservableList<SoundClip> items = this.getSelectionModel().getSelectedItems();
		List<SoundClip> clips = new ArrayList<>(items);
		return clips;
	}

	/**
	 * Creates the tableview
	 */
	private void createTableView() {
		TableColumn<SoundClip, String> column1 = new TableColumn<>("SoundClip");
		column1.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<SoundClip, Integer> column2 = new TableColumn<>("Rating");
		column2.setCellValueFactory(new PropertyValueFactory<>("rating"));

		TableColumn<SoundClip, ImageView> column3 = new TableColumn<>("Flagged");
		Image flag = new Image("Actions-flag-icon.png",20,18,true,false);
		column3.setCellValueFactory(c -> {
			if (c.getValue().getFlagged()) {
				return new SimpleObjectProperty<ImageView>(new ImageView(flag));
			} else return null;
		});
		this.getColumns().add(column1);
		this.getColumns().add(column2);
		this.getColumns().add(column3);
	}
}
