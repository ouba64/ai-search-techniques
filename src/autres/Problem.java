package autres;

import java.util.ArrayList;
import java.util.List;

import imageProcessing.ConvexPolygon;

public class Problem {
	int nPolys  = 50;
	StateForImage goal;
	
	public Problem(String path) {
		goal = new StateForImage(path);
	}

	public State getInitialState() {	
		// génération de npolys triangles
		List<ConvexPolygon> ls = new ArrayList<ConvexPolygon>();
		ConvexPolygon convexPolygon;
		for (int i=0;i<nPolys;i++) {
			convexPolygon = new ConvexPolygon(3, goal.getMaxX(), goal.getMaxY());
			/*convexPolygon.setMax_X(s0.getMaxX());
			convexPolygon.setMax_Y(s0.getMaxY());*/
			ls.add(convexPolygon);	
		}
		State state =new State(ls);
		return state;
	}
	
	public List<State> getInitialStates(int n){
		List<State> res = new ArrayList<>();
		State state;
		for(int i=0; i<n; i++) {
			state = getInitialState();
			res.add(state);
		}
		return res;
	}

	public int getnPolys() {
		return nPolys;
	}

	public void setnPolys(int nPolys) {
		this.nPolys = nPolys;
	}


	public StateForImage getGoal() {
		return goal;
	}

	public void setGoal(StateForImage goal) {
		this.goal = goal;
	}
	
	
}
