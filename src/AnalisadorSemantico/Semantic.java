/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalisadorSemantico;

import static compilador.Compilador.env;
import token.Integer_const;
import token.Literal;
import token.Token;
import token.Word;
import ts.Id;

/**
 *
 * @author MMoraes
 */
public class Semantic {
    
    public Semantic() {;
    }

    public Token addIdentifier(Token token, String tipo) {

        if (isUnique(token)) {
            env.put(token, new Id(((Word) token).getLexeme(), tipo, 0));
            return token;
        }
        return null;
    }

    public boolean isUnique(Token token) {
        //If getToken == null, return true; else return false
        return env.get(token) == null;
    }
    public boolean identifierExists (Token t) {
        return env.get(t) != null;
    }
    public String getIdentifierType (Token t){
        Id w = env.get(t);       
        return w.getTipo();
    }

    
    public boolean checkIntegerType (Token t){       
        return t instanceof Integer_const;    
    }

    public boolean checkLiteralType(Token token) {
        return token instanceof Literal;
    }

}
