package controller;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class MainViewController {
	private Main mainApp;
	@FXML
	private ImageView image;

	@FXML
	private void initialize() {
		assert (image!= null);
	}
	public void setMainApp(Main main) {
		mainApp = main;
	}
	public void setImage(Image img) {
		image.setImage(img);
	}
}