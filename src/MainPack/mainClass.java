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
	 * main������Ҫ�ҽ���Щʲô�أ������main��main�����
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
		WordMachine aWordMachine = new WordMachine();//����ע����ע������Ҫ��һ�����ִ�����˴�ҿ�����
		aWordMachine.setFile("TestOfCorpus.txt", "MiniFmm.txt", "Corpus.txt");
		aWordMachine.initialCorpus();
		System.out.println("flag");
		aWordMachine.HMMOfSepAndPOS();
		aWordMachine.Fmm();
		aWordMachine.checkAccuracy("Fmm","StandardAnswerOfSpeech.txt");
//		aWordMachine.Bmm();
		
		setTimeEnd();
		System.out.println("һ����ʱ�� " + getTimeUsed() + " ms");
	}
	/**
	 * ��������ԵĴ��ļ����õ����ִʵĴ��ļ�
	 * @param TargetFile Ŀ���ļ������ڴ˺���Ϊ���д��ԵĴ��ļ�
	 * @param ResultFile ����ļ������ڴ˺���Ϊ���ִʵĴ��ļ�
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
	 * ר�Ŵ��������ձ�Ԥ�Ͽ�ͷ������
	 * @param TargetFile Ŀ���ļ������ڴ˺���Ϊԭ���Ͽ⣨ÿ�仰ǰ��һ�����֣�
	 * @param ResultFile ����ļ������ڴ˺���Ϊ���������Ͽ⣨ɾ�����֣�
	 * @throws IOException
	 */
	public static void deleteTimeStamp(String TargetFile, String ResultFile) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(TargetFile));
		FileWriter fwriter = new FileWriter(ResultFile);
		int wordAnsi = 0;
		char wordChar = (char) wordAnsi;
		int next = 1;
		while (next == 1) {
			for (int i = 0; i < 23; i ++) {//�����ձ�Ԥ��ǰ���ʱ����պ���23����ĸ
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
