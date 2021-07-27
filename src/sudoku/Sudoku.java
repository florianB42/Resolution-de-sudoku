package sudoku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import csp.CSP;
import csp.Constraint;
import csp.ConstraintType;
import csp.Domain;
import csp.Variable;

public class Sudoku {
	private ArrayList<ArrayList<Integer>> sudoku = new ArrayList<ArrayList<Integer>>();
	
	public Sudoku() {
		for (int i = 0; i < 9; i++) {
			ArrayList<Integer> temps = new ArrayList<Integer>(); 
			sudoku.add(temps);
			for (int j = 0; j < 9; j++) {
				temps.add(null);
			}
		}
		sudoku.get(0).set(2, 6);
		sudoku.get(0).set(3, 1);
		sudoku.get(1).set(0, 5);
		sudoku.get(1).set(4, 4);
		sudoku.get(2).set(1, 2);
		sudoku.get(2).set(5, 9);
		sudoku.get(2).set(6, 7);
		sudoku.get(3).set(0, 8);
		sudoku.get(3).set(2, 5);
		sudoku.get(3).set(5, 6);
		sudoku.get(3).set(7, 9);
		sudoku.get(4).set(0, 6);
		sudoku.get(4).set(5, 1);
		sudoku.get(5).set(2, 4);
		sudoku.get(5).set(3, 9);
		sudoku.get(5).set(5, 2);
		sudoku.get(6).set(2, 1);
		sudoku.get(7).set(3, 8);
		sudoku.get(7).set(6, 2);
		sudoku.get(7).set(7, 7);
		sudoku.get(8).set(3, 5);
		sudoku.get(8).set(6, 6);
		sudoku.get(8).set(7, 3);
		
	}
	
	public Sudoku(CSP csp) {
		for (int i = 0; i < 9; i++) {
			ArrayList<Integer> temps = new ArrayList<Integer>(); 
			sudoku.add(temps);
			for (int j = 0; j < 9; j++) {
				temps.add(null);
			}
		}
		
		for (Variable var : csp.getVariables()) {
			int pos = Integer.parseInt(var.getName());
			sudoku.get((pos/10) -1).set((pos%10) - 1, (Integer) var.getValue());
		}
	}
	
	public Sudoku(int tailleCarré) throws NumberFormatException, IOException {
		for (int i = 0; i < tailleCarré * 3; i++) {
			ArrayList<Integer> temps = new ArrayList<Integer>(); 
			sudoku.add(temps);
			for (int j = 0; j < tailleCarré * 3; j++) {
				temps.add(null);
			}
		}
		
		Scanner scanner = new Scanner(System.in);
		System.out.println(this);
		System.out.println("Entrer les valeurs une par une en commencent par la gauche de la première ligne.\n Remplire ligne par ligne.\n entrer la valeur 0 (zero) pour une case vide");
		for (int i = 0; i < tailleCarré * 3; i++) {
			for (int j = 0; j < tailleCarré * 3; j++) {
				System.out.println("valeur de la ligne " + (i+1) + " colonne " + (j+1) );
				String name = scanner.next();
				if (Integer.parseInt(name) != 0)
					sudoku.get(i).set(j, Integer.parseInt(name));
			}
			System.out.println(this);
		}
	}
	
	public CSP makeCsp() throws Exception {
		CSP csp = new CSP();
		Domain normalDomain = new Domain();
		for (int i = 1; i <= 9; i++) {
			normalDomain.addToDomain(i);
		}
		
		ArrayList<ArrayList<Variable>> tabOfVar = new ArrayList<ArrayList<Variable>>();
		Integer indexLigne = 0;
		for (ArrayList<Integer> line : sudoku) {
			tabOfVar.add(new ArrayList<Variable>());
			indexLigne++;
			Integer indexColumn = 0;
			for (Integer value : line) {
				indexColumn++;
				Variable var;
				if (value == null) {
					var = new Variable(indexLigne.toString() + indexColumn.toString(),normalDomain);
				}
				else {
					Domain domain = new Domain();
					domain.addToDomain(value);
					var = new Variable(indexLigne.toString() + indexColumn.toString(),domain);
				}
				tabOfVar.get(indexLigne - 1).add(var);
				csp.AddVariable(var);
			}
		}
		
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				for (int line = 0; line < 9; line++) {
					if (line != i)
						csp.addConstraint(new Constraint(tabOfVar.get(i).get(j), tabOfVar.get(line).get(j), ConstraintType.different));
				}
				
				for (int column = 0; column < 9; column++) {
					if (column != j)
						csp.addConstraint(new Constraint(tabOfVar.get(i).get(j), tabOfVar.get(i).get(column), ConstraintType.different));
				}
				
				for (int line = 0; line < 3; line++) {
					for (int column = 0; column < 3; column++) {
						if (line != i%3 || column != j%3)
							csp.addConstraint(new Constraint(tabOfVar.get(i).get(j), tabOfVar.get(line + 3 * (i/3)).get(column + 3 * (j/3)), ConstraintType.different));
					}
				}
			}
		}
		
		return csp;
	}
	
	@Override
	public String toString() {
		String res = "";
		
		for (int i = 0; i < 9; i++) {
			if (i % 3 == 0) {
				for(int j = 0; j < 25; j++)
					res += "-";
				res += "\n";
			}
			for (int j = 0; j < 9; j++) {
				if (j % 3 == 0)
					res += "| ";
				if (sudoku.get(i).get(j) == null)
					res += "X ";
				else
					res += sudoku.get(i).get(j) + " ";
			}
			res += "|\n";
		}
		for(int j = 0; j < 25; j++)
			res += "-";
		res += "\n";
		return res;
	}
}
