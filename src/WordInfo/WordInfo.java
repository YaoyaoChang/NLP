package WordInfo;
import java.util.ArrayList;
import java.util.TreeSet;

import Trie.SpeechOfWord;
/**
 * 为了HMM同时处理分词和标注，需要一棵树，WordInfo为树的一个节点。
 */
public class WordInfo implements Comparable{
	public String wordString;
	public int cursor;//光标
	public ArrayList<SpeechOfWord> speechOfWordList;//直接存该词对应的词性信息
	public int height;//树的高度，也就是目前为止词的个数
	public int[] path;//path[i]记录着本单词的第i个词性所对应的最优路径是上一个单词的path[i]词性
	public double[] len;//len[i]记录本单词的第i个词性对应的最优路径值
	public TreeSet<WordInfo> son;//儿子节点
	public WordInfo father;//父亲节点。在最后找到最优路径值之后回溯整条路径时需要
	/**
	 * 构造函数。需要词语String参数，其余被初始化。
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
	 * TreeSet的节点强行要求写一个compareTo，反正这棵树其实不需要排序，所以就随便写了一个，按照wordString的ASCII码排序
	 */
	public int compareTo(Object aObject) {
		WordInfo aWordInfo = (WordInfo) aObject;
		return this.wordString.compareTo(aWordInfo.wordString);
	}
}