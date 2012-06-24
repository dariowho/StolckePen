package ontopt.pen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class represents a column in the chart of the Earley algorithm. 
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

public class ChartColumn
{
    /** The states stored into this chart column */
    private ArrayList<State> stateList;
    
    /** An hashed index of the stored states, to speed up the look-up operations */
    private Map<State,Integer> stateIndex;

    /**
     * A list that all stateList rows from all charts share. This list is used for constructing the parse tree. The
     * original Earley algorithm is a membership algorithm and not a parsing algorithm.
     * 
     * TODO: The role of this variable is not clear, and the name ambiguous: check... 
     */
    private ArrayList<State> commonStates;

    /**
     * The constructor
     * 
     * @param pStateList
     *            The commonStates that is common to all chartrows
     */
    public ChartColumn(ArrayList<State> pStateList)
    {
        this.commonStates = pStateList;
        stateList         = new ArrayList<State>();
        stateIndex        = new HashMap<State,Integer>();
    }

    /**
     * Adds a new row to this stateList
     * 
     * @param row
     *            The row to add
     */
    public void addState(State state)
    {
        state.incState(commonStates);
        stateList.add(state);
        stateIndex.put(state, this.stateList.size()-1);
    }

    /**
     * Gets the size of the stateList.
     * 
     * @return the size of the stateList
     */
    public int size()
    {
        return stateList.size();
    }

    /**
     * Gets the row at the specified index
     * 
     * @param index
     *            The specified (numeric) index of the state in the stateList
     * @return The state
     */
    public State getState(int index)
    {
        return (State) stateList.get(index);
    }

    public State getState(State stateIn) {
    	return this.getState(this.stateIndex.get(stateIn));
    }
    
    /**
     * Checks if the specified row exists in the column's stateList
     */
    public boolean exists(State stateIn) {
    	return this.stateIndex.containsKey(stateIn);
    }

    /**
     * Returns a string representation of this stateList
     * 
     * @return The string representation
     */
    public String toString()
    {
        String s = new String();
        for (int i = 0; i < stateList.size(); i++)
        {
            s += stateList.get(i).toString() + "\n";
        }
        return s;
    }

    /**
     * Gets the rows which have succeeded in parsing the sentence. These rows correspond to the roots of parse
     * tree.
     * 
     * @return A list rows
     */
    public ArrayList<State> getRoots()
    {
        State cw;
        ArrayList<State> roots = new ArrayList<State>();

        for (int i = 0; i < stateList.size(); i++)
        {
            cw = (State) stateList.get(i);
            if (cw.getRule().getHead() != null && cw.getRule().getHead().equals(Grammar.PARSE_ROOT) && cw.isComplete() && cw.getPositions()[0] == 0)
            {
                roots.add(cw);
            }
        }

        return roots;
    }
}