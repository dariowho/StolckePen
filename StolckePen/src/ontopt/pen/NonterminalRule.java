package ontopt.pen;

import java.util.ArrayList;

/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class represents the rule of a grammar, including its body.
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

public class NonterminalRule extends Rule
{
    /**
     * Represents the body of the rule. e.g. HEAD - B1, B2, ..., Bn Everything to the right of - is the body.
     */
    protected ArrayList<Integer> body;
    protected Integer head;

    /**
     * The Constructor
     * 
     * @param pHead
     *            The head of the rule
     * @param pBody
     *            The body of the rule
     * @param pGrammar
     *            The Grammar containing the rule
     */
    public NonterminalRule(Double pWeight, String pAnnotation, Integer pHead, ArrayList<Integer> pBody, Grammar pGrammar)
    {
        super(pWeight, pAnnotation, pHead, pGrammar);
        this.body = pBody;
    }

    /**
     * The Constructor
     * 
     * @param pHead
     *            The head of the rule
     * @param body
     *            The body of the rule
     * @param pGrammar
     *            The grammar containing the rule
     */
    public NonterminalRule(Double pWeight, String pAnnotation, Integer pHead, Integer body, Grammar pGrammar)
    {
        super(pWeight, pAnnotation, pHead, pGrammar);
        this.body = new ArrayList<Integer>();
        this.body.add(body);
    }

    /**
     * Gets the list representing the body
     * 
     * @return The list of the body
     */
    public ArrayList<Integer> getBody()
    {
        return body;
    }

    /**
     * Sets the body of the rule
     * 
     * @param pBody
     */
    public void setBody(ArrayList<Integer> pBody)
    {
        this.body = pBody;
    }

    public String getLeftmost() {
    	return this.grammar.getDataType(this.body.get(0));
    }
    
    public Integer size() {
    	return this.body.size();
    }
    
    /**
     * gets the string representation of this rule.
     * 
     * @return The string representation
     */
    public String toString()
    {
        String s = new String();
        s += grammar.getDataType(head) + " - ";

        for (int i = 0; i < body.size(); i++)
        {
            s += grammar.getDataType((Integer) body.get(i)) + " ";

        }
        return s;
    }

    /**
     * Checks if this rule is equal to another.
     * 
     * @param pRule
     *            The other rule
     * @return true if they are equal, false otherwise
     */
    public boolean equals(Rule pRule)
    {
        if (pRule instanceof NonterminalRule && this.head != null)
        {
            return (this.head.equals(pRule.head) && this.body.equals(((NonterminalRule) pRule).body));
        }

        return false;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((head == null) ? 0 : head.hashCode());
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
		NonterminalRule other = (NonterminalRule) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		return true;
	}
}