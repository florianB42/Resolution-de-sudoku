package csp;

import java.util.ArrayList;

public class Domain implements Cloneable{
	private ArrayList<Object> domain = new ArrayList<Object>();

	public void addToDomain(Object obj) {
		domain.add(obj);
	}
	
	public ArrayList<Object> getDomain() {
		return domain;
	}
	
	public int size() {
		return domain.size();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Domain clone = new Domain();
		for (Object object : domain) {
			clone.addToDomain(object);
		}
		
		return clone;
	}
	
	
}
