//------------------------------------------------------------------//
//                        COPYRIGHT NOTICE                          //
//------------------------------------------------------------------//
// Copyright (c) 2008, Francisco José Moreno Velo                   //
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
//          Departamento de Tecnologías de la Información           //
//   Área de Ciencias de la Computación e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//          Compilador del lenguaje Tinto [Versión 0.1]             //
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
 * Clase que describe la instrucción IF
 * 
 * @author Francisco José Moreno Velo
 */
public class IfStatement extends Statement {

	//----------------------------------------------------------------//
	//                        Miembros privados                       //
	//----------------------------------------------------------------//

	/**
	 * Condición de la instrucción IF
	 */
	private Expression condition;
	
	/**
	 * Instrucción a desarrollar cuando la condición se cumple
	 */
	private Statement thenInst;
	
	/**
	 * Instrucción a desarrollar cuando la condición no se cumple
	 */
	private Statement elseInst;

	//----------------------------------------------------------------//
	//                            Constructores                       //
	//----------------------------------------------------------------//

	/**
	 * Constructor
	 * @param type
	 */
	public IfStatement(Expression exp, Statement thenInst, Statement elseInst){
		this.condition = exp;
		this.thenInst = thenInst;
		this.elseInst = elseInst;
	}

	//----------------------------------------------------------------//
	//                          Métodos públicos                      //
	//----------------------------------------------------------------//

	/**
	 * Obtiene la condición de la instrucción
	 * @return
	 */
	public Expression getCondition() {
		return this.condition;
	}
	
	/**
	 * Obtiene la instrucción a realizar cuando la condición se cumple
	 * @return
	 */
	public Statement getThenInstruction() {
		return this.thenInst;
	}
	
	/**
	 * Obtiene la instrucción a realizar cuando la condición no se cumple
	 * @return
	 */
	public Statement getElseInstruction() {
		return this.elseInst;
	}
	
	/**
	 * Verifica si la instrucción alcanza siempre un "return".
	 * @return
	 */
	public boolean returns() {
		if(elseInst == null) return false;
		boolean thenBranch = thenInst.returns();
		boolean elseBranch = elseInst.returns();			
		return thenBranch && elseBranch;
	}

	public ArrayList<String> toJava() {
		ArrayList<String> cadena = new ArrayList<String>();
		
		String cabecera="if(";
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
		
		
		if(thenInst.getClass().equals(AssignStatement.class))
		{
			cadena.add(cabecera+")"+"\t"+ ((AssignStatement)thenInst).toJava() );
		}
		else if(thenInst.getClass().equals(BlockStatement.class))
		{
			cadena.add(cabecera+") "+"{");
			ArrayList<String> block =((BlockStatement)thenInst).toJava();
			
			int sizeBlock = block.size();
			
			for(int b = 0; b<sizeBlock; b++)
			{
				cadena.add(block.get(b));
			}
			cadena.add("}");
		}
		else if(thenInst.getClass().equals(IfStatement.class))
		{
			cadena.add(cabecera+") {");
			ArrayList<String> ifblock =((IfStatement)thenInst).toJava();
			
			int sizeBlock = ifblock.size();
			
			for(int b = 0; b<sizeBlock; b++)
			{
				cadena.add("\t"+ifblock.get(b));
			}
			cadena.add("}");
		}
		else if(thenInst.getClass().equals(ReturnStatement.class))
		{
			cadena.add(cabecera+")"+"\t"+ ((ReturnStatement)thenInst).toJava() );
		}
		else if(thenInst.getClass().equals(WhileStatement.class))
		{
			cadena.add(cabecera+") {");
			ArrayList<String> whileS = ((WhileStatement)thenInst).toJava();
			
			int sizeWhile = whileS.size();
			
			for(int b = 0; b<sizeWhile; b++)
			{
				cadena.add("\t"+whileS.get(b));
			}
			cadena.add("}");
		}
		
		
		if(elseInst != null)
		{
			
			if(elseInst.getClass().equals(AssignStatement.class))
			{
				cadena.add("else "+"\t"+ ((AssignStatement)elseInst).toJava() );
			}
			else if(elseInst.getClass().equals(BlockStatement.class))
			{
				cadena.add("else "+"{");
				ArrayList<String> block = ((BlockStatement)elseInst).toJava();
				
				int sizeBlock = block.size();
				
				for(int b = 0; b<sizeBlock; b++)
				{
					cadena.add(block.get(b));
				}
				cadena.add("}");
			}
			else if(elseInst.getClass().equals(IfStatement.class))
			{
				cadena.add("else "+"{");
				ArrayList<String> ifblock =((IfStatement)elseInst).toJava();
				
				int sizeBlock = ifblock.size();
				
				for(int b = 0; b<sizeBlock; b++)
				{
					cadena.add("\t"+ifblock.get(b));
				}
				cadena.add("}");
			}
			else if(elseInst.getClass().equals(ReturnStatement.class))
			{
				cadena.add("\t"+ ((ReturnStatement)elseInst).toJava() );
			}
			else if(elseInst.getClass().equals(WhileStatement.class))
			{
				cadena.add("else "+"{");
				ArrayList<String> whileS = ((WhileStatement)elseInst).toJava();
				
				int sizeWhile = whileS.size();
				
				for(int b = 0; b<sizeWhile; b++)
				{
					cadena.add("\t"+whileS.get(b));
				}
				cadena.add("}");
			}

		}
		
		return cadena;

	}
}
