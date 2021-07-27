package csp;

import java.util.ArrayList;

public class CSP {
	private ArrayList<Variable> variables = new ArrayList<Variable>();
	private ArrayList<Constraint> constraints = new ArrayList<Constraint>();
	
	public void AddVariable(Variable variable) throws Exception {
		if (variables.contains(variable))
			throw new Exception("a variable whith same name already exist");
		else
			variables.add(variable);
	}
	
	public void addConstraint(Constraint constraint) {
		if (!constraints.contains(constraint))
			constraints.add(constraint);
	}
	
	public ArrayList<Variable> getVariables() {
		return variables;
	}
	
	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}
}
