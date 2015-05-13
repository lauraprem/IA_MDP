package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import environnement.Action;
import environnement.Etat;
import environnement.MDP;
import environnement.gridworld.ActionGridworld;

/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent {
	// *** VOTRE CODE

	private double gamma;
	private double[] values;
	private List<Action>[] actions;

	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma, MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		this.values = new double[mdp.getNbEtats()];
		this.actions = new List[mdp.getNbEtats()];
		for (int i = 0; i < mdp.getNbEtats(); i++) {
			this.values[i] = 0;
		}
	}

	public ValueIterationAgent(MDP mdp) {
		this(0.9, mdp);

	}

	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration 
	 */
	@Override
	public void updateV() {
		this.delta = 0.0;
		double[] ancient_values = values.clone();
		List<Action> listAction;
		List<Etat> listEtat = this.getMdp().getEtatsAccessibles();
		for (Etat etat : listEtat) {
			listAction = this.getMdp().getActionsPossibles(etat);
			if (!etat.estTerminal() ) {
				double maxAction = -1000;
				for (Action action : listAction) {
					double somme = 0;
					try {
						Map<Etat, Double> proba = this.getMdp().getEtatTransitionProba(etat, action);
						for (Etat etatsAtteignables : proba.keySet()) {
							double recompense = this.getMdp().getRecompense(etat, action, etatsAtteignables);
							somme += proba.get(etatsAtteignables) * (recompense + this.gamma * ancient_values[etatsAtteignables.indice()]);
						} 
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					maxAction = Math.max(somme, maxAction);
				}
				this.vmax = Math.max(this.vmax, maxAction);
				this.vmin = Math.min(this.vmin, maxAction);
				this.values[etat.indice()] = maxAction;
				this.actions[etat.indice()] = this.getMdp().getActionsPossibles(etat);
			}
		}

		// ******************* a laisser a la fin de la methode
		this.notifyObs();
	}

	/**
	 * renvoi l'action donnee par la politique 
	 */
	@Override
	public Action getAction(Etat e) {
		List<Action> actions = this.getPolitique(e);
		if (actions.size() == 0)
			return ActionGridworld.NONE;
		int r = rand.nextInt(actions.size());// random entre 0 inclu et param
												// exlu
		return actions.get(r);
	}

	@Override
	public double getValeur(Etat _e) {
		return this.values[_e.indice()];
	}

	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans etat (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat etat) {
		List<Action> listAction = new ArrayList<Action>();
		double max = 0;
		for (Action action : this.getMdp().getActionsPossibles(etat)) {
			try {
				Map<Etat, Double> proba = this.getMdp().getEtatTransitionProba(etat, action);
				double sum = 0;
				for (Etat etatPossible : proba.keySet()) {
					sum += proba.get(etatPossible) * (mdp.getRecompense(etat, action, etatPossible) + this.gamma * values[etatPossible.indice()]);
				}
				if (max < sum) {
					max = sum;
					listAction.clear();
					listAction.add(action);
				} else if (max == sum) {
					listAction.add(action);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return listAction;

	}

	@Override
	public void reset() {
		super.reset();
		this.values = new double[mdp.getNbEtats()];
		this.notifyObs();

	}

	@Override
	public void setGamma(double arg0) {
		gamma = arg0;
	}

}
