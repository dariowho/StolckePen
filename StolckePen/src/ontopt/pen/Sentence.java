package ontopt.pen;

import java.util.ArrayList;

public abstract class Sentence {

	protected ArrayList<String> _sentence;
	
	protected ArrayList<Double> prefixProbabilities;
	
	public Sentence()
	{
		prefixProbabilities = new ArrayList<Double>();
	}
	
    public void setSentence(String sentence)
    {
        tokenize(sentence);
        
    } 
	
	protected abstract void tokenize(String sentence);
	
	 /**
     * Returns the word at the specified index
     * 
     * @param index
     *            the index to look up
     * @return The word at the specified index
     */

    public abstract String getWord(int index);
    
    public abstract int getSentenceSize();
   
    public abstract String toString();
    
    public abstract void updatePrefix(double prob,int index);
    
    public abstract double getPrefix(int index);
    
    public abstract double getSentenceSize(int index);
}
