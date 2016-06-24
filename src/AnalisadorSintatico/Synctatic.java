/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalisadorSintatico;

import AnalisadorSemantico.Semantic;
import java.util.LinkedList;
import java.util.List;
import commons.Tag;
import java.util.ArrayList;
import token.Token;
import ts.Env;
import ts.Id;

/**
 *
 * @author Mateus
 */
public class Synctatic {
    //private Id //aux;
    private List<Token> tokens;
    private Token token;
    private int depth;
    private String tipo;
    private List<String> synctacticErrors;
    private List<String> semanticErrors;
    private ArrayList <Id> expression;
    private String lastType;
    private Id aux;
    private Semantic sem;
    private Env env;
    public Synctatic() {
        
        tokens = new LinkedList<>();
        synctacticErrors = new ArrayList<>();
        semanticErrors = new ArrayList<>();
        expression = new ArrayList <> ();
        lastType = "";
        aux = new Id("");
        sem = new Semantic();
    }

    public void run(Env env) {
        this.env = env;
        token = tokens.get(0);
        switch (token.getTag()) {
            case Tag.BEGIN:
                eat(Tag.BEGIN);
                stmtList();
                break;
            case Tag.VARIABLE: 
                eat (Tag.VARIABLE);
                declList();
               // eat (Tag.PONTOVIRGULA);
                eat (Tag.BEGIN);                
                stmtList();
                break;
            default:
                synctacticError("program", token.getLine());
        }
    }

    public void addToken(Token t) {
        this.tokens.add(t);
    }

    private void eat(String tag) {
        if (token.getTag().equals(tag)) {
            System.out.println(token.getTag() + " : " + token.toString() + "\tLinha: " + token.getLine());
            advance();

        } else {
            System.out.println("Erro! Token não encontrado: " + tag);
            System.out.println("Token encontrado: " + token.getTag());
        }
    }

    private void advance() {
        if (tokens.size() > 1) {
            tokens.remove(0);
            token = tokens.get(0);
        } else if (tokens.size() == 1) {
            tokens.remove(0);
        } else {
            synctacticError("no tokens left", token.getLine());
            //System.exit(0);
        }
    }

    private void type() {
        switch (token.getTag()) {
            case Tag.INT:
                tipo = Tag.INT;
                eat(Tag.INT);
                break;
            case Tag.STRING:
                tipo = Tag.STRING;
                eat(Tag.STRING);
                break;
            default:
                synctacticError("type - Tipo nao encontrado", token.getLine());
                break;
        }
    }

    private void synctacticError(String producao, int linha) {
        synctacticErrors.add("Erro na análise sintática, linha" + linha + ": " + producao);
    }

    private void semanticError(String producao, int linha, String tipoErro) {
        semanticErrors.add("Erro na análise semântica, linha" + linha + ": " + producao + "- "+tipoErro);
    }

    private void printMethod(String local) {
        for (int i = 0; i < depth - 1; i++) {
            System.out.print(" ");
        }
        System.out.println(local);
    }

    private void declList() {        
        decl();
        switch (token.getTag()) {
            case Tag.PONTOVIRGULA:
                eat(Tag.PONTOVIRGULA);
                declList();
                break;
            case Tag.BEGIN:
                break;
            default:
                synctacticError ("declList - Ponto e Virgula faltando", token.getLine() );
        }
    }

    private void stmtList() {
        stmt();
        switch (token.getTag()) {
            case Tag.PONTOVIRGULA:
                eat(Tag.PONTOVIRGULA);
                stmtList();
                break;
        }

        for (Id i : expression) {
            if (!expression.get(1).getTipo().equals (i.getTipo())){ 
                semanticError("tipos incompatíveis", 0, expression.get(1).getNome()+" e "+i.getNome());
                System.out.println ("****"+i.getNome()+" "+i.getTipo()+"***"+expression.get(1).getNome()+" e "+expression.get(0).getTipo());
            }    
        }
        expression = new ArrayList<>();        
    }

/*   private void body() {
        switch (token.getTag()) {
            case Tag.INT:
            case Tag.STRING:
                declList();
            case Tag.BEGIN:
                eat(Tag.BEGIN);
                stmtList();
                eat(Tag.STOP);
                break;
            default:
                synctacticError("body", token.getLine());
                break;
        }
    } */

