package ontopt.pen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class represents a chart in the Earley algorithm. 
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
    /**
     * The chart used for storing previous computations
     */
    private ArrayList<State> chart;
    
	/**
	 * Indexes the chart to achieve linear search time for the enqueue() function.
	 * TODO: still in testing, might not be as effective as expected...
	 */
    private Set<State> chartIndex;

    /**
     * A list that all chart rows from all charts share. This list is used for constructing the parse tree. The
     * original Earley algorithm is a membership algorithm and not a parsing algorithm.
     * 
     * TODO: The role of this variable is not clear, and the name ambiguous: check... 
     */
    private ArrayList<State> stateList;
    
    /**
     * The constructor
     * 
     * FIXME: it is not clear what the input state list stands for; originally
     * the constructor took another parameter (an input list of States)
     * 
     * @param pStateList
     *            The stateList that is common to all States
     */
    public ChartColumn()
    {
        this.stateList = new ArrayList<State>();
        chart = new ArrayList<State>();
        chartIndex = new HashSet<State>();
    }

    /**
     * Adds a new row to this chart
     * 
     * @param row
     *            The row to add
     */
    public void addState(State state)
    {
        state.incState(stateList);
        chart.add(state);
        chartIndex.add(state);
    }

    /**
     * Gets the size of the chart.
     * 
     * @return the size of the chart
     */
    public int size()
    {
        return chart.size();
    }

    /**
     * Gets the row at the specified index
     * 
     * @param index
     *            The specified index
     * @return The row
     */
    public State getState(int index)
    {
        return (State) chart.get(index);
    }

    public State getState(State row)
    {
        for (int i = chart.size() - 1; i >= 0; i--)
        {
            if (((State) chart.get(i)).equals(row))
            {
                return chart.get(i);
            }
        }
        
        return null;
    }
    
    /**
     * Checks if the specified row exists in the chart
     */
    public boolean exists(State row)
    {
        return this.chartIndex.contains(row);
    }

    /**
     * Returns a string representation of this chart
     * 
     * @return The string representation
     */
    public String toString()
    {
        String s = new String();
        for (int i = 0; i < chart.size(); i++)
        {
            s += chart.get(i).toString() + "\n";
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

        for (int i = 0; i < chart.size(); i++)
        {
            cw = (State) chart.get(i);
            if (cw.getRule().getHead() != null && cw.getRule().getHead().equals(Grammar.PARSE_ROOT) && cw.isComplete() && cw.getPositions()[0] == 0)
            {
                roots.add(cw);
            }
        }

        return roots;
    }
}