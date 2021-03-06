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

import model.PathTracer;
//import model.RayTracer;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import scenes.SampleScene;
import scenes.SceneFromFile;
import controller.MainViewController;

public class Main extends Application {
	private static CommandLine cl;
	private static Options options;
	public static int AASamples;
	BorderPane rootLayout;
	private Stage primaryStage;
	@Override
	public void start(Stage primaryStage) throws IOException {
		File dir = new File("renders");
		dir.mkdir();
		int rayDepth = -1;
		if (cl.hasOption("d"))
			rayDepth = Integer.parseInt(cl.getOptionValue("d"));
		int aaSamples = Integer.parseInt(cl.getOptionValue("aa"));
		scenes.Scene scene;
		if (cl.hasOption("i")) {
			scene = new SceneFromFile(cl.getOptionValue("i"));
		} else {
			scene = new SampleScene();
		}
		PathTracer rayTracer = new PathTracer(scene, aaSamples, rayDepth, cl.hasOption("time"));
		String filename = cl.getOptionValue("o");
		if (!cl.hasOption("gui")) {
			if (!cl.hasOption("b")) {
				rayTracer.getProgressProperty().addListener((obj, o, n)->printProgress(n.doubleValue()));
				File file = new File("renders/"+ filename);
				BufferedImage i = rayTracer.render();
				ImageIO.write(i, "png", file);
				System.out.println("Imagen escrita a: " + file.getAbsolutePath());
			} else {
				int times = Integer.parseInt(cl.getOptionValue("b"));
				long time = System.currentTimeMillis();
				for (int i = 0; i < times; i++) {
					rayTracer.render();
				}
				System.out.println("Terminado.");
				System.out.println("Tiempo promedio por render: " + (System.currentTimeMillis()-time)/1000/times + " segundos");
				System.out.println("Tiempo total: " + (System.currentTimeMillis()-time)/1000 + " segundos");
			}
			System.exit(0);
		}
		this.primaryStage = primaryStage;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainViewController.class.getResource("MainView.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene fxScene = new Scene(rootLayout);
			MainViewController controller = loader.getController();
			controller.setMainApp(this);
			fxScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(fxScene);
			primaryStage.setTitle("Ray Tracer");
			primaryStage.show();
			rayTracer.getProgressProperty().addListener(
					(ob,o,n)->{
						Platform.runLater(()->controller.setProgress(n.doubleValue()));	
			});
			CompletableFuture.runAsync(()->{
				BufferedImage i = rayTracer.render();
				Platform.runLater(()->controller.setImage(SwingFXUtils.toFXImage(i,null)));
				try {
					File file = new File("renders/"+ filename);
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
		cl = setUpCliParser(args);
		if (cl.hasOption("help")) {
			printHelp();
			System.exit(0);
		}
		AASamples = Integer.parseInt(cl.getOptionValue("aa"));
		launch(args);
		return;
	}
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	@SuppressWarnings("static-access")
	public static CommandLine setUpCliParser(String[] args) {
		// create Options object
		options = new Options();
		
		Option gui = new Option("gui", "Mostrar la interfaz gráfica");
		Option help = new Option("help", "Mostrar la ayuda");
		Option time = new Option("time", "Mostrar en consola el tiempo que llevó el renderizado.");
		Option inputFile = OptionBuilder.withArgName("archivo")
                .hasArg()
                .withDescription("nombre del archivo de escena")
// 	            .isRequired() FIXME cambiar cuando el parser esté listo
                .create("i");
		
		Option outputFile = OptionBuilder.withArgName("archivo")
                .hasArg()
                .withDescription("nombre del archivo generado")
                .isRequired()
                .create("o");
		Option aaSamples = OptionBuilder.withArgName("cantidad de muestras")
                .hasArg()
                .withDescription("cantidad de muestras de antialiasing" )
                .isRequired()
                .create("aa");
		Option benchmark = OptionBuilder.withArgName("cantidad de veces")
                .hasArg()
                .withDescription("cantidad de veces que se corre el renderer")
                .create("benchmark");
		Option rayDepth = OptionBuilder.withArgName("profundidad")
                .hasArg()
                .withDescription("profundidad de reflejos y refracciones")
                .create("d");

		options.addOption(gui);
		options.addOption(time);
		options.addOption(help);
		options.addOption(inputFile);
		options.addOption(outputFile);
		options.addOption(aaSamples);
		options.addOption(benchmark);
		options.addOption(rayDepth);
		
	    CommandLineParser parser = new BasicParser();
	    try {
	        // parse the command line arguments
	        CommandLine line = parser.parse(options, args);
	        return line;
	    }
	    catch(ParseException exp) {
	        printHelp();
	    }
	    System.exit(1);
	    return null;
	}
	private static void printHelp() {
	     // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("raytracer", options, true);
	}
	private static void printProgress(double progress) {
		if (System.currentTimeMillis() % 500 == 0)
			System.out.printf("%2.1f%%\r", progress*100);
	}
}
