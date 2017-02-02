package Trie;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Trie���Ľڵ㣬����һ���֣���ΪҶ�ӽڵ㻹����ôʵĴ�����Ϣ��
 *
 */
public class TrieUnit implements Comparable{
	int wordANSI;//�ֵ�ANSI��
	char chinese;//�ֶ�Ӧ��char�����ڵ���ʱ��ʾ������
	int wordID;//����ID�������ѱ��ϳ�
	ArrayList<SpeechOfWord> speechOfWordList;//������Ϣ����SpeechOfWord�����顣
	TreeSet<TrieUnit> son;//���ӽڵ�
	
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
	 * �򱾵���������ĳ���ԡ����ô��Բ����ڣ��򴴽��ô��ԡ������ڣ�����Զ�Ӧ������һ��
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
	 * �ڱ��ֵĶ��ӽڵ��в��ҡ�
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
