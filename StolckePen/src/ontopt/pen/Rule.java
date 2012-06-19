package ontopt.pen;

/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class represents a rule in the grammar.
 * <p>
 * Copyright: Copyright (c) 2002-2009
 * </p>
 * <p>
 * Company: CISUC
 * </p>
 * 
 * @author Nuno Seco, Hugo Gon�alo Oliveira
 * @version 1.0
 */

public abstract class Rule
{

    public static final Double DEFAULT_WEIGHT = 0.;

    protected String annotation;

    /**
     * An Integer representing the rule weight
     */
    protected Double weight;

    /**
     * An Integer representing the phrase type of this rule. e.g. HEAD - B1, B2, ..., Bn
     */
    protected Integer head;

    /**
     * The grammar that contains this rule
     */
    protected Grammar grammar;

    /**
     * The Constructor
     * 
     * @param head
     *            The head of the rule
     * @param grammar
     *            The grammar that contains this rule
     */
    public Rule(Double weight, String annotation, Integer head, Grammar grammar)
    {
        this.weight = weight;
        this.annotation = annotation;
        this.head = head;
        this.grammar = grammar;
    }

    /**
     * The Constructor
     * 
     * @param head
     *            The head of the rule
     * @param grammar
     *            The grammar that contains this rule
     */
    public Rule(Integer head, Grammar grammar)
    {
        this.weight = DEFAULT_WEIGHT;
        this.annotation = "";
        this.head = head;
        this.grammar = grammar;
    }
    
    /**
     * Gets the nonterminal in the rule's left hand side (LHS).
     * 
     * @return The String representation of the rule's LHS
     */
    public String getLHS() {
    	return this.grammar.getDataType(head);
    }

    /**
     * Gets the Integer representation of the rule's left hand side (LHS).
     * 
     * TODO: "head" has another meaning in NLP: the name should be changed
     * 
     * @return The Integer Representation of this rule.
     */
    public Integer getHead()
    {
        return head;
    }

    /**
     * Sets the integer representation of this rule.
     * 
     * @param pHead
     *            The integer representation of this rule.
     */
    public void setHead(Integer pHead)
    {
        this.head = pHead;
    }
    
    public abstract Integer size();
    
    public abstract String getLeftmost();

    /**
     * An abstract method that forces all inheriting classes to implement this method. This method should
     * return true if this rule and the passed rule are equal.
     * 
     * @param pRule
     *            The rule to compare to
     * @return true if they are equal, false otherwise
     */
    public abstract boolean equals(Rule pRule);

    public Double getWeight()
    {
        return weight;
    }

    public String getAnnotation()
    {
        return annotation;
    }
}