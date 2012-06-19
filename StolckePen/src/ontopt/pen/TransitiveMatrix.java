package ontopt.pen;
import java.util.ArrayList;
import java.util.HashMap;

import matrix.Matrix;

public class TransitiveMatrix {
	private static ArrayList<String> nonTerminalList;
	private static Matrix probTransLCMatrix;
	private static Matrix probTransUnitMatrix;
	protected static HashMap<String, HashMap<String, Double >> probLCHash;
	protected static HashMap<String, HashMap<String, Double >> probUnitHash;

	public void getMatrix(ArrayList<String> nonTerminalList, rules){
		this.nonTerminalList=nonTerminalList;
		
		probTransLCMatrix = probabilisticLeftCornerRelation(nonTerminalList, rules);
    	probTransLCMatrix = computeInverseIdMinusMatrix(probTransLCMatrix, nonTerminalList.size());
    	probLCHash = matrixToHash(probTransLCMatrix, nonTerminalList);

		probTransUnitMatrix = probabilisticUnitProductions(nonTerminalList, rules);
    	probTransUnitMatrix = computeInverseIdMinusMatrix(probTransLCMatrix, nonTerminalList.size());
    	probUnitHash = matrixToHash(probTransLCMatrix, nonTerminalList);
	}
	/**
	 * Build matrix for left corner relations.
	 */
	
	public Matrix probabilisticLeftCornerRelation(ArrayList<String> nonterminal_symbols, HashMap<String, HashMap<ArrayList<String>, Double>> rules) {
		//P(X -->left Y) = Sum_{X --> Y mu} P(X --> Y)
		//rules are of the form: HM<lhs, HM(rhs, probability)>
		int nrNonTerminals = nonterminal_symbols.size();
		
		double[][] leftCornerProbabilities = new double[nrNonTerminals][nrNonTerminals];
		for (int i=0; i< nrNonTerminals; i++) {
			String lhs = nonterminal_symbols.get(i);
			for (ArrayList<String> rhs : rules.get(lhs).keySet() ) {
				String leftCorner = rhs.get(0);
				//add to probability of lhs --> leftcorner
				if (nonterminal_symbols.contains(leftCorner))
					leftCornerProbabilities[i][nonterminal_symbols.indexOf(leftCorner)] += rules.get(lhs).get(rhs);
			}
		}
		
		return new Matrix(leftCornerProbabilities);
	}
	/**
	 * Build matrix for left corner relations for unit productions.
	 */
	public Matrix probabilisticUnitProductions(ArrayList<String> nonterminal_symbols, HashMap<String, HashMap<ArrayList<String>, Double>> rules) {
		//P(X -->left Y) = Sum_{X --> Y mu} P(X --> Y)
		//rules are of the form: HM<lhs, HM(rhs, probability)>
		int nrNonTerminals = nonterminal_symbols.size();
		
		double[][] unitProductionProbabilities = new double[nrNonTerminals][nrNonTerminals];
		for (int i=0; i< nrNonTerminals; i++) {
			String lhs = nonterminal_symbols.get(i);
			for (ArrayList<String> rhs : rules.get(lhs).keySet() ) {
				String leftCorner = rhs.get(0);
				//add to probability of lhs --> leftcorner
				if (nonterminal_symbols.contains(leftCorner) && (rhs.size()==1))
					unitProductionProbabilities[i][nonterminal_symbols.indexOf(leftCorner)] += rules.get(lhs).get(rhs);
			}
		}
		
		return new Matrix(unitProductionProbabilities);
	}
	/**
	 * Compute R_{L} = inverse(I - P_{L})
	 */
	public Matrix computeInverseIdMinusMatrix(Matrix probLCMatrix, int nrNonTerminals) {
		
		double[][] matrixArray = new double[nrNonTerminals][nrNonTerminals];
		for (int i=0; i< nrNonTerminals; i++) {
			matrixArray[i][i] = 1.;
		}
		Matrix identityMatrix = new Matrix(matrixArray);
		
		//R_{L} = inverse(I - P_{L}) 
		return (identityMatrix.minus(probLCMatrix)).inverse();
	}
	
	/**
	 * Turn matrix into hashmap. Also entries with value zero are removed. This speeds up parsing later on.
	 */
	public HashMap<String, HashMap<String, Double>> matrixToHash(Matrix Matrix, ArrayList<String> nonterminal_symbols) {
		HashMap<String, HashMap<String, Double>> hash = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> temp_hash = new HashMap<String, Double>();
		
		for (int rowIndex = 0; rowIndex<nonterminal_symbols.size(); rowIndex++) {
			for (int columnIndex = 0; columnIndex<nonterminal_symbols.size(); columnIndex++) {
				double prob = Matrix.get(rowIndex, columnIndex);
				if (prob >0.) {
					temp_hash.put(nonterminal_symbols.get(columnIndex), prob);
					System.out.println(nonterminal_symbols.get(rowIndex)+" -->  " + nonterminal_symbols.get(columnIndex)+" = " + Double.toString(prob));
				}
			}
			hash.put(nonterminal_symbols.get(rowIndex), temp_hash);
			temp_hash.clear();
				
		}
			
			
		return hash;
	}
	public double getTransitiveRelation(String lhs, String rhs){
		return probLCHash.get(lhs).get(rhs);
	}
}
