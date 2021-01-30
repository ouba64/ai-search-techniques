package techniques;

import java.util.Random;

import autres.Problem;
import autres.State;

public class AlgoGenetiqueDistance extends AlgoGenetique{
	public AlgoGenetiqueDistance(Problem problem, Random random, double deltaratio, double ratio01, int n,
			double mutationProbability, double threshold) {
		super(problem, random, deltaratio, ratio01, n, mutationProbability,threshold);
	}

	@Override
	public boolean isStop(State bestState) {
		if(bestState.distance(problem.getGoal())<threshold) {
			return true;
		}
		return false;
	}
}
