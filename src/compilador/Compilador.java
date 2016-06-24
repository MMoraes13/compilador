/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.IOException;
import AnalisadorLexico.Lexer;
import AnalisadorSintatico.Synctatic;
import commons.Tag;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
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
        Synctatic syn = new Synctatic();
        ArrayList <Word> wordsWaitingForType = new ArrayList <> ();
        try {
            
            Env env = new Env (null);
            lexer = new Lexer (args[0]);
            for (int i = 0; i < lexer.getTamanho(); i++) {
                retorno = lexer.scan(); 
                
                if (!retorno.getTag().equals(Lexer.SVAZIO)) {
                    syn.addToken(retorno);
                }
              
                if (retorno.getTag().equals(Lexer.SVAZIO)) { // Arquivo vindo com caracteres "invisiveis"
                    break;
                }            
                if (retorno.getTag().equals(Tag.IDENTIFIER)) {           
                    wordsWaitingForType.add ((Word) retorno);
                    //env.put(retorno, new Id(((Word)retorno).getLexeme(), (retorno).getTipo(), 0)); //Insere na TS a nova variável
                   
                } else if (retorno.getClass().equals(Word.class)) {// Verifica se eh uma palavra reservada                    
                    if (env.get(retorno) == null) {
                       if (((Word) retorno).getLexeme().equals("int")) {
                           for (Iterator<Word> it = wordsWaitingForType.iterator(); it.hasNext();) {
                                Word ws = it.next();
//                                System.out.println (env.get(ws).getNome());
                               // wordsWaitingForType.get(wordsWaitingForType.indexOf(ws)).setTipo(((Word) retorno).getLexeme());                  
                                ws.setTipo(((Word) retorno).getLexeme());
                                env.put((Token) ws, new Id((ws).getLexeme(), ws.getTipo(), 0));
                           }                           
                           wordsWaitingForType = new ArrayList<> ();
                       }
                       
                       if (((Word) retorno).getLexeme().equals("string")) {
                           for (Iterator<Word> it = wordsWaitingForType.iterator(); it.hasNext();) {
                                Word ws = it.next();
//                                System.out.println (env.get(ws).getNome());
                               // wordsWaitingForType.get(wordsWaitingForType.indexOf(ws)).setTipo(((Word) retorno).getLexeme());                  
                                ws.setTipo(((Word) retorno).getLexeme());
                                env.put((Token) ws, new Id((ws).getLexeme(), ws.getTipo(), 0));
                           } 
                            wordsWaitingForType = new ArrayList<> ();
                       }                       
                       Id aux = new Id(((Word) retorno).getLexeme(), retorno.getTag(), 0);
                       env.put(retorno, aux); // Insere na TS a palavra reservada
                    }
                }   

                //System.out.println (retorno);
     

                for (String erro : syn.getSemanticErrors()) {
                    System.out.println(erro);
                }                
            }    
            syn.run(env);

            for (String erro : syn.getSyntacticErrors()) {
                System.out.println(erro);
            }
            for (String erro : syn.getSemanticErrors()) {
                System.out.println(erro);
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
