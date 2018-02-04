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


/**
 * Interfaz que define los códigos de las diferentes categorías léxicas
 *  
 * * @author Francisco José Moreno Velo
 *
 */
public interface TokenConstants {

	/**
	 * Final de fichero
	 */
	public int EOF = 0;
	
	//--------------------------------------------------------------//
	// Palabras clave
	//--------------------------------------------------------------//
	
	/**
	 * Palabra clave "perception"
	 */
	public int PERCEPTION = 1;
	
	/**
	 * Palabra clave "action"
	 */
	public int ACTION = 2;
	
	/**
	 * Palabra clave "rules"
	 */
	public int RULES = 3;
	
	/**
	 * Palabra clave "inner"
	 */
	public int INNER = 4;
	
	/**
	 * Palabra clave "int"
	 */
	public int INT = 5;
	
	/**
	 * Palabra clave "double"
	 */
	public int DOUBLE = 6;
	
	/**
	 * Palabra clave "boolean"
	 */
	public int BOOLEAN = 7;
	
	/**
	 * Palabra clave "true"
	 */
	public int TRUE = 8;
	
	/**
	 * Palabra clave "false"
	 */
	public int FALSE = 9;
	
	/**
	 * Palabra clave "speed"
	 */
	public int SPEED = 10;
	
	/**
	 * Palabra clave "angle"
	 */
	public int ANGLE = 11;
	
	/**
	 * Palabra clave "while"
	 */
	public int POSITION = 12;
	
	/**
	 * Palabra clave "rpm"
	 */
	public int RPM = 13;
	
	/**
	 * Palabra clave "sensor0"
	 */
	public int SENSOR0 = 14;
	
	/**
	 * Palabra clave "sensor1"
	 */
	public int SENSOR1 = 15;
	
	/**
	 * Palabra clave "sensor2"
	 */
	public int SENSOR2 = 16;
	
	/**
	 * Palabra clave "sensor3"
	 */
	public int SENSOR3 = 17;
	
	/**
	 * Palabra clave "sensor4"
	 */
	public int SENSOR4 = 18;
	
	/**
	 * Palabra clave "sensor5"
	 */
	public int SENSOR5 = 19;
	
	/**
	 * Palabra clave "sensor6"
	 */
	public int SENSOR6 = 20;
	
	/**
	 * Palabra clave "sensor7"
	 */
	public int SENSOR7 = 21;
	
	/**
	 * Palabra clave "sensor8"
	 */
	public int SENSOR8 = 22;
	
	/**
	 * Palabra clave "sensor9"
	 */
	public int SENSOR9 = 23;
	
	/**
	 * Palabra clave "sensor10"
	 */
	public int SENSOR10 = 24;
	
	/**
	 * Palabra clave "sensor11"
	 */
	public int SENSOR11 = 25;
	
	/**
	 * Palabra clave "sensor12"
	 */
	public int SENSOR12 = 26;
	
	/**
	 * Palabra clave "sensor13"
	 */
	public int SENSOR13 = 27;
	
	/**
	 * Palabra clave "sensor14"
	 */
	public int SENSOR14 = 28;
	
	/**
	 * Palabra clave "sensor15"
	 */
	public int SENSOR15 = 29;
	
	/**
	 * Palabra clave "sensor16"
	 */
	public int SENSOR16 = 30;
	
	/**
	 * Palabra clave "sensor17"
	 */
	public int SENSOR17 = 31;
	
	/**
	 * Palabra clave "sensor18"
	 */
	public int SENSOR18 = 32;
	
	/**
	 * Palabra clave "gear"
	 */
	public int GEAR = 33;
	
	/**
	 * Palabra clave "accelerate"
	 */
	public int ACCELERATE = 34;
	
	/**
	 * Palabra clave "brake"
	 */
	public int BRAKE = 35;
	
	/**
	 * Palabra clave "steering"
	 */
	public int STEERING = 36;
	
	/**
	 * Palabra clave "if"
	 */
	public int IF = 37;
	
	/**
	 * Palabra clave "else"
	 */
	public int ELSE = 38;
	
	/**
	 * Palabra clave "while"
	 */
	public int WHILE = 39;
	
	
	
	//--------------------------------------------------------------//
	// Identificadores y literales
	//--------------------------------------------------------------//

	/**
	 * Identificador
	 */
	public int IDENTIFIER = 40;

	/**
	 * Literal de tipo int
	 */
	public int INTEGER_LITERAL = 41;
	
	/**
	 * Literal de tipo double
	 */
	public int DOUBLE_LITERAL = 42;
	
	//--------------------------------------------------------------//
	// Separadores
	//--------------------------------------------------------------//
	
	/**
	 * Paréntesis abierto "("
	 */
	public int LPAREN = 43;
	
	/**
	 * Paréntesis cerrado ")"
	 */
	public int RPAREN = 44;
	
	/**
	 * Llave abierta "{"
	 */
	public int LBRACE = 45;
	
	/**
	 * Llave cerrada "}"
	 */
	public int RBRACE = 46;
	
	/**
	 * Punto y coma ";"
	 */
	public int SEMICOLON = 47;
	
	/**
	 * Coma ","
	 */
	public int COMMA = 48;
	
	/**
	 * Flecha "->"
	 */
	public int FLECHA = 49;
	
	//--------------------------------------------------------------//
	// Operadores
	//--------------------------------------------------------------//

	/**
	 * Asignación "<-"
	 */
	public int ASSIGN = 50;

	/**
	 * Igualdad "="
	 */
	public int EQ = 51;
	
	/**
	 * Menor "<"
	 */
	public int LT = 52;
	
	/**
	 * Menor o igual "<="
	 */
	public int LE = 53;
	
	/**
	 * Mayor ">"
	 */
	public int GT = 54;
	
	/**
	 * Mayor o igual ">="
	 */
	public int GE = 55;
	
	/**
	 * Distinto "<>"
	 */
	public int NE = 56;
	
	/**
	 * Disyunción "|"
	 */
	public int OR = 57;
	
	/**
	 * Conjunción "&"
	 */
	public int AND = 58;
	
	/**
	 * Negación "!"
	 */
	public int NOT = 59;
	
	/**
	 * Suma "+"
	 */
	public int PLUS = 60;
	
	/**
	 * Resta "-"
	 */
	public int MINUS = 61;
	
	/**
	 * Producto "*"
	 */
	public int PROD = 62;
	
	/**
	 * División "/"
	 */
	public int DIV = 63;
	
	/**
	 * Módulo "%"
	 */
	public int MOD = 64;
}
