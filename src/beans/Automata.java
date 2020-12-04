/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.ArrayList;

/**
 *
 * @author Ndadji
 */

public class Automata<Q, S> {
    private ArrayList<Q> states;
    private Q initialState;
    private ArrayList<Q> finalStates;
    private S[] symbols;
    private TransitionTable<Q, S> transitionTable;

    public Automata(S[] symbols) {
        this.symbols = symbols;
        states = new ArrayList<Q>();
        finalStates = new ArrayList<Q>();
        transitionTable = new TransitionTable<Q, S>(symbols);
    }
    
    /**
     * méthode permettant d'ajouter un état sur un automate
     * @param state 
     */
    public void addState(Q state){
        if(!states.contains(initialState)){
            states.add(state);
            if (initialState == null) {
                    initialState = state;
            }
            transitionTable.addEntry(state);
        }
    }
    
    /**
     * cette méthode va nous permettre d'ajouter une entrée dans la table de transition
     * @param state
     * @param symbol
     * @param nextState
     * @throws Exception 
     */
    public void addtransition(Q state, S symbol, Q nextState) throws Exception{
        transitionTable.put(state, symbol, nextState);
    }

    /**
     * cette methode nous retourne l'etat initial d'un automate
     * @return 
     */
    public Q getInitialState() {
        return initialState;
    }

    /**
     * cette methode nous permet de modifier l'etat initial d'un automate
     * @return 
     */
    
    public void setInitialState(Q initialState) throws Exception{
        if(states.contains(initialState))
            this.initialState = initialState;
        else
            throw new Exception(initialState.toString()+" n'est pas reconnu comme état de cet automate");
    }
    
    /**
     * Cette methode permet de transformer la table de transition en un tableau
     * @return 
     */
    public Object[][] getTransitionTableAsArray(){
        Object[][] array = new Object[states.size()][symbols.length];
        for(int i = 0; i < states.size(); i++){
            for(int j = 0; j < symbols.length; j++){
                try {
                    array[i][j] = transitionTable.get(states.get(i), symbols[j]);
                } catch (Exception ex) {

                }
            }
        }
        return array;
    }

    /**
     * methode permettant de recuperer tous les etats finaux d'un automate
     * @return 
     */
    public ArrayList<Q> getFinalStates() {
        return finalStates;
    }
    
    /**
     * methode permettant d'ajouter un etat final a un automate
     * @param state
     * @throws Exception 
     */
    public void addFinalState(Q state) throws Exception{
        if(states.contains(initialState))
            finalStates.add(state);
        else
            throw new Exception(initialState.toString()+" n'est pas reconnu comme état de cet automate");
    }
    
    /**
     * methode permettant de retirer un etat final a un automate
     * @param state
     * @throws Exception 
     */
    public void removeFinalState(Q state){
        finalStates.remove(state);
    }
    
     /**
     * methode permettant de retirer tous les etats finaux a un automate
     * @param state
     * @throws Exception 
     */
    public void removeAllFinalStates(){
        finalStates = new ArrayList<Q>();
    }

    /**
     * methode permettant de recuperer tous les etats d'un automate
     * @param state
     * @throws Exception 
     */
    public ArrayList<Q> getStates() {
        return states;
    }

    /**
     * methode nous permettant de recuperer la table de transition pour un automate donne 
     * @return 
     */
    public TransitionTable<Q, S> getTransitionTable() {
        return transitionTable;
    }

    public S[] getSymbols() {
        return symbols;
    }
}
