/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ndadji
 */
public class TransitionTable<Q, S> extends HashMap<Q, HashMap<S, ArrayList<Q>>> {

    private S[] symbols;
    
    public TransitionTable(S[] symbols) {
        this.symbols = symbols;
    }
    
    public TransitionTable(ArrayList<Q> states, S[] symbols) {
        this.symbols = symbols;
        init(states);
    }

    private void init(ArrayList<Q> states) {
        for(Q state : states)
            addEntry(state);
    }
    
    public void addEntry(Q state){
        HashMap<S, ArrayList<Q>> mp = new HashMap<S, ArrayList<Q>>();
        for(S symb : symbols)
            mp.put(symb, new ArrayList<Q>());
        this.put(state, mp);
    }
    
    public void put(Q state, S symbol, Q nextState) throws Exception{
        this.get(state, symbol).add(nextState);
    }
    
    public ArrayList<Q> get(Q state, S symbol) throws Exception{
        return this.get(state).get(symbol);
    }
}
