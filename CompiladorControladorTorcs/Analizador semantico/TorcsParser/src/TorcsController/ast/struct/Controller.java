package TorcsController.ast.struct;

import java.util.ArrayList;

import TorcsController.ast.expression.CallParameters;

public class Controller {

	/**
	 * Lista de Inner
	 */
	private ArrayList<Inner> inner;
	
	/**
	 * Lista de Perception
	 */
	private ArrayList<Perception> perception;
	
	/**
	 * Lista de Action
	 */
	private ArrayList<Action> action;
	
	/**
	 * Bloque de rules
	 */
	private Rules rules;

	/**
	 * Constructor de una declaracion de controlador
	 */
	public Controller(){
		this.inner = new ArrayList<Inner>();
		this.perception = new ArrayList<Perception>();
		this.action = new ArrayList<Action>();
		this.rules = new Rules();
	}
	
	/**
	 * Añade la declaracion de una variable de estado interno
	 * @param inner
	 */
	public void addInner(Inner inner){
		this.inner.add(inner);
	}
	
	/**
	 * Obtiene la lista de Inner
	 * @return
	 */
	public ArrayList<Inner> getInner(){
		return this.inner;
	}
	
	/**
	 * Obtiene el inner con el nombre indicado
	 * @param name
	 * @return
	 */
	public Inner getInner(String name){
		int size = inner.size();
		
		for (int i = 0; i<size; i++){
			if(inner.get(i).getName().equals(name)) return inner.get(i);
		}
		
		return null;
	}
	
	/**
	 * Añade una percepcion
	 * @param perception
	 */
	public void addPerception(Perception perception){
		this.perception.add(perception);
	}
	
	/**
	 * Obtiene la lista de Perception
	 * @param call 
	 * @param string 
	 * @return
	 */

	
	/**
	 * Obtiene el Perception con el identificador indicado
	 */
	public Perception getPerception(String name, CallParameters call){
		int size = this.perception.size();
		
		for(int i = 0; i<size; i++){
			if(perception.get(i).getName().equals(name))
				{
					if(perception.get(i).match(name, call.getTypes())) return perception.get(i);
					else if(perception.get(i).matchInteger(name, call.getTypes() )) return perception.get(i);
				}
		}
		return null;
	}
	
	public ArrayList<Perception> getPerception()
	{
		return perception;
	}
	/**
	 * Añade un action
	 * @param action
	 */
	public void addAction(Action action){
		this.action.add(action);
	}
	
	/** 
	 * Obtiene la lista de Action
	 * @return
	 */
	public ArrayList<Action> getAction(){
		return this.action;
	}
	
	/**
	 * Obtiene el Action con el mismo identificador indicado por parametro
	 * @param name
	 * @return
	 */
	public Action getAction(String name, CallParameters call){
		int size = this.action.size();
		
		for(int i = 0; i<size; i++){
			if(action.get(i).getName().equals(name))
				{
				if(action.get(i).match(name, call.getTypes())) 
					return action.get(i);
				}
		}
		return null;
	}
	
	/**
	 * Poner rules
	 * @param rules
	 */
	public void addRules(Rules rules){
		this.rules = rules;
	}
	
	/**
	 * Obtiene el rules
	 * @return
	 */
	public Rules getRules(){
		return this.rules;
	}
	
	
	
	
}
