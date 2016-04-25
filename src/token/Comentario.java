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
public class Comentario extends Token {

    private String lexeme = "";

    public Comentario(String s, String tag) {
        super(tag);
        lexeme = s;
    }

    public Comentario(String s, String tag, int line) {
        super(tag, line);
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