    private void decl() {
        identList();
        if (token.getTag().equals(Tag.IS)) {
            eat (Tag.IS);
            type();
        }
    }

    private void stmt() {
        switch (token.getTag()) {
            case Tag.IDENTIFIER: //assign-stmt
                aux = env.get(token);
                if (aux != null) expression.add(aux);
                eat(Tag.IDENTIFIER);
                eat(Tag.ATRIBUICAO);
                simpleExpr();
                break;
            case Tag.IF: //if-stmt 
                eat(Tag.IF);
                eat (Tag.ABREPARENTESE);
                expression();
                eat (Tag.FECHAPARENTESE);
                stmtList();
                ifStmtB();
                break;
            case Tag.END:
                eat (Tag.END);
                //eat (Tag.FECHAPARENTESE);              
                break;

            case Tag.DO:
                eat(Tag.DO);
                stmtList(); 
                stmtSuffix();
                break;
            case Tag.IN: //read-stmt
                eat(Tag.IN);
                eat(Tag.ABREPARENTESE);
                eat(Tag.IDENTIFIER);
                eat(Tag.FECHAPARENTESE);
                break;
            case Tag.OUT: //write-stmt
                eat(Tag.OUT);
                eat(Tag.ABREPARENTESE);
                writable();
                eat(Tag.FECHAPARENTESE);
                break;
            case Tag.THEN:
                eat(Tag.THEN);
                break;
            default:
                synctacticError("stmt - comando nao encontrado: "+token.getTag().toString(), token.getLine());
        }
    }

    private void identList() {
       
        switch (token.getTag()) {            
            case Tag.IDENTIFIER:
                eat (Tag.IDENTIFIER);
                while (token.getTag().equals(Tag.VIRGULA)) {
                    eat(Tag.VIRGULA);
                    eat(Tag.IDENTIFIER);
                }
                break;

        }
    }

    private void constant() {
        
        switch (token.getTag()) {
            case Tag.INTEGER_CONST:    
                if (!sem.checkIntegerType(token) && !lastType.equals("int")) semanticError("Integer_Const", token.getLine(), token.getTipo());
                eat(Tag.INTEGER_CONST);
                break;
            case Tag.LITERAL:
                if(!sem.checkLiteralType(token)) semanticError("Literal", token.getLine(), token.getTipo());
                eat (Tag.LITERAL);
                break;
            default:
                synctacticError("constant", token.getLine());
                break;
        };
    }

    private void operator() {

        switch (token.getTag()) {
            case Tag.OP_AND:
                eat(Tag.OP_AND);
                break;
            case Tag.OP_COMPARA:
                eat(Tag.OP_COMPARA);
                break;
            case Tag.OP_DIVISAO:
                eat(Tag.OP_DIVISAO);
                break;
            case Tag.OP_GT:
                eat(Tag.OP_GT);
                break;
            case Tag.OP_GTE:
                eat(Tag.OP_GTE);
                break;
            case Tag.OP_LT:
                eat(Tag.OP_LT);
                break;
            case Tag.OP_LTE:
                eat(Tag.OP_LTE);
                break;
            case Tag.OP_MOD:
                eat(Tag.OP_MOD);
                break;
            case Tag.OP_MULTIPLICACAO:
                eat(Tag.OP_MULTIPLICACAO);
                break;
            case Tag.OP_NOTEQUAL:
                eat(Tag.OP_NOTEQUAL);
                break;
            case Tag.OP_OR:
                eat(Tag.OP_OR);
                break;
            case Tag.OP_SOMA:
                eat(Tag.OP_SOMA);
                break;
            case Tag.OP_SUBTRACAO:
                eat(Tag.OP_SUBTRACAO);
                break;
            default:
                synctacticError("Operator", token.getLine());
                break;
        }
    }

