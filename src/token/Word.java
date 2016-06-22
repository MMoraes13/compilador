/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package token;

import commons.Tag;

/**
 *
 * @author MMoraes
 */
public class Word extends Token {

    private String lexeme = "";

    public Word(String s, String tipo) {
        super(tipo);
        lexeme = s;
    }
    
    public Word(String s, String tipo, int line) {
        super(tipo, line);
        lexeme = s;
    }
    
    @Override
    public String toString() {
        return "<(" + super.tag + "),(" + lexeme + ")>";
    }

    /**
     * @return the lexeme
     */
    public String getLexeme() {
        return lexeme;
    }

    /**
     * @param lexeme the lexeme to set
     */
    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }
}
