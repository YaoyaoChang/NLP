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

	public static void main(String[] args) throws IOException
	{
//		deleteTimeStamp("Corpus.txt","CorpusNew.txt");
//		System.exit(0);
		setTimeStart();
		WordMachine aWordMachine = new WordMachine();
		aWordMachine.setFile("TestOfCorpus.txt", "MiniFmm.txt", "Corpus.txt");
//		aWordMachine.initialCorpus();
//		System.out.println(aWordMachine.aCorpus.getWordTrieMaxLength());
//		System.out.println(aWordMachine.aCorpus.wordTrie.MaxString);
//		aWordMachine.Fmm();
		aWordMachine.checkAccuracy("StandardAnswer.txt");
//		aWordMachine.Bmm();
		
		setTimeEnd();
		System.out.println("一共用时： " + getTimeUsed() + " ms");
	}
	public static void deleteTimeStamp(String TargetFile, String ResultFile) throws IOException {//专门处理人民日报预料开头的数字
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
