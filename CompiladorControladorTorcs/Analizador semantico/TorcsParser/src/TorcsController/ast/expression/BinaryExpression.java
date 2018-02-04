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

package TorcsController.ast.expression;


/**
 * Clase que describe un operación unaria
 * 
 * @author Francisco José Moreno Velo
 */
public class BinaryExpression extends Expression {

	//----------------------------------------------------------------//
	//                       Constantes públicas                      //
	//----------------------------------------------------------------//

	/**
	 * Operador: AND
	 */
	public static final int AND = 1;

	/**
	 * Operador: OR
	 */
	public static final int OR = 2;

	/**
	 * Operador: PROD
	 */
	public static final int PROD = 3;

	/**
	 * Operador: DIV
	 */
	public static final int DIV = 4;

	/**
	 * Operador: MOD
	 */
	public static final int MOD = 5;

	/**
	 * Operador: PLUS
	 */
	public static final int PLUS = 6;

	/**
	 * Operador: MINUS
	 */
	public static final int MINUS = 7;

	/**
	 * Operador: EQ
	 */
	public static final int EQ = 8;

	/**
	 * Operador: NEQ
	 */
	public static final int NEQ = 9;

	/**
	 * Operador: GT
	 */
	public static final int GT = 10;

	/**
	 * Operador: GE
	 */
	public static final int GE = 11;

	/**
	 * Operador: LT
	 */
	public static final int LT = 12;

	/**
	 * Operador: LE
	 */
	public static final int LE = 13;
	
	//----------------------------------------------------------------//
	//                        Miembros privados                       //
	//----------------------------------------------------------------//

	/**
	 * Código del operador
	 */
	private int op;
	
	/**
	 * Primer operando
	 */
	private Expression left;
	
	/**
	 * Segundo operando
	 */
	private Expression right;
	
	//----------------------------------------------------------------//
	//                            Constructores                       //
	//----------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public BinaryExpression(int type, int op, Expression left, Expression right) {
		super(type);
		this.op = op;
		this.left = left;
		this.right = right;
	}
	
	//----------------------------------------------------------------//
	//                          Métodos públicos                      //
	//----------------------------------------------------------------//

	/**
	 * Obtiene el código del operador
	 * @return
	 */
	public int getOperator() {
		return this.op;
	}
	
	/**
	 * Obtiene la referencia a la expresión de la izquierda
	 * @return
	 */
	public Expression getLeftExpression() {
		return this.left;
	}
	
	/**
	 * Obtiene la referencia a la expresión de la derecha
	 * @return
	 */
	public Expression getRightExpression() {
		return this.right;
	}

	public String toJava() {
		
		String leftS="";
		if(left.getClass().equals(BinaryExpression.class) )
		{
			leftS = ((BinaryExpression)left).toJava();
		}
		else if(left.getClass().equals(BooleanLiteralExpression.class) )
		{
			leftS = ((BooleanLiteralExpression)left).toJava();
		}
		else if(left.getClass().equals(CallExpression.class) )
		{
			leftS = ((CallExpression)left).toJava();
		}
		else if(left.getClass().equals(DoubleLiteralExpression.class) )
		{
			leftS = ((DoubleLiteralExpression)left).toJava();
		}
		else if(left.getClass().equals(IntegerLiteralExpression.class) )
		{
			leftS = ((IntegerLiteralExpression)left).toJava();
		}
		else if(left.getClass().equals(PerceptionReferenceExpression.class) )
		{
			leftS = ((PerceptionReferenceExpression)left).toJava();
		}
		else if(left.getClass().equals(UnaryExpression.class) )
		{
			leftS = ((UnaryExpression)left).toJava();
		}
		else if(left.getClass().equals(VariableExpression.class) )
		{
			leftS = ((VariableExpression)left).toJava();
		}
		
		String rightS="";
		if(right.getClass().equals(BinaryExpression.class) )
		{
			rightS = ((BinaryExpression)right).toJava();
		}
		else if(right.getClass().equals(BooleanLiteralExpression.class) )
		{
			rightS = ((BooleanLiteralExpression)right).toJava();
		}
		else if(right.getClass().equals(CallExpression.class) )
		{
			rightS = ((CallExpression)right).toJava();
		}
		else if(right.getClass().equals(DoubleLiteralExpression.class) )
		{
			rightS = ((DoubleLiteralExpression)right).toJava();
		}
		else if(right.getClass().equals(IntegerLiteralExpression.class) )
		{
			rightS = ((IntegerLiteralExpression)right).toJava();
		}
		else if(right.getClass().equals(PerceptionReferenceExpression.class) )
		{
			rightS = ((PerceptionReferenceExpression)right).toJava();
		}
		else if(right.getClass().equals(UnaryExpression.class) )
		{
			rightS = ((UnaryExpression)right).toJava();
		}
		else if(right.getClass().equals(VariableExpression.class) )
		{
			rightS = ((VariableExpression)right).toJava();
		}
		
		String opS="";
		
		switch(op){
		
			case 1: opS = " && ";
				break;
			case 2: opS = " || ";
				break;
			case 3: opS = " * ";
				break;
			case 4: opS = " / ";
			break;
			case 5: opS = " % ";
			break;
			case 6: opS = " + ";
			break;
			case 7: opS = " - ";
			break;
			case 8: opS = " == ";
			break;
			case 9: opS = " != ";
			break;
			case 10: opS = " > ";
			break;
			case 11: opS = " >= ";
			break;
			case 12: opS = " < ";
			break;
			case 13: opS = " < =";
			break;
		}
		
		
		if(op == PROD || op == DIV)
		{
			if(left.getClass().equals(BinaryExpression.class) )
			{
				if(((BinaryExpression) left).op == MINUS || ((BinaryExpression) left).op == PLUS || ((BinaryExpression) left).op == MOD) return "("+leftS+")"+opS+ rightS;
			}
		}
		else if (op == MOD)
		{
			if(left.getClass().equals(BinaryExpression.class) )
			{
				if(((BinaryExpression) left).op == MINUS || ((BinaryExpression) left).op == PLUS)  return "("+leftS+")"+opS+ rightS;
			}
		}
		return leftS + opS + rightS;
		
	}
	
}
