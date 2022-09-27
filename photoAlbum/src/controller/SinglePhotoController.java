package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import App.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Admin;
import model.Tag;

/**
 * This class controls the view/ functions of a single image
 * @author Xiaowei Zhang
 *
 */
public class SinglePhotoController implements LogoutController {
	@FXML
	public ListView<String> listview;
	
	@FXML
	public ImageView displayArea;
	
	@FXML
	public Button mLogOff, mBack, mCaption, mAddTag, mRemoveTag;
	
	@FXML
	public TextField tfCaption, tfTagName, tfTagValue;


	public static Admin adminuser = Photos.admin;
	

	public static ArrayList<Tag> taglist = new ArrayList<>();
	
	public static ArrayList<String> tagdisplay = new ArrayList<>();
	

	public ObservableList<String> obstag;
	

	public static Photo photo; 


	public static Album album;
	

	
	/** 
	 * @param app_stage
	 */
	public void start(Stage app_stage) {
		app_stage.setTitle(album.getCurrenPhoto().get_caption() + " ");
		update();
		if(!taglist.isEmpty()) {
    		listview.getSelectionModel().select(0);
		}
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void saveCaption(ActionEvent event) throws IOException {
		String caption = tfCaption.getText().trim();
		photo.set_caption(caption);
		Photo.save(photo);
	}
	
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void addTag(ActionEvent event) throws IOException {
		String tagName = tfTagName.getText().trim();
		String tagValue = tfTagValue.getText().trim();
		if (!tagName.isEmpty() && !tagValue.isEmpty()) {
			Tag tag = new Tag(tagName, tagValue);
			album.getCurrenPhoto().add_tag(tag.name(), tag.value());
			update();
			Admin.save(adminuser);
		} 
	}
	
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void removeTag(ActionEvent event) throws IOException{
		int index = listview.getSelectionModel().getSelectedIndex();
	    ArrayList<Tag> taglist = album.getCurrenPhoto().get_taglist();
		album.getCurrenPhoto().remove_tag(taglist.get(index).name(), taglist.get(index).value());
		update();
		Admin.save(adminuser);
		
	}
	

	
	public void update() {
		File file;
		if (photo != null) {
			file = photo.getPic();
			Image image = new Image(file.toURI().toString());
			displayArea.setImage(image);
		}
		
		tagdisplay.clear();
		ArrayList<Tag> tags = album.getCurrenPhoto().get_taglist();
		
		for(Tag tag : tags) {
			tagdisplay.add("Name: " + tag.name() +    " | Value: " + tag.value());
		}
		obstag = FXCollections.observableArrayList(tagdisplay);
		listview.setItems(obstag);
		tfTagName.clear();
		tfTagValue.clear();
	}
	


	
	
	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void back(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));
		Parent sceneManager = (Parent) fxmlLoader.load();
		PhotoViewController photoViewController = fxmlLoader.getController();
		Scene adminScene = new Scene(sceneManager);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		photoViewController.start(appStage);
		appStage.setScene(adminScene);
		appStage.show();
	}
	
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void logOut(ActionEvent event) throws IOException {
		logMeOut(event);
	}
	
}