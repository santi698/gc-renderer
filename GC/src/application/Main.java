package application;
	
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import scenes.SampleScene;
import controller.MainViewController;

public class Main extends Application {
	AnchorPane rootLayout;
	private Stage primaryStage;
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("../controller/MainView.fxml"));
			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout);
			MainViewController controller = loader.getController();
			controller.setMainApp(this);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			Image i = new SampleScene().render();
			controller.setImage(i);
			try {
				File file = new File("test.png");
				ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png", file);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		return;
	}
	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
