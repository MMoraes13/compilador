/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package token;

/**
 *
 * @author MMoraes
 */
public class Token {

    public final String tag; // constante que representa o token
    int line = 1;

    public Token(String t) {
        tag = t;
    }
    
    public Token(String t, int line){
        tag = t;
        this.line = line;
    }

    @Override
    public String toString() {
        return "<(" + tag + ")>";
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    public int getLine() {
        return line;
    }
    
    public void setLine(int line){
        this.line = line;
    }

}
