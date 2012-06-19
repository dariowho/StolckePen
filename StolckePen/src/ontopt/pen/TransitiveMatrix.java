package ontopt.pen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import matrix.Matrix;

public class TransitiveMatrix {
	private static ArrayList<String> nonTerminalList;
	private Matrix probTransLCMatrix;
	private Matrix probTransUnitMatrix;
	protected static HashMap<String, HashMap<String, Double >> probLCHash;
	protected static HashMap<String, HashMap<String, Double >> probUnitHash;

	public void getMatrix(ArrayList<String> nonTerminalList, Grammar grammar){
		this.nonTerminalList=nonTerminalList;
		
		probabilisticTransitiveRelation(nonTerminalList, grammar);
    	probTransLCMatrix = computeInverseIdMinusMatrix(probTransLCMatrix, nonTerminalList.size());
    	probLCHash = matrixToHash(probTransLCMatrix, nonTerminalList);

		
    	probTransUnitMatrix = computeInverseIdMinusMatrix(probTransLCMatrix, nonTerminalList.size());
    	probUnitHash = matrixToHash(probTransLCMatrix, nonTerminalList);
	}
	/**
	 * Build matrix for left corner relations.
	 */
	
	public void probabilisticTransitiveRelation(ArrayList<String> nonTerminals, Grammar grammar) {
		//P(X -->left Y) = Sum_{X --> Y mu} P(X --> Y)
		//rules are of the form: HM<lhs, HM(rhs, probability)>
		int nrNonTerminals = nonTerminals.size();
		
		double[][] leftCornerProbabilities = new double[nrNonTerminals][nrNonTerminals];
		double[][] UnitProbabilities = new double[nrNonTerminals][nrNonTerminals];
		for (int i=0; i< nrNonTerminals; i++) {
			String lhs = nonTerminals.get(i);
			ArrayList<Rule> rules = grammar.getAllRulesWithHead(lhs);
			for (Rule r : rules){
				leftCornerProbabilities[nonTerminals.indexOf(r.getLeftmost())][i]+=r.getWeight();
				if (r.size()==1){
					UnitProbabilities[nonTerminals.indexOf(r.getLeftmost())][i]+=r.getWeight();
				}
			}
		}
		
		this.probTransLCMatrix = new Matrix(leftCornerProbabilities);
		this.probTransUnitMatrix = new Matrix(UnitProbabilities);
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
	public double getTransitiveLCRelation(String lhs, String rhs){
		return this.probLCHash.get(lhs).get(rhs);
	}
	public double getTransitiveUnitRelation(String lhs, String rhs){
		return this.probUnitHash.get(lhs).get(rhs);
	}
}
