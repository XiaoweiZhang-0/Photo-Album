package App;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Admin;

/**
 * @author Xiaowei Zhang
 */

 
public class Photos extends Application{
    public static Admin admin = new Admin();
    public Stage mainStage;
   
	

	
	/** 
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			mainStage = primaryStage;
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			AnchorPane root = (AnchorPane) fxmlLoader.load();

			
			Scene scene = new Scene(root);
			mainStage.setResizable(false);
			mainStage.setTitle("Photo Library");
			mainStage.setScene(scene);
			mainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		mainStage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>(){
			public void handle(WindowEvent we) {
				try {
					Admin.save(admin);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	

	
	/** 
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		try {
			admin = Admin.load();
		}catch (IOException e) {
			e.printStackTrace();
		}
		launch(args);
	}
}
