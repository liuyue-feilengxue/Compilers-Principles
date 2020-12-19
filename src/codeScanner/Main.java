package codeScanner;

public class Main {
	public static void main(String[] args) {
//		Analyzer analyzer = new Analyzer("input.txt");//输入输出可自己修改，文件放在当前文件夹下，刷新项目就可以看到了
//		analyzer.analyze(analyzer.getContent());
//		IrParser irParser = new IrParser();
//		irParser.parse();
		Semantics semantics = new Semantics();
		semantics.parse();
	}
}
