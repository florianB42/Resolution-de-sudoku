package csp.solveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import csp.CSP;
import csp.Constraint;
import csp.ConstraintType;
import csp.Domain;
import csp.Variable;

public class Backtracking {
	private int profondeur = 0;
	
	public ArrayList<Variable> backtrakingSearch(CSP csp) throws CloneNotSupportedException {
		ArrayList<Variable> assignment = new ArrayList<Variable>();
		
		/*Domaine de définition possible pour chaque variable.*/
		HashMap<Variable, Domain> variablesDomains = new HashMap<Variable, Domain>();
		for (Variable variable : csp.getVariables()) {
			try {
				variablesDomains.put(variable, (Domain) variable.getDomaine().clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		
		
		return recursiveBacktraking(assignment, csp,variablesDomains);
	}
	
	private ArrayList<Variable> recursiveBacktraking(ArrayList<Variable> assignement, CSP csp, HashMap<Variable, Domain> variablesDomains) throws CloneNotSupportedException {
		/*Réduction du domaine de chaque variable par propagation des contraintes. Algorithme AC-3.*/
		try {
			variablesDomains = ac_3(csp, variablesDomains);
		} catch (ImpossibleToSatisfyConstraints e) {
			/*Si une variable a un domaine de définition vide, pas de solutions.*/
			return null;
		}
		
		/*Si l'assignement est complet on a trouvé une solution et on la retourne*/
		if (assignement.size() == csp.getVariables().size()) 
			return assignement;
		
		/*On choisit une variable selon MRV et degree heuristic */
		Variable var = selectUnasignedVariable(assignement, csp, variablesDomains);
		
		/*On parcourt toutes les valeurs du domaine dans l'ordre choisi par  least constraining value*/
		for (Object value : orderDomainValue(var, assignement, csp, variablesDomains)) {
			var.setValue(value);
			/*Vérfication si la valeur choisie donne un assignement consistant*/
			if (isConsistent(var, assignement, csp)) {
				assignement.add(var);
				/*réduction du domaine de la variable choisie à la valeur choisie*/
				Domain newDomain = new Domain();
				newDomain.addToDomain(value);
				variablesDomains.put(var, newDomain);
				
				/*Récusivité, Si le résultat est nul pas de solution trouvée*/
				ArrayList<Variable> result = recursiveBacktraking(assignement, csp,variablesDomains);
				if (result == null) {
					assignement.remove(var);
					var.setValue(null);
				}else {
					return result;
				}
			}else {
				var.setValue(null);
			}
		}
		return null;
	}

	private Variable selectUnasignedVariable(ArrayList<Variable> assignement, CSP csp,HashMap<Variable, Domain> variablesDomains) {
		/*MRV*/
		int min = Integer.MAX_VALUE;
		ArrayList<Variable> VariablesMinDomain = new ArrayList<Variable>();
		for (Variable variable : csp.getVariables()) {
			if (!assignement.contains(variable)) {
				if (variablesDomains.get(variable).size() < min) {
					min = variablesDomains.get(variable).size();
					VariablesMinDomain = new ArrayList<Variable>();
					VariablesMinDomain.add(variable);
				}
				else if (variablesDomains.get(variable).size() == min) {
					VariablesMinDomain.add(variable);
				}
			}
		}

		/*degree heuristic*/
		int maxOfConstraint = -1;
		Variable selectedVariable = null;
		for (Variable variable : VariablesMinDomain) {
			int count = 0;
			for(Constraint constraint : csp.getConstraints()) {
				if (variable.equals(constraint.getVar1())) {
					if (!assignement.contains(constraint.getVar2())) {
						count++;
					}
				}
			}
			if (count > maxOfConstraint) {
				maxOfConstraint = count;
				selectedVariable = variable;
			}
		}
		return selectedVariable;
	}
	
	private ArrayList<Object> orderDomainValue(Variable var, ArrayList<Variable> assignement, CSP csp, HashMap<Variable, Domain> variablesDomains) {
		/*trie du domaine de la variable sélectionnée selon least constraining value*/
		ArrayList<Variable> impactedVariable = new ArrayList<Variable>();
		for (Constraint constraint : csp.getConstraints()) {
			if (constraint.getVar1().equals(var)) {
				impactedVariable.add(constraint.getVar2());
			}
		}

		ArrayList<Integer> orderedReductionValue = new ArrayList<Integer>();
		ArrayList<Object> orderedDomainValue = new ArrayList<Object>();
		for (Object obj : variablesDomains.get(var).getDomain()) {
			int reduction = 0;
			for (Variable variable : impactedVariable) {
				if(variablesDomains.get(variable).getDomain().contains(obj)){
					reduction++;
				}
			}
			int i = 0;
			for (i = 0; i < orderedDomainValue.size();i++) {
				if (reduction < orderedReductionValue.get(i)) {
					orderedReductionValue.add(i, reduction);
					orderedDomainValue.add(i,obj);
					break;
				}
			}
			if(i >= orderedDomainValue.size()) {
				orderedReductionValue.add(reduction);
				orderedDomainValue.add(obj);
			}
		}
		return orderedDomainValue;
	}
	
	/*test de consistance de l'assignement*/
	private boolean isConsistent(Variable var, ArrayList<Variable> assignement, CSP csp) {
		for (Constraint constraite : csp.getConstraints()) {
			if (constraite.getVar1().equals(var)) {
				for (Variable assigneVar : assignement) {
					if (constraite.getVar2().equals(assigneVar)) {
						switch (constraite.getConstraintType()) {
						case different : 
							if (var.getValue().equals(assigneVar.getValue())) {
								return false;}
							break;
						}
					}
				}
			}
		}
		return true;
	}
	
	private HashMap<Variable, Domain> ac_3(CSP csp, HashMap<Variable, Domain> variablesDomains) throws ImpossibleToSatisfyConstraints, CloneNotSupportedException{
		/*copie des domaines avant la propagation des contraintes*/
		HashMap<Variable, Domain> newVariablesDomains = new HashMap<Variable, Domain>();
		for (Entry<Variable, Domain> entry : variablesDomains.entrySet()) {
			newVariablesDomains.put(entry.getKey(), (Domain) entry.getValue().clone());
		}
		LinkedList<Constraint> queue = new LinkedList<Constraint>();
		queue.addAll(csp.getConstraints());
		while (!queue.isEmpty()) {
			Constraint constraint = queue.pop();
			if(removeInconsistentValues(newVariablesDomains.get(constraint.getVar1()),newVariablesDomains.get(constraint.getVar2()), constraint.getConstraintType())) {
				if (newVariablesDomains.get(constraint.getVar1()).getDomain().size() == 0) {
					throw new ImpossibleToSatisfyConstraints();
				}
				for (Constraint Cons : csp.getConstraints()) {
					if (Cons.getVar2() == constraint.getVar1())
						queue.add(Cons);
				}
			}
		}
		return newVariablesDomains;
	}
	
	private boolean removeInconsistentValues(Domain var1Domain, Domain var2Domain, ConstraintType constraintType) {
		boolean removed = false;
		ArrayList<Object> toRemove = new ArrayList<Object>();
		switch(constraintType) {
		case different :
			for (Object object : var1Domain.getDomain()) {
				boolean satisfy = false;
				for (Object object2 : var2Domain.getDomain()) {
					if (!object2.equals(object)) {
						satisfy = true;
						break;
					}
				}
				if (!satisfy) {
					removed = true;
					toRemove.add(object);
				}
			}
			break;
		}
		
		for (Object objectToRemove : toRemove) {
			var1Domain.getDomain().remove(objectToRemove);
		}
		return removed;
	}
	
	int test = 0;
}
