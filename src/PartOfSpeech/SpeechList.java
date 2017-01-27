package PartOfSpeech;

public class SpeechList {
	public static int unknown = 31;
	public static int size(){
		return 32; 
	}
	/**
	 * ������Ա�ţ����ش����ַ�
	 * @param SpeechId
	 * @return
	 */
	public static String getSpeechStr(int SpeechId) {
		
		switch (SpeechId)
		{
			case 1: // ���ݴ� ������
				return "a";
			case 2: // ����� ��������
				return "b";
			case 3: // ���� ������
				return "c";
			case 4: // ���� ������
				return "d";
			case 5:// ̾�� ̾����
				return "e";
			case 6:// ��λ�� ��λ����
				return "f";
			case 7:// ����
				return "i";
			case 8:// ϰ��
				return "j";
			case 9:// ���� ������
				return "m";
			case 10:// ������
				return "mq";
			case 11: // ���� ������
				return "n";
			case 12:// ������
				return "o";
			case 13:// ���
				return "p";
			case 14:// ���� ������
				return "q";
			case 15: // ���� ������
				return "r";
			case 16: // ������
				return "s";
			case 17:// ʱ���
				return "t";
			case 18: // ���� ������
				return "u";
			case 19:// ���� ������
				return "v";
			case 20:// ������
				return "w";
			case 21: // ��������
				return "x";
			case 22:// ������ ��������
				return "y";
			case 23:// ״̬��
				return "z";
			case 24:// ����
				return "nr";
			case 25:// ����
				return "ns";
			case 26:// ��������
				return "nt";
			case 27:// �����ַ�
				return "nx";
			case 28:// ����ר��
				return "nz";
			case 29:// ǰ�ӳɷ�
				return "h";
			case 30:// ��ӳɷ�
				return "k";
			default:
				return "unknown"; //δ��¼����
		}
	}
	/**
	 * ��������ַ������ش��Ա���
	 * @param S
	 * @return
	 */
	public static int getSpeechId(String S) {
		S = S.toLowerCase();       //ת����Сд
		char S_First = S.charAt(0);//�����ַ��ĵ�һ���ַ�
		if (S_First == 'a') { // ���ݴ� ������
			return 1;
		} else if (S_First == 'b') { // ����� ��������
			return 2;
		} else if (S_First == 'c') { // ���� ������
			return 3;
		} else if (S_First == 'd') { // ���� ������
			return 4;
		} else if (S_First == 'e') {// ̾�� ̾����
			return 5;
		} else if (S_First == 'f') {// ��λ�� ��λ����
			return 6;
		} else if (S_First == 'i') {// ����
			return 7;
		} else if (S_First == 'j' || S_First == 'l') {// ϰ��
			return 8;
		} else if (S.equals("mg") || S.equals("m")) {// ���� ������
			return 9;
		} else if (S.equals("mq")) {// ������
			return 10;
		} else if (S.equals("na") || S.equals("ng") || S.equals("n")) { // ����
			return 11;
		} else if (S_First == 'o') {// ������
			return 12;
		} else if (S_First == 'p') {// ���
			return 13;
		} else if (S_First == 'q') {// ���� ������
			return 14;
		} else if (S_First == 'r') { // ���� ������
			return 15;
		} else if (S_First == 's') { // ������
			return 16;
		} else if (S_First == 't') {// ʱ���
			return 17;
		} else if (S_First == 'u') { // ���� ������
			return 18;
		} else if (S_First == 'v') {// ���� ������
			return 19;
		} else if (S_First == 'w') {// ������
			return 20;
		} else if (S_First == 'x') { // ��������
			return 21;
		} else if (S_First == 'y') {// ������ ��������
			return 22;
		} else if (S_First == 'z') {// ״̬��
			return 23;
		} else if (S.equals("nr")) {// ����
			return 24;
		} else if (S.equals("ns")) {// ����
			return 25;
		} else if (S.equals("nt")) {// ��������
			return 26;
		} else if (S.equals("nx")) {// �����ַ�
			return 27;
		} else if (S.equals("nz")) {// ����ר��
			return 28;
		} else if (S.equals("h")) {// ǰ�ӳɷ�
			return 29;
		} else if (S_First == 'k') {// ��ӳɷ�
			return 30;
		} else {
			return 31; //δ��¼����
		}
	}
}
