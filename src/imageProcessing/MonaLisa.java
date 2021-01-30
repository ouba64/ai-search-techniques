package imageProcessing;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import autres.Problem;
import autres.State;
import autres.Technique;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.stage.Stage;
import techniques.AlgoGenetiqueDistance;
import techniques.AlgoGenetiqueDistanceElite;
import techniques.HillClimbing;
import techniques.LocalBeamSearch;

public class MonaLisa  extends Application{
	private static final long NO_TEST = 2;
	public static final boolean DEBUG = true;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {		
		String targetImage = "monaLisa-200.jpg";
		Problem problem = new Problem(targetImage);
		Random random  = new Random(NO_TEST);
		double deltaratio = (double)0.7;
		double ratio01 = (double)0.7;
		int nSucessors = 30;
		double threshold = 40;
		HillClimbing hillClimbing = new HillClimbing(problem, random, deltaratio,
				ratio01, nSucessors, primaryStage, threshold);
		
		
		int populationSize = 30;
		double mutationProbability = 0.3;
		AlgoGenetiqueDistance algoGenetiqueDistance = new AlgoGenetiqueDistance(problem, random, 
				deltaratio, ratio01, populationSize, mutationProbability, threshold);
		
		double cutoff = 0.3;
		AlgoGenetiqueDistanceElite algoGenitiqueDistanceNonStochastique = 
				new AlgoGenetiqueDistanceElite(problem, random, deltaratio, ratio01, 
						populationSize, mutationProbability, threshold, cutoff);
		
		int k = 10;
		LocalBeamSearch localBeamSearch = new LocalBeamSearch(problem, random, deltaratio, ratio01, threshold, k);
		
		Technique[] techniques = new Technique[] {hillClimbing};
		for(Technique technique : techniques) {
			State solution = technique.execute();
			solution.save(0);
		}
		

		

		
		// StockageA de l'image dans un fichier .png
		/*RenderedImage renderedImage = SwingFXUtils.fromFXImage(solution.getWimg(), null);
		try {
			ImageIO.write(renderedImage, "png", new File("solution.png"));
			System.out.println("wrote image in " + "test.png");
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		
		/*Scene image;
		image.snapshot(null, solution.getWimg());
		// affichage de l'image dans l'interface graphique
		Scene scene = new Scene(image, maxX, maxY);
		primaryStage.setScene(scene);
		primaryStage.show();*/
	}
}
