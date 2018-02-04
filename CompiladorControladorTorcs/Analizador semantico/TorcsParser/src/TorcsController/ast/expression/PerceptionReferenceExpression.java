package TorcsController.ast.expression;

import java.util.ArrayList;

import TorcsController.ast.struct.Perception;

public class PerceptionReferenceExpression extends Expression {

	//----------------------------------------------------------------//
		//                        Miembros privados                       //
		//----------------------------------------------------------------//

		/**
		 * Metodo
		 */
		private Perception perception;
		
		/**
		 * Parámetros de la llamada
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
		public PerceptionReferenceExpression(Perception perception, CallParameters call) {
			super(perception.getType());
			this.perception = perception;
			this.call = call;
		}
		
		//----------------------------------------------------------------//
		//                          Métodos públicos                      //
		//----------------------------------------------------------------//
		
		/**
		 * Obtiene la descripción del método
		 * @return
		 */
		public Perception getMethod() {
			return this.perception;
		}
		
		/**
		 * Obtiene la representacióon de los parámetros de la llamada
		 * @return
		 */
		public CallParameters getCallParameters() {
			return this.call;
		}

		public String toJava() {
			
			String s = perception.getName()+"(";
			ArrayList<Expression> calls = call.getParameters();
			 
			int size = calls.size();
			 
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
