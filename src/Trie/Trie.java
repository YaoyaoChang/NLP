package Trie;

import java.util.ArrayList;

public class Trie {
	TrieUnit root = new TrieUnit(0);
	int MaxWordLength = 0;
	public String MaxString;
	
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
