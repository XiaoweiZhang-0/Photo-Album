package controller;

import java.io.*;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Admin;
import model.User;

/**
 * 
 * @author Xiaowei Zhang
 *
 */
public class PhotoViewController implements LogoutController {
	@FXML
	public ListView<Photo> listview;
	
	@FXML
	public ImageView displayArea;
	
	@FXML
	public Button mLogOff, mBack, mAdd, mDelete, mSlideshow, mSearch, mDisplay, mCopy, mMove;
	
	@FXML
	public TextField tfCopy, tfMove;
	
	@FXML 
	public Text tCaption, tDate;


	public static ArrayList<Photo> photolist = new ArrayList<>();
	

	public ObservableList<Photo> observableList;	
	

	public static Admin adminuser = Photos.admin;
	

	public static User user;
	

	public static ArrayList<Album> albumlist;
	

	public static Album album; 
	

	
	/** 
	 * @param app_stage
	 */
	public void start(Stage app_stage) {
		
		app_stage.setTitle(album.Getname() + " Album Page");
		displayArea.setFitHeight(100);
		displayArea.setFitWidth(100);
		displayArea.setPreserveRatio(false);
		update();
		if(album.getPhotos().size() == 0) {
			mDelete.setVisible(false);
		}
		if(!photolist.isEmpty()) {
    		listview.getSelectionModel().select(0); //select first user
		}
		
		if (photolist.size() > 0) {
			tCaption.setText("Caption: " + photolist.get(0).get_caption());
			tDate.setText("Date: " + photolist.get(0).get_date());
			displayThumbnail();
		}
		listview.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
			displayThumbnail();
			updateCaption();
		});
	}
	

	
	/** 
	 * @throws IOException
	 */
	public void move() throws IOException {
		String moveAlbum = tfMove.getText().trim();
		boolean inList = false;
		int albumIndex = 0;
		for (int x = 0; x < albumlist.size(); x++) {
			Album tempalbum = albumlist.get(x);
			if (tempalbum.Getname().equals(moveAlbum)) {
				inList = true;
				albumIndex = x;
			}
		}

		// Move
		if (!moveAlbum.isEmpty() && inList) {
				Album newAlbum = albumlist.get(albumIndex);
				Photo photo = listview.getSelectionModel().getSelectedItem();
				newAlbum.add_photo(photo);
				album.remove_photo(listview.getSelectionModel().getSelectedIndex());
				
				Album.save(newAlbum);
				Album.save(album);
				update();
		} 

	}
	

	
	/** 
	 * @throws IOException
	 */
	public void copy() throws IOException {
		String copyAlbum = tfCopy.getText().trim();
		boolean inList = false;
		int albumIndex = 0;
		for (int x = 0; x < albumlist.size(); x++) {
			Album tempalbum = albumlist.get(x);
			if (tempalbum.Getname().equals(copyAlbum)) {
				inList = true;
				albumIndex = x;
			}
		}


		if (!copyAlbum.isEmpty() && inList) {
				Album newAlbum = albumlist.get(albumIndex);
				Photo photo = listview.getSelectionModel().getSelectedItem();
				newAlbum.add_photo(photo);
				
				Album.save(newAlbum);
				update();

		} 

	}
	

	public void displayThumbnail() {
		Photo photo = listview.getSelectionModel().getSelectedItem();
		File file;
		if (photo != null) {
			file = photo.getPic();
			if(user.get_user_name().equals("stock") && photo.is_stock) {
				String str = file.getAbsolutePath();
				File img = new File(str);
				Image image = new Image(img.toURI().toString());
				displayArea.setImage(image);
			}else {
				Image image = new Image(file.toURI().toString());
				displayArea.setImage(image);
			}	 
		} else {
			displayArea.setImage(null);
		}
		return;
	}
	

	public void updateCaption() {
		Photo photo = listview.getSelectionModel().getSelectedItem();
		if (photolist.size() > 0 && photo != null) {
			tCaption.setText("Caption: " + photo.get_caption());
			tDate.setText("Date: " + photo.get_date());
		} else {
			tCaption.setText("Caption: ");
			tDate.setText("Date: ");
		}
	}
	

	
	/** 
	 * @throws IOException
	 */
	public void addPhoto() throws IOException {
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.bmp", "*.jpeg", "*.gif");
		filechooser.getExtensionFilters().add(extFilterJPG);
		File imgfile = filechooser.showOpenDialog(null);
		
		if (imgfile != null) {
			String filepath = imgfile.getAbsolutePath();
				Photo newPhoto;
				if(user.get_user_name().equals("stock")) {
					int index;
					if (filepath.contains("stockphotos")) {
						index = filepath.indexOf("stockphotos");
						String newfilepath = filepath.substring(index,filepath.length());
						Photo newPhoto2 = new Photo(imgfile, newfilepath);
						newPhoto2.is_stock= true;
						album.add_photo(newPhoto2);
					} else {
						newPhoto = new Photo(imgfile, filepath);	
						album.add_photo(newPhoto);
					}
				} else {
					newPhoto = new Photo(imgfile, filepath);	
					album.add_photo(newPhoto);
				}
				update();
			
		}
		if(album.getPhotos().size() > 0) {
			mDelete.setVisible(true);
		}
		if(!photolist.isEmpty()) {
    		listview.getSelectionModel().select(0); 
		}
		
		Album.save(album);
		
	}
	

	
	/** 
	 * @throws IOException
	 */
	public void deletePhoto() throws IOException {

		int index = listview.getSelectionModel().getSelectedIndex();
			album.remove_photo(index);
			update();
			   
			if (album.getPhotos().size() == 0) {
				mDelete.setVisible(false);
		    }else {  
		    	int lastuserindex = album.getPhotos().size();
				if (album.getPhotos().size() == 1) {
					listview.getSelectionModel().select(0);
				} else if (index == lastuserindex) {
					listview.getSelectionModel().select(lastuserindex-1);
				} else { 
					listview.getSelectionModel().select(index);
				}
			}
			Album.save(album);

	}
	

	public void update() {
		photolist.clear();
		for (int i = 0; i < album.getPhotos().size(); i++) {
			photolist.add(album.getPhotos().get(i));
		}

		observableList = FXCollections.observableArrayList(photolist);
		listview.setItems(observableList);
		listview.refresh();
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void search(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Search.fxml"));
		Parent sceneManager = (Parent) fxmlLoader.load();
		SearchController searchController = fxmlLoader.getController();
		Scene adminScene = new Scene(sceneManager);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		searchController.start();
		appStage.setScene(adminScene);
		appStage.show();	
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void display(ActionEvent event) throws IOException {
		if (photolist.size() > 0) {
			boolean checked = false;
			for (int x = 0; x < photolist.size(); x++) {
				if (listview.getSelectionModel().isSelected(x)) {
					checked = true;
				}
			}
			
			if (checked) {
				int photoindex = listview.getSelectionModel().getSelectedIndex();
				Photo currentphoto = album.getPhotos().get(photoindex);
				album.setCurrentPhoto(currentphoto);
				SinglePhotoController.photo = listview.getSelectionModel().getSelectedItem();
				SinglePhotoController.album = album;
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SinglePhoto.fxml"));
				Parent sceneManager = (Parent) fxmlLoader.load();
				SinglePhotoController singlePhotoController = fxmlLoader.getController();
				Scene adminScene = new Scene(sceneManager);
				Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				singlePhotoController.start(appStage);
				appStage.setScene(adminScene);
				appStage.show();	
			}
		}
		
	}

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void slideshow(ActionEvent event) throws IOException {
		if (photolist.size() != 0) {
			SlideshowController.album = photolist;
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Slideshow.fxml"));
			Parent sceneManager = (Parent) fxmlLoader.load();
			SlideshowController slideshowController = fxmlLoader.getController();
			Scene adminScene = new Scene(sceneManager);
			Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			slideshowController.start();
			appStage.setScene(adminScene);
			appStage.show();
		} 
	}
	
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void back(ActionEvent event) throws IOException {
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
	public void logOut(ActionEvent event) throws IOException {
		logMeOut(event);
	}

}