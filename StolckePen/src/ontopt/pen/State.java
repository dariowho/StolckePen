package ontopt.pen;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class represents the row of a chart. 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002-2009
 * </p>
 * <p>
 * Company: CISUC
 * </p>
 * 
 * @author Nuno Seco
 * @version 1.0
 */

public class State
{
	public final static Double INVALID_PROBABILITY = -1.;
	
	public static final int STATE_UNKNOWN   = -1;
	public static final int STATE_SCANNED   =  1;
	public static final int STATE_COMPLETED =  2;
	public static final int STATE_PREDICTED =  3;
	
    /**
     * The list of all chart rows
     */
    private ArrayList<State> stateList;

    /**
     * The list of rules that contributed to the present state of this row
     */
    private ArrayList<Integer> parents;

    
    /**
     * The process that created the state (Predictor, Scanner or Completer).
     */
    private int origin;
    
    /**
     * The process that created the state (Predictor, Scanner or Completer),
     * encoded as a string.
     */
    private String originString;

    /**
     * The state of the row. A unique identifier
     */
    private Integer state;

    /**
     * The index of where the dot is located in reference to body of the rule
     */
    private int dot = 0;

    /**
     * points to the current rule being used
     */
    private Rule rule;

    /**
     * holds the position of the beginning of the rule in index 0 holds the position of the dot in index 1
     */
    private int[] positions;

    /**
     * Holds the forward probability in this state (each State represents a state)
     */
    private Double forwardProbability;
    
    /**
     * Holds the inner probability in this state (each State represents a state)
     */
    private Double innerProbability;
    
    /**
     * The constructor
     * 
     * @param pRule
     *            The rule used in this row
     */
    public State(Rule pRule)
    {
        this(pRule, new int[2]);
    }
    
    public State(Rule pRule, int[] pPositions)
    {
    	this(pRule, pPositions, INVALID_PROBABILITY, INVALID_PROBABILITY);
    }

    /**
     * The constructor
     * 
     * @param pRule
     *            The rule used in this row
     * @param pPositions
     *            An array where the first value corresponds to where the rule begins and the second to where
     *            the dot lies always in respect to the input sentence.
     * @param forwardProbabilityIn
     *            The forward probability that will be associated to this state.
     * @param innerProbabilityIn
     *            The inner probability that will be associated to this state.
     */
    public State(Rule pRule, int[] pPositions, Double forwardProbabilityIn, Double innerProbabilityIn)
    {
        parents   = new ArrayList<Integer>();
        this.rule = pRule;
        positions = pPositions;
        
        this.origin       = this.STATE_UNKNOWN;
        this.originString = "unknown";
        
        this.forwardProbability = forwardProbabilityIn;
        this.innerProbability   = innerProbabilityIn;
    }

    /**
     * Gets the row corresponding to the specified state
     * 
     * @param pState
     *            The state to look for
     * @return The row with the state
     */
    public State getStateFromState(Integer pState)
    {
        return (State) stateList.get(pState.intValue());
    }

    /**
     * Sets the name of the originString
     * 
     * @param p
     *            The name to be set
     */
    protected void setOriginString(String p)
    {
        originString = p;
    }
    
    protected void setOrigin(int originIn) {
    	this.origin = originIn;
    }
    
    protected int getOrigin() {
    	return this.origin;
    }

    protected void setForwardProbability(Double pIn)
    {
    	this.forwardProbability = pIn;
    }
    
    protected void setInnerProbability(Double pIn)
    {
    	this.innerProbability = pIn;
    }
    
    public Double getForwardProbability() {
    	return this.forwardProbability;
    }
    
    public Double getInnerProbability() {
    	return this.innerProbability;
    }
    
    /**
     * Adds this row to the state list.
     * 
     * @param pStateList
     *            The global state list that is shared
     */
    protected void incState(ArrayList<State> pStateList)
    {
        this.stateList = pStateList;
        state = new Integer(this.stateList.size());
        stateList.add(this);
    }

    /**
     * Sets the position of the dot relative to the size of this rule
     * 
     * @param dot
     *            the position
     */
    protected void setDot(int dot)
    {
        this.dot = dot;
    }

    /**
     * Gets the position of the dot relative to the size of this rule
     * 
     * @return the position of the dot
     */
    public int getDot()
    {
        return dot;
    }

    /**
     * Gets the state of this row, the state is a unique id
     * 
     * @return The state of this row
     */
    public Integer getState()
    {
        return state;
    }

    /**
     * Adds a new parent to this row
     * 
     * @param s
     *            the parent to add
     */
    public void addParentState(Integer s)
    {
        parents.add(s);
    }

    /**
     * Adds a list of parents to this row
     * 
     * @param s
     *            the list of parents
     */
    public void addParentStates(ArrayList<Integer> s)
    {
        for (int i = 0; i < s.size(); i++)
        {
            if (!parents.contains(s.get(i)))
            {
                parents.add(s.get(i));
            }
        }
    }

    /**
     * Gets the list of parents
     * 
     * @return list of parents
     */
    public ArrayList<Integer> getParents()
    {
        return parents;
    }

    /**
     * gets the array corresponding to the position of where this rule begins and where the dot lies. These
     * values are in respect to the input sentence.
     * 
     * @return
     */
    public int[] getPositions()
    {
        return positions;
    }

    /**
     * Sets the array of positions where the first value corresponds to where the rule begins and the second
     * to where the dot lies always in respect to the input sentence.
     * 
     * @param positions
     *            the array of positions
     */
    protected void setPositions(int[] positions)
    {
        this.positions = positions;
    }

    /**
     * The rule associated to this row
     * 
     * @return The rule
     */
    public Rule getRule()
    {
        return rule;
    }

    /**
     * Check if this rule has been completely fired
     * 
     * @return true if it is complete, false otherwise
     */
    public boolean isComplete()
    {
        if (rule instanceof NonterminalRule)
        {
            return (dot == ((NonterminalRule) rule).body.size());
        }

        return true; // its a terminal
    }

    /**
     * Gets the next part of rule that needs to be fired
     * 
     * @return an Integer corresponding to the next part of the rule
     */
    public Integer getNextConstituent()
    {
        if (rule instanceof NonterminalRule && !isComplete())
        {
            return (Integer) ((NonterminalRule) rule).getBody().get(dot);
        }

        return null;
    }

    /**
     * Check if this row is equal to another.
     * 
     * @param row
     *            The row to compare to
     * @return true if they are equal, false otherwise
     */
//    public boolean equals(State row)
//    {
//        return (row.rule.equals(this.rule) && row.positions[0] == this.positions[0] && row.positions[1] == this.positions[1] && this.getParents().equals(row.getParents()));
//    }
//
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + ((parents == null) ? 0 : parents.hashCode());
		result = prime * result + Arrays.hashCode(positions);
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (parents == null) {
			if (other.parents != null)
				return false;
		} else if (!parents.equals(other.parents))
			return false;
		if (!Arrays.equals(positions, other.positions))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		return true;
	}

	/**
     * gets the string representation of this row.
     * 
     * @return The string representation.
     */
    public String toString()
    {
        return state + " " + rule.toString() + " [" + positions[0] + " " + positions[1] + "]" + " " + getParents().toString() + " " + originString + " " + dot + " {for:"+this.forwardProbability+", inn:"+this.innerProbability+"}";
    }
}