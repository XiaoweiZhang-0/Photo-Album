package controller;

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
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Admin;
import model.User;

/**
 * @author Xiaowei Zhang
 */
public class UserController implements LogoutController{
	@FXML
	public ListView<Album> listview;
	
    @FXML
	public Button mLogOff, mDisplay, mOpenAlbum, mRenameAlbum, mDeleteAlbum, mSearch, mAddAlbum;
	
	@FXML
	public MenuButton mSortBy;
	
	@FXML
	public Text tUser, tNumber, tDateSpan;
	
	@FXML
	public TextField tfName, tfNewAlbum;
	

	public static String username;
	
	public static ArrayList<Album> albumlist = new ArrayList<Album>();
	
	public ObservableList<Album> observableList;	
	
	public static Admin adminuser = Photos.admin;
	
	public static User user;
	
	public static boolean stock;
	

	
	/** 
	 * @param app_stage
	 */
	public void start(Stage app_stage) {
		update();
		
		app_stage.setTitle(username + " Collection of Photos");
		if(!albumlist.isEmpty()) {
    		listview.getSelectionModel().select(0);
		}
	
		if (albumlist.size() > 0) {
			tfName.setText(albumlist.get(0).Getname());	
			tNumber.setText("Number of Photos: " + albumlist.get(0).photoCount());
			tDateSpan.setText("Date Span: \n\t" + albumlist.get(0).getFirstDate() + "\n\t" + albumlist.get(0).getLastDate());
		}
		listview.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> updateContent(newValue) );
	}
	

	
	/** 
	 * @param newValue
	 */
	private void updateContent(Album newValue) {
		if (newValue != null) {
			tfName.setText(newValue.Getname());	
			tNumber.setText("Number of Photos: " + newValue.photoCount());
			tDateSpan.setText("Date Span: \n\t" + newValue.getFirstDate() + " \n\t" + newValue.getLastDate());
		}
	}
	

	public void updateContentBack() {
		if (albumlist.size() > 0) {
			Album alb = listview.getSelectionModel().getSelectedItem();
			tNumber.setText("Number of Photos: " + alb.photoCount());
			tDateSpan.setText("Date Span: \n\t" + alb.getFirstDate() + "\n\t" + alb.getLastDate());
		}
	}
	

	
	/** 
	 * @throws IOException
	 */
	public void addAlbum() throws IOException {
		String albumname = tfNewAlbum.getText().trim();
		if(!albumname.isEmpty() && albumname != null && !user.album_exist(albumname)) {
			user.create_album(albumname);
			update();
			tfNewAlbum.clear();
		}
		User.save(user);	
	}
	

	
	/** 
	 * @throws IOException
	 */
	public void renameAlbum() throws IOException {
		String newName = tfName.getText().trim();
		int index = listview.getSelectionModel().getSelectedIndex();
		Album album = user.get_albums().get(index);
		if (newName.length() != 0 && !newName.equals(album.Getname()) && !user.album_exist(newName)) {
			album.SetName(newName);
			update();
			User.save(user);
		}
	}
	
	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void openAlbum(ActionEvent event) throws IOException {
		PhotoViewController.user = user;
		PhotoViewController.album = listview.getSelectionModel().getSelectedItem();
		PhotoViewController.albumlist = albumlist;
		int albumindex = listview.getSelectionModel().getSelectedIndex();
		if(user.get_albums().size() != 0) { 
		Album album = user.get_albums().get(albumindex);
		user.setCurrentAlbum(album);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));
		Parent sceneManager = (Parent) fxmlLoader.load();
		PhotoViewController photoController = fxmlLoader.getController();
		Scene adminScene = new Scene(sceneManager);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		photoController.start(appStage);
		appStage.setScene(adminScene);
		appStage.show();
		}
	}
	

	
	/** 
	 * @throws IOException
	 */
	public void deleteAlbum() throws IOException 
	{
		if(user.get_albums().size() != 0)
		{
			int index = listview.getSelectionModel().getSelectedIndex();
			user.delete_album(index);
			update();
			User.save(user);
		}

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
	public void logOut(ActionEvent event) throws IOException {
		logMeOut(event);
	}
	

	public void update() {
		tUser.setText(username + "'s Album List:");
		user = adminuser.GetUser(username);
		if(!albumlist.isEmpty())
		{
			albumlist.clear();
		}
		for(Album album : user.get_albums())
		{
			albumlist.add(album);
		}
		observableList = FXCollections.observableArrayList(albumlist);
		listview.setItems(observableList);
		listview.refresh();
	}
}
