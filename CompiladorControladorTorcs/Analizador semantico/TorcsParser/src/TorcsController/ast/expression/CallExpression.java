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

package TorcsController.ast.expression;

import java.util.ArrayList;



import TorcsController.ast.Type;
import TorcsController.ast.struct.Method;


/**
 * Clase que describe la expresi�n formada por la llamada a un m�todo
 * 
 * @author Francisco Jos� Moreno Velo
 */
public class CallExpression extends Expression {
		
	//----------------------------------------------------------------//
	//                        Miembros privados                       //
	//----------------------------------------------------------------//

	/**
	 * Metodo
	 */
	private Method method;
	
	/**
	 * Par�metros de la llamada
	 */
	private CallParameters call;
	
	
	//----------------------------------------------------------------//
	//                            Constructores                       //
	//----------------------------------------------------------------//

	/**
	 * Constructor
	 * @param left
	 * @param method
	 * @param call
	 */
	public CallExpression(Method method, CallParameters call) {
		super(method.getType());
		this.method = method;
		this.call = call;
	}
	
	//----------------------------------------------------------------//
	//                          M�todos p�blicos                      //
	//----------------------------------------------------------------//
	
	/**
	 * Obtiene la descripci�n del m�todo
	 * @return
	 */
	public Method getMethod() {
		return this.method;
	}
	
	/**
	 * Obtiene la representaci�on de los par�metros de la llamada
	 * @return
	 */
	public CallParameters getCallParameters() {
		return this.call;
	}

	public String toJava() {

		String s = method.getName()+"(";
		
		if(method.getType()==Type.VOID_TYPE) s = s+"sensors,action";
		else s = s+"sensors";
		
		ArrayList<Expression> calls = call.getParameters();
		 
		int size = calls.size();
		
		if(size!=0) s = s+ ", ";
		
		for(int i = 0; i<size;i++)
		{
			Expression callActual = calls.get(i);
			
			if(callActual.getClass().equals(BinaryExpression.class) )
			{
				s = s +((BinaryExpression)callActual).toJava();
			}
			else if(callActual.getClass().equals(BooleanLiteralExpression.class) )
			{
				s = s +((BooleanLiteralExpression)callActual).toJava();
			}
			else if(callActual.getClass().equals(CallExpression.class) )
			{
				s = s +((CallExpression)callActual).toJava();
			}
			else if(callActual.getClass().equals(DoubleLiteralExpression.class) )
			{
				s = s +((DoubleLiteralExpression)callActual).toJava();
			}
			else if(callActual.getClass().equals(IntegerLiteralExpression.class) )
			{
				s = s +((IntegerLiteralExpression)callActual).toJava();
			}
			else if(callActual.getClass().equals(PerceptionReferenceExpression.class) )
			{
				s = s +((PerceptionReferenceExpression)callActual).toJava();
			}
			else if(callActual.getClass().equals(UnaryExpression.class) )
			{
				s = s +((UnaryExpression)callActual).toJava();
			}
			else if(callActual.getClass().equals(VariableExpression.class) )
			{
				s = s +((VariableExpression)callActual).toJava();
			}
			 
			if(i<(size-1)) s = s +", ";
		}
		 
		return s+ ")";
	}
	
}
