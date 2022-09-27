
package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import App.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.Tag;

/**
 * @author Xiaowei Zhang
 */

public class SearchController implements LogoutController {

	
	@FXML
	public Button mBack, mLogOff, mAddTag, mAnySearch, mAllSearch, mSearchDate, mCreateAlbum;
	
	@FXML
	public TextField tfName, tfValue;
	
	@FXML
	public ListView<String> listview;
	
	@FXML
	public ListView<Photo> photolistview;
	
	@FXML
	public ImageView imageview;
	
	@FXML
	public DatePicker dTo, dFrom;
	

	public ArrayList<Tag> taglist = new ArrayList<Tag>();
	

	public ArrayList<String> tagdisplay = new ArrayList<String>();
	

	public ObservableList<String> obsTag;
	

	public ObservableList<Photo> obsPhoto;
	

	public ArrayList<Photo> photolist = new ArrayList<Photo>();
	
	

	public void start() {
		
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void searchByDate(ActionEvent event) throws IOException {
		this.photolist.clear();
		LocalDate from = dFrom.getValue();
		LocalDate to = dTo.getValue();
		
		if(from != null && to != null && !to.isBefore(from)) {
			mAnySearch.setVisible(false);
			mAnySearch.setDisable(true);
			mAllSearch.setVisible(false);
			mAllSearch.setDisable(true);
		this.photolist = Photos.admin.getCurrent().getPhotosInRange(from,to);
		}
		displayPhotos();
		
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void orSearch(ActionEvent event) throws IOException{
		this.photolist.clear();
		if(!taglist.isEmpty() && taglist != null) {
			mAllSearch.setVisible(false); 
			mAllSearch.setDisable(true);
			mSearchDate.setVisible(false);
			mSearchDate.setDisable(true);
			this.photolist = Photos.admin.getCurrent().getOrTaggedPhotos(taglist);
			displayPhotos();
		}
		
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void andSearch(ActionEvent event) throws IOException {
		if(!taglist.isEmpty() && taglist != null) {
			mAnySearch.setVisible(false);
			mAnySearch.setDisable(true);
			mSearchDate.setVisible(false);
			mSearchDate.setDisable(true);
			this.photolist = Photos.admin.getCurrent().getAndTaggedPhotos(taglist);
			displayPhotos();
		}

	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void addTag(ActionEvent event) throws IOException {
		if(!tfName.getText().trim().isEmpty() && !tfValue.getText().trim().isEmpty()) {		
			Tag tag = new Tag(tfName.getText().trim(), tfValue.getText().trim());
			taglist.add(tag);
			updateTagList();
		}

	}
	

	public void updateTagList() {
		tagdisplay.clear();
		int i=0;
		while(i<taglist.size())
		{
			tagdisplay.add("Name: " + taglist.get(i).name() +    " | Value: " + taglist.get(i).value());
			i++;
		}
		obsTag = FXCollections.observableArrayList(tagdisplay);
		listview.setItems(obsTag);
		tfName.clear();
		tfValue.clear();

	}
	

	public void displayPhotos() {
		obsPhoto = FXCollections.observableArrayList(photolist);
		
		photolistview.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
			@Override
			public ListCell<Photo> call(ListView<Photo> p){
				return new Results();
			}
			
		});
		
		photolistview.setItems(obsPhoto);
		
	    if(!obsPhoto.isEmpty()) {
	    		photolistview.getSelectionModel().select(0); 
	    }
		
	}
	

	private class Results extends ListCell<Photo>{
		AnchorPane anchor = new AnchorPane();
		StackPane stackpane = new StackPane();
		
		ImageView imageView = new ImageView();
		
		public Results() {
			super();
			
			imageView.setFitWidth(100.0);
			imageView.setFitHeight(100.0);
			imageView.setPreserveRatio(true);

			StackPane.setAlignment(imageView, Pos.TOP_LEFT);
			stackpane.getChildren().add(imageView);			
			stackpane.setPrefHeight(110.0);
			stackpane.setPrefWidth(90.0);
			
			AnchorPane.setLeftAnchor(stackpane, 0.0);
			anchor.getChildren().addAll(stackpane);
			anchor.setPrefHeight(110.0);
			setGraphic(anchor);	
			
		}
		
		@Override
		public void updateItem(Photo photo, boolean empty) {
			super.updateItem(photo, empty);
			setText(null);
			if(photo != null)
			{				
				Image img = new Image(photo.getPic().toURI().toString());
				imageView.setImage(img);				
			}
			
		}
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void createAlbum(ActionEvent event) throws IOException{
		if(photolist.isEmpty()){
			return;		
			
		}
		Dialog<String> dialog = new Dialog<>();
		   dialog.setTitle("Create a New Album from search results");
		   dialog.setHeaderText("Name for album created from search results ");
		   dialog.setResizable(true);
		   
		   Label albumnameLabel = new Label("Album Name: ");
		   TextField albumnameTextField = new TextField();
		   
		   GridPane grid = new GridPane();
		   grid.add(albumnameLabel, 1, 1);
		   grid.add(albumnameTextField, 2, 1);
		   
		   dialog.getDialogPane().setContent(grid);
		   
		   ButtonType buttonTypeOk = new ButtonType("Add", ButtonData.OK_DONE);
		   dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		   
		   dialog.setResultConverter(new Callback<ButtonType, String>() {
			   @Override
			   public String call(ButtonType b) {
				   if (b == buttonTypeOk && !albumnameTextField.getText().trim().isEmpty()) {
						String album_name = albumnameTextField.getText().trim();
						if(!Photos.admin.getCurrent().album_exist(album_name))
						{
							System.out.println(album_name);
						 return album_name;	
						 }
						 else
						 {
							 return null;
						 }
					}
					else
					{
						return null;
					}				   
				   }
			
		   });
		   
		   Optional<String> result = dialog.showAndWait();
		   
		   if (result.isPresent()) {
			   Album albumFromSearch = new Album(result.get());
			   Photos.admin.getCurrent().add_album(albumFromSearch);
			   for(Photo photo : photolist) {
				   albumFromSearch.add_photo(photo);
			   }
			   
		   }
		
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void back(ActionEvent event) throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/User.fxml"));
		Parent sceneManager = (Parent) fxmlLoader.load();
		UserController userController = fxmlLoader.getController();
		Scene adminScene = new Scene(sceneManager);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		userController.start(appStage);
		appStage.setScene(adminScene);
		appStage.show();
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void logOut(ActionEvent event) throws IOException{
		logMeOut(event);
	}


}