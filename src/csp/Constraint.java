package csp;

import java.util.Objects;

public class Constraint {
	private Variable var1;
	private Variable var2;
	private ConstraintType constraintType;

	public Constraint(Variable var1, Variable var2, ConstraintType constraintType) {
		this.var1 = var1;
		this.var2 = var2;
		this.constraintType = constraintType;
	}

	public Variable getVar1() {
		return var1;
	}

	public Variable getVar2() {
		return var2;
	}
	
	public ConstraintType getConstraintType() {
		return constraintType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(constraintType, var1, var2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Constraint other = (Constraint) obj;
		return constraintType == other.constraintType && Objects.equals(var1, other.var1)
				&& Objects.equals(var2, other.var2);
	}
	
	
}
