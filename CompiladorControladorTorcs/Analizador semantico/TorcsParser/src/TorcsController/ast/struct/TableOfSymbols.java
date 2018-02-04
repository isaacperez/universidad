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

import java.util.*;


/**
 * Clase que desarrolla la tabla de s�mbolos
 * 
 * @author Francisco Jos� Moreno Velo
 */
public class TableOfSymbols {

	//----------------------------------------------------------------//
	//                        Miembros privados                       //
	//----------------------------------------------------------------//
	
	/**
	 * Pila de tablas hash
	 */
	private Stack< Hashtable<String,Variable> > stack;
	
	//----------------------------------------------------------------//
	//                            Constructores                       //
	//----------------------------------------------------------------//

	/**
	 * Constructor
	 *
	 */
	public TableOfSymbols() {
		this.stack  = new Stack< Hashtable<String,Variable> >();
	}

	//----------------------------------------------------------------//
	//                          M�todos p�blicos                      //
	//----------------------------------------------------------------//

	/**
	 * Crea un nuevo �mbito de declaraciones
	 *
	 */
	public void createContext() {
		this.stack.push(new Hashtable<String,Variable>());
	}
	
	/**
	 * Elimina un �mbito de declaraciones
	 *
	 */
	public void deleteContext() {
		this.stack.pop();
	}
	
	/**
	 * A�ade una declaraci�n de variable
	 * @param var
	 */
	public void addVariable(Variable var) {
		this.stack.lastElement().put(var.getName(),var);
	}
	
	/**
	 * Obtiene una declaraci�n de variable a partir de su identificador
	 * @param name
	 * @return
	 */
	public Variable getVariable(String name) {
		int size = this.stack.size();
		for(int i=size-1; i>= 0; i--) {
			Hashtable<String,Variable> table = this.stack.elementAt(i);
			if(table.containsKey(name)) return (Variable) table.get(name);
		}
		return null;
	}
	
	/**
	 * Obtiene una declaraci�n de variable a partir de su identificador
	 * buscando s�lo en el �ltimo �mbito.
	 * @param name
	 * @return
	 */
	public Variable getVariableInContext(String name) {
		Hashtable<String,Variable> table = this.stack.lastElement();
		if(table.containsKey(name)) return (Variable) table.get(name);
		else return null;
	}
}
