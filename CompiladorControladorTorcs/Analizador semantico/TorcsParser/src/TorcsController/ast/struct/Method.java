//------------------------------------------------------------------//
//                        COPYRIGHT NOTICE                          //
//------------------------------------------------------------------//
// Copyright (c) 2008, Francisco Jos� Moreno Velo                   //
// All rights reserved.                                             //
//                                                                  //
// Redistribution and use in source and binary forms, with or       //
// without modification, are permitted provided that the following  //
// conditions are met:                                              //
//                                                                  //
// * Redistributions of source code must retain the above copyright //
//   notice, this list of conditions and the following disclaimer.  // 
//                                                                  //
// * Redistributions in binary form must reproduce the above        // 
//   copyright notice, this list of conditions and the following    // 
//   disclaimer in the documentation and/or other materials         // 
//   provided with the distribution.                                //
//                                                                  //
// * Neither the name of the University of Huelva nor the names of  //
//   its contributors may be used to endorse or promote products    //
//   derived from this software without specific prior written      // 
//   permission.                                                    //
//                                                                  //
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND           // 
// CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,      // 
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF         // 
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE         // 
// DISCLAIMED. IN NO EVENT SHALL THE COPRIGHT OWNER OR CONTRIBUTORS //
// BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,         // 
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED  //
// TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,    //
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND   // 
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT          //
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING   //
// IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF   //
// THE POSSIBILITY OF SUCH DAMAGE.                                  //
//------------------------------------------------------------------//

//------------------------------------------------------------------//
//                      Universidad de Huelva                       //
//          Departamento de Tecnolog�as de la Informaci�n           //
//   �rea de Ciencias de la Computaci�n e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//          Compilador del lenguaje Tinto [Versi�n 0.1]             //
//                                                                  //
//------------------------------------------------------------------//

package TorcsController.ast.struct;

import java.util.ArrayList;

import TorcsController.ast.Type;

/**
 * Clase que almacena la informaci�n sobre un m�todo
 * 
 * @author Francisco Jos� Moreno Velo
 */
public class Method  {

	//----------------------------------------------------------------//
	//                        Miembros privados                       //
	//----------------------------------------------------------------//

	
	/**
	 * Identificador del m�todo
	 */
	private String name;
	
	/**
	 * Tipo de dato que devuelve el m�todo
	 */
	private int type;
	
	/**
	 * Lista de argumentos del m�todo
	 */
	private ArrayList<Variable> argument;
	
	/**
	 * Lista de variables locales definidas en el m�todo
	 */
	private ArrayList<Variable> localVar;
	
	/**
	 * Tabla de s�mbolos
	 */
	private TableOfSymbols tos;
	
	//----------------------------------------------------------------//
	//                            Constructores                       //
	//----------------------------------------------------------------//

	/**
	 * Constructor
	 * 
	 * @param name Nombre del m�todo o constructor
	 * @param classname Nombre de la clase a la que pertenece
	 */
	public Method(int type, String name) {
		this.type = type;
		this.name = name.toString();
		this.argument = new ArrayList<Variable>();
		this.localVar = new ArrayList<Variable>();
		this.tos = new TableOfSymbols();
		this.tos.createContext();
		
		// A�ade las variables de 
	}
	
	//----------------------------------------------------------------//
	//                          M�todos p�blicos                      //
	//----------------------------------------------------------------//

	/**
	 * Obtiene el nombre del m�todo
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Obtiene el tipo de dato que devuelve el m�todo
	 * @return
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * A�ade un argumento al m�todo
	 * @param var
	 */
	public void addArgument(Variable var) {
		argument.add(var);
		tos.addVariable(var);
	}
	
	/**
	 * Obtiene la lista de argumentos del m�todo
	 * @return
	 */
	public ArrayList<Variable> getArguments() {
		return this.argument;
	}
	
	/**
	 * Asigna la lista de argumentos completa
	 * @param vector
	 */
	public void setArguments(ArrayList<Variable> vector) {
		tos.deleteContext();
		tos.createContext();
		
		int size = vector.size();
		this.argument = new ArrayList<Variable>();
		
		for(int i=0; i<size; i++) {
			argument.add(vector.get(i));
			tos.addVariable(vector.get(i));
		}
	}
	
	/**
	 * Asigna la lista de argumentos (s�lo los tipos)
	 * @param vector
	 */
	public void setArgumentTypes(int[] argTypes) {
		tos.deleteContext();
		tos.createContext();
		
		int size = argTypes.length;
		this.argument = new ArrayList<Variable>();
		
		for(int i=0; i<size; i++) {
			argument.add( new Variable(argTypes[i],"arg"+i));
			tos.addVariable(argument.get(i));
		}
	}
	
	/**
	 * Compara los tipos de datos con los del m�todo,
	 * devolviendo true si los datos corresponden al m�todo
	 * 
	 * @param name
	 * @param arg
	 * @return 
	 */
	public boolean match(String name, int[] argTypes) {
		if(argTypes.length != argument.size()) return false;
		if(!this.name.equals(name)) return false;
		for(int i=0; i<argument.size(); i++) {
			if(argument.get(i).getType() != argTypes[i]) return false;
		}
		return true;
	}
	
	/**
	 * Si el metodo tiene argumentos doubles se le puede pasar int
	 * @param name
	 * @param argTypes
	 * @return
	 */
	public boolean matchInteger(String name, int[] argTypes) {
		if(argTypes.length != argument.size()) return false;
		if(!this.name.equals(name)) return false;
		for(int i=0; i<argument.size(); i++) {
			if(argument.get(i).getType() != argTypes[i])
				{
					if(argument.get(i).getType()==Type.DOUBLE_TYPE)
						{
							if(argTypes[i]!=Type.INT_TYPE && argTypes[i]!=Type.DOUBLE_TYPE )return false;
						}
				}
		}
		return true;
	}

	/**
	 * A�ade una declaraci�n de variable local al m�todo
	 * @param var
	 */
	public void addLocalVariable(Variable var) {
		localVar.add(var);
		tos.addVariable(var);
	}
	
	/**
	 * Obtiene la lista de variables locales
	 * @return
	 */
	public ArrayList<Variable> getLocalVariables() {
		return this.localVar;
	}
	
	/**
	 * Crea un nuevo �mbito de declaraci�n de variables locales
	 */
	public void createContext() {
		this.tos.createContext();
	}
	
	/**
	 * Elimina un �mbito de declaraci�n de variables locales
	 *
	 */
	public void deleteContext() {
		this.tos.deleteContext();
	}
	
	
	/**
	 * Obtiene una variable del m�todo a partir de su nombre
	 * @param varname Nombre de la variable
	 * @return
	 */
	public Variable getVariable(String varname) {
		return tos.getVariable(varname);
	}
	
	/**
	 * Verifica si en el �ltimo �mbito se ha definido una variable
	 * con un cierto nombre.
	 * @param varname Nombre de la variable
	 * @return true si est� declarada en el �mbito activo.
	 */
	public boolean existsInContext(String varname) {
		return (tos.getVariableInContext(varname) != null);
	}
	
}