    private void simpleExpr() {
        switch (token.getTag()) {
            case Tag.LITERAL:
                eat(Tag.LITERAL);
                break;
            case Tag.LOGIC_AND:
                eat(Tag.LOGIC_AND);                
            case Tag.IDENTIFIER:
            case Tag.INTEGER_CONST:
            case Tag.FLOAT_CONST:
            case Tag.ABREPARENTESE:
            case Tag.OP_MULTIPLICACAO:
            case Tag.OP_DIVISAO:
            case Tag.OP_AND:
                term();
                simpleExpr();
                break;
            case Tag.EXCLAMACAO:
                eat(Tag.EXCLAMACAO);
                term();
                break;
            case Tag.TRACO:
                eat(Tag.TRACO);
                term();
                break;
            case Tag.OP_SOMA:
                eat(Tag.OP_SOMA);
                term();
                simpleExpr();
                break;
            case Tag.OP_SUBTRACAO:
                eat(Tag.OP_SUBTRACAO);
                term();
                simpleExpr();
                break;
            case Tag.ABRECHAVE:
                eat(Tag.ABRECHAVE);
                term();
                eat(Tag.FECHACHAVE);
                break;

        }
    }

    private void term() { 
           
        switch (token.getTag()) {
            case Tag.IDENTIFIER:                      
                aux = env.get(token);  
                if (aux != null) {
                    expression.add (aux);
                }  
                eat(Tag.IDENTIFIER);
                break;
            case Tag.LITERAL:
                eat (Tag.LITERAL);                
                break;
            case Tag.FLOAT_CONST:
            case Tag.INTEGER_CONST:
                constant();
                break;
            case Tag.ABREPARENTESE:
                eat(Tag.ABREPARENTESE);
                expression();
                eat(Tag.FECHAPARENTESE);
                break;
            case Tag.OP_MULTIPLICACAO:
                eat(Tag.OP_MULTIPLICACAO);
                term();
                term();
                break;
            case Tag.OP_DIVISAO:
                eat(Tag.OP_DIVISAO);
                term();
                term();
                break;
            case Tag.OP_AND:
                eat(Tag.OP_AND);
                term();
                term();
                break;
        }
    }

    private void expression() {
        simpleExpr();
        switch (token.getTag()) {
            case Tag.OP_COMPARA:
            case Tag.OP_GT:
            case Tag.OP_GTE:
            case Tag.OP_LT:
            case Tag.OP_LTE:
            case Tag.OP_NOTEQUAL:
                operator();
                expression();
                break;
        }

    }

    private void writable() {
        switch (token.getTag()) {

            case Tag.LITERAL:
            case Tag.IDENTIFIER:
            case Tag.INT_CONST:
            case Tag.FLOAT_CONST:
            
            case Tag.EXCLAMACAO:
            case Tag.TRACO:
            case Tag.OP_SOMA:
                simpleExpr();
                break;
        }
    }

    private void stmtSuffix() {
        eat(Tag.WHILE);
        expression(); //condition ::= expression
    }

    private void ifStmtB() {
        switch (token.getTag()) {
            case Tag.IDENTIFIER: 
                eat (Tag.IDENTIFIER);
                eat (Tag.ATRIBUICAO);
                simpleExpr();
                eat (Tag.PONTOVIRGULA);
                ifStmtB();
                break;
            case Tag.ELSE:
                eat(Tag.ELSE);
                stmtList();
                break;
            case Tag.END:
                eat (Tag.END);

        }
    }

    public List<String> getSyntacticErrors() {
        return synctacticErrors;
    }

    public void setSyntacticErrors(List<String> syntacticErrors) {
        this.synctacticErrors = syntacticErrors;
    }

    public List<String> getSemanticErrors() {
        return semanticErrors;
    }

    public void setSemanticErrors(List<String> semanticErrors) {
        this.semanticErrors = semanticErrors;
    }

}
