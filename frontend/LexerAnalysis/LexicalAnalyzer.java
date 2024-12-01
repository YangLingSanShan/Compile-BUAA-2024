package frontend.LexerAnalysis;

import io.Conponent.ErrorToken;
import io.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class LexicalAnalyzer implements ErrorThrower {

    private boolean isComment = false;
    private String word = "";

    private final HashSet<Character> singleWords = new HashSet<>();
    private final HashSet<Character> compareWords = new HashSet<>();

    public LexicalAnalyzer() {
        Collections.addAll(singleWords, '{', '}', '[', ']', '(', ')', '+', '-', '*', '%', ';', ',');
        Collections.addAll(compareWords, '>', '=', '<', '!');
    }

    public ArrayList<String> analyze(String line, int lineNum) {
        ArrayList<String> originTokens = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            //TODO: 处理分隔符，处理比较，处理与或，处理其它字符串，处理注释，处理引号内字符串
            if (this.isComment) {   // 此时只会是/**/的注释形式，因为//的注释在检测到的时候直接break了。
                if (i + 1 < line.length() && (line.charAt(i) == '*' && line.charAt(i + 1) == '/')) {
                    this.isComment = false;     //不再是注释
                    i++;                        //此时是* 需要再向前一步
                }
                continue;
            }
            char nowChar = line.charAt(i);      //读现在的字符
            if (compareWords.contains(nowChar)) {          //现在是比较，需要先把前面的存进去，然后再看后面有没有等于号
                this.addWord(originTokens);
                if (i + 1 < line.length() && line.charAt(i + 1) == '=') {
                    this.addWord(originTokens, nowChar + "=");
                    i++;
                } else {
                    this.addWord(originTokens, nowChar);
                }
            } else if (singleWords.contains(nowChar)) {    //需要把前面读入的全部存进去, 同时把自己读进去
                this.addWord(originTokens);
                this.addWord(originTokens, nowChar);
            } else if (Character.isLetterOrDigit(nowChar) || nowChar == '_') {
                this.word += nowChar;
            } else if (Character.isSpaceChar(nowChar) || nowChar == '\t') {
                this.addWord(originTokens);
            } else if (nowChar == '|' || nowChar == '&') {
                this.addWord(originTokens);
                if (i + 1 < line.length() && line.charAt(i + 1) == nowChar) {
                    this.addWord(originTokens, nowChar + "" + nowChar);
                    i = i + 1;
                } else { //抛出错误a
                    this.addWord(originTokens, nowChar + "" + nowChar);
                    this.throwError('a', lineNum);
                }
            } else if (nowChar == '/') {
                this.addWord(originTokens);
                if (i + 1 < line.length() && line.charAt(i + 1) == '/') {
                    break;      //是 // 类单行注释
                } else if (i + 1 < line.length() && line.charAt(i + 1) == '*') {
                    this.isComment = true;      //是/**/类注释
                    i++;
                } else {
                    this.addWord(originTokens, nowChar);    // 可能是一个除号
                }
            } else if (nowChar == '\"') {   //字符串,但必然同行
                this.addWord(originTokens);
                word += line.charAt(i++);
                while (i < line.length() && line.charAt(i) != '\"') {
                    if (line.charAt(i) == '\\' && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                        // 解决"\""的问题
                        word += "\\\"";
                        i += 2;
                    } else {
                        word += line.charAt(i++);
                    }
                }
                word += line.charAt(i);
                this.addWord(originTokens);

            } else if (nowChar == '\'') {   //char const
                this.addWord(originTokens);
                word += line.charAt(i++);
                if (line.charAt(i) == '\\') {
                    word += line.charAt(i++);
                }
                word += line.charAt(i++);
                word += line.charAt(i);
                this.addWord(originTokens);
            }
        }
        this.addWord(originTokens);
        return originTokens;
    }

    private void addWord(ArrayList<String> words) {
        if (!word.isEmpty()) {
            words.add(word);
            word = "";
        }
    }

    private void addWord(ArrayList<String> words, char ch) {
        String temp = String.valueOf(ch);
        if (!temp.isEmpty()) {
            words.add(temp);
        }
    }

    private void addWord(ArrayList<String> words, String str) {
        if (!str.isEmpty()) {
            words.add(str);
        }
    }

    @Override
    public void throwError(char type, int lineNum) {
        ErrorToken error = new ErrorToken('a', lineNum);
        Settings settings = Settings.getInstance();
        settings.addErrorToken(error);
    }
}
