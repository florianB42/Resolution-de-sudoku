package main;

import java.util.ArrayList;
import java.util.Scanner;

import csp.CSP;
import csp.Variable;
import csp.solveur.Backtracking;
import sudoku.Sudoku;

public class Main {
	public static void main(String[] args) throws Exception {
		boolean choixCorrect = false;
		Sudoku sudoku = null;
		do {
			System.out.println("Voulez vous utilisé le sudoku enregistré ?");
			System.out.println("enter y si oui ou n si non:");
			Scanner scanner = new Scanner(System.in);
			String test = scanner.next();
			if (test.contentEquals("y") || test.contentEquals("Y")) {
				sudoku = new Sudoku();
				choixCorrect = true;
			}
			else if(test.contentEquals("n") || test.contentEquals("N")){
				sudoku = new Sudoku(3);
				choixCorrect = true;
			}
		}while(!choixCorrect);
			
		//Sudoku sudoku = new Sudoku();
		System.out.println("Le problème : ");
		System.out.println(sudoku);
		
		CSP csp = sudoku.makeCsp();
		Backtracking backtracking = new Backtracking();
		backtracking.backtrakingSearch(csp);
		
        System.out.println("La solution :");
		System.out.println(new Sudoku(csp));
	}
}
