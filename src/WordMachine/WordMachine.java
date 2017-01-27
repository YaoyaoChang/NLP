package WordMachine;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;

import CorpusMachine.*;
import Trie.SpeechOfWord;
import PartOfSpeech.*;


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
	public void initialCorpus() throws IOException {
		aCorpus = new Corpus();
		aCorpus.readCorpusFile(CorpusFile);
	}
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
		
		//unfinished code here. I need to calculate the probability of speech-transform and a certain speech of word in this speech.
		//Then I can use these information to calculate the minimum length. Of course, I hadn't consider the zero .
		
		//Initialize
		int i,j,k;
		int num;
		StringBuilder word = new StringBuilder();
		ArrayList<SpeechOfWord> SpeechList = new ArrayList<SpeechOfWord>();
		ArrayList<SpeechOfWord> preSpeechList = new ArrayList<SpeechOfWord>();
		i = 1;
		word.append(sentence.get(0));
		SpeechList = aCorpus.findWord(word.toString());
		if (SpeechList == null) {
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
	private double getLenOfWordInSpeech(String word, int speechID, int num) {
		double freq = aCorpus.aSpeech.getSpeechFreq(speechID);
		if (speechID == 32) {
			freq = 0.01 * aCorpus.getCorpusSum();
		}
		double len = - Math.log(num / freq);
		return len;
	}
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
			do {//����һ��������
				temRead = input.read();
				temChar = (char) temRead;
				if (temChar == '\r') {
					continue;
				} else if (temChar == '\n') {
					nextSentenceFlag = 1;
					break;
				} else if (temRead == -1) {//���������ļ�ĩβ�����ǣ���ʾ�����걾����֮����˳���ѭ��
					nextSentenceFlag = 0;
					break;
				} else {
					sentence.append(temChar);
				}
			} while(true);
			
			//��ʼ������
			cursor = 0;
			while (cursor < sentence.length()) {//���һֱ����ߣ�ֱ������ĩβΪֹ
				loopLen = (sentence.length() - cursor) < aCorpus.getWordTrieMaxLength() ? 
						(sentence.length() - cursor) : aCorpus.getWordTrieMaxLength();//ѭ������Ϊ����ʣ�೤�Ⱥ����ʳ��ȵ���Сֵ
				while (loopLen > 0) {//����cursor��ͷ�ĵ��ʽ��б��������Ƿ�Ϊ�ʣ���loopLen�����ֵһֱ������0
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
				if (loopLen == 0) {//��û���ҵ��κδʣ����������
					loopLen ++;
					string.delete(0, string.length());
					string.append(sentence.subSequence(cursor, cursor + loopLen));
//					fwriter.write(string.toString() + " ");
				} else {//���ҵ��ôʣ������
//					fwriter.write(string.toString() + " ");
				}
				wordArray.add(string.toString());
				cursor += loopLen;//�������ƶ�
			}
			sentence.delete(0, sentence.length());
			ArrayList<String> speechOfEachWord = Veterbi(wordArray);
			if (speechOfEachWord == null) {
				continue;
			}
			int size = wordArray.size();
			for (int i = 0; i < size; i ++) {
				fwriter.write(wordArray.get(i));
				fwriter.write("/");
				fwriter.write(speechOfEachWord.get(i) + "  ");
			}
			
			wordArray.clear();
			//after the Veterbi algorithm finished, here should be output statement by using wordArray and speechOfEachWord
			
			fwriter.write("\r\n");
		}
		
		input.close();
		fwriter.close();
	}
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
	public void checkAccuracy(String AnswerFile) throws IOException {
		ResultFile = new String("FmmTestOfCorpus.txt");
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
			do {//����һ��������
				temRead = inputAns.read();
				temChar = (char) temRead;
				if (temChar == '\r') {
					continue;
				} else if (temChar == '\n') {
					nextAnswerSentenceFlag = 1;
					break;
				} else if (temRead == -1) {//���������ļ�ĩβ�����ǣ���ʾ�����걾����֮����˳���ѭ��
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
			do {//����һ��������
				temRead = inputRes.read();
				temChar = (char) temRead;
				if (temChar == '\r') {
					continue;
				} else if (temChar == '\n') {
					nextResultSentenceFlag = 1;
					break;
				} else if (temRead == -1) {//���������ļ�ĩβ�����ǣ���ʾ�����걾����֮����˳���ѭ��
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
				//�ж϶Դ�ʱ����ǰ�ͺ�ֱ���3λ������û�жԵĴ𰸣���ΪҲ�����ĳ����ʱ��з�Ϊ����С��Ȼ�����Ĵ�ȫ��λ�ˡ�
				for (int k = i - 4; k <= i + 2; k ++) {
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
	
