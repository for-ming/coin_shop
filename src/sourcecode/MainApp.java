package sourcecode;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sourcecode.controller.LoginLayoutController;
import sourcecode.controller.RegisterMemberDialogController;

import sourcecode.model.Person;

import sourcecode.controller.RootLayoutController;


public class MainApp extends Application {

	private Stage primaryStage;
	private Stage loginPopup;
	private BorderPane rootLayout;
	
	private LoginLayoutController loginController;
	@Override
	public void start(Stage primaryStage) throws Exception {

		this.primaryStage = primaryStage;

		this.primaryStage.setTitle("CRUD JavaFX");

		primaryStage.getIcons()
				.add(new Image(getClass().getResourceAsStream("/resources/images/informacao-pessoal.png")));

		if(initRootLayout()) {
			showLoginLayout();
			// showPersonLayout();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Unexpected error");
			alert.setContentText("An error occurred while trying to start the program");
			alert.showAndWait();
		}
		
		loginPopup.setOnCloseRequest(event -> {
			terminate();
		});

	}
	
	public static void terminate() {
		Platform.exit();
	}
	public boolean initRootLayout() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(MainApp.class.getResource("view/fxml/RootLayout.fxml"));
			rootLayout = (BorderPane) fxmlLoader.load();

			
			RootLayoutController rootController = fxmlLoader.getController();
	
			rootController.setMainApp(this);
			rootController.setDialogStage(primaryStage);
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
	
			rootController.createSegment();
			
			//rootLayout.setCenter(rootController.getAllProduct());
			//rootLayout.setCenter(rootController.getMyProduct());
			//rootLayout.setCenter(rootController.getRankChart());
			primaryStage.show();
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public boolean showLoginLayout() {
		loginPopup = new Stage(StageStyle.UTILITY);
		loginPopup.initModality(Modality.WINDOW_MODAL);
		loginPopup.initOwner(primaryStage);
		
		try {
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(MainApp.class.getResource("view/fxml/LoginLayout.fxml"));

			Parent loginLayout = fxmlloader.load();

			loginPopup.setScene(new Scene(loginLayout));
			loginPopup.setResizable(false);
			
			
			loginController = fxmlloader.getController();
			loginController.setMainApp(this);
			
			loginPopup.show();	

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if(loginController.isLoginSuccess())
			return true;
		
		return true;
	}

	

	public static void main(String[] args) {
		launch(args);
	}

}
