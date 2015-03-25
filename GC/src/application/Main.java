package application;
	
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import model.RayTracer;
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
			BufferedImage i = new RayTracer().render(new SampleScene());
			controller.setImage(SwingFXUtils.toFXImage(i,null));
			try {
				File file = new File("test.png");
				ImageIO.write(i, "png", file);
				
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
