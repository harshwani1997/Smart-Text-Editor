package spelling;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author You
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;
    

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
		size=0;
	}
	
	
	/**
	 
	 * This method adds a word by creating and linking the necessary trie nodes 
	 * into the trie, as described outlined in the videos for this week. It 
	 * should appropriately use existing nodes in the trie, only creating new 
	 * nodes when necessary. E.g. If the word "no" is already in the trie, 
	 * then adding the word "now" would add only one additional node 
	 * (for the 'w').
	 * 
	 * @return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	public boolean addWord(String word)
	{
	    TrieNode node=root;
	    for (Character c : word.toLowerCase().toCharArray()) {
			TrieNode child = node.getChild(c);

			if (child != null) {
				node = child;
			} else {
				node = node.insert(c);
			}
		}

		if (node.endsWord()) {
			return false;
		}

		node.setEndsWord(true);
		++size;

		return true;
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
	    return size;
	}
	
	
	/** Returns whether the string is a word in the trie, using the algorithm
	 * described in the videos for this week. */
	@Override
	public boolean isWord(String s) 
	{
		TrieNode node=root;
		for (Character ch:s.toLowerCase().toCharArray()) {
			TrieNode child = node.getChild(ch);
			if (child!=null) node=child;
			else return false;
		}
		
		return node.endsWord();
	}

	/** 
     * 
     * The list of completions contains 
     * all of the shortest completions, but when there are ties, it may break 
     * them in any order. For example, if there the prefix string is "ste" and 
     * only the words "step", "stem", "stew", "steer" and "steep" are in the 
     * dictionary, when the user asks for 4 completions, the list must include 
     * "step", "stem" and "stew", but may include either the word 
     * "steer" or "steep".
     * 
     * If this string prefix is not in the trie, it returns an empty list.
     * 
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 
    	 TrieNode node = root;

 		for (Character c : prefix.toLowerCase().toCharArray()) {
 			TrieNode child = node.getChild(c);
 			if (child != null) {
 				node = child;
 			} else {
 				return Collections.<String> emptyList();
 			}
 		}

 		List<String> completions = new LinkedList<>();
 		Queue<TrieNode> queue = new LinkedList<>();
 		queue.offer(node);

 		while (!queue.isEmpty() && numCompletions > 0) {
 			TrieNode t = queue.poll();

 			if (t.endsWord()) {
 				completions.add(t.getText());
 				--numCompletions;
 			}

 			for (Character c : t.getValidNextCharacters()) {
 				queue.offer(t.getChild(c));
 			}

 		}

 		return completions;
     }

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		
 		System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}