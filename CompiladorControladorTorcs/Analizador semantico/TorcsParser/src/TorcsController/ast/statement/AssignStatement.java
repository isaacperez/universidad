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

import TorcsController.ast.Type;
import TorcsController.ast.expression.BinaryExpression;
import TorcsController.ast.expression.BooleanLiteralExpression;
import TorcsController.ast.expression.CallExpression;
import TorcsController.ast.expression.DoubleLiteralExpression;
import TorcsController.ast.expression.Expression;
import TorcsController.ast.expression.IntegerLiteralExpression;
import TorcsController.ast.expression.PerceptionReferenceExpression;
import TorcsController.ast.expression.UnaryExpression;
import TorcsController.ast.expression.VariableExpression;
import TorcsController.ast.struct.Variable;

/**
 * Clase que describe una instrucci�n de asignaci�n.
 * 
 * @author Francisco Jos� Moreno Velo
 */
public class AssignStatement extends Statement {

	//----------------------------------------------------------------//
	//                        Miembros privados                       //
	//----------------------------------------------------------------//

	/**
	 * Parte izquierda de una asignaci�n
	 */
	private Variable lefthand;
	
	/**
	 * Expresi�n de la asignaci�n
	 */
	private Expression expression;
	
	private boolean declaracion;

	//----------------------------------------------------------------//
	//                            Constructores                       //
	//----------------------------------------------------------------//

	/**
	 * Constructor
	 * @param b 
	 * @param type
	 */
	public AssignStatement(Variable lefthand, Expression exp, boolean b){
		this.lefthand = lefthand;
		this.expression = exp;
		this.declaracion = b;
	}
	
	//----------------------------------------------------------------//
	//                          M�todos p�blicos                      //
	//----------------------------------------------------------------//

	/**
	 * Obtiene la parte izquierda de la asignaci�n.
	 * @return
	 */
	public Variable getLeftHand() {
		return this.lefthand;
	}
	
	/**
	 * Obtiene la expresi�n de la instrucci�n de asignaci�n
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
		return false;
	}

	public String toJava() {
		
		String s="";
		if(declaracion)
		{
			if(lefthand.getType() == Type.BOOLEAN_TYPE)
			{
				s = "boolean ";
			}
			else if (lefthand.getType() == Type.DOUBLE_TYPE)
			{
				s = "double ";
			}
			else if (lefthand.getType() == Type.INT_TYPE)
			{
				s = "int ";
			}	
		}
		
		if(expression == null)
		{
			return s+getNombreBueno()+";";
		}
		else if(expression.getClass().equals(BinaryExpression.class) )
		{
			return s + getNombreBueno()+" = "+ ((BinaryExpression)expression).toJava()+";";
		}
		else if(expression.getClass().equals(BooleanLiteralExpression.class) )
		{
			return s + getNombreBueno()+" = "+((BooleanLiteralExpression)expression).toJava()+";";
		}
		else if(expression.getClass().equals(CallExpression.class) )
		{
			return s + getNombreBueno()+" = "+((CallExpression)expression).toJava()+";";
		}
		else if(expression.getClass().equals(DoubleLiteralExpression.class) )
		{
			return s + getNombreBueno()+" = "+((DoubleLiteralExpression)expression).toJava()+";";
		}
		else if(expression.getClass().equals(IntegerLiteralExpression.class) )
		{
			return s + getNombreBueno()+" = "+((IntegerLiteralExpression)expression).toJava()+";";
		}
		else if(expression.getClass().equals(PerceptionReferenceExpression.class) )
		{
			return s + getNombreBueno()+" = "+((PerceptionReferenceExpression)expression).toJava()+";";
		}
		else if(expression.getClass().equals(UnaryExpression.class) )
		{
			return s + getNombreBueno()+" = "+((UnaryExpression)expression).toJava()+";";
		}
		else if(expression.getClass().equals(VariableExpression.class) )
		{
			return s + getNombreBueno()+" = "+((VariableExpression)expression).toJava()+";";
		}
		else return "Expression no reconocida";
		
	}
	
	public String getNombreBueno() {
		Variable var = lefthand;
		if(var.getName().equals("accelerate")) return "action.accelerate";
		else if(var.getName().equals("brake")) return "action.brake";
		else if(var.getName().equals("steering")) return "action.steering";
		else if(var.getName().equals("gear")) return "action.gear";
		else return var.getName();
	}
	
}
