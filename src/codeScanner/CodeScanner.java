package codeScanner;

/**
 * 
 * @author Administrator
 *if 1
 *then 2
 *while 3
 *do 4
 *for 5
 *int 6
 *double 7
 *char 8
 *begin 9
 *字母数字等自定义字符 10
 *数字 11
 * + 13
 * - 14
 * * 15
 * / 16
 * : 17
 * :=18
 * >20
 * != 21
 * <=22
 * <23
 * >=24
 * =25
 * ; 26
 * (27
 * )28
 * { 29
 * }30
 * "31
 * 
 * 单行注释50
 * 多行51
 */

public class CodeScanner {
	private static String _KEY_WORD_END = "end string of string";
	private char[] input = new char[255];
	private char[] token = new char[255];
	private int p_input=0;
	private int p_token=0;
	private char ch;
	
	private String[] rwtab = {"if","then","while","do","for","int","double","char","begin",_KEY_WORD_END};
	
	public CodeScanner(char[] input) {
		this.input = input;
	}
	/**
	 * 取下一个字符
	 * @return
	 */
	public char m_getch() {
		if(p_input < input.length) {
			ch = input[p_input];
			p_input++;
		}
		return ch;
	}
	/**
	 * 如果是标识符或者空白符就取下一个字符
	 */
	public void getbc() {
		while((ch == ' ' || ch == '\t') && p_input < input.length) {
			ch=input[p_input];
			p_input++;
		}
	}
	/**
	 * 把当前字符和原有字符串连接
	 */
	public void concat() {
		token[p_token] = ch;
		p_token++;
		token[p_token] = '\0';
	}
	
	public boolean letter() {
		if(ch>='a'&&ch<='z'||ch>='A'&&ch<='Z')
			return true;
		else
			return false;
	}
	
	public boolean digit() {
		if(ch>='0'&&ch<='9')
			return true;
		else
			return false;
	}
	/**
	 * 回退一个字符
	 */
	public void retract() {
		p_input--;
	}
	/**
	 * 将token中的数字串转换成二进制值表示
	 * @return
	 */
	public String dtb() {
		int num = token[0] - 48;
		for(int i = 1; i < p_token; i++) {
			num = num * 10 + token[i] - 48;
		}
		StringBuilder result = new StringBuilder();
		while(num>0) {
			int r = num % 2;
			int s = num / 2;
			result.append(r);
			num = s;
		}
		return result.reverse().toString();
	}
	
	/**
	 * 查看token中的字符串是否是关键字，是的话返回关键字种别编码，否则返回10
	 * @return
	 */
	public int reserve() {
		int  i=0;
		while(rwtab[i].compareTo(_KEY_WORD_END)!=0) {
			if(rwtab[i].compareTo(new String(token).trim()) == 0) {
				return i+1;
			}
			i++;
		}
		return 10;
	}
	/**
	 * 能够识别换行，单行注释和多行注释的
	 * 换行的种别码设置成30
	 * 多行注释的种别码设置成31
	 * @return
	 */
	public Word scan() {
		token = new char[255];
		Word myWord = new Word();
		myWord.setTypenum(10);
		myWord.setWord("");
		
		p_token=0;
		m_getch();
		getbc();
		if(letter()) {
			while(letter()||digit()) {
				concat();
				m_getch();
			}
			retract();
			myWord.setTypenum(reserve());
			myWord.setWord(new String(token).trim());
			return myWord;
		}else if(digit()) {
			while(digit()) {
				concat();
				m_getch();
			}
			retract();
			myWord.setTypenum(11);
			myWord.setWord(new String(token).trim());	//输出token中的数字串字符形式
//			myWord.setWord(dtb());						//输出token中的数字串10进制值的二进制字符串形式
			return myWord;
		}
		else 
			switch (ch) {
			case '=':
				myWord.setTypenum(25);
				myWord.setWord("=");
				return myWord;
			case '+':
				myWord.setTypenum(13);
				myWord.setWord("+");
				return myWord;
			case '-':
				myWord.setTypenum(14);
				myWord.setWord("-");
				return myWord;
			case '*':
				myWord.setTypenum(15);
				myWord.setWord("*");
				return myWord;
			case '/':
				m_getch();
				//识别单行注释
				if (ch == '/') {
					while(m_getch() != '\n');
					myWord.setTypenum(50);
					myWord.setWord("单行注释");
					return myWord;
				}
				//识别多行注释
				if(ch=='*') {
					String string = "";
					while(true) {
						if (ch == '*') {
							if (m_getch() == '/') {
								myWord.setTypenum(51);
								myWord.setWord("多行注释");
								return myWord;
							}
							retract();
						}
						if (m_getch() == '\n') {
							string += "";
						}
					}
				}
				retract();
				myWord.setTypenum(16);
				myWord.setWord("/");
				return myWord;
			case ':':
				m_getch();
				if(ch=='=') {
					myWord.setTypenum(18);
					myWord.setWord(":=");
					return myWord;
				}
				retract();
				myWord.setTypenum(17);
				myWord.setWord(":");
				return myWord;
			case '!':
				m_getch();
				if (ch == '=') {
					myWord.setTypenum(21);
					myWord.setWord("!=");
					return myWord;
				}
				retract();
				myWord.setTypenum(-1);
				myWord.setWord("");
				return myWord;
			case '<':
				m_getch();
				if(ch=='=') {
					myWord.setTypenum(22);
					myWord.setWord("<=");
					return myWord;
				}
				retract();
				myWord.setTypenum(20);
				myWord.setWord("<");
				return myWord;
			case '>':
				m_getch();
				if(ch=='=') {
					myWord.setTypenum(24);
					myWord.setWord(">=");
					return myWord;
				}
				retract();
				myWord.setTypenum(23);
				myWord.setWord(">");
				return myWord;
			case ';':
				myWord.setTypenum(26);
				myWord.setWord(";");
				return myWord;
			case '(':
				myWord.setTypenum(27);
				myWord.setWord("(");
				return myWord;
			case ')':
				myWord.setTypenum(28);
				myWord.setWord(")");
				return myWord;
			case '{':
				myWord.setTypenum(29);
				myWord.setWord("{");
				return myWord;
			case '}':
				myWord.setTypenum(30);
				myWord.setWord("}");
				return myWord;
			case '"':
				myWord.setTypenum(31);
				myWord.setWord("\"");
				return myWord;
			case '\n':
				myWord.setTypenum(40);
				myWord.setWord("\\n");
				return myWord;
			case '#':
				myWord.setTypenum(0);
				myWord.setWord("#");
				return myWord;
			
			default:
				concat();
				myWord.setTypenum(-1);
				myWord.setWord("ERROR INFO: WORD = \"" + new String(token).trim() + "\"");
				return myWord;
			}
	}

}
