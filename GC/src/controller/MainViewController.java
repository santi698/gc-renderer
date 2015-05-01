package controller;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
	private Label progressLabel;
	@FXML
	private void initialize() {
	}
	public void setMainApp(Main main) {
		mainApp = main;
		mainApp.getPrimaryStage().setHeight(progressBar.getPrefHeight()+25);
		mainApp.getPrimaryStage().setWidth(progressBar.getPrefWidth());
	}
	public void setImage(Image img) {
		image.setImage(img);
		mainApp.getPrimaryStage().setHeight(img.getHeight()+25);
		mainApp.getPrimaryStage().setWidth(img.getWidth());
	}
	public void setProgress(double progress) {
		progressBar.setProgress(progress);
		progressLabel.setText(String.format("%.1f%%", progress*100));
	}
}