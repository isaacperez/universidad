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

package TorcsController.ast.statement;

import TorcsController.ast.expression.BooleanLiteralExpression;
import TorcsController.ast.expression.Expression;

/**
 * Clase que describe la instrucci�n RETURN
 *
 * @author Francisco Jos� Moreno Velo
 */
public class ReturnStatement extends Statement {

	//----------------------------------------------------------------//
	//                        Miembros privados                       //
	//----------------------------------------------------------------//

	/**
	 * Expresi�n de la instrucci�n RETURN
	 */
	private BooleanLiteralExpression expression;

	//----------------------------------------------------------------//
	//                            Constructores                       //
	//----------------------------------------------------------------//

	/**
	 * Constructor
	 * @param exp Expresi�n que calcula el valor a devolver
	 * @return 
	 */
	public ReturnStatement(BooleanLiteralExpression exp){
		this.expression = exp;
	}

	//----------------------------------------------------------------//
	//                          M�todos p�blicos                      //
	//----------------------------------------------------------------//

	/**
	 * Obtiene la expresi�n de la instrucci�n RETURN
	 * @return
	 */
	public Expression getExpression() {
		return this.expression;
	}

	/**
	 * Verifica si la instrucci�n alcanza siempre un "return".
	 * @return
	 */
	public boolean returns() {
		return true;
	}

	public String toJava() {
		// TODO Auto-generated method stub
		return "return "+expression.toJava()+";";
	}
}
