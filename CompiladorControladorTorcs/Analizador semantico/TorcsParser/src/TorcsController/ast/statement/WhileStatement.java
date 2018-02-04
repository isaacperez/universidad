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

import java.util.ArrayList;

import TorcsController.ast.expression.BinaryExpression;
import TorcsController.ast.expression.BooleanLiteralExpression;
import TorcsController.ast.expression.CallExpression;
import TorcsController.ast.expression.DoubleLiteralExpression;
import TorcsController.ast.expression.Expression;
import TorcsController.ast.expression.IntegerLiteralExpression;
import TorcsController.ast.expression.PerceptionReferenceExpression;
import TorcsController.ast.expression.UnaryExpression;
import TorcsController.ast.expression.VariableExpression;

/**
 * Clase que describe la instrucci�n WHILE
 * 
 * @author Francisco Jos� Moreno Velo
 */
public class WhileStatement extends Statement {

	//----------------------------------------------------------------//
	//                        Miembros privados                       //
	//----------------------------------------------------------------//

	/**
	 * Condici�n de la instrucci�n WHILE
	 */
	private Expression condition;
	
	/**
	 * Instrucci�n a desarrollar mientras la condici�n se cumple
	 */
	private Statement instruction;
	
	//----------------------------------------------------------------//
	//                            Constructores                       //
	//----------------------------------------------------------------//

	/**
	 * Constructor
	 * @param type
	 */
	public WhileStatement(Expression exp, Statement inst){
		this.condition = exp;
		this.instruction = inst;
	}
	
	//----------------------------------------------------------------//
	//                          M�todos p�blicos                      //
	//----------------------------------------------------------------//

	/**
	 * Obtiene la condici�n de la instrucci�n
	 * @return
	 */
	public Expression getCondition() {
		return this.condition;
	}
	
	/**
	 * Obtiene la instrucci�n a realizar mientras la condici�n se cumple
	 * @return
	 */
	public Statement getInstruction() {
		return this.instruction;
	}
	
	/**
	 * Verifica si la instrucci�n alcanza siempre un "return".
	 * @return
	 */
	public boolean returns() {
		return false;
	}

	public ArrayList<String> toJava() {
		ArrayList<String> cadena = new ArrayList<String>();
		
		String cabecera="while(";
		if(condition.getClass().equals(BinaryExpression.class) )
		{
			cabecera = cabecera + ((BinaryExpression)condition).toJava();
		}
		else if(condition.getClass().equals(BooleanLiteralExpression.class) )
		{
			cabecera = cabecera +  ((BooleanLiteralExpression)condition).toJava();
		}
		else if(condition.getClass().equals(CallExpression.class) )
		{
			cabecera = cabecera + ((CallExpression)condition).toJava();
		}
		else if(condition.getClass().equals(DoubleLiteralExpression.class) )
		{
			cabecera = cabecera + ((DoubleLiteralExpression)condition).toJava();
		}
		else if(condition.getClass().equals(IntegerLiteralExpression.class) )
		{
			cabecera = cabecera + ((IntegerLiteralExpression)condition).toJava();
		}
		else if(condition.getClass().equals(PerceptionReferenceExpression.class) )
		{
			cabecera = cabecera + ((PerceptionReferenceExpression)condition).toJava();
		}
		else if(condition.getClass().equals(UnaryExpression.class) )
		{
			cabecera = cabecera + ((UnaryExpression)condition).toJava();
		}
		else if(condition.getClass().equals(VariableExpression.class) )
		{
			cabecera = cabecera + ((VariableExpression)condition).toJava();
		}
		
		
		if(instruction.getClass().equals(AssignStatement.class))
		{
			cadena.add(cabecera+")"+"\t"+ ((AssignStatement)instruction).toJava() );
		}
		else if(instruction.getClass().equals(BlockStatement.class))
		{
			cadena.add(cabecera+")"+"{");
			ArrayList<String> block =((BlockStatement)instruction).toJava();
			
			int sizeBlock = block.size();
			
			for(int b = 0; b<sizeBlock; b++)
			{
				cadena.add(block.get(b));
			}
			cadena.add("}");
		}
		else if(instruction.getClass().equals(IfStatement.class))
		{
			cadena.add(cabecera+") {");
			ArrayList<String> ifblock =((IfStatement)instruction).toJava();
			
			int sizeBlock = ifblock.size();
			
			for(int b = 0; b<sizeBlock; b++)
			{
				cadena.add("\t"+ifblock.get(b));
			}
			cadena.add("}");
			
		}
		else if(instruction.getClass().equals(ReturnStatement.class))
		{
			cadena.add(cabecera+")"+"\t"+ ((ReturnStatement)instruction).toJava() );
		}
		else if(instruction.getClass().equals(WhileStatement.class))
		{
			cadena.add(cabecera+") {");
			ArrayList<String> whileblock =((WhileStatement)instruction).toJava();
			
			int sizeBlock = whileblock.size();
			
			for(int b = 0; b<sizeBlock; b++)
			{
				cadena.add("\t"+whileblock.get(b));
			}
			cadena.add("}");
		}

		
		return cadena;
	}
}
