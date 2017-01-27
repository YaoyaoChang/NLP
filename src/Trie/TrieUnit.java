package Trie;
import java.util.ArrayList;
import java.util.TreeSet;


public class TrieUnit implements Comparable{
	int wordANSI;
	char chinese;
	int wordID;
	ArrayList<SpeechOfWord> speechOfWord;
	TreeSet<TrieUnit> son;
	
	public TrieUnit(int word) {
		wordANSI = word;
		chinese = (char) wordANSI;
		wordID = 0;
		son = new TreeSet<TrieUnit>();
		speechOfWord = null;
	}
	public int compareTo(Object aObject) {
		TrieUnit aTrieUnit = (TrieUnit) aObject;
		return this.wordANSI - aTrieUnit.wordANSI;
	}
	public boolean equals(TrieUnit aTrieUnit) {
		return this.wordANSI == aTrieUnit.getWordANSI();
	}
	public void add(TrieUnit aTrieUnit) {
		son.add(aTrieUnit);
	}
	public void setVal(int v) {
		wordID = v;
	}
	public void addSpeech(int aSpeechID) {
		if (speechOfWord == null ) {
			speechOfWord = new ArrayList<SpeechOfWord>();
		}
		int exist = 0;
		for (SpeechOfWord aSpeechOfWord : speechOfWord) {
			if (aSpeechOfWord.SpeechID == aSpeechID) {
				aSpeechOfWord.number ++;
				exist = 1;
				break;
			}
		}
		if (exist == 0) {
			SpeechOfWord aSpeechOfWord = new SpeechOfWord(aSpeechID);
			speechOfWord.add(aSpeechOfWord);
		}
	}
	public ArrayList<SpeechOfWord> getArrayListSpeechOfWord() {
		return speechOfWord;
	}
	public int getWordANSI() {
		return wordANSI;
	}
	public int getWordID() {
		return wordID;
	}
	public TrieUnit findChar(int c) {
		TrieUnit ret = son.floor(new TrieUnit(c));
		if (ret != null && ret.getWordANSI() == c) {
			return ret;
		} else {
			return null;
		}
	}
}
