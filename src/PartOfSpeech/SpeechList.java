package PartOfSpeech;

public class SpeechList {
	public static int unknown = 31;
	public static int size(){
		return 32; 
	}
	/**
	 * 输入词性编号，返回词性字符
	 * @param SpeechId
	 * @return
	 */
	public static String getSpeechStr(int SpeechId) {
		
		switch (SpeechId)
		{
			case 1: // 形容词 形语素
				return "a";
			case 2: // 区别词 区别语素
				return "b";
			case 3: // 连词 连语素
				return "c";
			case 4: // 副词 副语素
				return "d";
			case 5:// 叹词 叹语素
				return "e";
			case 6:// 方位词 方位语素
				return "f";
			case 7:// 成语
				return "i";
			case 8:// 习语
				return "j";
			case 9:// 数词 数语素
				return "m";
			case 10:// 数量词
				return "mq";
			case 11: // 名词 名语素
				return "n";
			case 12:// 拟声词
				return "o";
			case 13:// 介词
				return "p";
			case 14:// 量词 量语素
				return "q";
			case 15: // 代词 代语素
				return "r";
			case 16: // 处所词
				return "s";
			case 17:// 时间词
				return "t";
			case 18: // 助词 助语素
				return "u";
			case 19:// 动词 动语素
				return "v";
			case 20:// 标点符号
				return "w";
			case 21: // 非语素字
				return "x";
			case 22:// 语气词 语气语素
				return "y";
			case 23:// 状态词
				return "z";
			case 24:// 人名
				return "nr";
			case 25:// 地名
				return "ns";
			case 26:// 机构团体
				return "nt";
			case 27:// 外文字符
				return "nx";
			case 28:// 其他专名
				return "nz";
			case 29:// 前接成分
				return "h";
			case 30:// 后接成分
				return "k";
			default:
				return "unknown"; //未登录词性
		}
	}
	/**
	 * 输入词性字符，返回词性编码
	 * @param S
	 * @return
	 */
	public static int getSpeechId(String S) {
		S = S.toLowerCase();       //转换成小写
		char S_First = S.charAt(0);//词性字符的第一个字符
		if (S_First == 'a') { // 形容词 形语素
			return 1;
		} else if (S_First == 'b') { // 区别词 区别语素
			return 2;
		} else if (S_First == 'c') { // 连词 连语素
			return 3;
		} else if (S_First == 'd') { // 副词 副语素
			return 4;
		} else if (S_First == 'e') {// 叹词 叹语素
			return 5;
		} else if (S_First == 'f') {// 方位词 方位语素
			return 6;
		} else if (S_First == 'i') {// 成语
			return 7;
		} else if (S_First == 'j' || S_First == 'l') {// 习语
			return 8;
		} else if (S.equals("mg") || S.equals("m")) {// 数词 数语素
			return 9;
		} else if (S.equals("mq")) {// 数量词
			return 10;
		} else if (S.equals("na") || S.equals("ng") || S.equals("n")) { // 名词
			return 11;
		} else if (S_First == 'o') {// 拟声词
			return 12;
		} else if (S_First == 'p') {// 介词
			return 13;
		} else if (S_First == 'q') {// 量词 量语素
			return 14;
		} else if (S_First == 'r') { // 代词 代语素
			return 15;
		} else if (S_First == 's') { // 处所词
			return 16;
		} else if (S_First == 't') {// 时间词
			return 17;
		} else if (S_First == 'u') { // 助词 助语素
			return 18;
		} else if (S_First == 'v') {// 动词 动语素
			return 19;
		} else if (S_First == 'w') {// 标点符号
			return 20;
		} else if (S_First == 'x') { // 非语素字
			return 21;
		} else if (S_First == 'y') {// 语气词 语气语素
			return 22;
		} else if (S_First == 'z') {// 状态词
			return 23;
		} else if (S.equals("nr")) {// 人名
			return 24;
		} else if (S.equals("ns")) {// 地名
			return 25;
		} else if (S.equals("nt")) {// 机构团体
			return 26;
		} else if (S.equals("nx")) {// 外文字符
			return 27;
		} else if (S.equals("nz")) {// 其他专名
			return 28;
		} else if (S.equals("h")) {// 前接成分
			return 29;
		} else if (S_First == 'k') {// 后接成分
			return 30;
		} else {
			return 31; //未登录词性
		}
	}
}
