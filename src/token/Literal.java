/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package token;

import commons.Tag;

/**
 *
 * @author mmoraes
 */
public class Literal extends Token{
    
    public final String value;

    public Literal(String value) {
        super(Tag.LITERAL);
        this.value = value;
    }
    
    public Literal(String value, int line) {
        super(Tag.LITERAL, line);
        this.value = value;
    }

    @Override
    public String toString() {
        return "<(" + Tag.LITERAL + "),(" + value + ")>";
    }
    
}
