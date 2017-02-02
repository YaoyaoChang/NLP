package PartOfSpeech;

/**
 * 词性信息。
 * 包含词性矩阵。
 * 包含词性转移矩阵。
 * 其中，1~30代表词性，31代表unknown，0代表BOS，32代表EOS。
 */
public class Speech {
	//0 represents BOS(beginning of sentence),32 represents EOS(end of sentence)
	int[] speechFreq = new int[32 + 3];
	int[][] speechTran = new int[35][35];
	public void addSpeechFreq(int aSpeechID) {
		speechFreq[aSpeechID] ++;
	}
	public int getSpeechFreq(int aSpeechID) {
		return speechFreq[aSpeechID];
	}
	public void addSpeechTran(int aSpeechID, int bSpeechID) {
		speechTran[aSpeechID][bSpeechID] ++;
	}
	public int getSpeechTran(int aSpeechID, int bSpeechID) {
		return speechTran[aSpeechID][bSpeechID];
	}
}
