package csp;

import java.util.ArrayList;
import java.util.Objects;

public class Variable implements Cloneable{
	private String name;
	private Domain domaine;
	private Object value = null;
	
	public Variable(String name, Domain domaine) {
		this.name = name;
		this.domaine = domaine;
	}

	public String getName() {
		return name;
	}

	public Domain getDomaine() {
		return domaine;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value2) {
		this.value = value2;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
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
		Variable other = (Variable) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return name + " = " + value;
	}

	/*@Override
	protected Object clone() throws CloneNotSupportedException {
		Variable var = (Variable) super.clone();
		var.domaine = (ArrayList<Object>) domaine.clone();
		var.value = value.clone();
		return var;
	}*/
	
	
	
}
