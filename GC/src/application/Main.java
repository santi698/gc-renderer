package application;
	
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import model.RayTracer;
import scenes.SampleScene;
import controller.MainViewController;

public class Main extends Application {
	BorderPane rootLayout;
	private Stage primaryStage;
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("../controller/MainView.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			MainViewController controller = loader.getController();
			controller.setMainApp(this);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Ray Tracer");
			primaryStage.show();
			RayTracer rayTracer = new RayTracer(new SampleScene());
			rayTracer.getProgressProperty().addListener((ob,o,n)->controller.setProgress(n.doubleValue()));
			CompletableFuture.runAsync(()->{
				BufferedImage i = rayTracer.render();
				Platform.runLater(()->controller.setImage(SwingFXUtils.toFXImage(i,null)));
				try {
					File file = new File("test.png");
					ImageIO.write(i, "png", file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

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
