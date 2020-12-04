/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Cette classe nous permet d'effectuer tous traitements de notre application.
 * Elle va nous permettre de manipuler l'alphabet, et de déterminiser un automate dans les cas où cela 
 * est possible
 * @author  Brell
 */
public class Analyser {
    public static final String EPSILON = "£";
    public static String[] SYMBOLS = {"a", "b", "c", "d", "e", EPSILON};
    public static final String [] OPERATORS = {"+", "*"};

    private static ArrayList<Integer> unify(ArrayList<Integer> transition) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(Integer e : transition)
            if(!list.contains(e))
                list.add(e);
        return list;
    }
    
    public Analyser (){
        
    }
    /**
     * Méthode permettant d'obtenir tous les symboles de l'alphabet se trouvant 
     * dans de l'expression régulière entrée par l'utilisateur sur l'application
     * @param regEx
     * @return 
     */
    public static String[] getRegexSymbols(String regEx){
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < regEx.length(); i++){
            if(regEx.charAt(i) != '*' && regEx.charAt(i) != '+' && regEx.charAt(i) != ')' && regEx.charAt(i) != '(' && regEx.charAt(i) != '~' && regEx.charAt(i) != EPSILON.charAt(0))
                if(!list.contains(regEx.charAt(i)+""))
                    list.add(regEx.charAt(i)+"");
        }
        String[] res = new String[list.toArray().length];
        int i = 0;
        for(Object obj : list.toArray()){
            res[i] = (String)obj;
            i++;
        }
        return res;
    }
    
    /**
     * Méthode permettant de modifier tous les symboles de l'alphabet
     * @param symbols 
     */
    public static void setSymbols(String[] symbols){
        SYMBOLS = new String[symbols.length + 1];
        int i = 0;
        for(String c : symbols){
            SYMBOLS[i] = c;
            i++;
        }
        SYMBOLS[i] = EPSILON;
    }
    
    /**
     * transformation de l'expréssion régulière entrée par l'utilisateur en forme postfixée
     * @param regEx
     * @return 
     */
    public static String postfixForm(String regEx){
        String concatForm = "", postFix = "";
        for(int i = 0; i < regEx.length(); i++){
            if(regEx.charAt(i) == '(' || (regEx.charAt(i) != '*' && regEx.charAt(i) != '+' && regEx.charAt(i) != ')')){
                if(i - 1 >= 0){
                    if(regEx.charAt(i - 1) != '(' && regEx.charAt(i - 1) != '+')
                        concatForm += "~";
                }
            }
            concatForm += regEx.charAt(i);
        }
        Stack<Character> stack = new Stack<Character>();
        for(int i = 0; i < concatForm.length(); i++){
            if(concatForm.charAt(i) != '*' && concatForm.charAt(i) != '+' && concatForm.charAt(i) != ')' && concatForm.charAt(i) != '(' && concatForm.charAt(i) != '~')
                postFix += concatForm.charAt(i);
            else{
                if(concatForm.charAt(i) == '(')
                    stack.push(concatForm.charAt(i));
                else{
                    if(concatForm.charAt(i) == ')'){
                        while(stack.peek() != '(')
                            postFix += stack.pop();
                        stack.pop();
                    }
                    else{
                        while(!stack.empty() && (priority(stack.peek()) >= priority(concatForm.charAt(i))))
                            postFix += stack.pop();
                        stack.push(concatForm.charAt(i));
                    }
                }
            }
        }
        while(!stack.empty())
            postFix += stack.pop();
        return postFix;
    }
    
    /**
     * définition de la priorité des opérateurs
     * @param elt
     * @return 
     */
    public static int priority(char elt){
        switch(elt){
            case '*' : return 3;
            case '~' : return 2;
            case '+' : return 1;
            default : return 0;
        }
    }
    
    /**
     * Méthode permettant de vérifier si un automate est déterministe
     * @param automate
     * @return
     * @throws Exception 
     */
    public static boolean isAutomataDeterminised (Automata automate) throws Exception {
        for (Object state : automate.getStates()) {
            if(!automate.getTransitionTable().get(state, EPSILON).isEmpty())
                return false;
        }
        for (Object state : automate.getStates()) {
            for (int i = 0; i < SYMBOLS.length - 1; i++) {
                if(!(automate.getTransitionTable().get(state, SYMBOLS[i]).size() <= 1))
                    return false;
            }         
        }
        return true;
    }
    
   
    /**
     * Algorithme de déterminisation d'un automate de Thompson
     * @param automate
     * @return
     * @throws Exception 
     */
    public static Automata <Integer,String> determiniseThompson (Automata<Integer,String> automate) throws Exception {
        if(isAutomataDeterminised(automate))
            return automate;
        ArrayList<Integer> eFerm = getEpsFermeture(automate, automate.getInitialState());
        ArrayList<ArrayList<Integer>> dEtats = new ArrayList<ArrayList<Integer>>();
        Automata<Integer, String> automata = new Automata<Integer, String>(SYMBOLS);
        dEtats.add(eFerm);
        automata.addState(1);
        automata.setInitialState(1);
        int j = 1;
        for(int i = 0; i < SYMBOLS.length - 1; i++){
            ArrayList<Integer> transition = new ArrayList<Integer>();
            for(Integer e : eFerm)
                transition.addAll(automate.getTransitionTable().get(e, SYMBOLS[i]));
            transition = unify(transition);
            if(!transition.isEmpty() && !containsStates(dEtats, transition)){
                dEtats.add(transition);
                automata.addState(++j);
                automata.addtransition(1, SYMBOLS[i], j);
            }else{
                if(!transition.isEmpty() && containsStates(dEtats, transition))
                    automata.addtransition(1, SYMBOLS[i], indexOf(dEtats, transition) + 1);
            }
        }
        int i = 1;
        while(i < dEtats.size()){
            eFerm = getManyEpsFerm(automate, dEtats.get(i));
            for(int k = 0; k < SYMBOLS.length - 1; k++){
                ArrayList<Integer> transition = new ArrayList<Integer>();
                for(Integer e : eFerm)
                    transition.addAll(automate.getTransitionTable().get(e, SYMBOLS[k]));
                transition = unify(transition);
                if(!transition.isEmpty() && !containsStates(dEtats, transition)){
                    dEtats.add(transition);
                    automata.addState(++j);
                    automata.addtransition(i + 1, SYMBOLS[k], j);
                }else{
                    if(!transition.isEmpty() && containsStates(dEtats, transition))
                        automata.addtransition(i + 1, SYMBOLS[k], indexOf(dEtats, transition) + 1);
                }
            }
            i++;
        }
        for(ArrayList<Integer> list : dEtats){
            for(Integer e : list){
                if(automate.getFinalStates().contains(e)){
                    automata.addFinalState(dEtats.indexOf(list) + 1);
                    break;
                }
            }
        }
        return automata;
    }
    
    /**
     * Obtention de l'epsilone ferméture d'un etat sur un automate
     * @param automate
     * @param state
     * @return 
     */
    public static ArrayList<Integer> getEpsFermeture (Automata<Integer,String> automate, int state) {
        ArrayList<Integer> listToReturn = new ArrayList<Integer>();
        listToReturn.add(state);
        try {
            ArrayList<Integer> epsTrans = automate.getTransitionTable().get(state, EPSILON);
            if(epsTrans.size() > 0)
                listToReturn.addAll(epsTrans);
            listToReturn = unify(listToReturn);
            for(Integer e : listToReturn){
                if(e != state){
                    for(Integer i : getEpsFermeture(automate, e))
                        if(!listToReturn.contains(i))
                            listToReturn.add(i);
                }
            }
        }
        catch (Exception ex) {
            
        }
        return listToReturn;
    }
    
    /**
     * Obtention de l'epsilone fermeture d'un ensemble d'etats sur un automate
     * @param automate
     * @param states
     * @return 
     */
    public static ArrayList<Integer> getManyEpsFerm (Automata<Integer,String> automate, ArrayList<Integer> states ) {
        ArrayList<Integer> listToReturn = new ArrayList<Integer>();
        ArrayList<Integer> listEps;
        for (Integer state : states) {
             listEps = getEpsFermeture(automate, state);
             for (Integer eps : listEps) {
                 if(!listToReturn.contains(eps)) {
                     listToReturn.add(eps);
                 }
             }
        }
        return listToReturn;
    }
    
    /**
     * Méthode permettant de vérifier si Détat contient déjà un état
     * @param liste1
     * @param liste2
     * @return 
     */
    public static boolean containsStates (ArrayList<ArrayList<Integer>> liste1, ArrayList<Integer> liste2) {
       
        for (int i=0; i<liste1.size(); i++) {
            ArrayList<Integer> listToCompare = liste1.get(i);
            if ((listToCompare.size() == liste2.size()) && (listToCompare.containsAll(liste2))) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Méthode permettant de retourner la position d'un etat dans Détat
     * @param liste1
     * @param liste2
     * @return 
     */
    public static int indexOf(ArrayList<ArrayList<Integer>> liste1, ArrayList<Integer> liste2) {
       
        for (int i=0; i<liste1.size(); i++) {
            ArrayList<Integer> listToCompare = liste1.get(i);
            if ((listToCompare.size() == liste2.size()) && (listToCompare.containsAll(liste2))) {
                return i;
            }
        }
        
        return -1;
    }
    
    public static boolean check(Automata aut, String mot, ArrayList<Object> trace) throws Exception{
        Object state = aut.getInitialState();
        trace.add(state);
        if(!Analyser.isAutomataDeterminised(aut))
            return false;
        if(mot == null)
            return aut.getFinalStates().contains(aut.getInitialState());
        int i = 0;
        while(i < mot.length()){
            trace.add(mot.charAt(i)+"");
            ArrayList<Object> list = aut.getTransitionTable().get(state, mot.charAt(i)+"");
            if(list.isEmpty())
                return false;
            state = list.get(0);
            trace.add(state);
            i++;
        }
        return aut.getFinalStates().contains(state);
    }
    
    public static boolean check(Automata aut, String mot, Object initState, ArrayList<Object> trace) throws Exception{
        trace.add(initState);
        if(mot == null || "".equals(mot))
            return aut.getFinalStates().contains(initState);
        ArrayList<Object> list = aut.getTransitionTable().get(initState, mot.charAt(0)+"");
        if(list != null && !list.isEmpty()){
            for(Object iState : list){
                ArrayList<Object> tr = new ArrayList<Object>();
                if(check(aut, mot.substring(1), iState, tr)){
                    trace.add(mot.charAt(0)+"");
                    trace.addAll(tr);
                    return true;
                }
            }
        }
        list = aut.getTransitionTable().get(initState, Analyser.EPSILON);
        if(list != null && !list.isEmpty()){
            for(Object iState : list){
                ArrayList<Object> tr = new ArrayList<Object>();
                if(check(aut, mot, iState, tr)){
                    trace.add(Analyser.EPSILON);
                    trace.addAll(tr);
                    return true;
                }
            }
        }
        return false;
    }
}
