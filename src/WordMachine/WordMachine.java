package WordMachine;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import CorpusMachine.*;
import Trie.SpeechOfWord;
import WordInfo.WordInfo;
import PartOfSpeech.*;

/**
 * 文字处理机。
 * 包含几个文件名称，语料库。
 * 包含分词及标注算法。
 */
public class WordMachine {
	String TargetFile;
	String ResultFile;
	String CorpusFile;
	public Corpus aCorpus;
	
	public void setFile(String aTargetFile,String aResultFile, String aCorpusFile) {
		TargetFile = aTargetFile;
		ResultFile = aResultFile;
		CorpusFile = aCorpusFile;
	}
	public String getTargetFile() {
		return TargetFile;
	}
	public String getResultFile() {
		return ResultFile;
	}
	public String getCorpusFile() {
		return CorpusFile;
	}
	/**
	 * 初始化语料库。读入语料库文件，生成语料库模型。
	 * @throws IOException
	 */
	public void initialCorpus() throws IOException {
		aCorpus = new Corpus();
		aCorpus.readCorpusFile(CorpusFile);
	}
	/**
	 * 维特比算法。适用于Fmm中。将Fmm得到的词语数组，进行词性标注，返回词性数组。
	 * @param sentence ArrayList<String>，词语数组
	 * @return ArrayList<String>，返回词性数组
	 */
	public ArrayList<String> Veterbi(ArrayList<String> sentence) {
		if (sentence.size() == 0) {
			return null;
		}
		double[][] len = new double[sentence.size() + 5][33];
		for (int i = 0; i < sentence.size() + 5; i ++) {
			for (int j = 0; j < 33; j ++) {
				len[i][j] = Double.MAX_VALUE;
			}
		}
		int[][] path = new int[sentence.size() + 5][33];
		ArrayList<String> speechOfEachWord = new ArrayList<String>();
		
		//Initialize
		int i,j,k;
		int num;
		StringBuilder word = new StringBuilder();
		ArrayList<SpeechOfWord> SpeechList = new ArrayList<SpeechOfWord>();
		ArrayList<SpeechOfWord> preSpeechList = new ArrayList<SpeechOfWord>();
		i = 1;
		word.append(sentence.get(0));
		SpeechList = aCorpus.findWord(word.toString());
		if (SpeechList == null) {//佩服Java的检查错误机制，逼我不得不写的更加健壮
			SpeechList = new ArrayList<SpeechOfWord>();
			SpeechList.add(new SpeechOfWord(32));
//			return null;
		}
		for (SpeechOfWord speech : SpeechList ) {
			j = speech.SpeechID;
			num = speech.number;
			path[i][j] = 0;
			len[i][j] = getLenOfWordInSpeech(word.toString(),j,num) + getLenOfSpeechTransform(0,j);
		}
		preSpeechList = SpeechList;
		
		//veterbi dynamic programming
		for (i = 2; i <= sentence.size(); i ++ ) {
			word.delete(0, word.length());
			word.append(sentence.get(i-1));
			SpeechList = aCorpus.findWord(word.toString());
			if (SpeechList == null) {
				SpeechList = new ArrayList<SpeechOfWord>();
				SpeechList.add(new SpeechOfWord(32));
//				continue;
			}
			for (SpeechOfWord speech : SpeechList) {
				j = speech.SpeechID;
				num = speech.number;
				len[i][j] = Double.MAX_VALUE;
				for (SpeechOfWord preSpeech : preSpeechList) {
					k = preSpeech.SpeechID;
					double tem = getLenOfWordInSpeech(word.toString(),j,num) + getLenOfSpeechTransform(k,j) + len[i - 1][k];
					if (len[i][j] > tem) {
						len[i][j] = tem;
						path[i][j] = k;
					}
				}
			}
			preSpeechList = SpeechList;
		}
		//EOS(end of sentence)
		i = sentence.size();
		for (SpeechOfWord speech : SpeechList) {
			j = speech.SpeechID;
			len[i][j] += getLenOfSpeechTransform(j,32);
		}
		//find the best path
		i = sentence.size();
		int[] best = new int[sentence.size()+2];
		double min = Double.MAX_VALUE;
		//find the minimum length of path
		for (j = 1; j <= 32; j ++) {
			if (min > len[i][j] ) {
				min = len[i][j];
				best[i] = j;
			}
		}
		//record the path from the end of the sentence using path[][]. save into speechOfEachWord.
		for (i = sentence.size() - 1; i > 0; i --) {
			best[i] = path[i + 1][best[i + 1]];
		}
		for (i = 1; i <= sentence.size(); i ++) {
			speechOfEachWord.add(PartOfSpeech.SpeechList.getSpeechStr(best[i]));
		}
		return speechOfEachWord;
	}
	/**
	 * 得到词性转移概率。如名词到动词的转移概率。
	 * @param preSpeechID 
	 * @param nowSpeechID 
	 * @return 概率取对数的负数
	 */
	private double getLenOfSpeechTransform(int preSpeechID, int nowSpeechID) {
		double tran = aCorpus.aSpeech.getSpeechTran(preSpeechID, nowSpeechID);
		double now = aCorpus.aSpeech.getSpeechFreq(nowSpeechID);
		if (nowSpeechID == 32) {
			now = 0.01 * aCorpus.getCorpusSum();
			tran += 1;
		}
		double probability = 0.7 * tran / now + 0.3 * aCorpus.aSpeech.getSpeechFreq(nowSpeechID) / aCorpus.getCorpusSum();
		double len = - Math.log(probability);
		return len;
	}
	/**
	 * 得到某词语在某词性下的概率。如“希望”可作为名词，则输入对应参数，返回一个名词是“希望”的概率。
	 * @param word 词语，String型，在有第三个参数num的情况下可省。
	 * @param speechID 词性ID
	 * @param num 数量，即word对应speechID的数量。可以通过前两项查找到第三项的值，但一般可在函数外得到num，则顺便传进来。
	 * @return
	 */
	private double getLenOfWordInSpeech(String word, int speechID, int num) {
		double freq = aCorpus.aSpeech.getSpeechFreq(speechID);
		if (speechID == 32) {
			freq = 0.01 * aCorpus.getCorpusSum();
		}
		double len = - Math.log(num / freq);
		return len;
	}
	/**
	 * 使用隐含马尔科夫同时解决分词和词性标注。即在分词阶段就利用字典穷举所有可能性,比如“研究生物”会分词为“研究 生物”和“研究生 物”，然后分别计算词性标注路径长度取小者。
	 * @throws IOException
	 */
	public void HMMOfSepAndPOS() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(TargetFile));
		StringBuilder sentence = new StringBuilder("");
		int temRead;
		char temChar;
		int nextSentenceFlag = 1;
		
		ResultFile = new String("HMM" + TargetFile);
		FileWriter fwriter = new FileWriter(ResultFile);
		
		StringBuilder sb = new StringBuilder("");
		int cursor= 0;
		int loopLen = 0;
		
		ArrayList<String> wordArray = new ArrayList<String>();
		ArrayList<String> speechArray = new ArrayList<String>();
		
		while (nextSentenceFlag == 1) {
			do {//读入一整个句子
				temRead = input.read();
				temChar = (char) temRead;
				if (temChar == '\r') {
					continue;
				} else if (temChar == '\n') {
					nextSentenceFlag = 1;
					break;
				} else if (temRead == -1) {//若读到了文件末尾，则标记，表示处理完本句子之后就退出大循环
					nextSentenceFlag = 0;
					break;
				} else {
					sentence.append(temChar);
				}
			} while(true);
//			System.out.println("##" + sentence + "&&");
			//开始处理本句
			//首先分词。本处利用了树的结果，如“小明研究生物”的树，树根为“小明”，左右儿子分别为“研究”，“研究生”，分别有单儿子为“生物”，“物”。
			cursor = 0;
			WordInfo root = new WordInfo(null);
			WordInfo now = null;
			boolean IsWord = false;
			Queue<WordInfo> queue = new LinkedList<WordInfo>();//利用队列，进行广搜。
			queue.offer(root);//先入队根节点
			boolean BreakFlag = false;
			
			while (queue.size() > 0) {
				if (queue.size() > sentence.length() / 4) {//因为此算法时间复杂度随着句子长度的增加而指数级增加，所以如果发现本次广搜宽度过大的话就以后不再产生分支
					BreakFlag = true;
				}
				now = queue.poll();//取出当前节点
				cursor = now.cursor;
				loopLen = (sentence.length() - cursor) < aCorpus.getWordTrieMaxLength() ? 
						(sentence.length() - cursor) : aCorpus.getWordTrieMaxLength();//循环长度为句子剩余长度和最大词长度的最小值
				IsWord = false;
//				System.out.println(now.wordString);
				for (int i = loopLen;i >= 1; i --) {//遍历所有可能的词
					if (i == 1 && IsWord == true) {//若i==1(单字)，且之前没有查找到任何词(IsWord==false)，则继续查找本字
						continue;
					}
					sb.delete(0, sb.length());
					sb.append(sentence.subSequence(cursor, cursor + i));
					ArrayList<SpeechOfWord> speechOfWord = aCorpus.findWord(sb.toString());
					if (speechOfWord != null || i == 1) {//(1)若查找到本单词则进入(2)若只剩单字，则直接进入
						//处理新词及各种信息
						IsWord = true;
						WordInfo next = new WordInfo(sb.toString());
						next.cursor = cursor + i;
						next.height = now.height + 1;
						next.speechOfWordList = speechOfWord;
						next.father = now;
						now.son.add(next);
						queue.offer(next);
						if (BreakFlag == true) {
							break;
						}
					} else {//若未查找到本词，不需要做任何处理
						
					}
				}					
			}
//			System.out.println("flag2");
			//Veterbi算法(针对HMMOfSepAndPOS算法)，算出树上的每个节点上的每个词性的当前最优值
			WordInfo pre = null;
			root.speechOfWordList = new ArrayList<SpeechOfWord>();
			root.speechOfWordList.add(new SpeechOfWord(32));
			root.path[32] = 0;
			root.len[32] = 0;
			queue.offer(root);
			Iterator<WordInfo> iterator = null;
			int j,k,num;
			double tem;
			double min = Double.MAX_VALUE;
			int minSpeech = 0;
			WordInfo minWordInfo = null;
			
			while(queue.size() > 0) {
				pre = queue.poll();
				if (pre.speechOfWordList == null) {
					pre.speechOfWordList = new ArrayList<SpeechOfWord>();
					pre.speechOfWordList.add(new SpeechOfWord(32));
				}
				iterator = pre.son.iterator();
				while ( iterator.hasNext()) {
					now = iterator.next();
					if (now.speechOfWordList == null) {
						now.speechOfWordList = new ArrayList<SpeechOfWord>();
						now.speechOfWordList.add(new SpeechOfWord(32));
//						System.out.println("now.speechOfWordList is null");
//						return;
					}
					for (SpeechOfWord speech : now.speechOfWordList) {
						j = speech.SpeechID;
						num = speech.number;
						now.len[j] = Double.MAX_VALUE;
						for (SpeechOfWord preSpeech : pre.speechOfWordList) {
							k = preSpeech.SpeechID;
							tem = getLenOfWordInSpeech(null,j,num) + getLenOfSpeechTransform(k,j) + pre.len[k];
							if (now.len[j] > tem) {
								now.len[j] = tem;
								now.path[j] = k;
							}
						}
						//(1)find the minimum length of path  (2)record the WordInfo and speech
						if (now.cursor == sentence.length() && min > now.len[j]) {
							min = now.len[j];
							minSpeech = j;
							minWordInfo = now;
						}
					}
					queue.offer(now);
					
				}
			}
			//No EOS (I've forgotten the meaning of EOS so I didn't write the code of EOS)
			
			//record the best path
//			System.out.println(min);
			int[] path = new int[sentence.length() + 1];
			if (minWordInfo == null) {
				continue;
			}
			int height = minWordInfo.height;
			now = minWordInfo;
			int nowSpeech = minSpeech;
//			System.out.println(now.height + "  " + sentence.length());
			while (now.father != null) {
				path[now.height] = nowSpeech;
				wordArray.add(now.wordString);
				speechArray.add(PartOfSpeech.SpeechList.getSpeechStr(nowSpeech));
				nowSpeech = now.path[nowSpeech];
				now = now.father;
			}
			
			sentence.delete(0, sentence.length());
			if (speechArray == null) {
				continue;
			}
			int size = wordArray.size();
			for (int i = size - 1; i >= 0; i --) {
				fwriter.write(wordArray.get(i));
				fwriter.write("/");
				fwriter.write(speechArray.get(i));
				fwriter.write("  ");
			}
			
			wordArray.clear();
			speechArray.clear();
			
			fwriter.write("\r\n");
		}
		
		input.close();
		fwriter.close();
	}
	/**
	 * FMM即正向最大匹配法。本函数同时进行词性标注。对于待切分句库(TargetFile)生成文件名为"Fmm"+TargetFile的标注文件。
	 * @throws IOException
	 */
	public void Fmm() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(TargetFile));
		StringBuilder sentence = new StringBuilder("");
		int temRead;
		char temChar;
		int nextSentenceFlag = 1;
		
		ResultFile = new String("Fmm" + TargetFile);
		FileWriter fwriter = new FileWriter(ResultFile);
		
		StringBuilder string = new StringBuilder("");
		int cursor= 0;
		int loopLen = 0;
		int wordID = 0;
		
		ArrayList<String> wordArray = new ArrayList<String>();
		
		while (nextSentenceFlag == 1) {
			do {//读入一整个句子
				temRead = input.read();
				temChar = (char) temRead;
				if (temChar == '\r') {
					continue;
				} else if (temChar == '\n') {
					nextSentenceFlag = 1;
					break;
				} else if (temRead == -1) {//若读到了文件末尾，则标记，表示处理完本句子之后就退出大循环
					nextSentenceFlag = 0;
					break;
				} else {
					sentence.append(temChar);
				}
			} while(true);
//			System.out.println(sentence);
			//开始处理本句
			cursor = 0;
			while (cursor < sentence.length()) {//光标一直向后走，直到句子末尾为止
				loopLen = (sentence.length() - cursor) < aCorpus.getWordTrieMaxLength() ? 
						(sentence.length() - cursor) : aCorpus.getWordTrieMaxLength();//循环长度为句子剩余长度和最大词长度的最小值
				while (loopLen > 0) {//对于cursor开头的单词进行遍历查找是否为词，从loopLen的最大值一直遍历到0
					string.delete(0, string.length());
					string.append(sentence.subSequence(cursor, cursor + loopLen));
					ArrayList<SpeechOfWord> speechOfWord = aCorpus.findWord(string.toString());
					if (speechOfWord == null) {
						loopLen --;
						continue;
					} else {
						break;
					}
				}
				if (loopLen == 0) {//若没有找到任何词，则输出本字
					loopLen ++;
					string.delete(0, string.length());
					string.append(sentence.subSequence(cursor, cursor + loopLen));
//					fwriter.write(string.toString() + " ");
				} else {//若找到该词，则输出
//					fwriter.write(string.toString() + " ");
				}
				wordArray.add(string.toString());
				cursor += loopLen;//光标向后移动
			}
			sentence.delete(0, sentence.length());
			ArrayList<String> speechArray = Veterbi(wordArray);
			if (speechArray == null) {
				continue;
			}
			int size = wordArray.size();
			for (int i = 0; i < size; i ++) {
				fwriter.write(wordArray.get(i));
				fwriter.write("/");
				fwriter.write(speechArray.get(i));
				fwriter.write("  ");
			}
			
			wordArray.clear();
			
			fwriter.write("\r\n");
		}
		
		input.close();
		fwriter.close();
	}
	/**
	 * 逆向最大匹配算法。使用时需将带有“//Bmm”注释的句子进行特殊处理。
	 * @throws IOException
	 */
	public void Bmm() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(TargetFile));
		StringBuilder sentence = new StringBuilder("");
		StringBuilder temSentence = new StringBuilder("");//Bmm
		int temRead;
		char temChar;
		int nextSentenceFlag = 1;
		
		ResultFile = new String("Bmm" + TargetFile);//Bmm
		FileWriter fwriter = new FileWriter(ResultFile);
		
		StringBuilder string = new StringBuilder("");
		int cursor= 0;
		int loopLen = 0;
		int wordID = 0;
		
		while (nextSentenceFlag == 1) {
			do {
				temRead = input.read();
				temChar = (char) temRead;
				if (temChar == '\r') {
					continue;
				} else if (temChar == '\n') {
					nextSentenceFlag = 1;
					break;
				} else if (temRead == -1) {
					nextSentenceFlag = 0;
					break;
				} else {
					sentence.append(temChar);
				}
			} while(true);
		
			cursor = 0;
			sentence.reverse();//Bmm
			while (cursor < sentence.length()) {
				loopLen = (sentence.length() - cursor) < aCorpus.getWordTrieMaxLength() ? 
						(sentence.length() - cursor) : aCorpus.getWordTrieMaxLength();
				while (loopLen > 0) {
					string.delete(0, string.length());
					string.append(sentence.subSequence(cursor, cursor + loopLen));
					string.reverse();//Bmm
					ArrayList<SpeechOfWord> speechOfWord = aCorpus.findWord(string.toString());
					if (speechOfWord == null) {
						loopLen --;
						continue;
					} else {
						break;
					}
				}
				if (loopLen == 0) {
					loopLen ++;
					string.delete(0, string.length());
					string.append(sentence.subSequence(cursor, cursor + loopLen));
					temSentence.insert(0, string.toString() + " ");//Bmm
//					fwriter.write(string.toString() + " ");
				} else {
					temSentence.insert(0, string.toString() + " ");//Bmm
//					fwriter.write(string.toString() + " ");
				}
				cursor += loopLen;
			}
			sentence.delete(0, sentence.length());
			fwriter.write(temSentence + "\r\n");
			temSentence.delete(0, temSentence.length());//Bmm
		}
		
		input.close();
		fwriter.close();
	}
	/**
	 * 对比结果文件与答案文件，计算正确率。
	 * @param prefix 结果文件的前缀。结果文件=prefix+目标文件。取值为{"Fmm","HMM"}
	 * @param AnswerFile 标准答案文件
	 * @throws IOException
	 */
	public void checkAccuracy(String prefix,String AnswerFile) throws IOException {
//		ResultFile = new String("FmmTestOfCorpus.txt");
		ResultFile = new String(prefix + TargetFile);
		BufferedReader inputAns = new BufferedReader(new FileReader(AnswerFile));
		BufferedReader inputRes = new BufferedReader(new FileReader(ResultFile));
		int nextAnswerSentenceFlag = 1, nextResultSentenceFlag = 1;
		int temRead;
		char temChar;
		StringBuilder sentenceAns = new StringBuilder("");
		StringBuilder sentenceRes = new StringBuilder("");
		ArrayList<String> wordsAns = new ArrayList<String>();
		ArrayList<String> wordsRes = new ArrayList<String>();
		StringBuilder word = new StringBuilder("");
		int TotalAnswer = 0;
		int RightAnswer = 0;
		while (nextAnswerSentenceFlag == 1 && nextResultSentenceFlag == 1) {
			do {//读入一整个句子
				temRead = inputAns.read();
				temChar = (char) temRead;
				if (temChar == '\r') {
					continue;
				} else if (temChar == '\n') {
					nextAnswerSentenceFlag = 1;
					break;
				} else if (temRead == -1) {//若读到了文件末尾，则标记，表示处理完本句子之后就退出大循环
					nextAnswerSentenceFlag = 0;
					break;
				} else {
					sentenceAns.append(temChar);
				}
			} while(true);
			sentenceAns.append(' ');
			//read strings in the sentence which are separated by the space
			for (int i = 0; i < sentenceAns.length(); i ++) {
				temChar = sentenceAns.charAt(i);
				if (temChar != ' ') {
					word.append(temChar);
				} else {
					if (i + 1 < sentenceAns.length() && sentenceAns.charAt(i + 1) == ' ') {
						i ++;
					}
					wordsAns.add(word.toString());
					word.delete(0, word.length());
				}
			}
			do {//读入一整个句子
				temRead = inputRes.read();
				temChar = (char) temRead;
				if (temChar == '\r') {
					continue;
				} else if (temChar == '\n') {
					nextResultSentenceFlag = 1;
					break;
				} else if (temRead == -1) {//若读到了文件末尾，则标记，表示处理完本句子之后就退出大循环
					nextResultSentenceFlag = 0;
					break;
				} else {
					sentenceRes.append(temChar);
				}
			} while(true);
			sentenceRes.append(' ');
			for (int i = 0; i < sentenceRes.length(); i ++) {
				temChar = sentenceRes.charAt(i);
				if (temChar != ' ') {
					word.append(temChar);
				} else {
					if (i + 1 < sentenceRes.length() && sentenceRes.charAt(i + 1) == ' ') {
						i ++;
					}
					wordsRes.add(word.toString());
					word.delete(0, word.length());
				}
			}
			//Calculate the number of right answer within all answers
			
			for (int i = 0; i < wordsAns.size(); i ++) {
				TotalAnswer ++;
				//判断对错时，往前和后分别找3位看看有没有对的答案，因为也许可能某个大词被切分为两个小词然后后面的词全错位了。
				for (int k = i - 3; k <= i + 3; k ++) {
					if (k >= 0 && k < wordsRes.size() && wordsAns.get(i).equals(wordsRes.get(k))) {
						RightAnswer ++;
						break;
					}
				}
//				if (i < wordsRes.size() && wordsAns.get(i).equals(wordsRes.get(i)) && i + 2 < wordsRes.size() && wordsAns.get(i).equals(wordsRes.get(i + 2))) {
//					RightAnswer ++;
//					continue;
//				}
//				if (i + 1 < wordsRes.size() && wordsAns.get(i).equals(wordsRes.get(i + 1))) {
//					RightAnswer ++;
//					continue;
//				}
//				if (i + 2 < wordsRes.size() && wordsAns.get(i).equals(wordsRes.get(i + 2))) {
//					RightAnswer ++;
//					continue;
//				}
//				if (i - 1 < wordsRes.size() && i - 1 >= 0 && wordsAns.get(i).equals(wordsRes.get(i - 1))) {
//					RightAnswer ++;
//					continue;
//				}
//				if (i - 2 < wordsRes.size() && i - 2 >= 0 && wordsAns.get(i).equals(wordsRes.get(i - 2))) {
//					RightAnswer ++;
//					continue;
//				}
//				if (i - 3 < wordsRes.size() && i - 3 >= 0 && wordsAns.get(i).equals(wordsRes.get(i - 3))) {
//					RightAnswer ++;
//					continue;
//				}
			}
			sentenceAns.delete(0, sentenceAns.length());
			sentenceRes.delete(0, sentenceRes.length());
			wordsAns.clear();
			wordsRes.clear();
		}
		System.out.println("Total word's number is " + TotalAnswer);
		System.out.println("Right word's number is " + RightAnswer);
		System.out.println("Right rate is " + (double)RightAnswer/TotalAnswer);
		inputAns.close();
		inputRes.close();
	}
}
	
