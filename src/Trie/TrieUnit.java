package Trie;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Trie树的节点，保存一个字，若为叶子节点还保存该词的词性信息。
 *
 */
public class TrieUnit implements Comparable{
	int wordANSI;//字的ANSI码
	char chinese;//字对应的char，可在调试时显示出中文
	int wordID;//单词ID，此项已被废除
	ArrayList<SpeechOfWord> speechOfWordList;//词性信息。是SpeechOfWord的数组。
	TreeSet<TrieUnit> son;//儿子节点
	
	public TrieUnit(int word) {
		wordANSI = word;
		chinese = (char) wordANSI;
		wordID = 0;
		son = new TreeSet<TrieUnit>();
		speechOfWordList = null;
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
	/**
	 * 向本单词中增加某词性。若该词性不存在，则创建该词性。若存在，则词性对应数量加一。
	 * @param aSpeechID
	 */
	public void addSpeech(int aSpeechID) {
		if (speechOfWordList == null ) {
			speechOfWordList = new ArrayList<SpeechOfWord>();
		}
		int exist = 0;
		for (SpeechOfWord aSpeechOfWord : speechOfWordList) {
			if (aSpeechOfWord.SpeechID == aSpeechID) {
				aSpeechOfWord.number ++;
				exist = 1;
				break;
			}
		}
		if (exist == 0) {
			SpeechOfWord aSpeechOfWord = new SpeechOfWord(aSpeechID);
			speechOfWordList.add(aSpeechOfWord);
		}
	}
	public ArrayList<SpeechOfWord> getArrayListSpeechOfWord() {
		return speechOfWordList;
	}
	public int getWordANSI() {
		return wordANSI;
	}
	public int getWordID() {
		return wordID;
	}
	/**
	 * 在本字的儿子节点中查找。
	 * @param c
	 * @return
	 */
	public TrieUnit findChar(int c) {
		TrieUnit ret = son.floor(new TrieUnit(c));
		if (ret != null && ret.getWordANSI() == c) {
			return ret;
		} else {
			return null;
		}
	}
}
