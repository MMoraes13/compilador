/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.IOException;
import AnalisadorLexico.Lexer;
//import syntactic.Synctatic;
import commons.Tag;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import semantic.Semantic;
import token.Token;
import token.Word;
import ts.Env;
import ts.Id;

/**
 *
 * @author Mateus
 */
public class Compilador {

    public static Env env;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Lexer lexer;
        Token retorno = null;
        
        try {
            
            Env env = new Env (null);
            lexer = new Lexer (args[0]);
            for (int i = 0; i < lexer.getTamanho(); i++) {
                retorno = lexer.scan();

                if (retorno.getTag().equals(Tag.IDENTIFIER)) {
                    env.put(retorno, new Id(((Word)retorno).getLexeme(), retorno.getTag(), 0)); //Insere na TS a nova variável
                   
                } else if (retorno.getClass().equals(Word.class)) {// Verifica se eh uma palavra reservada
                    if (env.get(retorno) == null) {
                        
                         env.put(retorno, new Id(((Word) retorno).getLexeme(), retorno.getTag(), 0)); // Insere na TS a palavra reservada
                    }
                }
                if (retorno.getTag().equals(Lexer.SVAZIO)) { // Arquivo vindo com caracteres "invisiveis"
                    break;
                }                
                System.out.println (retorno);
            }    
            System.out.println("---- LISTA DE TOKENS IDENTIFICADOS E TABELAS DE SIMBOLOS----");  
             env.imprimir();
               
                    
        }catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
