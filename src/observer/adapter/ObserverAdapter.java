/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package observer.adapter;

import beans.Automata;
import java.util.ArrayList;
import observer.Observer;

/**
 *
 * @author Ndadji
 */
public abstract class ObserverAdapter implements Observer {
    
    @Override
    public void updateSuccess(String message){
        
    }
    
    @Override
    public void updateError(String error){
        
    }

    @Override
    public void updateAutomata(Automata automata) {
        
    }

    @Override
    public void updateDeterminisedAutomata(Automata automata) {
        
    }

    @Override
    public void updateMinimizedAutomata(Automata automata) {
        
    }

    @Override
    public void updateTestResult(String mot, boolean result, ArrayList<Object> trace) {
        
    }
}
