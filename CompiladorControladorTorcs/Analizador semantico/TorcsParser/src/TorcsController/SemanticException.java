package TorcsController;

public class SemanticException extends Exception {


	public static final int TYPE_MISMATCH_EXCEPTION = 1;
	public static final int DUPLICATED_VARIABLE_EXCEPTION = 2;
	public static final int DUPLICATED_PERCEPTION_EXCEPTION = 3;
	public static final int DUPLICATED_ARGUMENT_EXCEPTION = 4;
	public static final int DUPLICATED_ACTION_EXCEPTION = 5;
	public static final int PERCEPTION_DECLARATION_INNER = 6;
	public static final int NUMBER_FORMAT_EXCEPTION = 7;
	public static final int PERCEPTION_DECLARATION_TYPE = 8;
	public static final int PERCEPTION_ASSIGN_DECLARATION = 9;
	public static final int PERCEPTION_ASSIGN_INNER = 10;
	public static final int UNKNOWN_VARIABLE_EXCEPTION = 11;
	public static final int UNKNOWN_METHOD_EXCEPTION = 12;
	public static final int NUMPARAM_METHOD_EXCEPTION = 13;
	public static final int INNER_ARGUMENT_EXCEPTION = 14;
	public static final int UNFINISHED_METHOD = 15;
	public static final int UNREACHABLE_CODE = 16;
	public static final int ACTION_ASSIGN_DECLARATION = 17;
	public static final int CONDITION_NO_VALIDA = 18;
	public static final int CONDITION_ERROR = 19;

	//----------------------------------------------------------------//
	//                        Miembros privados                       //
	//----------------------------------------------------------------//

	/**
	 * Mensaje de error
	 */
	private String msg;

	//----------------------------------------------------------------//
	//                           Constructores                        //
	//----------------------------------------------------------------//

	/**
	 * Constructor con un solo tipo esperado
	 * @param token
	 * @param expected
	 */
	public SemanticException(int code, Token token) {
		this.msg = "Parse exception at row "+token.getRow();
		msg += ", column "+token.getColumn()+".\n";
		msg += getExplanationForCode(code)+"\n";
	}

	/**
	 * Obtiene el mensaje de error
	 */
	public String toString() {
		return this.msg;
	}

	/**
	 * Obtiene la descripcion del error
	 * @param code
	 * @return
	 */
	private static String getExplanationForCode(int code) {
		switch(code) {
			case TYPE_MISMATCH_EXCEPTION:
				return "  No hay concordancia de tipos.";
			case DUPLICATED_VARIABLE_EXCEPTION:
				return "  Variable duplicada.";
			case DUPLICATED_PERCEPTION_EXCEPTION:
				return "  Identificador de perception duplicado.";
			case DUPLICATED_ARGUMENT_EXCEPTION:
				return "  Variable de argumento duplicada.";
			case DUPLICATED_ACTION_EXCEPTION:
				return "  Identificador de action duplicado.";
			case PERCEPTION_DECLARATION_INNER:
				return "  Este identificador es una variable inner.";
			case NUMBER_FORMAT_EXCEPTION:
				return "  Literal no valido.";
			case PERCEPTION_DECLARATION_TYPE:
				return "  No hay concordancia de tipos entre el valor devuelto y el declarado.";
			case PERCEPTION_ASSIGN_DECLARATION:
				return "  La variable no ha sido declarada.";
			case PERCEPTION_ASSIGN_INNER:
				return "  No se pueden modificar variables internas en perceptions.";
			case UNKNOWN_VARIABLE_EXCEPTION:
				return "  Variable desconocida.";
			case UNKNOWN_METHOD_EXCEPTION:
				return "  Metodo desconocido.";
			case NUMPARAM_METHOD_EXCEPTION:
				return "  No coincide el numero de parametros.";
			case INNER_ARGUMENT_EXCEPTION:
				return "  No se puede usar el identificador, esta declarado como inner.";
			case UNFINISHED_METHOD:
				return "  El metodo debe devolver algun valor.";
			case UNREACHABLE_CODE:
				return "  Codigo inalcanzale";
			case ACTION_ASSIGN_DECLARATION:
				return "  La variable no ha sido declarada.";
			case CONDITION_NO_VALIDA:
				return "  La expresion de la condicion no devuelve nada.";
			case CONDITION_ERROR:
				return "  El tipo que devuelve la condicion debe ser boolean.";
			default: return "";
		}
	}



}
