package controller;

import java.io.IOException;

import java.util.*;

import App.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import model.*;

/**
 * @author Xiaowei Zhang
 */


public class AdminController implements LogoutController {
	

	@FXML
	public ListView<String> listview;
	@FXML
	public Button mCreate, mDelete, mLogOff;
	@FXML
	public TextField tfUsername;
	

	public static ArrayList<String> userlist = new ArrayList<>();
	

	public ObservableList<String> observableList;
	

	public static Admin admin = Photos.admin;
	

	public void start() {
		update();
		if(!userlist.isEmpty()) {
    		listview.getSelectionModel().select(0);
		}
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void logOut(ActionEvent event) throws IOException {
		logMeOut(event);
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void addUser(ActionEvent event) throws IOException {
		String username = tfUsername.getText().trim();
		if (username.equals("admin")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Admin Error");
			alert.setContentText("Cannot add 'admin' to Users.");
			alert.showAndWait();
			return;
		} else if(!username.isEmpty() && username != null && admin.GetUserIndx(username) == -1){
			admin.addUser(username);
			update();
			tfUsername.clear();
		}
		Admin.save(admin);	
	}
	

	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void deleteUser(ActionEvent event) throws IOException 
    {

		int indx = listview.getSelectionModel().getSelectedIndex();
		int adminList = userlist.indexOf("admin");
		
				   
               if(indx == adminList)
               {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Delete User Error");
                    alert.setContentText("Cannot delete admin");
                    alert.showAndWait();
                    return;
               }
			   admin.deleteUser(indx);
			   update();
			   Admin.save(admin);
			   
			   if(admin.GetUsers().size() == 0) 
               {
					mDelete.setVisible(false);
		       }
			      
		
	}
	

	public void update() 
    {
		userlist.clear();
		extracted();
		listview.refresh();
		observableList = FXCollections.observableArrayList(userlist);
		listview.setItems(observableList);
		listview.refresh();

	
	}

	private void extracted() {
		for (int i = 0; i < admin.GetUsers().size(); i++) {
			userlist.add(admin.GetUsers().get(i).get_user_name());
		}
	}
	
	

}