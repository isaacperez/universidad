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
//           Departamento de Tecnologías de la Información          //
//   Área de Ciencias de la Computación e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//          Compilador del lenguaje Tinto [Versión 0.0]             //
//                                                                  //
//------------------------------------------------------------------//

package TorcsController;

/**
 * Clase que describe un excepción sintáctica
 * 
 * @author Francisco José Moreno Velo
 */
public class SintaxException extends Exception implements TokenConstants {

	/**
	 * Constante asignada al objeto serializable
	 */
	private static final long serialVersionUID = 20080002L;

	/**
	 * Mensaje de error
	 */
	private String msg;
	
	/**
	 * Constructor con un solo tipo esperado
	 * @param token
	 * @param expected
	 */
	public SintaxException(Token token, int expected) {
		this.msg = "Sintax exception at row "+token.getRow();
		msg += ", column "+token.getColumn()+".\n";
		msg += "  Found "+token.getLexeme()+"\n";
		msg += "  while expecting "+getLexemeForKind(expected)+".\n";
	}
	
	/**
	 * Constructor con una lista de tipos esperados
	 * @param token
	 * @param expected
	 */
	public SintaxException(Token token, int[] expected) {
		this.msg = "Sintax exception at row "+token.getRow();
		msg += ", column "+token.getColumn()+".\n";
		msg += "  Found "+token.getLexeme()+"\n";
		msg += "  while expecting one of\n";
		for(int i=0; i<expected.length; i++) {
			msg += "    "+getLexemeForKind(expected[i])+"\n";
		}
	}
	
	/**
	 * Obtiene el mensaje de error
	 */
	public String toString() {
		return this.msg;
	}
	
	/**
	 * Descripción del token
	 * @param kind
	 * @return
	 */
	private String getLexemeForKind(int kind) {
		switch(kind) {
			
		case PERCEPTION: return "perception";
		case ACTION: return "action";
		case RULES: return "rules";
		case INNER: return "inner";
		case INT: return "int";
		case DOUBLE: return "double";
		case BOOLEAN: return "boolean";
		case TRUE: return "true";
		case FALSE: return "false";
		case SPEED: return "speed";
		case ANGLE: return "angle";
		case POSITION: return "position";
		case RPM: return "rpm";
		case SENSOR0: return "sensor0";
		case SENSOR1: return "sensor1";
		case SENSOR2: return "sensor2";
		case SENSOR3: return "sensor3";
		case SENSOR4: return "sensor4";
		case SENSOR5: return "sensor5";
		case SENSOR6: return "sensor6";
		case SENSOR7: return "sensor7";
		case SENSOR8: return "sensor8";
		case SENSOR9: return "sensor9";
		case SENSOR10: return "sensor10";
		case SENSOR11: return "sensor11";
		case SENSOR12: return "sensor12";
		case SENSOR13: return "sensor13";
		case SENSOR14: return "sensor14";
		case SENSOR15: return "sensor15";
		case SENSOR16: return "sensor16";
		case SENSOR17: return "sensor17";
		case SENSOR18: return "sensor18";
		case GEAR: return "gear";
		case ACCELERATE: return "accelerate";
		case BRAKE: return "brake";
		case STEERING: return "steering";
		case IF: return "if";
		case ELSE: return "else";
		case WHILE: return "while";
		case IDENTIFIER: return "identifier";
		case INTEGER_LITERAL: return "integer_literal";
		case DOUBLE_LITERAL: return "double_literal";
		case LPAREN: return "(";
		case RPAREN: return ")";
		case LBRACE: return "{";
		case RBRACE: return "}";
		case SEMICOLON: return ";";
		case COMMA: return ",";
		case FLECHA: return "->";
		case ASSIGN: return "<-";
		case EQ: return "=";
		case LT: return "<";
		case LE: return "<=";
		case GT: return ">";
		case GE: return ">=";
		case NE: return "<>";
		case OR: return "|";
		case AND: return "&";
		case NOT: return "!";
		case PLUS: return "+";
		case MINUS: return "-";
		case PROD: return "*";
		case DIV: return "/";
		case MOD: return "%";

		default: return "no se que ibas a poner";
		}
	}
}
