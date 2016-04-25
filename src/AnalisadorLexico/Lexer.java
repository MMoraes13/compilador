/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalisadorLexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import commons.Tag;
import token.Comentario;
import token.Integer_const;
import token.Literal;
import token.Token;
import token.Word;

/**
 *
 * @author MMoraes
 */
public class Lexer {

    public static int line = 1; // contador de linhas

    private static final int VAZIO = 65535, TKDESC = 1,LITERALMALFORMADA = 2, CONSTANTEMAFORMADA = 3, IDENTIFICADORMALFORMADO = 4;
    public static final String SVAZIO = "65535";

    private char caracterAtual = ' '; // caractere lido do arquivo
    private String fileName;
    private FileReader file;
    private Hashtable<String, Word> words = new Hashtable<String, Word>();

    /*
     * Metodo para inserir palavras reservadas na HashTable
     */
    private void reserve(Word w) {
        words.put(w.getLexeme(), w); // lexema eh a chave para entrada na HashTable
    }

    /*
     * Metodo construtor
     */
    public Lexer(String fileName) throws FileNotFoundException {
        try {
            this.fileName = fileName;
            this.file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo nao encontrado");
            throw e;
        }

        // Insere palavras reservadas na HashTable
        reserve(new Word("program", Tag.PROGRAM));
        reserve(new Word("begin", Tag.BEGIN));
        reserve(new Word("end", Tag.END));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("string", Tag.STRING));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("read", Tag.READ));
        reserve(new Word("out", Tag.OUT));
        reserve(new Word("stop", Tag.STOP));
        reserve (new Word ("is", Tag.IS));
        reserve(new Word ("in", Tag.IN));
        reserve (new Word("var", Tag.VARIABLE));
    }

    /*
     * Le o proximo caractere do arquivo
     */
    private void readch() throws IOException {
        caracterAtual = (char) file.read();

    }

    /*
     * Le o proximo caractere do arquivo e verifica se e igual a c
     * @return boolean
     * @param char c
     */
    private boolean readch(char c) throws IOException {
        readch();
        if (caracterAtual != c) {
            return false;
        }
        caracterAtual = ' ';
        return true;
    }

    /*
     * Trata erro
     */
    private void erro(int tipo) {
        if (tipo == TKDESC) {
            System.out.println("\t ERRO (TOKEN DESCONHECIDO) - Linha:"+line);
        } 
        else if(tipo == LITERALMALFORMADA){
            System.out.println("\t ERRO (LITERAL MAL FORMADO) - Linha:"+line);
        }
        else if (tipo == CONSTANTEMAFORMADA){
            System.out.println("\t ERRO (CONSTANTE MÁ FORMADA) - Linha:"+line);
        }
        else if (tipo == IDENTIFICADORMALFORMADO) {
            System.out.println("\t ERRO (IDENTIFICADOR MAL FORMADO) - Linha:"+line);
        }
    }

    /*
     * Desconsidera delimitadores na entrada
     */
    private void desconsideraDelimitadores() throws IOException {

        for (;; readch()) {
            if (caracterAtual == ' ' || caracterAtual == '\t' || caracterAtual == '\r' || caracterAtual == '\b') {
                continue;
            } else if (caracterAtual == '\n') {
                line++;
            } else {
                break;
            }
        }
    }

    /*
     * Escaneia o codigo
     */
    public Token scan() throws IOException {
    
        desconsideraDelimitadores();
        
        // Operadores e pontuacao
        switch (caracterAtual) {
            case ';': {
                readch();
                return new Token(Tag.PONTOVIRGULA, line);
            }
            case ',': {
                readch();
                return new Token(Tag.VIRGULA, line);
            }
            case '(': {
                readch();
                return new Token(Tag.ABREPARENTESE, line);
            }
            case ')': {
                readch();
                return new Token(Tag.FECHAPARENTESE, line);
            }
            case '{': {
                StringBuilder sb = new StringBuilder();
                sb.append(caracterAtual);
                
                readch();
                
                if(Character.isDefined(caracterAtual)){
                    
                    while(Character.isDefined(caracterAtual) && caracterAtual != '}'){
                        sb.append(caracterAtual);
                        readch();
                    }
                    if(caracterAtual == '}'){
                        sb.append(caracterAtual);
                        readch();
                        return new Literal(sb.toString(), line);
                    }
                    else {
                        erro(LITERALMALFORMADA);
                        caracterAtual = ' ';
                        return new Token(sb.toString(), line);
                    }
                }
                else{
                    return new Token(Tag.ABRECHAVE, line);
                }
            }
            case '}': {
                readch();
                return new Token(Tag.FECHACHAVE, line);
            }
            case '"': {
                readch();
                return new Token(Tag.ABREASPAS, line);
            }
            case ':': {
                if (readch('=')) {
                    readch();
                    return new Token(Tag.ATRIBUICAO, line);
                }
                else{
                    erro(TKDESC);
                    return new Token(String.valueOf(':'), line);
                }
            }
            case '+': {
                readch();
                return new Token(Tag.OP_SOMA, line);
            }
            case '-': {
                readch();
                return new Token(Tag.OP_SUBTRACAO, line);
            }
            case '/': {
                    readch();
                    return new Token(Tag.OP_DIVISAO, line);
            }
            case '*': {
                readch();
                return new Token(Tag.OP_MULTIPLICACAO, line);
            }
            case '#': {
                readch();
                return new Token(Tag.OP_MOD, line);
            }
            case '=':{
                    readch();
                    return new Token(Tag.OP_COMPARA, line);
            }
            case '<':
                readch();
                if (caracterAtual == '=') {
                    return new Token(Tag.OP_LTE, line);
                } 
                else if (caracterAtual == '>'){
                    return new Token(Tag.OP_NOTEQUAL, line);
                }
                else {
                    return new Token(Tag.OP_LT, line);
                }
               

            case '>':
                readch();
                if (caracterAtual == '=') {
                    return new Token(Tag.OP_GTE, line);
                } else {
                    return new Token(Tag.OP_GT, line);
                }
            case '&': {
                    if(readch('&')){
                        return new Token(Tag.OP_AND, line);
                    }
                    else{
                        erro(TKDESC);
                        return new Token(String.valueOf('&'), line);
                    }
                }
            case '|': {
                    if(readch('|')){
                        return new Token(Tag.OP_OR, line);
                    }
                    else{
                        erro(TKDESC);
                        return new Token(String.valueOf('|'), line);
                    }
                }
            case '%': {
                    StringBuilder sb = new StringBuilder();
                    sb.append(caracterAtual);
                    
                    while (caracterAtual != '\n') {
                        readch();
                        sb.append(caracterAtual);
                    }
                    return new Comentario(sb.toString(), Tag.COMENTARIO, line);
                }
        }

        // Numeros
        if (Character.isDigit(caracterAtual)) {
            float value = 0;
            do {                
                value += Character.digit(caracterAtual, 10);
                readch();
            } while (Character.isDigit(caracterAtual));
            
            //if(!Character.isDigit(caracterAtual)) erro(CONSTANTEMAFORMADA);
            if (Character.isLetter(caracterAtual)) erro (IDENTIFICADORMALFORMADO);
            else return new Integer_const((int) value, line);
        }
        // Identificadores
        if (Character.isLetter(caracterAtual) || caracterAtual == '_') {
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(caracterAtual);
                readch();
            } while (Character.isLetterOrDigit(caracterAtual) || caracterAtual == '_');
            
            String s = sb.toString();
            Word w = words.get(s);

            if (w != null) {
                return new Word(w.getLexeme(), w.getTag(), line);
            }
            w = new Word(s, Tag.IDENTIFIER, line);
                
            if (!s.equals("_")) 
                words.put(s, w);                
            else erro (LITERALMALFORMADA);
            return w;
        }
        // Caracteres nao especificados

        if (caracterAtual == VAZIO) {
            Token t = new Token(String.valueOf((int) caracterAtual), line);
            caracterAtual = ' ';
            return t;
        } else {
            Token t = new Token(String.valueOf(caracterAtual), line);
            erro(TKDESC);
            caracterAtual = ' ';
            return t;
        }

    }

    /**
     * @return the file
     */
    public FileReader getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(FileReader file) {
        this.file = file;
    }

    /**
     * @return the words
     */
    public Hashtable<String, Word> getWords() {
        return words;
    }

    /**
     * @param words the words to set
     */
    public void setWords(Hashtable<String, Word> words) {
        this.words = words;
    }

    /**
     * @return the tamanho
     */
    public int getTamanho() {

        File arquivo = new File(fileName);

        int tamanho = 0;

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(arquivo));

            while (reader.read() != -1) {
                tamanho++;

            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tamanho;
    }

}
