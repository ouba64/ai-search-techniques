package techniques;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import autres.Problem;
import autres.State;
import autres.Technique;
import imageProcessing.MonaLisa;

public class LocalBeamSearch extends Technique {
	int k;

	public LocalBeamSearch(Problem problem, Random random, double deltaratio, double ratio01, double threshold) {
		super(problem, random, deltaratio, ratio01, threshold);
	}
	
	

	public LocalBeamSearch(Problem problem, Random random, double deltaratio, double ratio01, double threshold, int k) {
		super(problem, random, deltaratio, ratio01, threshold);
		this.k = k;
	}



	@Override
	public State execute() {
		List<State> states = problem.getInitialStates(k);
		List<State> successors;
		State successor;
		List<State> allSuccessors;
		while(true) {		
			allSuccessors = new ArrayList<>();
			for(State state : states) {
				successors = new ArrayList<>();
				for(int i=0; i<k; i++) {
					successor = getSuccessor(state);
					if(MonaLisa.DEBUG) {
						//state.printOnScreen(problem.getGoal());
					}
					successors.add(successor);					
				}
				allSuccessors.addAll(successors);
			}
			trier(allSuccessors);
			if(MonaLisa.DEBUG) {
				System.out.println();
				for(State state : allSuccessors) {
					state.printOnScreen(problem.getGoal());
				}
			}
			states=allSuccessors.subList(0, k);
			if(isStop(states.get(0))) {
				return states.get(0);
			}
		}
	}

}
