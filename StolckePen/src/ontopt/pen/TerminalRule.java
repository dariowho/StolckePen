package ontopt.pen;

/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class represents a terminal rule, consisting of a terminal token.
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

public class TerminalRule extends Rule
{
    /**
     * A string representation of the word associated to the terminal symbol
     */
    private String word;

    /**
     * The constructor
     * 
     * @param pHead
     *            The Head of the rule (terminal)
     * @param pWord
     *            The word associated
     * @param grammar
     *            The grammar that is being used.
     */
    public TerminalRule(Integer pHead, String pWord, Grammar grammar)
    {
        super(pHead, grammar);
        this.word = pWord;
    }

    /**
     * Gets the word associated to the terminal symbol
     * 
     * @return The word
     */
    public String getWord()
    {
        return word;
    }

    public String getLeftmost() {
    	return getWord();
    }
    
    public Integer size() {
    	return 1;
    }
    
    public boolean isUnary() {
    	// False, because a unary is defined as X→Y, where X and Y are nonterminals.
    	return false;
    }
    
    /**
     * Gets a string representation of this rule
     * 
     * @return The string
     */
    public String toString()
    {
        String s;
        s = grammar.getDataType(head) + " - " + word;

        return s;
    }

    /**
     * Checks if this rule is equal to the one being passed.
     * 
     * @param pRule
     *            The rule to check
     * @return true if they are equal, false otherwise
     */
//    public boolean equals(Rule pRule)
//    {
//        if (pRule instanceof TerminalRule)
//        {
//            return (this.head.equals(pRule.head) && this.word.equals(((TerminalRule) pRule).word));
//        }
//
//        return false;
//    }
//
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((head == null) ? 0 : head.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
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
		TerminalRule other = (TerminalRule) obj;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
    
}