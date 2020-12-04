/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package observer.adapter;

import beans.Automata;
import java.util.ArrayList;
import java.util.List;
import observer.Observable;
import observer.Observer;

/**
 *
 * @author Ndadji
 */
public abstract class ObservableAdapter implements Observable {

    protected List<Observer> observerList;
    
    public ObservableAdapter(){
        this.observerList = new ArrayList<Observer>();
    }
    
    @Override
    public void addObserver(Observer observer){
        this.observerList.add(observer);
    }
    
    @Override
    public void notifySuccess(String message){
        for(Observer observer : observerList){
            observer.updateSuccess(message);
        }
    }
    
    @Override
    public void notifyError(String error){
        for(Observer observer : observerList){
            observer.updateError(error);
        }
    }

    @Override
    public void notifyAutomata(Automata automata) {
        for(Observer observer : observerList){
            observer.updateAutomata(automata);
        }
    }

    @Override
    public void notifyDeterminisedAutomata(Automata automata) {
        for(Observer observer : observerList){
            observer.updateDeterminisedAutomata(automata);
        }
    }

    @Override
    public void notifyMinimizedAutomata(Automata automata) {
        for(Observer observer : observerList){
            observer.updateMinimizedAutomata(automata);
        }
    }

    @Override
    public void notifyTestResult(String mot, boolean result, ArrayList<Object> trace) {
        for(Observer observer : observerList){
            observer.updateTestResult(mot, result, trace);
        }
    }
}
