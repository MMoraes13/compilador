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
public class Integer_const extends Token {

    public final int value;

    public Integer_const(int value) {
        super(Tag.INTEGER_CONST);
        this.value = value;
    }
    
    public Integer_const(int value, int line) {
        super(Tag.INTEGER_CONST, line);
        this.value = value;
    }

    @Override
    public String toString() {
        return "<(" + Tag.INTEGER_CONST + "),(" + value + ")>";
    }
}
