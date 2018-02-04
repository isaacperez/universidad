package TorcsController.ast.expression;

import TorcsController.ast.Type;

public class DoubleLiteralExpression extends Expression{

	//----------------------------------------------------------------//
		//                        Miembros privados                       //
		//----------------------------------------------------------------//

		/**
		 * Valor de un literal boolean
		 */
		private double value;
			
		//----------------------------------------------------------------//
		//                            Constructores                       //
		//----------------------------------------------------------------//

		/**
		 * Constructor basado en el lexema
		 * 
		 * @param lexeme
		 * @throws IntegerOutOfValueException
		 */
		public DoubleLiteralExpression(String lexeme) {
			super(Type.DOUBLE_TYPE);
			this.value = Double.parseDouble(lexeme);
		}
		
		/**
		 * Contrutor de tipo boolean
		 * @param value
		 */
		public DoubleLiteralExpression(double value) {
			super(Type.DOUBLE_TYPE);
			this.value = value;
		}
			
		//----------------------------------------------------------------//
		//                          Métodos públicos                      //
		//----------------------------------------------------------------//

		/**
		 * Obtiene el valor del literal boolean
		 * @return
		 */
		public double getValue() {
			return this.value;
		}

		public String toJava() {
			return String.valueOf(value);
		}
	
}
