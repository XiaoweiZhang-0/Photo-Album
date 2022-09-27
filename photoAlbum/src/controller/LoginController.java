package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Admin;
import model.User;
import App.Photos;

/**
 * @author Xiaowei Zhang
 */

public class LoginController {
	@FXML public Button mLogIn;
	
	@FXML public TextField tfUsername;

	public static Admin admin = Photos.admin;


	
	/** 
	 * @param event
	 * @throws IOException
	 */
	public void login(ActionEvent event) throws IOException {
		
		String username = tfUsername.getText().trim();
		

		if (username.equals("admin")) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
			Parent sceneManager = (Parent) fxmlLoader.load();
			AdminController adminController = fxmlLoader.getController();
			Scene adminScene = new Scene(sceneManager);
			Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			adminController.start();
			appStage.setScene(adminScene);
			appStage.show();
		}
		else if (admin.GetUserIndx(username) != -1) {
			User currentUser = admin.GetUser(username);
			UserController.username = username;
			UserController.user = currentUser;
			admin.setCurrent(currentUser);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/User.fxml"));
			Parent sceneManager = (Parent) fxmlLoader.load();
			UserController userController = fxmlLoader.getController();
			Scene userScene = new Scene(sceneManager);
			Stage appStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			userController.start(appStage);
			appStage.setScene(userScene);
			appStage.show();
		}
		
		
	}    
}
