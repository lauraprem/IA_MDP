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
        double[]ancient_values = values.clone();
        List<Action> l;
        List<Etat> listEtat = this.getMdp().getEtatsAccessibles();
        for (Etat e : listEtat) {
            l = this.getMdp().getActionsPossibles(e);
            if (!e.estTerminal() && ancient_values[e.indice()] == 0) {
                double maxAction = -1000;
                for (Action a : l) {
                    double somme = 0;
                    try {
                        Map<Etat, Double> proba = this.getMdp().getEtatTransitionProba(e, a);
                        for(Etat e1:proba.keySet()){
                            double recompense = this.getMdp().getRecompense(e, a, e1);
                             somme += proba.get(e1) * (recompense + this.gamma * ancient_values[e1.indice()]);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    maxAction = Math.max(somme, maxAction);
                }
                this.vmax = Math.max(this.vmax, maxAction);
                this.vmin = Math.min(this.vmin, maxAction);
                this.values[e.indice()] = maxAction;
                this.actions[e.indice()] = this.getMdp().getActionsPossibles(e);
            }
        }


        //******************* a laisser a la fin de la methode
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
	        int r = rand.nextInt(actions.size());//random entre 0 inclu et param exlu
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
	public List<Action> getPolitique(Etat _e) {
	    List<Action> l = new ArrayList<Action>();
        double max = 0;
        for (Action a: this.getMdp().getActionsPossibles(_e)){
            try {
                Map<Etat, Double> proba = this.getMdp().getEtatTransitionProba(_e, a);
                double sum=0;
                for (Etat e :proba.keySet()) {
                    sum+=proba.get(e) * (mdp.getRecompense(_e,a,e) + this.gamma * values[e.indice()]);
                }
                if (max < sum) {
                    max = sum;
                    l.clear();
                    l.add(a);
                } else if (max == sum) {
                    l.add(a);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return l;

	}

	@Override
	public void reset() {
        super.reset();
        this.values= new double[mdp.getNbEtats()];
        this.notifyObs();

	}

	@Override
	public void setGamma(double arg0) {
		gamma = arg0;
	}

}
