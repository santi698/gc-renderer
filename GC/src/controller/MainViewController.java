package controller;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class MainViewController {
	private Main mainApp;
	@FXML
	private ImageView image;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private void initialize() {
		assert (image!= null);
	}
	public void setMainApp(Main main) {
		mainApp = main;
	}
	public void setImage(Image img) {
		image.setImage(img);
		mainApp.getPrimaryStage().setHeight(img.getHeight()+25);
		mainApp.getPrimaryStage().setWidth(img.getWidth());
	}
	public void setProgress(double progress) {
		progressBar.setProgress(progress);
	}
}