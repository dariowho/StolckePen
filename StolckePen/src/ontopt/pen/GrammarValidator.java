package ontopt.pen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class validates the grammar's syntax. 
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

public class GrammarValidator
{
    private Grammar grammar;

    private HashSet<Rule> validated;

    public GrammarValidator(Grammar grammar)
    {
        this.grammar = grammar;

        validated = new HashSet<Rule>();
    }

    public void validate() throws GrammarException
    {
        validate(Grammar.PARSE_ROOT);
    }

    private void validate(Integer root) throws GrammarException
    {
        if (root.intValue() > Grammar.PHRASE_LOWER_LIMIT)
        {
            List<Rule> rules = grammar.getAllRulesWithHead(root);
            System.out.print(root + " ");
            System.out.print(grammar.getDataType(root) + " ");
        	System.out.println(rules.size());
            if (rules == null || rules.isEmpty())
            {
                throw new GrammarException("Couldn't find rule for symbol: "
                		+ root + " (AKA " + grammar.getDataType(root)
                		+ ") in " + grammar.getGrammarFileName());
            }

            Rule rule;
            ArrayList<Integer> body;
            Integer token;
//            ex is not used yet...
//            GrammarException ex;

            for (Iterator<Rule> i = rules.iterator(); i.hasNext();)
            {
                rule = i.next();

                if (validated.contains(rule))
                    continue;

                validated.add(rule);

                body = ((PhraseRule) rule).getBody();

                for (Iterator<Integer> j = body.iterator(); j.hasNext();)
                {
                    token = (Integer) j.next();
                    validate(token);
                }
            }
        }
    }
}