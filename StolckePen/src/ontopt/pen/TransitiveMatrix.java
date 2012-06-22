package ontopt.pen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import matrix.Matrix;

public class TransitiveMatrix {
	private Matrix probTransLCMatrix;
	private Matrix probTransUnitMatrix;
	private HashMap<String, HashMap<String, Double >> probLCHash;
	private HashMap<String, HashMap<String, Double >> probUnitHash;

	public static TransitiveMatrix getMatrix(Grammar grammar){
		TransitiveMatrix rMatrix = new TransitiveMatrix();

		ArrayList<String> nonTerminalList= new ArrayList<String>(grammar.getNonterminals());
		Matrix[] r=probabilisticTransitiveRelation(nonTerminalList, grammar);
		
    	rMatrix.probTransLCMatrix = computeInverseIdMinusMatrix(r[0], nonTerminalList.size());
    	rMatrix.probLCHash = matrixToHash(rMatrix.probTransLCMatrix, nonTerminalList);

		
    	rMatrix.probTransUnitMatrix = computeInverseIdMinusMatrix(r[1], nonTerminalList.size());
    	rMatrix.probUnitHash = matrixToHash(rMatrix.probTransUnitMatrix, nonTerminalList);
    	return rMatrix;
	}
	/**
	 * Build matrix for left corner relations.
	 */
	
	public static Matrix[] probabilisticTransitiveRelation(List<String> nonTerminals, Grammar grammar) {
		//P(X -->left Y) = Sum_{X --> Y mu} P(X --> Y)
		//rules are of the form: HM<lhs, HM(rhs, probability)>
		int nrNonTerminals = nonTerminals.size();
		
		double[][] leftCornerProbabilities = new double[nrNonTerminals][nrNonTerminals];
		double[][] UnitProbabilities = new double[nrNonTerminals][nrNonTerminals];
		for (int i=0; i< nrNonTerminals; i++) {
			String lhs = nonTerminals.get(i);
			ArrayList<Rule> rules = grammar.getAllRulesWithHead(lhs);
			for (Rule r : rules){
				if ( nonTerminals.contains(r.getLeftmost())){//check if not terminal
					
					leftCornerProbabilities[i][nonTerminals.indexOf(r.getLeftmost())]+=r.getWeight();
					if (r.size()==1){
						UnitProbabilities[i][nonTerminals.indexOf(r.getLeftmost())]+=r.getWeight();
					}
				}
			}
		}
		
		for (int i =0; i < nonTerminals.size(); i++) {
			for (int j = 0; j < nonTerminals.size(); j++) {	
			System.out.print(" " + leftCornerProbabilities[i][j]);	
			}
			System.out.println("");
			}
		
		
		Matrix[] r= new Matrix[2];
		r[0]= new Matrix(leftCornerProbabilities);
		r[1] = new Matrix(UnitProbabilities);
		return r;
	}

	/**
	 * Compute R_{L} = inverse(I - P_{L})
	 */
	public static Matrix computeInverseIdMinusMatrix(Matrix probLCMatrix, int nrNonTerminals) {
		
		double[][] matrixArray = new double[nrNonTerminals][nrNonTerminals];
		for (int i=0; i< nrNonTerminals; i++) {
			matrixArray[i][i] = 1.;
		}
		Matrix identityMatrix = new Matrix(matrixArray);
		
		//R_{L} = inverse(I - P_{L}) 
		(identityMatrix.minus(probLCMatrix)).inverse().print(10,10);
		
		return (identityMatrix.minus(probLCMatrix)).inverse();
	}
	
	/**
	 * Turn matrix into hashmap. Also entries with value zero are removed. This speeds up parsing later on.
	 */
	public static HashMap<String, HashMap<String, Double>> matrixToHash(Matrix Matrix, List<String> nonterminal_symbols) {
		HashMap<String, HashMap<String, Double>> hash = new HashMap<String, HashMap<String, Double>>();
		
		
		for (int rowIndex = 0; rowIndex<nonterminal_symbols.size(); rowIndex++) {
			HashMap<String, Double> temp_hash = new HashMap<String, Double>();
			for (int columnIndex = 0; columnIndex<nonterminal_symbols.size(); columnIndex++) {
				double prob = Matrix.get(rowIndex, columnIndex);
				//System.out.println(Double.toString(prob));
				if ( prob >0.) {
					System.out.println(nonterminal_symbols.get(columnIndex));
					temp_hash.put(nonterminal_symbols.get(columnIndex), prob);
					//System.out.println(nonterminal_symbols.get(rowIndex)+" -->  " + nonterminal_symbols.get(columnIndex)+" = " + Double.toString(prob));
				}
			}
			hash.put(nonterminal_symbols.get(rowIndex), temp_hash);
				
		}
			
			
		return hash;
	}
	public double getTransitiveLCRelation(String lhs, String rhs){		
		if (this.probLCHash.get(lhs).containsKey(rhs)){
			return this.probLCHash.get(lhs).get(rhs);
		}
		return 0;
	}
	
	public Set<Entry<String, Double>> getTransitiveLCRelationSet(String lhs){		
		
		return this.probLCHash.get(lhs).entrySet();
	}
	public double getTransitiveUnitRelation(String lhs, String rhs){
		if (this.probUnitHash.get(lhs).containsKey(rhs)){
			this.probUnitHash.get(lhs).get(rhs);
		}
		return 0;
	}
	
	public void printRMatrix() {
		for (String lhs : this.probLCHash.keySet()) {
			System.out.print("["+lhs+"]\n\t");
			if (this.probLCHash.get("lhs") == null) {
				System.out.println("[null rhs]");
				continue;
			}
			for (String rhs : this.probLCHash.get("lhs").keySet()) {
				System.out.print("["+rhs+": "+this.probLCHash.get("lhs").get("rhs")+"] ");
			}
			System.out.println();
		}
	}
}
