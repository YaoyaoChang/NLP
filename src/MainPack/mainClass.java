package MainPack;
//here is a comment to test the .gitignore
//here is another comment to test the .gitignore
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import WordMachine.WordMachine;

public class mainClass {
	static long TimeStart;
	static long TimeEnd;
	public static void setTimeStart() {
		TimeStart = System.currentTimeMillis();
	}
	public static void setTimeEnd() {
		TimeEnd = System.currentTimeMillis();
	}
	public static long getTimeUsed() {
		return TimeEnd - TimeStart;
	}
	/**
	 * main函数需要我解释些什么呢？万物归main，main生万物。
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
//		deleteTimeStamp("Corpus.txt","CorpusNew.txt");
//		deleteSpeech("StandardAnswerOfSpeech.txt","StandardAnswerOfSeparate.txt");
//		System.out.println("ok");
//		System.exit(0);
		setTimeStart();
		WordMachine aWordMachine = new WordMachine();//嘿咻注意啦注意啦我要开一个文字处理机了大家看好了
		aWordMachine.setFile("TestOfCorpus.txt", "MiniFmm.txt", "Corpus.txt");
		aWordMachine.initialCorpus();
		System.out.println("flag");
		aWordMachine.HMMOfSepAndPOS();
		aWordMachine.Fmm();
		aWordMachine.checkAccuracy("Fmm","StandardAnswerOfSpeech.txt");
//		aWordMachine.Bmm();
		
		setTimeEnd();
		System.out.println("一共用时： " + getTimeUsed() + " ms");
	}
	/**
	 * 处理带词性的答案文件，得到仅分词的答案文件
	 * @param TargetFile 目标文件，对于此函数为带有词性的答案文件
	 * @param ResultFile 结果文件，对于此函数为仅分词的答案文件
	 * @throws IOException
	 */
	public static void deleteSpeech(String TargetFile, String ResultFile) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(TargetFile));
		FileWriter fwriter = new FileWriter(ResultFile);
		int temRead = 0;
		char temChar = (char) temRead;
		int next = 1;
		while(next == 1) {
			temRead = input.read();
			temChar = (char) temRead;
			if (temRead == -1) {
				next = 0;
			}
			if (temChar != '/') {
				fwriter.write(temChar);
			} else {
				do {
					temRead = input.read();
					temChar = (char) temRead;
					
				} while (temChar != ' ');
				fwriter.write(temChar);
			}
		}
		input.close();
		fwriter.close();		
	}
	/**
	 * 专门处理人民日报预料开头的数字
	 * @param TargetFile 目标文件，对于此函数为原语料库（每句话前含一串数字）
	 * @param ResultFile 结果文件，对于此函数为处理后的语料库（删掉数字）
	 * @throws IOException
	 */
	public static void deleteTimeStamp(String TargetFile, String ResultFile) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(TargetFile));
		FileWriter fwriter = new FileWriter(ResultFile);
		int wordAnsi = 0;
		char wordChar = (char) wordAnsi;
		int next = 1;
		while (next == 1) {
			for (int i = 0; i < 23; i ++) {//人民日报预料前面的时间戳刚好是23个字母
				input.read();
			}
			do {
				wordAnsi = input.read();
				if (wordAnsi == -1) {
					next = 0;
					break;
				}
				wordChar = (char) wordAnsi;
				fwriter.write(wordChar);
			} while(wordChar != '\n');
		}
		input.close();
		fwriter.close();
	}
}
