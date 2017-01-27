package PartOfSpeech;

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
