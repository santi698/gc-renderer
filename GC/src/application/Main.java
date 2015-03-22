package application;
	
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import scenes.SampleScene;


public class Main extends Application {
	AnchorPane rootLayout;
	@FXML ImageView image;
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("../MainView.fxml"));
			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout);
			image = (ImageView) scene.lookup("#image");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			Image i = new SampleScene().render();
			try {
				File file = new File("test.png");
				ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png", file);
				image.setImage(i);
				
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
}
