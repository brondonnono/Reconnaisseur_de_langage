/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package observer;

import beans.Automata;
import java.util.ArrayList;


/**
 *
 * @author Ndadji
 */
public interface Observer {
    public void updateSuccess(String message);
    public void updateError(String error);
    public void updateAutomata(Automata automata);
    public void updateDeterminisedAutomata(Automata automata);
    public void updateMinimizedAutomata(Automata automata);
    public void updateTestResult(String mot, boolean result, ArrayList<Object> trace);
}
