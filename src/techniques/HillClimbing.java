package techniques;

import java.util.ArrayList;
import java.util.Random;

import autres.Problem;
import autres.State;
import autres.StateForImage;
import autres.Technique;
import imageProcessing.ConvexPolygon;
import imageProcessing.MonaLisa;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class HillClimbing extends Technique {
    int nSucessors = 20;
    Stage primaryStage;
    
	
    
	public HillClimbing(Problem problem, Random random, double deltaratio, double ratio01, int nSucessors,
			Stage primaryStage, double threshold) {
		super(problem, random, deltaratio, ratio01, threshold);
		this.nSucessors = nSucessors;
		this.primaryStage = primaryStage;
	}

	
	/*public HillClimbing(String path) {
		s0 = new StateForImage(path);
	}*/
	
	@Override
	public State execute() {
		State current = problem.getInitialState();
		current.save(0);
		
		//current.render(0, primaryStage);
		State neighbor;
		double d;
		for(int i=0; ; i++) {
			neighbor = successor(current);
			d = value(neighbor);
			if(true/*i%100 == 0*/) {
				
				System.out.println("Iteration " + i + " d = " + d);
			}
			// si on ne peut plus evoluer
			if(d > value(current)){				
				return current;
			}
			neighbor.save(i);
			// sinon il continue avec le voisin
			current = neighbor;
		}
	}
	
	public State execute2(Problem problem) {
		State current = problem.getInitialState();
		current.save(0);
		
		//current.render(0, primaryStage);
		State neighbor;
		double d;
		for(int i=0; ; i++) {
			neighbor = successor(current);
			d = value(neighbor);
			if(MonaLisa.DEBUG) {
				if(i%100==0) {
					neighbor.save(i);
				}
				System.out.println("Iteration " + i + " d = " + d);
			}
			// si on ne peut plus evoluer
			if(d < value(current)){				
				current = neighbor;
			}else 
			     if(value(current)==0) { 
			    	 return current ; 
			}
		}
	}




	private double value(State neighbor) {
		// d(s0, neighbor)
		double d = neighbor.distance(goal);
		return d;
	}

	private State successor(State current) {
		State successor ;
		State successorBest = null;
		double d;
		double dBest = Double.MAX_VALUE;
		for(int i=0; i<nSucessors; i++) {
			successor = getSuccessor(current);
			
			d = successor.distance(goal);
			if(MonaLisa.DEBUG) {
				System.out.println("       d(Successeur candidat) = " + d);
			}
			if(d<dBest) {
				successorBest = successor;
				dBest = d;
			}
		}
		return successorBest;
	}
	

	
	

	
	public StateForImage getS0() {
		return goal;
	}

	public void setS0(StateForImage s0) {
		this.goal = s0;
	}


}
