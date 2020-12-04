/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Analyser;
import models.MainModel;
import java.util.Stack;

/**
 * Cette classe va nous aider a controller toutes entrees de l'utilisateur comme le parenthesage, 
 * la compatibilite des caracteres entrees par l'utilisateur avec l'alphabet
 * @author  Brell
 */
public class MainController {
    private MainModel model;

    public MainController(MainModel model) {
        this.model = model;
    }

    public MainController() {
        model = new MainModel();
    }

    public MainModel getModel() {
        return model;
    }
    
    public String getRegEx(){
        return model.getRegEx();
    }
    
    /**
     * Méthode vérifiant si l'expréssion régulière entrée par l'utilisateur respecte l'alphabet
     * @param regEx
     * @return
     * @throws RuntimeException 
     */
    public boolean isRegexCompatibleToAlphabet (String regEx) throws RuntimeException {
        if (!regEx.trim().isEmpty()){
            for (int i = 0; i < regEx.length(); i++){
                char caracter = regEx.charAt(i);
                if (caracter == '.') {
                    continue;
                }
                if (caracter == '(' || caracter == ')' ){
                    continue;
                }
                if (caracter != '.' && caracter != '(' && caracter != ')') {
                    boolean test = false;
                    for (String st : Analyser.SYMBOLS){
                        if(st.equals(caracter+"")){
                            test = true;
                            break;
                        }
                    }
                    if(!test){
                        for (String operator : Analyser.OPERATORS) {
                            if (operator.equals(caracter+"")){
                                test = true;
                                break;  
                            }
                        }
                    }
                    if(!test)
                        throw new RuntimeException("Caractère '"+caracter+"' inconnu");
                }
            }
            return true;  
        }
        else 
            throw new RuntimeException("Entrez une Regex");
    }
    
    /**
     * Cette méthode vérifie si une expression régulière est bien formée, dépendant des spécifications
     * @param regEx
     * @throws RuntimeException
     * @throws Exception 
     */
    public void isRegexWellFormed(String regEx) throws RuntimeException, Exception {
      
        if (this.isRegexCompatibleToAlphabet(regEx)) {
            Stack<Character> pile = new Stack<Character>();
            for (String operator : Analyser.OPERATORS) {
                if (regEx.startsWith(operator)) {
                    throw new RuntimeException ("Opérateur \""+operator+"\" en début");
                }
            }
            if(regEx.endsWith("+"))
                throw new RuntimeException ("Chaine terminée par \"+\"");
            if(regEx.endsWith("("))
                throw new RuntimeException ("Chaine terminée par \"(\"");
            
            for (int i=0;i<regEx.length();i++) {
                char caracter =regEx.charAt(i);
                if (caracter == '(') {
                    pile.push(caracter);
                }

                if (caracter == ')' && pile.isEmpty()) {
                    throw new RuntimeException ("Mauvais parenthésage");
                }else{
                    if (caracter == ')' && !pile.isEmpty()) {
                        pile.pop();
                    }
                }
                if ((caracter == '+') && (regEx.charAt(i+1) == ')')) {
                    throw new RuntimeException ("Parenthèse ) après +");
                }
                if ((caracter == '+') && (regEx.charAt(i+1) == '*')) {
                    throw new RuntimeException ("Opérateur * après +");
                }
                if ((caracter == '*') && (regEx.charAt(i-1) == '*')) {
                    throw new RuntimeException ("Opérateur * après *");
                }
                if ((caracter == '(') && (regEx.charAt(i+1) == '*')) {
                    throw new RuntimeException ("Opérateur * après (");
                }
                if ((caracter == '(') && (regEx.charAt(i+1) == '+')) {
                    throw new RuntimeException ("Opérateur + après (");
                }
                if ((caracter == '(') && (regEx.charAt(i+1) == ')')) {
                    throw new RuntimeException ("Parenthèses inutiles");
                }
            }
            if (!pile.isEmpty())
                throw new RuntimeException("Mauvais parenthésage");
            model.compile(regEx);
        }
        
      }
    /**
     * modification de l'alphabet
     * @param text
     * @throws RuntimeException 
     */
    public void changeAlphabet(String text) throws RuntimeException{
        if(text != null && text.matches("^([a-zA-Z0-9_]-){0,}[a-zA-Z0-9_]$")){
            Analyser.setSymbols(text.split("-"));
        }else
            throw new RuntimeException("Mauvais alphabet");
    }

    /*public void check(String mot) {
        boolean trouve = false;
        for(int i = 0; i < mot.length(); i++){
            for(int j = 0; j < Analyser.SYMBOLS.length - 1; j++){
                if(Analyser.SYMBOLS[j].equals(""+mot.charAt(i))){
                    trouve = true;
                    break;
                }
            }
            if(!trouve)
                throw new RuntimeException("Caractère '"+mot.charAt(i)+"' inconnu");
            trouve = false;
        }
        model.check(mot);
    }*/
    public boolean isOK(char c) {
        boolean ok=false;
        for(int i=0; i<Analyser.SYMBOLS.length-1; i++){
            if(Analyser.SYMBOLS[i].equals(""+c)){//remettre ""+ avant mot.charAt
                    ok = true;
                    break;
                }
        }
        return ok;
    }
    public void check(String mot) {//, String exp
        
        boolean trouve = false;
        for(int i=0; i<mot.length(); i++){
            trouve=isOK(mot.charAt(i));
            
            if(!trouve)
                throw new RuntimeException("Caractère '"+mot.charAt(i)+"' inconnu");
            trouve = false;   
        }
        model.check(mot);
    }
     
}
