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

import TorcsController.ast.struct.Variable;

/**
 * Clase que describe la expresión formada por una variable local o
 * por un argumento de un método.
 * 
 * @author Francisco José Moreno Velo
 */
public class VariableExpression extends Expression {
	
	//----------------------------------------------------------------//
	//                        Miembros privados                       //
	//----------------------------------------------------------------//

	/**
	 * Descripción de la variable
	 */
	private Variable var;
	
	//----------------------------------------------------------------//
	//                            Constructores                       //
	//----------------------------------------------------------------//

	/**
	 * Constructor
	 * @param decl Descripción de la clase
	 */
	public VariableExpression(Variable var) {
		super(var.getType());
		this.var = var;
	}
	
	//----------------------------------------------------------------//
	//                          Métodos públicos                      //
	//----------------------------------------------------------------//

	/**
	 * Obtiene la referencia a la declaración de la variable
	 * @return
	 */
	public Variable getVariable() {
		return this.var;
	}

	public String toJava() {

		if(var.getName().equals("gear")) return "sensors.getGear()";
		else if(var.getName().equals("speed")) return "sensors.getSpeed()";
		else if(var.getName().equals("angle")) return "sensors.getAngleToTrackAxis()";
		else if(var.getName().equals("rpm")) return "sensors.getRPM()";
		else if(var.getName().equals("position")) return "sensors.getTrackPosition()";
		else if(var.getName().equals("sensor0")) return "sensors.getTrackEdgeSensors()[0]";
		else if(var.getName().equals("sensor1")) return "sensors.getTrackEdgeSensors()[1]";
		else if(var.getName().equals("sensor2")) return "sensors.getTrackEdgeSensors()[2]";
		else if(var.getName().equals("sensor3")) return "sensors.getTrackEdgeSensors()[3]";
		else if(var.getName().equals("sensor4")) return "sensors.getTrackEdgeSensors()[4]";
		else if(var.getName().equals("sensor5")) return "sensors.getTrackEdgeSensors()[5]";
		else if(var.getName().equals("sensor6")) return "sensors.getTrackEdgeSensors()[6]";
		else if(var.getName().equals("sensor7")) return "sensors.getTrackEdgeSensors()[7]";
		else if(var.getName().equals("sensor8")) return "sensors.getTrackEdgeSensors()[8]";
		else if(var.getName().equals("sensor9")) return "sensors.getTrackEdgeSensors()[9]";
		else if(var.getName().equals("sensor10")) return "sensors.getTrackEdgeSensors()[10]";
		else if(var.getName().equals("sensor11")) return "sensors.getTrackEdgeSensors()[11]";
		else if(var.getName().equals("sensor12")) return "sensors.getTrackEdgeSensors()[12]";
		else if(var.getName().equals("sensor13")) return "sensors.getTrackEdgeSensors()[13]";
		else if(var.getName().equals("sensor14")) return "sensors.getTrackEdgeSensors()[14]";
		else if(var.getName().equals("sensor15")) return "sensors.getTrackEdgeSensors()[15]";
		else if(var.getName().equals("sensor16")) return "sensors.getTrackEdgeSensors()[16]";
		else if(var.getName().equals("sensor17")) return "sensors.getTrackEdgeSensors()[17]";
		else if(var.getName().equals("sensor18")) return "sensors.getTrackEdgeSensors()[18]";
		else return var.getName();
	}
	
}
