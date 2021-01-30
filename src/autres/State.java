package autres;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import imageProcessing.ConvexPolygon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Une State est une representation de l'état courant de l'avancée de l'algorithme.
 * Une State ne peut representer qu'un seul état (un ensemble de polygone avec des caractéristiques 
 * bien définies), donc dès qu'on change une caractéristique, il faut créer un nouveau State.
 * @author Ouba
 *
 */
public class State  {
	List<ConvexPolygon> polys;
	WritableImage wimg;
	Group image;
	int maxX;
	int maxY;
	/**
	 * Distance de ce State par rapport à la goal. Cette variable nous d'éviter de faire plusieurs
	 * appel à la méthode distance qui est une méthode très couteuse.
	 */
	Double distance;
	

	public State(State current) {
		polys = new ArrayList<>();
		for(ConvexPolygon poly : current.getPolys()) {			
			try {
				polys.add((ConvexPolygon)poly.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		initialiser();
	}
	
	public State(List<ConvexPolygon> polys) {
		this.polys = polys;
		initialiser();
	}

	protected void initialiser() {
		maxX = polys.get(0).getMax_X();
		maxY = polys.get(0).getMax_Y();
		// formation de l'image par superposition des polygones
		image = new Group();
		for (ConvexPolygon p : polys) {
			image.getChildren().add(p);
		}
		// Calcul de la couleur de chaque pixel.Pour cela, on passe par une instance de
		// WritableImage, qui possède une méthode pour obtenir un PixelReader.
		wimg = new WritableImage(maxX, maxY);
		takeSnapshot();
	}
	
	@Override
	public boolean equals(Object obj) {
		State s2 ;
		// meme type
		if(obj instanceof State) {
			s2 = (State) obj;
			// poly
			for (int i=0; i<polys.size(); i++) {
				ConvexPolygon p1 = polys.get(i);
				ConvexPolygon p2 = s2.getPolys().get(i);
				if(!p1.equals(p2)) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	
	public void takeSnapshot() {
		image.snapshot(null, wimg);
	}


	public List<ConvexPolygon> getPolys() {
		return polys;
	}

	public void setPolys(List<ConvexPolygon> polys) {
		this.polys = polys;
	}
	
	public double distance(StateForImage goal) {
		if(distance!=null) {
			return distance;
		}
		PixelReader pr = wimg.getPixelReader();
		// On utilise le PixelReader pour lire chaque couleur
		// ici, on calcule la somme de la distance euclidienne entre le vecteur (R,G,B)
		// de la couleur du pixel cible et celui du pixel de l'image générée
		double res = 0;
		Color[][] target = goal.getTarget();
		for (int i = 0; i < getMaxx(); i++) {
			for (int j = 0; j < getMaxy(); j++) {
				Color c = pr.getColor(i, j);
				res += Math.pow(c.getBlue() - target[i][j].getBlue(), 2)
						+ Math.pow(c.getRed() - target[i][j].getRed(), 2)
						+ Math.pow(c.getGreen() - target[i][j].getGreen(), 2);
			}
		}
		distance = Math.sqrt(res);
		return distance;
	}
	
	public int getMaxx() {
		return maxX;
	}
	
	public int getMaxy() {
		return maxY;
	}
	
	// Stockage de l'image dans un fichier .png
	
	public void save(int iteration) {
		RenderedImage renderedImage = SwingFXUtils.fromFXImage(wimg, null);
		try {
			ImageIO.write(renderedImage, "png", new File("s" + iteration+".png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void render(int iteration, Stage primaryStage) {
		Group image = new Group();
		for (ConvexPolygon p : polys)
			image.getChildren().add(p);

		// Calcul de la couleur de chaque pixel.Pour cela, on passe par une instance de
		// WritableImage, qui possède une méthode pour obtenir un PixelReader.
		WritableImage wimg = new WritableImage(maxX, maxY);
		image.snapshot(null, wimg);
		
		// affichage de l'image dans l'interface graphique
		Scene scene = new Scene(image, maxX, maxY);
		primaryStage.setScene(scene);

	}

	public WritableImage getWimg() {
		return wimg;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}
	
	@Override
	public String toString() {
		String res = "";
		if(polys!=null) {
			for(ConvexPolygon poly : polys) {
				res = res+ poly+" ";
			}
		}
		return res;
	}
	
	public void printOnScreen(StateForImage goal) {
		String res = toString()+ " " + String.format("[%20f]", distance(goal));
		System.out.println(res);
	}
	
	public static void printStateNumber(int n, int iteration) {
		System.out.println();
		System.out.println(iteration);
		for(int i =0; i<n; i++) {
			System.out.print("===");
		}
		System.out.println();
		for(int i =0; i<n; i++) {
			String output = String.format("%2d", i);
			System.out.print(output+" ");
		}
		System.out.println();
		for(int i =0; i<n; i++) {
			System.out.print("---");
		}
		System.out.println();
	}	
}
