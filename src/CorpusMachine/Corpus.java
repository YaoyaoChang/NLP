package CorpusMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Trie.*;
import PartOfSpeech.*;

public class Corpus {
	public Trie wordTrie = new Trie();
	public Speech aSpeech = new Speech();
	int CorpusSum = 0;
	
	public int getCorpusSum() {
		return CorpusSum;
	}
	public void addNewWord(String word,Integer wordId) {
		wordTrie.insert(word, wordId);
	}
	public void addSpeechOfWord(String aWord, int aSpeechID) {
		wordTrie.addSpeechOfWord(aWord, aSpeechID);
	}
	public ArrayList<SpeechOfWord> findWord(String word) {
		return wordTrie.find(word);
	}
	public int getWordTrieMaxLength() {
		return wordTrie.getMaxWordLength();
	}
	private boolean isEnd(char c) {
		if (c == ' ' || c == '\n' || c == '\r') {
			return true;
		} else {
			return false;
		}
	}
	public void readCorpusFile(String CorpusFile) throws IOException{
		BufferedReader input = new BufferedReader(new FileReader(CorpusFile));
//		FileWriter fwriter = new FileWriter("TestOfCorpus.txt");//write a test.txt based on corpus.txt
//		FileWriter fwriter = new FileWriter("StandardAnswer.txt");//

		StringBuilder wordString = new StringBuilder("");
		StringBuilder partosString = new StringBuilder("");
		int wordIdCounter = 0;
		
		int wordANSI = 0;
		char wordChar = (char) wordANSI;
		int pre = 0;
		boolean isBos = true;
		int wordID = 0;
		
		while ((wordANSI = input.read()) != -1) {
			wordChar = (char) wordANSI;
			if (wordChar == '/') {//������'/'����ʼ�������
				CorpusSum ++;//�����Ĵʵ�������һ��ͬһ���ʳ��ֶ�����μ�����
				
				while (!isEnd(wordChar)) {
					wordANSI = input.read();
					if (wordANSI == -1) {
						break;
					}
					wordChar = (char) wordANSI;
					if (wordChar == ']') {//question? i don't understand how he deals with the combination of specific word
						while (!isEnd(wordChar) || wordChar == '/') {
							wordANSI = input.read();
							if (wordANSI == -1) {
								break;
							}
							wordChar = (char) wordANSI;
						}
					} else if (!isEnd(wordChar)) {//��Щ���ݾ�Ϊ����
						partosString.append(wordChar);
					}
				}
				String word = wordString.toString();
				String speech = partosString.toString();
//				fwriter.write(word);//write a test.txt based on corpus.txt
//				fwriter.write("/" + speech + "  ");
				int speechID = SpeechList.getSpeechId(speech);
				ArrayList<SpeechOfWord> speechOfWord = findWord(word);
				if (speechOfWord == null) {//�����´�
//					wordId = wordIdCounter;
//					wordIdCounter ++;
					addNewWord(word,wordIdCounter);//�����´�
					wordIdCounter ++;
				} else {//���Ǿɴʣ��ݲ���Ҫ����
					
				}
				addSpeechOfWord(word,speechID);
				
				if (isBos) {//Mr.Guan write a code which I can't understand. And then I write my own code according to my idea.
					{//process EOS at the same time 
						aSpeech.addSpeechTran(pre, 32);
					}
					pre = 0;
					isBos = false;
				}
				aSpeech.addSpeechFreq(speechID);
				aSpeech.addSpeechTran(pre, speechID);
				pre = speechID;
				
				wordString.delete(0, wordString.length());
				partosString.delete(0, partosString.length());
			} else if (wordChar == ' ' || wordChar == '[' || wordChar == '\r' || wordChar == '\n') {
				if (wordChar == '\n' || wordChar == '\r') {
					isBos = true;
//					fwriter.write(wordChar);//write a test.txt based on corpus.txt
				}
				continue;
			} else {//��������»�һֱ�������ӵ�wordString����
				wordString.append(wordChar);
			}
		}
		input.close();
//		fwriter.close();//write a test.txt based on corpus.txt
	}
}
