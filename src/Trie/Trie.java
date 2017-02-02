package Trie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 一颗神奇的Trie树。可以高效查询单词。每个节点存一个字，叶子节点所经过的路径即为一个单词。
 * @author Administrator
 *
 */
public class Trie {
	TrieUnit root = new TrieUnit(0);
	int MaxWordLength = 0;
	public String MaxString;
	
	/**
	 * 插入单词
	 * @param s
	 * @param v 即wordID，现在此参数已废
	 */
	public void insert(String s, int v) {
		TrieUnit r = root;
		int n = s.length();
		if (n > 7) {//默认将所有长度大于7的词都pass掉
			return ;
		}
		MaxString = n > MaxWordLength ? s : MaxString;
		MaxWordLength = n > MaxWordLength ? n : MaxWordLength;
		for (int i = 0; i < n; i ++) {
			int c = (int) s.charAt(i);
			TrieUnit next = r.findChar(c);
			if (next == null) {
				next = new TrieUnit(c);
				r.add(next);
			}
			r = next;
		}
		r.setVal(v);
	}
	/**
	 * 查找单词。
	 * @param s
	 * @return 若找到单词，则返回该单词对应的词性信息数组。否则返回null
	 */
	public ArrayList<SpeechOfWord> find(String s) {
		TrieUnit r = root;
		int n = s.length();
		for (int i = 0; i < n; i ++ ) {
			int c = (int) s.charAt(i);
			TrieUnit next = r.findChar(c);
			if (next == null) {
				return null;
			}
			r = next;
		}
		if(r.getWordID() > 0) {
			return r.getArrayListSpeechOfWord();
		} else {
			return null;
		}
	}
	/**
	 * 向某单词增加某词性。
	 * @param aWord
	 * @param aSpeechID
	 */
	public void addSpeechOfWord(String aWord, int aSpeechID) {
		TrieUnit r = root;
		int n = aWord.length();
		for (int i = 0; i < n; i ++ ) {
			int c = (int) aWord.charAt(i);
			TrieUnit next = r.findChar(c);
			if (next == null) {//if I don't use this "if" statement, the compiler will give an error with no tip. How strange!
				return;
			}
			r = next;
		}
		r.addSpeech(aSpeechID);
	}
	public int getMaxWordLength() {
		return MaxWordLength;
	}
}
