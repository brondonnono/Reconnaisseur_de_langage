/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.Stack;

/**
 *
 * @author Ndadji
 */
public class Thompson {
    
    /**
     * Construction de l'automate de thompson
     * @param regEx
     * @return 
     */
    public static Automata<Integer, String> constructAutomata(String regEx){
        Automata automata = null;
        String postfixEx = Analyser.postfixForm(regEx);
        if(postfixEx.length() == 1){
            automata = singleAutomata(postfixEx.charAt(0)+"");
        }else{
            int i = 0;
            Stack<Automata> stack = new Stack<Automata>();
            while(i < postfixEx.length()){
                if(postfixEx.charAt(i) == '~'){
                    Automata aut1 = stack.pop(), aut2 = stack.pop();
                    stack.push(concatenateAutomata(aut2, aut1));
                }
                if(postfixEx.charAt(i) == '+'){
                    Automata aut1 = stack.pop(), aut2 = stack.pop();
                    stack.push(orAutomata(aut2, aut1));
                }
                if(postfixEx.charAt(i) == '*'){
                    Automata aut = stack.pop();
                    stack.push(starAutomata(aut));
                }
                if(postfixEx.charAt(i) != '*' && postfixEx.charAt(i) != '~' && postfixEx.charAt(i) != '+')
                    stack.push(singleAutomata(postfixEx.charAt(i)+""));
                i++;
            }
            automata = stack.pop();
        }
        return automata;
    }
    
    /**
     * methode permettant de construire un automate avec un seul caractere comme expression reguliere
     * @param regEx
     * @return 
     */
    private static Automata<Integer, String> singleAutomata(String regEx){
        Automata<Integer, String> automata = new Automata<Integer, String>(Analyser.SYMBOLS);
        automata.addState(1);
        automata.addState(2);
        try{
            automata.setInitialState(1);
            automata.addFinalState(2);
            automata.addtransition(1, regEx, 2);
        }catch(Exception e){
            throw new RuntimeException("L'automate n'a pas pu être construit");
        }
        return automata;
    }
    
    /**
     * methode qui permet de construire l'automate a*
     * @param automata1
     * @return 
     */
    private static Automata<Integer, String> starAutomata(Automata<Integer, String> automata1){
        Automata automata = new Automata(Analyser.SYMBOLS);
        try{
            automata.addState(1);
            automata.setInitialState(1);
            for(Integer i : automata1.getStates())
                automata.addState(1 + i);
            int nbre = automata.getStates().size();
            automata.addState(nbre + 1);
            /*for(Integer i : automata1.getFinalStates())
                automata.addFinalState(1 + i);*/
            automata.addFinalState(nbre + 1);
            automata.addtransition(automata.getInitialState(), Analyser.EPSILON, automata1.getInitialState() + 1);
            automata.addtransition(automata.getInitialState(), Analyser.EPSILON, nbre + 1);
            for(Integer i : automata1.getFinalStates()){
                automata.addtransition(i + 1, Analyser.EPSILON, automata1.getInitialState() + 1);
                automata.addtransition(i + 1, Analyser.EPSILON, nbre + 1);
            }
            for(Integer i : automata1.getStates()){
                for(String symb : Analyser.SYMBOLS){
                    for(Integer e : automata1.getTransitionTable().get(i, symb))
                        automata.addtransition(i + 1, symb, e + 1);
                }
            }
        }catch(Exception e){
            throw new RuntimeException("L'automate n'a pas pu être construit");
        }
        return automata;
    }
    
    /**
     * automate reconnaissant la concatenation. exemple: a.b
     * @param automata1
     * @param automata2
     * @return 
     */
    private static Automata<Integer, String> concatenateAutomata(Automata<Integer, String> automata1, Automata<Integer, String> automata2){
        Automata automata = new Automata(Analyser.SYMBOLS);
        try{
            for(Integer i : automata1.getStates())
                automata.addState(i);
            int nbre = automata.getStates().size();
            for(Integer i : automata2.getStates())
                automata.addState(i + nbre);
            automata.setInitialState(automata1.getInitialState());
            for(Integer i : automata1.getFinalStates())
                automata.addtransition(i, Analyser.EPSILON, automata2.getInitialState() + nbre);
            for(Integer i : automata2.getFinalStates())
                automata.addFinalState(i + nbre);
            for(Integer i : automata1.getStates()){
                for(String symb : Analyser.SYMBOLS){
                    for(Integer e : automata1.getTransitionTable().get(i, symb))
                        automata.addtransition(i, symb, e);
                }
            }
            for(Integer i : automata2.getStates()){
                for(String symb : Analyser.SYMBOLS){
                    for(Integer e : automata2.getTransitionTable().get(i, symb))
                        automata.addtransition(i + nbre, symb, e + nbre);
                }
            }
        }catch(Exception e){
            throw new RuntimeException("L'automate n'a pas pu être construit");
        }
        return automata;
    }
    
    /**
     * automate reconnaissant a ou b.
     * @param automata1
     * @param automata2
     * @return 
     */
    private static Automata<Integer, String> orAutomata(Automata<Integer, String> automata1, Automata<Integer, String> automata2){
        Automata automata = new Automata(Analyser.SYMBOLS);
        try{
            automata.addState(1);
            automata.setInitialState(1);
            for(Integer i : automata1.getStates())
                automata.addState(1 + i);
            int nbre = automata.getStates().size();
            for(Integer i : automata2.getStates())
                automata.addState(nbre + i);
            automata.addtransition(automata.getInitialState(), Analyser.EPSILON, automata1.getInitialState() + 1);
            automata.addtransition(automata.getInitialState(), Analyser.EPSILON, automata2.getInitialState() + nbre);
            for(Integer i : automata1.getFinalStates())
                automata.addFinalState(1 + i);
            for(Integer i : automata2.getFinalStates())
                automata.addFinalState(nbre + i);
            for(Integer i : automata1.getStates()){
                for(String symb : Analyser.SYMBOLS){
                    for(Integer e : automata1.getTransitionTable().get(i, symb))
                        automata.addtransition(i + 1, symb, e + 1);
                }
            }
            for(Integer i : automata2.getStates()){
                for(String symb : Analyser.SYMBOLS){
                    for(Integer e : automata2.getTransitionTable().get(i, symb))
                        automata.addtransition(i + nbre, symb, e + nbre);
                }
            }
        }catch(Exception e){
            throw new RuntimeException("L'automate n'a pas pu être construit");
        }
        return automata;
    }
}
