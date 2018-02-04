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


import java.io.*;

/**
 * Clase que desarrolla el analizador léxico de Tinto
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class TorcsLexer extends Lexer implements TokenConstants {

	/**
	 * Transiciones del autómata del analizador léxico
	 * 
	 * @param state Estado inicial
	 * @param symbol Símbolo del alfabeto
	 * @return Estado final
	 */
	protected int transition(int state, char symbol) {
		switch(state) {
		
			case 0: 
				
				if(symbol >= 'a' && symbol <= 'z') return 1;
				else if(symbol >= 'A' && symbol <= 'Z') return 1;
				else if(symbol == '_') return 1;
				else if(symbol == ' ' || symbol == '\t') return 2;
				else if(symbol == '\r' || symbol == '\n') return 2;		// quitar '\r' si no se puede usar
				else if(symbol == '#') return 3;
				else if(symbol == '+') return 5;
				else if(symbol == '-') return 6;
				else if(symbol == '*') return 7;
				else if(symbol == '/') return 8;
				else if(symbol == '%') return 9;
				else if(symbol == '|') return 10;
				else if(symbol == '!') return 11;
				else if(symbol == '&') return 12;
				else if(symbol == '>') return 13;
				else if(symbol == '<') return 15;
				else if(symbol == '=') return 19;
				else if(symbol == '{') return 20;
				else if(symbol == '}') return 21;
				else if(symbol == '(') return 22;
				else if(symbol == ')') return 23;
				else if(symbol == ',') return 25;
				else if(symbol == ';') return 26;				
				else if(symbol >= '1' && symbol <= '9') return 27;
				else if(symbol == '0') return 28;
				else return -1;
				
			case 1:
				
				if(symbol >= 'a' && symbol <= 'z') return 1;
				else if(symbol >= 'A' && symbol <= 'Z') return 1;
				else if(symbol == '_') return 1;
				else if(symbol >= '0' && symbol <= '9') return 1;
				else return -1;
				
			case 2:
				
				if(symbol == ' ' || symbol == '\t') return 2;
				else if(symbol == '\r' || symbol == '\n') return 2;		// quitar '\r' si no se puede usar
				else return -1;
				
			case 3:
				
				if(symbol == '\n') return 4;
				else return 3;
				
			case 6:
				
				if(symbol == '>') return 24;
				else return -1;
				
			case 13:
				
				if(symbol == '=') return 14;
				else return -1;
				
			case 15:
				
				if(symbol == '=') return 16;
				else if(symbol == '>') return 17;
				else if(symbol == '-') return 18;
				else return -1;
	
			case 27:
				
				if(symbol >= '0' && symbol <= '9') return 27;
				else if(symbol == '.') return 32;
				else if(symbol == 'e' || symbol == 'E') return 34;
				else return -1;
				
			case 28:
				
				if(symbol >= '0' && symbol <= '7') return 29;
				else if(symbol == '.') return 32;
				else if(symbol == 'e' || symbol == 'E') return 34;
				else if(symbol == 'x' || symbol == 'X') return 30;
				else return -1;
				
			case 29:
				
				if(symbol >= '0' && symbol <= '7') return 29;
				else return -1;
				
			case 30:
				
				if(symbol >= '0' && symbol <= '9') return 31;
				else if(symbol >= 'a' && symbol <= 'f') return 31;
				else if(symbol >= 'A' && symbol <= 'F') return 31;
				else return -1;
				
			case 31:

				if(symbol >= '0' && symbol <= '9') return 31;
				else if(symbol >= 'a' && symbol <= 'f') return 31;
				else if(symbol >= 'A' && symbol <= 'F') return 31;
				else return -1;
				
			case 32:
				
				if(symbol >= '0' && symbol <= '9') return 33;
				else return -1;
				
			case 33:
				
				if(symbol >= '0' && symbol <= '9') return 33;
				else return -1;

			case 34:
				
				if(symbol == '-' || symbol == '+') return 35;
				else return -1;
				
			case 35:
				
				if(symbol >= '0' && symbol <= '9') return 36;
				else return -1;
				
			case 36:
				
				if(symbol >= '0' && symbol <= '9') return 36;
				else return -1;			
				
			default:
				return -1;
		}
	}
	
	/**
	 * Verifica si un estado es final
	 * 
	 * @param state Estado
	 * @return true, si el estado es final
	 */
	protected boolean isFinal(int state) {
		if(state <=0 || state > 36) return false;
		switch(state) {
			case 3:
			case 30:
			case 32:
			case 34:
			case 35:
				return false;
			default: 
				return true;
		}
	}
	
	/**
	 * Genera el componente léxico correspondiente al estado final y
	 * al lexema encontrado. Devuelve null si la acción asociada al
	 * estado final es omitir (SKIP).
	 * 
	 * @param state Estado final alcanzado
	 * @param lexeme Lexema reconocido
	 * @param row Fila de comienzo del lexema
	 * @param column Columna de comienzo del lexema
	 * @return Componente léxico correspondiente al estado final y al lexema
	 */
	protected Token getToken(int state, String lexeme, int row, int column) {
		
		switch(state) {
		
			case 1: return new Token(getKind(lexeme),lexeme, row, column);
			case 2: return null;
			case 4: return null;
			case 5: return new Token(PLUS,lexeme, row, column);
			case 6: return new Token(MINUS,lexeme, row, column);
			case 7: return new Token(PROD,lexeme, row, column);
			case 8: return new Token(DIV,lexeme, row, column);
			case 9: return new Token(MOD,lexeme, row, column);
			case 10: return new Token(OR,lexeme, row, column);
			case 11: return new Token(NOT,lexeme, row, column);
			case 12: return new Token(AND,lexeme, row, column);
			case 13: return new Token(GT,lexeme, row, column);
			case 14: return new Token(GE,lexeme, row, column);
			case 15: return new Token(LT,lexeme, row, column);
			case 16: return new Token(LE,lexeme, row, column);
			case 17: return new Token(NE,lexeme, row, column);
			case 18: return new Token(ASSIGN,lexeme, row, column);
			case 19: return new Token(EQ,lexeme, row, column);
			case 20: return new Token(LBRACE,lexeme, row, column);
			case 21: return new Token(RBRACE,lexeme, row, column);
			case 22: return new Token(LPAREN,lexeme, row, column);
			case 23: return new Token(RPAREN,lexeme, row, column);
			case 24: return new Token(FLECHA,lexeme, row, column);
			case 25: return new Token(COMMA,lexeme, row, column);
			case 26: return new Token(SEMICOLON,lexeme, row, column);
			case 27: return new Token(INTEGER_LITERAL,lexeme, row, column);
			case 28: return new Token(INTEGER_LITERAL,lexeme, row, column);
			case 29: return new Token(INTEGER_LITERAL,lexeme, row, column);
			case 31: return new Token(INTEGER_LITERAL,lexeme, row, column);
			case 33: return new Token(DOUBLE_LITERAL,lexeme, row, column);
			case 36: return new Token(DOUBLE_LITERAL,lexeme, row, column);
			
			default: return null;
		}
	}
	
	/**
	 * Estudia si un identificador corresponde a una palabra clave del lenguaje
	 * y devuelve el código del token adecuado
	 * @param id
	 * @return
	 */
	private int getKind(String lexeme) {
		
		if(lexeme.equals("perception")) return PERCEPTION;
		else if(lexeme.equals("action")) return ACTION;
		else if(lexeme.equals("rules")) return RULES;
		else if(lexeme.equals("inner")) return INNER;
		else if(lexeme.equals("int")) return INT;
		else if(lexeme.equals("double")) return DOUBLE;
		else if(lexeme.equals("boolean")) return BOOLEAN;
		else if(lexeme.equals("true")) return TRUE;
		else if(lexeme.equals("false")) return FALSE;
		else if(lexeme.equals("speed")) return SPEED;
		else if(lexeme.equals("angle")) return ANGLE;
		else if(lexeme.equals("position")) return POSITION;
		else if(lexeme.equals("rpm")) return RPM;
		else if(lexeme.equals("sensor0")) return SENSOR0;
		else if(lexeme.equals("sensor1")) return SENSOR1;
		else if(lexeme.equals("sensor2")) return SENSOR2;
		else if(lexeme.equals("sensor3")) return SENSOR3;
		else if(lexeme.equals("sensor4")) return SENSOR4;
		else if(lexeme.equals("sensor5")) return SENSOR5;
		else if(lexeme.equals("sensor6")) return SENSOR6;
		else if(lexeme.equals("sensor7")) return SENSOR7;
		else if(lexeme.equals("sensor8")) return SENSOR8;
		else if(lexeme.equals("sensor9")) return SENSOR9;
		else if(lexeme.equals("sensor10")) return SENSOR10;
		else if(lexeme.equals("sensor11")) return SENSOR11;
		else if(lexeme.equals("sensor12")) return SENSOR12;
		else if(lexeme.equals("sensor13")) return SENSOR13;
		else if(lexeme.equals("sensor14")) return SENSOR14;
		else if(lexeme.equals("sensor15")) return SENSOR15;
		else if(lexeme.equals("sensor16")) return SENSOR16;
		else if(lexeme.equals("sensor17")) return SENSOR17;
		else if(lexeme.equals("sensor18")) return SENSOR18;
		else if(lexeme.equals("gear")) return GEAR;
		else if(lexeme.equals("accelerate")) return ACCELERATE;
		else if(lexeme.equals("brake")) return BRAKE;
		else if(lexeme.equals("steering")) return STEERING;
		else if(lexeme.equals("if")) return IF;
		else if(lexeme.equals("else")) return ELSE;
		else if(lexeme.equals("while")) return WHILE;
		else return IDENTIFIER;
	}
	
	/**
	 * Constructor
	 * @param filename Nombre del fichero fuente
	 * @throws IOException En caso de problemas con el flujo de entrada
	 */
	public TorcsLexer(String filename) throws IOException {
		super(filename);
	}
	
	/**
	 * Punto de entrada para ejecutar pruebas del analizador léxico
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length == 0) return;
		try {
			TorcsLexer lexer = new TorcsLexer(args[0]);
			Token tk;
			do {
				tk = lexer.getNextToken();
				System.out.println(tk.toString());
			} while(tk.getKind() != Token.EOF);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
