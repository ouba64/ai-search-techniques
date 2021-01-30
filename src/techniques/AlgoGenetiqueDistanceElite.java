package techniques;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import autres.Problem;
import autres.State;


/**
 * Dans cette variante de l'algo génétique, on ne choisi pas les parents de l'ensemble de la 
 * population, mais d'une selection des meilleurs individus que nous appelons 'Elite'. 
 * L'attribut cutoff définit cette selection,
 * il correspond au pourcentage des meilleurs individus qu'il faut considéré. Lors de chaque croisement,
 * le choix des parents est fait de manière aléatoire à partir de cette élite.
 *
 */
public class AlgoGenetiqueDistanceElite extends AlgoGenetiqueDistance {
	ArrayList<State> populationTriee;
	/**
	 * Si cutoff = 25%, les parents seront selectionnés au hasard parmi les 25% meilleurs individus
	 * de la population
	 */
	double cutoff = 0.3;
	public AlgoGenetiqueDistanceElite(Problem problem, Random random, double deltaratio, double ratio01,
			int n, double mutationProbability, double threshold, double cutoff) {
		super(problem, random, deltaratio, ratio01, n, mutationProbability,threshold);
		this.cutoff = cutoff;
	}
	
	@Override
	public double processNewPopulation(List<State> population) {
		this.populationTriee = (ArrayList<State>) population;
		trier(populationTriee);
		return 0d;
	}
	
	/**
	 * Le choix d'un des parents pour le croisement est réalisé sur la base des cutoff% meilleurs individus
	 * de la population courante.
	 */
	@Override
	protected State randomSelection(List<State> population, double som) {
		int nmax = (int) (cutoff * n);
		int i = random.nextInt(nmax);
		State state = populationTriee.get(i);
		return state;
	}

}
