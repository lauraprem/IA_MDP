package agent.planningagent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.HashMapUtil;
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

	private double		gamma;

	private HashMapUtil	previousStates;

	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma, MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		previousStates = new HashMapUtil();
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
		// TODO 1 + mémoriser la liste
		// mdp.getRecompense(_e, _a, _es)
		double valeurMax;
		this.delta = 0.0;
		Map<Etat, Double> probaCaseDest;

		for (Etat etat : mdp.getEtatsAccessibles()) {
			try {
				// Pour la première action
				probaCaseDest = mdp.getEtatTransitionProba(etat, mdp.getActionsPossibles(etat).get(0));
				Set listKeys = probaCaseDest.keySet();
				Iterator iterateur = listKeys.iterator();
				while (iterateur.hasNext()) {
					Etat key = (Etat) iterateur.next();
					valeurMax = probaCaseDest.get(key) * (mdp.getRecompense(etat, mdp.getActionsPossibles(etat).get(0), key) + gamma * previousStates.get(key));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// valeurMax = mdp.getEtatTransitionProba(etat,
			// mdp.getActionsPossibles(etat).get(0))*;
			for (int i = 1; i < mdp.getActionsPossibles(etat).size(); i++) {

			}
		}

		// mise a jour vmax et vmin pour affichage
		// ...

		// ******************* a laisser a la fin de la methode
		this.notifyObs();
	}

	/**
	 * renvoi l'action donnee par la politique 
	 */
	@Override
	public Action getAction(Etat e) {
		// *** VOTRE CODE
		// TODO 3

		return ActionGridworld.NONE;
	}

	@Override
	public double getValeur(Etat _e) {
		// *** VOTRE CODE

		return 0.0;
	}

	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans etat (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		List<Action> l = new ArrayList<Action>();
		// *** VOTRE CODE
		// TODO 2

		return l;

	}

	@Override
	public void reset() {
		super.reset();
		// *** VOTRE CODE

		/*-----------------*/
		this.notifyObs();

	}

	@Override
	public void setGamma(double arg0) {
		gamma = arg0;
	}

}
