package WordInfo;
import java.util.ArrayList;
import java.util.TreeSet;

import Trie.SpeechOfWord;
/**
 * Ϊ��HMMͬʱ����ִʺͱ�ע����Ҫһ������WordInfoΪ����һ���ڵ㡣
 */
public class WordInfo implements Comparable{
	public String wordString;
	public int cursor;//���
	public ArrayList<SpeechOfWord> speechOfWordList;//ֱ�Ӵ�ôʶ�Ӧ�Ĵ�����Ϣ
	public int height;//���ĸ߶ȣ�Ҳ����ĿǰΪֹ�ʵĸ���
	public int[] path;//path[i]��¼�ű����ʵĵ�i����������Ӧ������·������һ�����ʵ�path[i]����
	public double[] len;//len[i]��¼�����ʵĵ�i�����Զ�Ӧ������·��ֵ
	public TreeSet<WordInfo> son;//���ӽڵ�
	public WordInfo father;//���׽ڵ㡣������ҵ�����·��ֵ֮���������·��ʱ��Ҫ
	/**
	 * ���캯������Ҫ����String���������౻��ʼ����
	 * @param wordString
	 */
	public WordInfo (String wordString) {
		this.wordString = wordString;
		cursor = 0;
		speechOfWordList = null;
		height = 0;
		path = new int[33];
		len = new double[33];
		son = new TreeSet<WordInfo>();
		father = null;
	}
	/**
	 * TreeSet�Ľڵ�ǿ��Ҫ��дһ��compareTo�������������ʵ����Ҫ�������Ծ����д��һ��������wordString��ASCII������
	 */
	public int compareTo(Object aObject) {
		WordInfo aWordInfo = (WordInfo) aObject;
		return this.wordString.compareTo(aWordInfo.wordString);
	}
}