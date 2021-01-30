package techniques;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import autres.Problem;
import autres.State;
import autres.Technique;
import imageProcessing.ConvexPolygon;
import imageProcessing.MonaLisa;

public abstract class AlgoGenetique extends Technique{

	private static final String ESPACE = "                        ";

	/**
	 * Taille de la population
	 */
	int n;
	
	/**
	 * Probabilité de mutation
	 */
	double mutationProbability = 0.3;
	
	
	

	
	public AlgoGenetique(Problem problem, Random random, double deltaratio, double ratio01, int n,
			double mutationProbability, double threshold) {
		super(problem, random, deltaratio, ratio01, threshold);
		this.n = n;
		this.mutationProbability = mutationProbability;
	}
	@Override
	public State execute() {
		List<State> population = problem.getInitialStates(n);
		List<State> newPopulation;
		State x = null;
		State y = null;
		State[] children;
		State bestState = null;
		double bounds;

		double bestDistance = 0;
		int iteration = 0;
		while(true) {
			iteration++;
			bounds = processNewPopulation(population);
			if(MonaLisa.DEBUG) {
				State.printStateNumber(problem.getnPolys(), iteration);
				System.out.println("La population :");
				for(State state : population) {
					System.out.println(state + "(" + fitnessFn(state, bounds)+")  ("+state.distance(goal)+")");
				}
			}
			newPopulation = new ArrayList<>();
			for(int i=0; i<n/2; i++) {
				x = randomSelection(population, bounds);
				y = randomSelection(population, bounds);
				if(MonaLisa.DEBUG) {
					String constat;
					if(x.equals(y)) {
						constat = "pareils";
					}
					else {
						constat = "différents";
					}
					System.out.println("--------------  x et y sont "+constat+", croisement :  ");
					System.out.println(x+ ESPACE + x.distance(goal));
					System.out.println(y+ ESPACE + y.distance(goal));						
				}

				children = reproduce(x,y);
				if(MonaLisa.DEBUG) {
					System.out.println(children[0]+ ESPACE + children[0].distance(goal));
					System.out.println(children[1]+ ESPACE + children[1].distance(goal));
					System.out.println("--------------  ");
				}
				if(random.nextDouble() < mutationProbability) {
					// on mute toujours le premier children
					children[0] = mutate(children[0]);
					if(MonaLisa.DEBUG) {
						System.out.println("Il y a eu mutation,");
						System.out.println(children[0]+ ESPACE + children[0].distance(goal));
					}
				}
				newPopulation.add(children[0]);
				newPopulation.add(children[1]);
			}
			population = newPopulation;


			// retrouver le meilleur element de la population
			Object[] res = getBestStateOfPopulation(population);
			bestState=(State) res[0];
			bestDistance = (double) res[1];
			if(MonaLisa.DEBUG) {
				System.out.println("Le meilleur est :");
				System.out.println(bestState+ ESPACE + bestState.distance(goal));
				System.out.println("d="+ bestDistance );
			}
			// doit on continuer la recherche?
			if(isStop(bestState)) {
				return bestState;
			}	
			// si la nouvelle population ne contient qu'un seul individu
		}
	}
	
	public Object[] getBestStateOfPopulation(List<State> population) {
		double bestDistance = Double.MAX_VALUE;
		State bestState = null;
		// retrouver le meilleur element de la population
		for(State state : population) {
			if(state.distance(problem.getGoal())<bestDistance) {
				bestDistance = state.distance(problem.getGoal());
				bestState = state;
			}
		}
		return new Object[] {bestState, bestDistance};
	}
	
	
	public abstract boolean isStop(State bestState) ;
	

	
	private State mutate(State state) {	
		for(int i=0; i<1; i++) {
			State state2 = getSuccessor(state);
			state = state2;
		}
		return state;
	}
	private State[] reproduce(State x, State y) {
		int nPolys = problem.getnPolys();
		int c = 1+random.nextInt(nPolys-1);
		if(MonaLisa.DEBUG) {
			System.out.println("cut = " + c);
		}
		return reproduce(x, y, c,nPolys) ;
	}
	
	private State[] reproduce(State x, State y, int c, int n) {
		List<ConvexPolygon> polys1 = x.getPolys();
		List<ConvexPolygon> polys2 = y.getPolys();
		
		List<ConvexPolygon> polys1_ = new ArrayList<>();
		List<ConvexPolygon> polys2_ = new ArrayList<>();
		try {
			for(int i=0; i<c; i++) {
				polys1_.add((ConvexPolygon) polys1.get(i).clone());
				polys2_.add((ConvexPolygon) polys2.get(i).clone());
			}
			// le c est inclu dans la partie de droite
			for(int i=c; i<n; i++) {
				polys1_.add((ConvexPolygon) polys2.get(i).clone());
				polys2_.add((ConvexPolygon) polys1.get(i).clone());
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		State child1 = new State(polys1_);
		State child2 = new State(polys2_);
		return new State[] {child1, child2};
	}
	
	/**
	 * Calcule [dmin, dmax, pmin, pmax]
	 * @param population
	 * @return
	 */
	public double processNewPopulation(List<State> population) {
		double som=0;
		for(State s : population) {
			som+= s.distance(goal);
		}

		return som;
	}
	
	public double fitnessFn(State state, double som) {
		double fitness;
		double pi = state.distance(goal)/som;
		fitness = (1-pi)/(n-1);
		return fitness;
	}
	
	
	protected State randomSelection(List<State> population, double som) {
		double[] pCumule = new double[n+1];
		double p;
		State state;
		pCumule[0] = 0;
		System.out.print("Tableau cumulé : " + pCumule[0]);
		// creer un tableau de probabilités cumulées
		for(int i=0; i<n; i++) {
			state = population.get(i);
			pCumule[i+1] = pCumule[i] + fitnessFn(state, som);
			System.out.print(", " + pCumule[i+1]);
		}

		pCumule[n] = 1;

		// choisir un r
		double r = random.nextDouble();
		System.out.print("          | r=" + r);
		// ou est r dans le tableau ?
		for(int i=0; i<n; i++) {
			if(pCumule[i] < r && r<=pCumule[i+1]) {
				state = population.get(i);
				System.out.println(",   state selectionné " + i);
				//System.out.println(state + "(" + fitnessFn(state, som)+")");
				return state;
			}
		}
		return null;
	}

}
