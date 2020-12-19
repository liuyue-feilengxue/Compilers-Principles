package codeScanner;

import java.util.ArrayList;

public class IrParser {
 
	private Analyzer analyzer;
	private ArrayList<Word> list = new ArrayList<>();
	private Word word;
	private int index = 0; // 从列表中获取单词的下标
	private boolean error = false;
	private int line = 1;
 
	public IrParser() {
		analyzer = new Analyzer("input.txt");
		analyzer.analyze(analyzer.getContent());
		list = analyzer.getList();
	}
	
	public void parse() {
		word = getNext(list);
		if (word.getTypenum() == 9) {
			word = getNext(list);
			pretreatment();
			if (word.getTypenum() == 0 && !error) {
//				完全没有错的
				System.out.println("success");
			}
//			else {
//				if (error && word.getTypenum() == 0) {
////					有错的
//				}
//			}
		}else {
			error = true;
			System.out.println("第"+line+"行"+"begin错误");
			word = getNext(list);
			while(word.getTypenum() != 40) {
				word = getNext(list);
			}
		}
	}

 
	public void pretreatment() {
		statement();
		while (word.getTypenum() == 26||
				word.getTypenum() ==50||
				word.getTypenum() ==51||
				word.getTypenum() ==40) {
			word = getNext(list);
			if (word.getTypenum() == 0) {//结束符
				return;
			}
			if (word.getTypenum() ==50||word.getTypenum() ==51) {
				continue;
			}
			if (word.getTypenum() ==40) {
				line++;
				continue;
			}
			statement();
		}
	}
 
	public void statement() {
//		注释不用管
		if (word.getTypenum() ==50||word.getTypenum() ==51) {
			return;
		}
//		回车不用管
		else if (word.getTypenum() ==40){
			return;
		}
		//int，double，char
		if (word.getTypenum() == 6||word.getTypenum() == 7||word.getTypenum() == 8) {
			//数据定义类型
			word = getNext(list);
			if (word.getTypenum() == 10) {//某变量
				word = getNext(list);
				if (word.getTypenum() == 25) {//赋值
					word = getNext(list);
					expression();
				} else {
					error = true;
					System.out.println("第"+line+"行"+"赋值号错误");
					word = getNext(list);
					while(word.getTypenum() != 40) {
						word = getNext(list);
					}
				}
			} else {
				error = true;
				System.out.println("第"+line+"行"+"语句错误");
				word = getNext(list);
				while(word.getTypenum() != 40) {
					word = getNext(list);
				}
			}
		}
		else {
			word = getNext(list);
			if (word.getTypenum() == 25) {//赋值
				word = getNext(list);
				expression();
			} else {
				error = true;
				System.out.println("第"+line+"行"+"赋值号错误");
				word = getNext(list);
				while(word.getTypenum() != 40) {
					word = getNext(list);
				}
			}
		}
	}
 
	public void expression() {
		term();
		while (word.getTypenum() == 13 || word.getTypenum() == 14) {
			word = getNext(list);
			term();
		}
	}
 
	public void term() {
		factor();
		while (word.getTypenum() == 15 || word.getTypenum() == 16) {
			word = getNext(list);
			factor();
		}
	}
 
	public void factor() {
		if (word.getTypenum() == 10 || word.getTypenum() == 11) {
			word = getNext(list);
		} else if (word.getTypenum() == 27) {
			word = getNext(list);
			expression();
			if (word.getTypenum() == 28) {
				word = getNext(list);
			} else {
				error = true;
				System.out.println("第"+line+"行"+"')'错误");
				word = getNext(list);
				while(word.getTypenum() != 40) {
					word = getNext(list);
				}
			}
		} else {
			error = true;
			System.out.println("第"+line+"行"+"表达式错误");
			word = getNext(list);
			while(word.getTypenum() != 40) {
				word = getNext(list);
			}
		}
	}
 
	public Word getNext(ArrayList<Word> list) {
		if (index < list.size()) {
			return list.get(index++);
		} else {
			return null;
		}
	}
 
	public int getIndex() {
		return index;
	}
 
	public void setIndex(int index) {
		this.index = index;
	}
 
}