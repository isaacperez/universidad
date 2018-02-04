package TorcsController;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import TorcsController.ast.Type;
import TorcsController.ast.expression.BinaryExpression;
import TorcsController.ast.expression.BooleanLiteralExpression;
import TorcsController.ast.expression.CallExpression;
import TorcsController.ast.expression.CallParameters;
import TorcsController.ast.expression.DoubleLiteralExpression;
import TorcsController.ast.expression.Expression;
import TorcsController.ast.expression.IntegerLiteralExpression;
import TorcsController.ast.expression.UnaryExpression;
import TorcsController.ast.expression.VariableExpression;
import TorcsController.ast.statement.AssignStatement;
import TorcsController.ast.statement.BlockStatement;
import TorcsController.ast.statement.IfStatement;
import TorcsController.ast.statement.ReturnStatement;
import TorcsController.ast.statement.Statement;
import TorcsController.ast.statement.WhileStatement;
import TorcsController.ast.struct.Action;
import TorcsController.ast.struct.Controller;
import TorcsController.ast.struct.Inner;
import TorcsController.ast.struct.Method;
import TorcsController.ast.struct.Perception;
import TorcsController.ast.struct.Rule;
import TorcsController.ast.struct.Rules;
import TorcsController.ast.struct.Variable;



public class TorcsParser implements TokenConstants {

	private boolean Escrito = false;
	
	private boolean ErrorSemantico = false;
	/**
	 * Fichero a generar
	 */
	
	
	private FileWriter ficheroJava;
	
	/**
	 *  Analizador l�xico
	 */
	private TorcsLexer lexer;

	/**
	 * Ultimo token analizado
	 */
	private Token prevToken;

	/**
	 *  Siguiente token de la cadena de entrada
	 */
	private Token nextToken;

	/**
	 * Contador de errores
	 */
	private int errorCount;

	/**
	 * Mensaje de errores
	 */
	private String errorMsg;

	/**
	 * Raiz AST
	 */

	private Controller controller;

	private String nombreFichero;
	
	public TorcsParser(String string) {
		try {
			string = string.substring(0, string.length()-3);
			ficheroJava = new FileWriter(string+".java");
		
			nombreFichero = string;
			// Escribir en el fichero codigo invariante
			EscribirFicheroJava("import champ2011client.*;",0);
			EscribirSaltoFicheroJava(1);
			EscribirFicheroJava("public class "+string+" extends Controller { ",0);
			
			// Comienzo del codigo generado por el proceso de compilacion
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//-----------------------------------------------------------------//
	//			Creacion de fichero .java							   //
	//-----------------------------------------------------------------//
	
	private void EscribirSaltoFicheroJava(int num) {
		PrintWriter pw = new PrintWriter(ficheroJava);
		for(int i =0;i<num;i++)pw.println("");
	}
	
	private void EscribirMetodosFicheroJava(){
		if(!Escrito)
		{
			Escrito = true;
			
			EscribirSaltoFicheroJava(1);
			EscribirFicheroJava("public "+nombreFichero+"() {", 1);
			EscribirFicheroJava("System.out.println(\"Iniciando\");", 2);
			EscribirFicheroJava("}", 1);
			EscribirSaltoFicheroJava(1);
			
			EscribirFicheroJava("public void reset() {", 1);
			EscribirFicheroJava("System.out.println(\"Reiniciando la carrera\");", 2);
			EscribirFicheroJava("}", 1);
			EscribirSaltoFicheroJava(1);
			
			EscribirFicheroJava("public void shutdown() {", 1);
			EscribirFicheroJava("System.out.println(\"CARRERA TERMINADA\");", 2);
			EscribirFicheroJava("}", 1);
			
		}
	}
	
	private void CrearFicheroJava()
	{	
		
		// Cerramos la clase 
		EscribirSaltoFicheroJava(1);
		EscribirFicheroJava("}",0);
		try {
			ficheroJava.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void EscribirFicheroJava(String cadena,int tab)
	{
			String tabulacion = "";
			for(int i = 0;i<tab;i++) tabulacion= tabulacion+"\t";
			PrintWriter pw = new PrintWriter(ficheroJava);
			pw.println(tabulacion+cadena);

	}
	
	private void EscribirFicheroJava(Rules rules) {
		ArrayList<String> creacion = rules.toJava();
		
		int size = creacion.size();
		for(int i = 0; i<size; i++) EscribirFicheroJava(creacion.get(i),1);
		
	}

	private void EscribirFicheroJava(Perception perception) {
		ArrayList<String> creacion = perception.toJava();
		
		int size = creacion.size();
		for(int i = 0; i<size; i++) EscribirFicheroJava(creacion.get(i),1);
		
	}
	
	private void EscribirFicheroJava(Action action) {
		ArrayList<String> creacion = action.toJava();
		
		int size = creacion.size();
		for(int i = 0; i<size; i++) EscribirFicheroJava(creacion.get(i),1);
	}
	
	
	private void EscribirFichero() {
		if(!ErrorSemantico)
		{
			ArrayList<Inner> inner = controller.getInner();
			int sizeInner = inner.size();
	
			for(int i = 0; i<sizeInner;i++)
			{
				EscribirSaltoFicheroJava(1);
				EscribirFicheroJava(inner.get(i).toJava(), 1);
			}
			
			EscribirMetodosFicheroJava();
	
			// Escribe las rules
			EscribirSaltoFicheroJava(1);
			EscribirFicheroJava(controller.getRules());
			
			ArrayList<Perception> perception = controller.getPerception();
			int sizePerception = perception.size();
			
			for(int i = 0; i<sizePerception; i++)
			{
				EscribirSaltoFicheroJava(1);
				EscribirFicheroJava(perception.get(i));
			}
			
			ArrayList<Action> action = controller.getAction();
			int sizeAction = action.size();
			
			for(int i = 0; i<sizeAction; i++)
			{
				EscribirSaltoFicheroJava(1);
				EscribirFicheroJava(action.get(i));
			}
		}
		
	}
	

	//----------------------------------------------------------------//
	//       Metodos relacionados con el tratamiento de errores       //
	//----------------------------------------------------------------//

	

	/**
	 * Obtiene el numero de errores del analisis
	 * @return
	 */
	public int getErrorCount() {
		return this.errorCount;
	}

	/**
	 * Obtiene el mensaje de error del analisis
	 * @return
	 */
	public String getErrorMsg() {
		return this.errorMsg;
	}

	/**
	 * Almacena un error de analisis
	 * @param ex
	 */
	private void catchError(Exception ex) {
		ErrorSemantico = true;
		ex.printStackTrace();
		this.errorCount++;
		this.errorMsg += ex.toString();
	}

	/**
	 * Sincroniza la cadena de entrada al conjunto de tokens
	 * seleccionado
	 *
	 * @param left
	 * @param right
	 */
	private void skipTo(int[] left, int[] right) {
		boolean flag = false;
		if(prevToken.getKind() == EOF || nextToken.getKind() == EOF) flag = true;
		for(int i=0; i<left.length; i++) if(prevToken.getKind() == left[i]) flag = true;
		for(int i=0; i<right.length; i++) if(nextToken.getKind() == right[i]) flag = true;

		while(!flag) {
			prevToken = nextToken;
			nextToken = lexer.getNextToken();
			if(prevToken.getKind() == EOF || nextToken.getKind() == EOF) flag = true;
			for(int i=0; i<left.length; i++) if(prevToken.getKind() == left[i]) flag = true;
			for(int i=0; i<right.length; i++) if(nextToken.getKind() == right[i]) flag = true;
		}
	}
	
	


	private void verifyCondition(Expression condition,Token id) {
		if(condition.getType() == Type.VOID_TYPE){
			int errorcode = SemanticException.CONDITION_NO_VALIDA;
			catchError(new SemanticException(errorcode,id));
		}
		else if(condition.getType() != Type.BOOLEAN_TYPE){
			int errorcode = SemanticException.CONDITION_ERROR;
			catchError(new SemanticException(errorcode,id));
		}
		
	}

	private void verifyDuplicatedVariableInner(Token id) {

		if(controller.getInner(id.getLexeme()) != null){
			int errorcode = SemanticException.DUPLICATED_VARIABLE_EXCEPTION;
			catchError(new SemanticException(errorcode,id));
		}
	}

	private void verifyDuplicatedPerception(Token id, Perception perception){
		
		CallParameters call = new CallParameters();
		
		ArrayList<Variable> arrayList = perception.getArguments();
		int size2 = arrayList.size();
		for (int i = 0; i<size2; i++) call.addParameter(new VariableExpression(arrayList.get(i)));
		
		if(controller.getPerception(id.getLexeme(),call) != null){

			ArrayList<Variable> variables = perception.getArguments();
			
			int size = variables.size();
			int [] argTypes = new int[size];
			
			for(int i = 0; i<size; i++)
			{
				argTypes[i] = variables.get(i).getType();
			}
			
			if(controller.getPerception(id.getLexeme(),call).match(id.getLexeme(), argTypes))
			{
				
				if(controller.getPerception(id.getLexeme(), call).matchInteger(id.getLexeme(),argTypes))
				{
					int errorcode = SemanticException.DUPLICATED_PERCEPTION_EXCEPTION;
					catchError(new SemanticException(errorcode,id));
				}

			}
		}
	}

	private void verifyDuplicatedAction(Token id, Action action) {
		
		CallParameters call = new CallParameters();
		
		ArrayList<Variable> arrayList = action.getArguments();
		int size2 = arrayList.size();
		for (int i = 0; i<size2; i++) call.addParameter(new VariableExpression(arrayList.get(i)));
		
		if(controller.getAction(id.getLexeme(),call) != null){
			
			
			ArrayList<Variable> variables = action.getArguments();
			
			int size = variables.size();
			int [] argTypes = new int[size];
			
			for(int i = 0; i<size; i++)
			{
				argTypes[i] = variables.get(i).getType();
			}
			
			if(controller.getAction(id.getLexeme(),call).match(id.getLexeme(), argTypes))
			{
				// Si es de tipo entero el nuevo metodo que se esta creando y el otro era de tipo double no falla
				if(controller.getAction(id.getLexeme(), call).matchInteger(id.getLexeme(),argTypes))
				{
					int errorcode = SemanticException.DUPLICATED_ACTION_EXCEPTION;
					catchError(new SemanticException(errorcode,id));
				}
				
			}

		}

	}

	private void verifyDuplicatedVariableArgument(Token prevToken2,Method method) {
		if(method.getVariable(prevToken2.getLexeme()) != null)
		{
			int errorcode = SemanticException.DUPLICATED_ARGUMENT_EXCEPTION;
			catchError(new SemanticException(errorcode,prevToken2));
		}
		else if(controller.getInner(prevToken2.getLexeme()) != null )
		{
			int errorcode = SemanticException.INNER_ARGUMENT_EXCEPTION;
			catchError(new SemanticException(errorcode,prevToken2));
		}

	}

	private void verifyPcptDecl(Token prevToken2, Perception perception) {
		/*if(controller.getInner(prevToken2.getLexeme())!= null)
		{
			int errorcode = SemanticException.PERCEPTION_DECLARATION_INNER;
			catchError(new SemanticException(errorcode,prevToken2));
		}*/
		if (perception.existsInContext(prevToken2.getLexeme()))
		{
			int errorcode = SemanticException.DUPLICATED_VARIABLE_EXCEPTION;
			catchError(new SemanticException(errorcode,prevToken2));
		}
		else if (perception.getVariable(prevToken2.getLexeme()) != null)
		{
			int errorcode = SemanticException.DUPLICATED_VARIABLE_EXCEPTION;
			catchError(new SemanticException(errorcode,prevToken2));
		}

	}

	private void verifyPerceptionAssign(Token identifier,
			Perception perception) {
		if(controller.getInner(identifier.getLexeme()) != null)
		{
			int errorcode = SemanticException.PERCEPTION_ASSIGN_INNER;
			catchError(new SemanticException(errorcode,identifier));
		}
		else if (perception.getVariable(identifier.getLexeme())== null)
		{
			int errorcode = SemanticException.PERCEPTION_ASSIGN_DECLARATION;
			catchError(new SemanticException(errorcode,identifier));
		}

	}

	private void verifyUnknownMethod(Token tk, CallParameters param, Method method) {

		// Si no es un action
		if(controller.getAction(tk.getLexeme(),param) == null)
		{
			// Si no es un perception
			if(controller.getPerception(tk.getLexeme(),param) == null )
			{
				// No existe
				int errorcode = SemanticException.UNKNOWN_METHOD_EXCEPTION;
				catchError(new SemanticException(errorcode,tk));
			}
			else	
			{
				// Si es un perception tiene que coincidir los parametros de llamada
				if(!controller.getPerception(tk.getLexeme(),param).match(tk.getLexeme(), param.getTypes()))
				{
					if(!controller.getPerception(tk.getLexeme(),param).matchInteger(tk.getLexeme(), param.getTypes()))
					{
						int errorcode = SemanticException.NUMPARAM_METHOD_EXCEPTION;
						catchError(new SemanticException(errorcode,tk));
					}
					
				}
			}
		}
		else	// Si es un action tiene que coincidir los parametros de llamada
		{
			if(!controller.getAction(tk.getLexeme(),param).match(tk.getLexeme(), param.getTypes()))
			{
				if(!controller.getAction(tk.getLexeme(),param).matchInteger(tk.getLexeme(), param.getTypes()))
				{
					int errorcode = SemanticException.NUMPARAM_METHOD_EXCEPTION;
					catchError(new SemanticException(errorcode,tk));
				}

			}
		}

	}


	private void verifyBooleanTypes(Token tk, Expression exp1, Expression exp2) {
		if(exp1.getType() != Type.BOOLEAN_TYPE || exp2.getType() != Type.BOOLEAN_TYPE) {
			int errorcode = SemanticException.TYPE_MISMATCH_EXCEPTION;
			catchError(new SemanticException(errorcode,tk));
		}
	}

	private void verifyBooleanType(Token tk, Expression expr) {
		if(expr.getType() != Type.BOOLEAN_TYPE) {
			int errorcode = SemanticException.TYPE_MISMATCH_EXCEPTION;
			catchError(new SemanticException(errorcode,tk));
		}
	}

	private void verifyRelationTypes(Token tk, int relop, Expression exp1, Expression exp2) {
		if(exp1.getType() != exp2.getType()) {
			
			if( ((exp1.getType()== Type.INT_TYPE) || (exp1.getType()==Type.DOUBLE_TYPE) ) && ( (exp2.getType() != Type.DOUBLE_TYPE) && (exp2.getType() !=Type.INT_TYPE) ) )
			{
				int errorcode = SemanticException.TYPE_MISMATCH_EXCEPTION;
				catchError(new SemanticException(errorcode,tk));
			}
			else if(exp1.getType()==Type.BOOLEAN_TYPE || exp2.getType()==Type.BOOLEAN_TYPE)
			{
				int errorcode = SemanticException.TYPE_MISMATCH_EXCEPTION;
				catchError(new SemanticException(errorcode,tk));
			}
			
		}
		else if(relop != BinaryExpression.EQ && relop != BinaryExpression.NEQ &&
		   (exp1.getType() == Type.BOOLEAN_TYPE || exp2.getType() == Type.BOOLEAN_TYPE)) {
		   	int errorcode = SemanticException.TYPE_MISMATCH_EXCEPTION;
			catchError(new SemanticException(errorcode,tk));
		}

	}

	private void verifyNumberType(Token tk, Expression expr) {
		if(expr.getType() != Type.INT_TYPE) {

			if(expr.getType() != Type.DOUBLE_TYPE)
			{
				int errorcode = SemanticException.TYPE_MISMATCH_EXCEPTION;
				catchError(new SemanticException(errorcode,tk));
			}

		}
	}

	private void verifyNumberTypes(Token tk, Expression exp1, Expression exp2) {
		if( ((exp1.getType() != Type.INT_TYPE) && (exp1.getType() != Type.DOUBLE_TYPE) ) && ((exp2.getType() != Type.DOUBLE_TYPE) && (exp2.getType() != Type.INT_TYPE) ) ) {

				int errorcode = SemanticException.TYPE_MISMATCH_EXCEPTION;
				catchError(new SemanticException(errorcode,tk));
		}
	}

	private void verifyTypeMismatch(Token tk, Expression exp) {
		// Si se ha asignado algo comprueba la concordancia de tipos
		if(exp != null)
		{
			if( tk.getKind() == INT && ( (exp.getType()!=INT) && (exp.getType()!=Type.INT_TYPE) ) )
			{
				int errorcode = SemanticException.PERCEPTION_DECLARATION_TYPE;
				catchError(new SemanticException(errorcode,tk));
			}
			else if( tk.getKind() == DOUBLE && ( (exp.getType()!=DOUBLE) && (exp.getType()!=Type.DOUBLE_TYPE) ) && ( (exp.getType()!=INT) && (exp.getType()!=Type.INT_TYPE) ) )
			{
				int errorcode = SemanticException.PERCEPTION_DECLARATION_TYPE;
				catchError(new SemanticException(errorcode,tk));
			}
			else if( (tk.getKind() == BOOLEAN) && ( (exp.getType()!=BOOLEAN) &&(exp.getType()!=Type.BOOLEAN_TYPE) ) )
			{
				int errorcode = SemanticException.PERCEPTION_DECLARATION_TYPE;
				catchError(new SemanticException(errorcode,tk));
			}

		}

	}
	

	private void verifyTypeMismatch(Token tk, Expression exp,
			Variable var) {
		// Si se ha asignado algo comprueba la concordancia de tipos
		if(exp != null)
		{
			if( var.getType()==Type.INT_TYPE && ( (exp.getType()!=INT) && (exp.getType()!=Type.INT_TYPE) ) )
			{
				int errorcode = SemanticException.PERCEPTION_DECLARATION_TYPE;
				catchError(new SemanticException(errorcode,tk));
			}
			else if( var.getType()==Type.DOUBLE_TYPE && ( (exp.getType()!=DOUBLE) && (exp.getType()!=Type.DOUBLE_TYPE) ) && ( (exp.getType()!=INT) && (exp.getType()!=Type.INT_TYPE) ) )
			{
				int errorcode = SemanticException.PERCEPTION_DECLARATION_TYPE;
				catchError(new SemanticException(errorcode,tk));
			}
			else if( (var.getType()==Type.BOOLEAN_TYPE) && ( (exp.getType()!=BOOLEAN) &&(exp.getType()!=Type.BOOLEAN_TYPE) ) )
			{
				int errorcode = SemanticException.PERCEPTION_DECLARATION_TYPE;
				catchError(new SemanticException(errorcode,tk));
			}

		}
		
	}


	/**
	 * Metodo de anaisis de un fichero
	 *
	 * @param filename Nombre del fichero a analizar
	 * @return Resultado del an�lisis sint�ctico
	 */
	public boolean parse(String filename) {
		try {
			this.lexer = new TorcsLexer(filename);
			this.prevToken = null;
			this.nextToken = lexer.getNextToken();
			this.controller = new Controller();
			 parseController();
			if(nextToken.getKind() == EOF) return true;
			else return false;
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return false;
		}
	}

	/**
	 * Punto de entrada para ejecutar pruebas del analizador sint�ctico
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0) return;

		TorcsParser parser = new TorcsParser(args[0]);
		if(parser.parse(args[0])) {
			System.out.println("Correcto");
			parser.EscribirFichero();
		} else {
			System.out.println("Incorrecto");
		}
		
		parser.CrearFicheroJava();
		
	}

	/**
	 * M�todo que consume un token de la cadena de entrada
	 * @param kind Tipo de token a consumir
	 * @throws SintaxException Si el tipo no coincide con el token
	 */
	private Token match(int kind) throws SintaxException {
		if(nextToken.getKind() == kind){
			prevToken = nextToken;
			nextToken = lexer.getNextToken();
			return prevToken;
		}
		else throw new SintaxException(nextToken,kind);
	}

	/**
	 * Analiza el s�mbolo <Controller>
	 * @throws SintaxException
	 */
	private void parseController() throws SintaxException {
		// expected: simbolos del conjunto de prediccion de todas las reglas que tienen de simbolo a la izquierda <controller>
		int [] expected = { INNER, PERCEPTION, ACTION, RULES};
				// Para cada simbolo hacer un case, los simbolos del conjunto de prediccion de cada regla actuan segun sea la regla
				// Controller -> Controller' Rules
		switch(nextToken.getKind()) {
			case INNER:
			case PERCEPTION:
			case ACTION:
			case RULES:
				parseControllerPrima();
				tryparseRules();
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
	}


	/**
	 * Analiza el s�mbolo <ControllerPrima>
	 * @throws SintaxException
	 */
	private void parseControllerPrima() throws SintaxException {
		int [] expected = { INNER, PERCEPTION, ACTION, RULES};

		switch(nextToken.getKind()) {
			case INNER:
			case PERCEPTION:
			case ACTION:
				parseDeclaration();
				parseControllerPrima();
				break;
			case RULES:
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
	}

	/**
	 * Analiza el s�mbolo <Declaration>
	 * @throws SintaxException
	 */
	private void parseDeclaration() throws SintaxException {
		int [] expected = { INNER, PERCEPTION, ACTION};

		switch(nextToken.getKind()) {
			case INNER:
				tryInnerDecl();
				break;
			case PERCEPTION:
				tryPerceptionDecl();
				break;
			case ACTION:
				tryActionDecl();
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
	}






	private void tryInnerDecl(){
		int [] lsync = { SEMICOLON };
		int [] rsync = { INNER,PERCEPTION,ACTION,RULES};
		try { parseInnerDecl(); }
		catch(Exception ex){ catchError(ex);skipTo(lsync,rsync);}
	}

	/**
	 * Analiza el s�mbolo <InnerDecl>
	 * @throws SintaxException
	 */
	private void parseInnerDecl() throws SintaxException {
		int [] expected = { INNER};

		switch(nextToken.getKind()) {
			case INNER:
				match(INNER);
				Token type = parseType();
				match(IDENTIFIER);
				Token id = prevToken;
				verifyDuplicatedVariableInner(id);

				match(ASSIGN);
				Token typeLiteral = parseLiteral(null);

				match(SEMICOLON);
				int tipo = getTipo(type);
				Expression literal;
				if(getTipo(typeLiteral)== Type.DOUBLE_TYPE) literal = new DoubleLiteralExpression(typeLiteral.getLexeme());
				else if (getTipo(typeLiteral)== Type.INT_TYPE) literal = new IntegerLiteralExpression(typeLiteral.getLexeme());
				else if (getTipo(typeLiteral)== Type.BOOLEAN_TYPE) literal = new BooleanLiteralExpression(typeLiteral.getLexeme());
				else literal = null;
				if(literal!=null)verifyTypeMismatch(type,literal);
				Inner inner = new Inner(id.getLexeme(),tipo,literal);
				controller.addInner(inner);

			break;
			default:
				throw new SintaxException(nextToken, expected);
		}

	}



	private int getTipo(Token type) {
		if(type.getKind()==40)
		{
			if(type.getLexeme().equals("accelerate")) return Type.DOUBLE_TYPE;
			else if (type.getLexeme().equals("brake")) return Type.DOUBLE_TYPE;
			else if (type.getLexeme().equals("steering")) return Type.DOUBLE_TYPE;
			else return Type.INT_TYPE;
		}
		else if(type.getKind()==INT || type.getKind()==INTEGER_LITERAL) return Type.INT_TYPE;
		else if(type.getKind()== DOUBLE || type.getKind()== DOUBLE_LITERAL) return Type.DOUBLE_TYPE;
		else if(type.getKind() == BOOLEAN|| type.getKind()== TRUE || type.getKind()==FALSE) return Type.BOOLEAN_TYPE;
		else return Type.MISMATCH_TYPE;
	}

	/**
	 * Analiza el s�mbolo <Type>
	 * @throws SintaxException
	 */
	private Token parseType() throws SintaxException {
		int [] expected = { INT, DOUBLE, BOOLEAN};
		switch(nextToken.getKind()) {
		case INT:
			match(INT);
			return prevToken;

		case DOUBLE:
			match(DOUBLE);
			return prevToken;

		case BOOLEAN:
			match(BOOLEAN);
			return prevToken;

		default:
			throw new SintaxException(nextToken, expected);
		}
	}


	private void tryPerceptionDecl() {
		int [] lsync = { };
		int [] rsync = { LBRACE, LPAREN,PERCEPTION,ACTION,RULES};
		try { parsePerceptionDecl(); }
		catch(Exception ex){ catchError(ex);skipTo(lsync,rsync);}

	}

	/**
	 * Analiza el s�mbolo <PerceptionDecl>
	 * @throws SintaxException
	 */
	
	private void parsePerceptionDecl() throws SintaxException {
		int [] expected = {PERCEPTION};

		switch(nextToken.getKind()) {
		case PERCEPTION:
			match(PERCEPTION);
			//////// IDENTIFICADOR PERCEPTION //////////////
			match(IDENTIFIER);
			Token tk = prevToken;
			Perception perception = new Perception(Type.BOOLEAN_TYPE,prevToken.getLexeme());
			////////////////////////////////////////////////

			///////////ARGUMENTOS PERCEPTION //////////////
			tryparseArgumentDecl(perception);
			////////////////////////////////////////////////
			verifyDuplicatedPerception(tk,perception);
			///////////CUERPO PERCEPTION //////////////
			tryparsePerceptionBody(perception);
			////////////////////////////////////////////////

			controller.addPerception(perception);
			break;
		default:
			throw new SintaxException(nextToken, expected);
		}
		
	}



	private void tryActionDecl() {
		int [] lsync = { };
		int [] rsync = { LBRACE, LPAREN,PERCEPTION,ACTION,RULES};
		try { parseActionDecl(); }
		catch(Exception ex){ catchError(ex);skipTo(lsync,rsync);}

	}

	/**
	 * Analiza el s�mbolo <ActionDecl>
	 * @throws SintaxException
	 */
	private void parseActionDecl() throws SintaxException {
		int [] expected = { ACTION};

		switch(nextToken.getKind()) {
			case ACTION:
				match(ACTION);

				//////// IDENTIFICADOR ACTION //////////////
				match(IDENTIFIER);
				Token tk = prevToken;
				Action action = new Action(Type.VOID_TYPE,prevToken.getLexeme());
				////////////////////////////////////////////////

				///////////ARGUMENTOS ACTION //////////////
				tryparseArgumentDecl(action);
				////////////////////////////////////////////////
				verifyDuplicatedAction(tk,action);
				///////////CUERPO ACTION //////////////
				tryparseActionBody(action);
				////////////////////////////////////////////////

				controller.addAction(action);
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
	}



	private void tryparseArgumentDecl(Method method) {
		int [] lsync = { };
		int [] rsync = { LBRACE, LPAREN,PERCEPTION,ACTION,RULES};
		try { parseArgumentDecl(method); }
		catch(Exception ex){ catchError(ex);skipTo(lsync,rsync);}

	}

	/**
	 * Analiza el s�mbolo <ArgumentDecl>
	 * @param method
	 * @throws SintaxException
	 */
	private void parseArgumentDecl(Method method) throws SintaxException {
		int [] expected = { LPAREN};

		switch(nextToken.getKind()) {
			case LPAREN:
				match(LPAREN);
				parseArgumentDeclPrima(method);
				match(RPAREN);
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ArgumentDeclPrima>
	 * @param method
	 * @throws SintaxException
	 */
	private void parseArgumentDeclPrima(Method method) throws SintaxException {
		int [] expected = { INT, DOUBLE, BOOLEAN, RPAREN};

		switch(nextToken.getKind()) {
			case INT:
			case DOUBLE:
			case BOOLEAN:
				parseArgument(method);
				parseArgumentDeclPrimaPrima(method);
				break;
			case RPAREN:
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ArgumentDeclPrimaPrima>
	 * @param method
	 * @throws SintaxException
	 */
	private void parseArgumentDeclPrimaPrima(Method method) throws SintaxException {
		int [] expected = { COMMA, RPAREN};

		switch(nextToken.getKind()) {
			case COMMA:
				match(COMMA);
				parseArgument(method);
				parseArgumentDeclPrimaPrima(method);
				break;
			case RPAREN:
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
	}

	/**
	 * Analiza el s�mbolo <Argument>
	 * @param method
	 * @throws SintaxException
	 */
	private void parseArgument(Method method) throws SintaxException {
		int [] expected = { INT, DOUBLE, BOOLEAN};

		switch(nextToken.getKind()) {
			case INT:
			case DOUBLE:
			case BOOLEAN:
				Token type = parseType();
				match(IDENTIFIER);
				verifyDuplicatedVariableArgument(prevToken,method);
				int tipo = getTipo(type);
				method.addArgument(new Variable(tipo,prevToken.getLexeme()));
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}

	}

	private void tryparsePerceptionBody(Perception perception) {
		int [] lsync = { };
		int [] rsync = { LBRACE,PERCEPTION,ACTION,RULES};
		try { parsePerceptionBody(perception); }
		catch(Exception ex){ catchError(ex);skipTo(lsync,rsync);}


	}


	/**
	 * Analiza el s�mbolo <PerceptionBody>
	 * @param perception
	 * @throws SintaxException
	 */
	private void parsePerceptionBody(Perception perception) throws SintaxException {
		int [] expected = { LBRACE};

		switch(nextToken.getKind()) {
			case LBRACE:
				match(LBRACE);
				BlockStatement block = new BlockStatement();
				parsePerceptionBodyPrima(perception,block);
				match(RBRACE);
				Token tk = prevToken;
				verifyUnfinishedMethod(tk,block,perception);
				perception.setPerceptionBody(block);
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
	}

	private void verifyUnfinishedMethod(Token tk, BlockStatement body,
			Method method) {
		if(method.getType() != Type.VOID_TYPE && !body.returns() ){
			int errorcode = SemanticException.UNFINISHED_METHOD;
			catchError(new SemanticException(errorcode,tk));
		}

	}

	private void verifyUnreachableCode(Statement stm, BlockStatement block) {
		if(stm != null && block.returns() ) {
			int errorcode = SemanticException.UNREACHABLE_CODE;
			catchError(new SemanticException(errorcode,prevToken));
		}
		
		
	}



	/**
	 * Analiza el s�mbolo <PerceptionBodyPrima>
	 * @param perception
	 * @throws SintaxException
	 */
	private void parsePerceptionBodyPrima(Perception perception,BlockStatement block) throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,IF,WHILE,TRUE,FALSE,LBRACE, RBRACE};

		switch(nextToken.getKind()) {
			case INT:
			case DOUBLE:
			case BOOLEAN:
			case IDENTIFIER:
			case IF:
			case WHILE:
			case TRUE:
			case FALSE:
			case LBRACE:
				Statement stm = tryPcptStatement(perception);
				verifyUnreachableCode(stm,block);
				block.addStatement(stm);
				parsePerceptionBodyPrima(perception,block);
				break;
			case RBRACE:
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
	}

	private void tryparseActionBody(Action action) {
		int [] lsync = { };
		int [] rsync = { LBRACE,PERCEPTION,ACTION,RULES};
		try { parseActionBody(action); }
		catch(Exception ex){ catchError(ex);skipTo(lsync,rsync);}


	}

	/**
	 * Analiza el s�mbolo <ActionBody>
	 * @param action
	 * @throws SintaxException
	 */
	private void parseActionBody(Action action) throws SintaxException {
		int [] expected = { LBRACE};

		switch(nextToken.getKind()) {
		case LBRACE:
			match(LBRACE);
			BlockStatement block = new BlockStatement();
			parseActionBodyPrima(action,block);
			match(RBRACE);
			action.setActionBody(block);
			break;
		default:
			throw new SintaxException(nextToken, expected);
	}


	}

	/**
	 * Analiza el s�mbolo <ActionBodyPrima>
	 * @param action
	 * @param block
	 * @throws SintaxException
	 */
	private void parseActionBodyPrima(Action action, BlockStatement block) throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,GEAR,ACCELERATE, BRAKE, STEERING,IF, WHILE,LBRACE, RBRACE};

		switch(nextToken.getKind()) {
		case INT:
		case DOUBLE:
		case BOOLEAN:
		case IDENTIFIER:
		case GEAR:
		case ACCELERATE:
		case BRAKE:
		case STEERING:
		case IF:
		case WHILE:
		case LBRACE:

			Statement stm = tryparseActStatement(action);
			block.addStatement(stm);
			parseActionBodyPrima(action,block);
			break;
		case RBRACE:
			break;
		default:
			throw new SintaxException(nextToken, expected);
	}

	}

	private void tryparseRules() {
		int[] lsync = { SEMICOLON, LBRACE };
		int[] rsync = { };
		try {  parseRules();  }
		catch(Exception ex) { catchError(ex); skipTo(lsync,rsync); }
	}
	
	/**
	 * Analiza el s�mbolo <Rules>
	 * @throws SintaxException
	 */
	private void parseRules() throws SintaxException {
		int [] expected = { RULES};

		switch(nextToken.getKind()) {
			case RULES:
				match(RULES);
				match(LBRACE);
				Rules rules = new Rules();
				parseRulesPrima(rules);
				controller.addRules(rules);
				match(RBRACE);
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <RulesPrima>
	 * @param rules 
	 * @throws SintaxException
	 */
	private void parseRulesPrima(Rules rules) throws SintaxException {
		int [] expected = { NOT,MINUS,PLUS,INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN,RBRACE};

		switch(nextToken.getKind()) {
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case DOUBLE_LITERAL:
			case TRUE:
			case FALSE:
			case GEAR:
			case SPEED:
			case ANGLE:
			case POSITION:
			case RPM:
			case SENSOR0:
			case SENSOR1:
			case SENSOR2:
			case SENSOR3:
			case SENSOR4:
			case SENSOR5:
			case SENSOR6:
			case SENSOR7:
			case SENSOR8:
			case SENSOR9:
			case SENSOR10:
			case SENSOR11:
			case SENSOR12:
			case SENSOR13:
			case SENSOR14:
			case SENSOR15:
			case SENSOR16:
			case SENSOR17:
			case SENSOR18:
			case IDENTIFIER:
			case LPAREN:
				Rule rule = tryparseRule();
				rules.addRule(rule);
				parseRulesPrima(rules);
				break;
			case RBRACE:
				break;

			default:
				throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <InputVar>
	 * @param method
	 * @throws SintaxException
	 */
	private Expression parseInputVar(Method method) throws SintaxException {
		int [] expected = {GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18};
		Expression exp = null;
		switch(nextToken.getKind()) {
			case GEAR:
				match(GEAR);
				exp = new VariableExpression(new Variable(Type.INT_TYPE, "gear"));
				break;
			case SPEED:
				match(SPEED);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "speed"));
				break;
			case ANGLE:
				match(ANGLE);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "angle"));
				break;
			case POSITION:
				match(POSITION);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "position"));
				break;
			case RPM:
				match(RPM);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "rpm"));
				break;
			case SENSOR0:
				match(SENSOR0);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor0"));
				break;
			case SENSOR1:
				match(SENSOR1);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor1"));
				break;
			case SENSOR2:
				match(SENSOR2);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor2"));
				break;
			case SENSOR3:
				match(SENSOR3);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor3"));
				break;
			case SENSOR4:
				match(SENSOR4);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor4"));
				break;
			case SENSOR5:
				match(SENSOR5);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor5"));
				break;
			case SENSOR6:
				match(SENSOR6);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor6"));
				break;
			case SENSOR7:
				match(SENSOR7);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor7"));
				break;
			case SENSOR8:
				match(SENSOR8);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor8"));
				break;
			case SENSOR9:
				match(SENSOR9);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor9"));
				break;
			case SENSOR10:
				match(SENSOR10);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor10"));
				break;
			case SENSOR11:
				match(SENSOR11);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor11"));
				break;
			case SENSOR12:
				match(SENSOR12);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor12"));
				break;
			case SENSOR13:
				match(SENSOR13);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor13"));
				break;
			case SENSOR14:
				match(SENSOR14);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor14"));
				break;
			case SENSOR15:
				match(SENSOR15);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor15"));
				break;
			case SENSOR16:
				match(SENSOR16);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor16"));
				break;
			case SENSOR17:
				match(SENSOR17);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor17"));
				break;
			case SENSOR18:
				match(SENSOR18);
				exp = new VariableExpression(new Variable(Type.DOUBLE_TYPE, "sensor18"));
				break;

			default:
				throw new SintaxException(nextToken, expected);
	}

		return exp;

	}

	/**
	 * Analiza el s�mbolo <OutputVar>
	 * @return
	 * @throws SintaxException
	 */
	private Token parseOutputVar() throws SintaxException {
		int [] expected = { GEAR,ACCELERATE, BRAKE,STEERING};

		switch(nextToken.getKind()) {
		case GEAR:
			match(GEAR);
			break;
		case ACCELERATE:
			match(ACCELERATE);
			break;
		case BRAKE:
			match(BRAKE);
			break;
		case STEERING:
			match(STEERING);
			break;

		default:
			throw new SintaxException(nextToken, expected);
		}
		return prevToken;

	}

	private Statement tryPcptStatement(Perception method) {
		int[] lsync = { SEMICOLON, LBRACE };
		int[] rsync = { };
		try {  return parsePcptStatement(method);  }
		catch(Exception ex) { catchError(ex); skipTo(lsync,rsync); return null;}
	}

	/**
	 * Analiza el s�mbolo <PcptStatement>
	 * @param perception
	 * @return
	 * @throws SintaxException
	 */
	private Statement parsePcptStatement(Perception perception) throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,IF,WHILE,TRUE,FALSE,LBRACE};

		switch(nextToken.getKind()) {
		case INT:
		case DOUBLE:
		case BOOLEAN:
			return parsePcptDecl(perception);
		case IDENTIFIER:
			return parsePcptAssignStm(perception);
		case IF:
			return parsePcptIfStm(perception);
		case WHILE:
			return parsePcptWhileStm(perception);
		case TRUE:
			return parsePcptTrueStm(perception);
		case FALSE:
			return parsePcptFalseStm(perception);
		case LBRACE:
			return parsePcptBlockStm(perception);

		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <PcptDecl>
	 * @param perception
	 * @return
	 * @throws SintaxException
	 */
	private Statement parsePcptDecl(Perception perception) throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN};

		switch(nextToken.getKind()) {
			case INT:
			case DOUBLE:
			case BOOLEAN:
				////////////////// IDENTIFICADOR DE LA DECLARACION ////////////////////
				Token type = parseType();
				match(IDENTIFIER);
				verifyPcptDecl(prevToken,perception);
				Token identifier = prevToken;
				/////////////////////////////////////////////////////////////////////

				//////////////////EXPRESION A ASIGNAR ///////////////////////////////
				Expression exp = parsePcptDeclPrima(perception);
				match(SEMICOLON);
				/////////////////////////////////////////////////////////////////////
				int tipo = getTipo(type);
				Variable var = new Variable(tipo,identifier.getLexeme() );
				perception.addLocalVariable(var);

				verifyTypeMismatch(type,exp);
				return new AssignStatement(var, exp,true);
			default:
				throw new SintaxException(nextToken, expected);
		}

	}




	/**
	 * Analiza el s�mbolo <PcptDeclPrima>
	 * @param perception
	 * @throws SintaxException
	 */
	private Expression parsePcptDeclPrima(Perception perception) throws SintaxException {
		int [] expected = { ASSIGN,SEMICOLON};

		switch(nextToken.getKind()) {
		case ASSIGN:
			match(ASSIGN);
			return tryparseExpr(perception);
		case SEMICOLON:
			return null;
		default:
			throw new SintaxException(nextToken, expected);
		}

	}



	/**
	 * Analiza el s�mbolo <PcptIfStm>
	 * @param perception
	 * @return
	 * @throws SintaxException
	 */
	private Statement parsePcptIfStm(Perception perception) throws SintaxException {
		int [] expected = { IF};

		switch(nextToken.getKind()) {
		case IF:
			match(IF);
			match(LPAREN);
			Expression condition = tryparseExpr(perception);
			match(RPAREN);
			Token id = prevToken;
			verifyCondition(condition,id);
			Statement thenInst = tryPcptStatement(perception);
			Statement elseInst = parsePcptIfStmPrima(perception);
			
			return new IfStatement(condition, thenInst, elseInst);

		default:
			throw new SintaxException(nextToken, expected);
		}

	}



	/**
	 * Analiza el s�mbolo <PcptIfStmPrima>
	 * @param perception
	 * @return
	 * @throws SintaxException
	 */
	private Statement parsePcptIfStmPrima(Perception perception) throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,IF,WHILE,TRUE,FALSE,LBRACE,RBRACE,ELSE};

		switch(nextToken.getKind()) {
		case ELSE:
			match(ELSE);
			return tryPcptStatement(perception);

		case INT:
		case DOUBLE:
		case BOOLEAN:
		case IDENTIFIER:
		case IF:
		case WHILE:
		case TRUE:
		case FALSE:
		case LBRACE:
		case RBRACE:
			return null;
		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <PcptWhileStm>
	 * @param perception
	 * @return
	 * @throws SintaxException
	 */
	private Statement parsePcptWhileStm(Perception perception) throws SintaxException {
		int [] expected = { WHILE};

		switch(nextToken.getKind()) {
		case WHILE:
			match(WHILE);
			match(LPAREN);
			Expression condition = tryparseExpr(perception);
			match(RPAREN);
			Token id = prevToken;
			verifyCondition(condition,id);
			Statement instruction = tryPcptStatement(perception);
			return new WhileStatement(condition, instruction);

		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <PcptAssignStm>
	 * @param perception
	 * @return
	 * @throws SintaxException
	 */
	private Statement parsePcptAssignStm(Perception perception) throws SintaxException {
		int [] expected = { IDENTIFIER};

		switch(nextToken.getKind()) {
		case IDENTIFIER:
			match(IDENTIFIER);
			Token identifier = prevToken;
			verifyPerceptionAssign(identifier,perception);
			match(ASSIGN);

			Expression exp = tryparseExpr(perception);
			
			match(SEMICOLON);

			Variable var = perception.getVariable(identifier.getLexeme());
			if(var == null && controller.getInner(identifier.getLexeme())!=null) var = new Variable(controller.getInner(identifier.getLexeme()).getType(),controller.getInner(identifier.getLexeme()).getName());
			
			if(var!=null) verifyTypeMismatch(identifier, exp,var);
			return new AssignStatement(var, exp,false);

		default:
			throw new SintaxException(nextToken, expected);
		}

	}






	/**
	 * Analiza el s�mbolo <PcptTrueStm>
	 * @param perception
	 * @return
	 * @throws SintaxException
	 */
	private Statement parsePcptTrueStm(Perception perception) throws SintaxException {
		int [] expected = { TRUE};

		switch(nextToken.getKind()) {
		case TRUE:
			match(TRUE);
			match(SEMICOLON);
			return new ReturnStatement(new BooleanLiteralExpression(true));

		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <PcptFalseStm>
	 * @param perception
	 * @return
	 * @throws SintaxException
	 */
	private Statement parsePcptFalseStm(Perception perception) throws SintaxException {
		int [] expected = { FALSE};

		switch(nextToken.getKind()) {
		case FALSE:
			match(FALSE);
			match(SEMICOLON);
			return new ReturnStatement(new BooleanLiteralExpression(false));

		default:
			throw new SintaxException(nextToken, expected);
		}

	}


	/**
	 * Analiza el s�mbolo <PcptBlockStm>
	 * @param perception
	 * @return
	 * @throws SintaxException
	 */
	private Statement parsePcptBlockStm(Perception perception) throws SintaxException {
		int [] expected = { LBRACE};

		switch(nextToken.getKind()) {
		case LBRACE:
			perception.createContext();
			BlockStatement block = new BlockStatement();
			match(LBRACE);
			parsePcptBlockStmPrima(perception,block);
			match(RBRACE);
			perception.deleteContext();
			return block;

		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <PcptBlockStmPrima>
	 * @param perception
	 * @param block
	 * @return
	 * @throws SintaxException
	 */
	private void parsePcptBlockStmPrima(Perception perception, BlockStatement block) throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,IF,WHILE,TRUE,FALSE,LBRACE,RBRACE};

		switch(nextToken.getKind()) {
		case INT:
		case DOUBLE:
		case BOOLEAN:
		case IDENTIFIER:
		case IF:
		case WHILE:
		case TRUE:
		case FALSE:
		case LBRACE:
			Statement stm = tryPcptStatement(perception);
			block.addStatement(stm);
			parsePcptBlockStmPrima(perception,block);
			break;
		case RBRACE:
			break;
		default:
			throw new SintaxException(nextToken, expected);
		}
	}



	private Statement tryparseActStatement(Action method) {
		int[] lsync = { SEMICOLON, LBRACE };
		int[] rsync = { };
		try {  return parseActStatement(method);  }
		catch(Exception ex) { catchError(ex); skipTo(lsync,rsync); return null;}
	}

	/**
	 * Analiza el s�mbolo <ActStatement>
	 * @param action
	 * @throws SintaxException
	 */
	private Statement parseActStatement(Action action) throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,GEAR,ACCELERATE, BRAKE, STEERING,IF, WHILE,LBRACE};

		switch(nextToken.getKind()) {
			case INT:
			case DOUBLE:
			case BOOLEAN:
				return parseActDecl(action);
			case IDENTIFIER:
			case GEAR:
			case ACCELERATE:
			case BRAKE:
			case STEERING:
				return parseActAssignStm(action);
			case IF:
				return parseActIfStm(action);
			case WHILE:
				return parseActWhileStm(action);
			case LBRACE:
				return parseActBlockStm(action);
			default:
				throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <ActDecl>
	 * @param action
	 * @throws SintaxException
	 */
	private Statement parseActDecl(Action action) throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN};

		switch(nextToken.getKind()) {
		case INT:
		case DOUBLE:
		case BOOLEAN:
			Token type = parseType();
			match(IDENTIFIER);
			verifyActDecl(prevToken,action);
			Token identifier = prevToken;

			Expression exp = parseActDeclPrima(action);
			match(SEMICOLON);

			int tipo = getTipo(type);
			Variable var = new Variable(tipo,identifier.getLexeme());
			action.addLocalVariable(var);

			verifyTypeMismatch(type,exp);
			return new AssignStatement(var,exp,true);
		default:
			throw new SintaxException(nextToken, expected);
		}
	}

	private void verifyActDecl(Token prevToken2, Action action) {
		if(controller.getInner(prevToken2.getLexeme())!= null)
		{
			int errorcode = SemanticException.PERCEPTION_DECLARATION_INNER;
			catchError(new SemanticException(errorcode,prevToken2));
		}
		else if (action.existsInContext(prevToken2.getLexeme()))
		{
			int errorcode = SemanticException.DUPLICATED_VARIABLE_EXCEPTION;
			catchError(new SemanticException(errorcode,prevToken2));
		}
		else if (action.getVariable(prevToken2.getLexeme()) != null)
		{
			int errorcode = SemanticException.DUPLICATED_VARIABLE_EXCEPTION;
			catchError(new SemanticException(errorcode,prevToken2));
		}
	}

	/**
	 * Analiza el s�mbolo <ActDeclPrima>
	 * @param action
	 * @return
	 * @throws SintaxException
	 */
	private Expression parseActDeclPrima(Action action) throws SintaxException {
		int [] expected = { ASSIGN, SEMICOLON};

		switch(nextToken.getKind()) {
			case ASSIGN:
				match(ASSIGN);
				return tryparseExpr(action);
			case SEMICOLON:
				return null;
			default:
				throw new SintaxException(nextToken, expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ActIfStm>
	 * @param action
	 * @throws SintaxException
	 */
	private Statement parseActIfStm(Action action) throws SintaxException {
		int [] expected = { IF};

		switch(nextToken.getKind()) {
		case IF:
			match(IF);
			match(LPAREN);
			Expression condition = tryparseExpr(action);
			match(RPAREN);
			Token id = prevToken;
			verifyCondition(condition,id);
			Statement thenInst = tryparseActStatement(action);
			Statement elseInst = parseActIfStmPrima(action);

			return new IfStatement(condition, thenInst, elseInst);

		default:
			throw new SintaxException(nextToken, expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ActIfStmPrima>
	 * @param action
	 * @throws SintaxException
	 */
	private Statement parseActIfStmPrima(Action action) throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,GEAR,ACCELERATE, BRAKE, STEERING,IF, WHILE,LBRACE, RBRACE,ELSE};
		switch(nextToken.getKind()) {
		case INT:
		case DOUBLE:
		case BOOLEAN:
		case IDENTIFIER:
		case GEAR:
		case ACCELERATE:
		case BRAKE:
		case STEERING:
		case IF:
		case WHILE:
		case LBRACE:
		case RBRACE:
			return null;
		case ELSE:
			match(ELSE);
			return tryparseActStatement(action);

		default:
			throw new SintaxException(nextToken, expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ActWhileStm>
	 * @param action
	 * @return
	 * @throws SintaxException
	 */
	private Statement parseActWhileStm(Action action) throws SintaxException {
		int [] expected = { WHILE};

		switch(nextToken.getKind()) {
		case WHILE:
			match(WHILE);
			match(LPAREN);
			Expression condition = tryparseExpr(action);
			match(RPAREN);
			Token id = prevToken;
			verifyCondition(condition,id);
			Statement instruccion = tryparseActStatement(action);
			return new WhileStatement(condition, instruccion);
		default:
			throw new SintaxException(nextToken, expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ActAssignStm>
	 * @param action
	 * @return
	 * @throws SintaxException
	 */
	private Statement parseActAssignStm(Action action) throws SintaxException {
		int [] expected = {IDENTIFIER,GEAR,ACCELERATE, BRAKE, STEERING};

		switch(nextToken.getKind()) {
		case IDENTIFIER:
		case GEAR:
		case ACCELERATE:
		case BRAKE:
		case STEERING:
			Token id = parseActAssignStmPrima(action);
			verifyActAssign(id,action);
			match(ASSIGN);

			Expression exp = tryparseExpr(action);
			match(SEMICOLON);

			Variable var= null;

			if(action.getVariable(id.getLexeme())!=null) var = action.getVariable(id.getLexeme());
			if(var == null && controller.getInner(id.getLexeme())!=null) var = new Variable(controller.getInner(id.getLexeme()).getType(),controller.getInner(id.getLexeme()).getName());
			if(var==null) var = new Variable(getTipoOutput(id.getLexeme()),id.getLexeme());
				
			if(var.getType()!= Type.MISMATCH_TYPE) verifyTypeMismatch(id, exp,var);
			return new AssignStatement(var, exp,false);
		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	private int getTipoOutput(String lexeme) {

		if(lexeme.equals("gear")) return Type.INT_TYPE;
		else if(lexeme.equals("accelerate")) return Type.DOUBLE_TYPE;
		else if(lexeme.equals("brake")) return Type.DOUBLE_TYPE;
		else if(lexeme.equals("steering")) return Type.DOUBLE_TYPE;
		else return Type.MISMATCH_TYPE;
	}

	private void verifyActAssign(Token id, Action action) {
		if (action.getVariable(id.getLexeme())== null && (id.getKind()==IDENTIFIER) && (controller.getInner(id.getLexeme()) == null) )
		{
			int errorcode = SemanticException.ACTION_ASSIGN_DECLARATION;
			catchError(new SemanticException(errorcode,id));
		}

	}

	/**
	 * Analiza el s�mbolo <ActAssignStmPrima>
	 * @param action
	 * @throws SintaxException
	 */
	private Token parseActAssignStmPrima(Action action) throws SintaxException {
		int [] expected = {IDENTIFIER,GEAR,ACCELERATE, BRAKE, STEERING};

		switch(nextToken.getKind()) {
		case IDENTIFIER:
			match(IDENTIFIER);
			return prevToken;
		case GEAR:
		case ACCELERATE:
		case BRAKE:
		case STEERING:
			return parseOutputVar();

		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <ActBlockStm>
	 * @param action
	 * @return 
	 * @throws SintaxException
	 */
	private Statement parseActBlockStm(Action action) throws SintaxException {
		int [] expected = { LBRACE};

		switch(nextToken.getKind()) {
		case LBRACE:
			action.createContext();
			BlockStatement block = new BlockStatement();
			match(LBRACE);
			parseActBlockStmPrima(action,block);
			match(RBRACE);
			action.deleteContext();
			return block;
			
		default:
			throw new SintaxException(nextToken, expected);
		}


	}

	/**
	 * Analiza el s�mbolo <ActBlockStmPrima>
	 * @param action
	 * @param block 
	 * @throws SintaxException
	 */
	private void parseActBlockStmPrima(Action action, BlockStatement block) throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,GEAR,ACCELERATE, BRAKE, STEERING,IF, WHILE,LBRACE, RBRACE};

		switch(nextToken.getKind()) {
		case INT:
		case DOUBLE:
		case BOOLEAN:
		case IDENTIFIER:
		case GEAR:
		case ACCELERATE:
		case BRAKE:
		case STEERING:
		case IF:
		case WHILE:
		case LBRACE:
			Statement stm = parseActStatement(action);
			block.addStatement(stm);
			parseActBlockStmPrima(action,block);
			break;
		case RBRACE:
			break;
		default:
			throw new SintaxException(nextToken, expected);
		}
	}

	
	private Rule tryparseRule() {
		int[] lsync = { SEMICOLON };
		int[] rsync = { };
		try {  return parseRule();  }
		catch(Exception ex) { catchError(ex); skipTo(lsync,rsync); return null;}
	}
	
	/**
	 * Analiza el s�mbolo <Rule>
	 * @return 
	 * @throws SintaxException
	 */
	private Rule parseRule() throws SintaxException {
		int [] expected = { NOT,MINUS,PLUS,INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN};
		
		switch(nextToken.getKind()) {
		case NOT:
		case MINUS:
		case PLUS:
		case INTEGER_LITERAL:
		case DOUBLE_LITERAL:
		case TRUE:
		case FALSE:
		case GEAR:
		case SPEED:
		case ANGLE:
		case POSITION:
		case RPM:
		case SENSOR0:
		case SENSOR1:
		case SENSOR2:
		case SENSOR3:
		case SENSOR4:
		case SENSOR5:
		case SENSOR6:
		case SENSOR7:
		case SENSOR8:
		case SENSOR9:
		case SENSOR10:
		case SENSOR11:
		case SENSOR12:
		case SENSOR13:
		case SENSOR14:
		case SENSOR15:
		case SENSOR16:
		case SENSOR17:
		case SENSOR18:
		case IDENTIFIER:
		case LPAREN:
			Expression exp = tryparseExpr(new Method(Type.MISMATCH_TYPE, "rule"));
			Rule rule = new Rule(exp);
			match(FLECHA);
			Token id = prevToken;
			verifyCondition(exp,id);
			tryparseActionCall(rule);
			parseRulePrima(rule);
			match(SEMICOLON);
			return rule;
		default:
			throw new SintaxException(nextToken, expected);
}

	}

	/**
	 * Analiza el s�mbolo <RulePrima>
	 * @param actionCall 
	 * @throws SintaxException
	 */
	private void parseRulePrima(Rule rule) throws SintaxException {
		int [] expected = { COMMA, SEMICOLON};

		switch(nextToken.getKind()) {
		case COMMA:
			match(COMMA);
			tryparseActionCall(rule);
			parseRulePrima(rule);
			break;
		case SEMICOLON:
			break;
		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	private void tryparseActionCall(Rule rule) {
		int[] lsync = { SEMICOLON,COMMA };
		int[] rsync = { };
		try {  parseActionCall(rule);  }
		catch(Exception ex) { catchError(ex); skipTo(lsync,rsync); }
	}
	
	/**
	 * Analiza el s�mbolo <ActionCall>
	 * @throws SintaxException
	 */
	private void parseActionCall(Rule rule) throws SintaxException {
		int [] expected = { IDENTIFIER};

		switch(nextToken.getKind()) {
		case IDENTIFIER:
			match(IDENTIFIER);
			Token id = prevToken;
			Action method = new Action(Type.MISMATCH_TYPE, id.getLexeme());
			
			
			match(LPAREN);
			CallParameters call = new CallParameters();
			parseActionCallPrima(method,call);
			match(RPAREN);
			verifyUnknownMethod(id, call, method);
			
			if(controller.getAction(id.getLexeme(),call)!= null ) method = controller.getAction(id.getLexeme(),call);
			else method = new Action(Type.MISMATCH_TYPE, id.getLexeme());
			
			rule.addCallExpression(new CallExpression(method, call));
			break;
		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <ActionCallPrima>
	 * @param method
	 * @param call 
	 * @return 
	 * @throws SintaxException
	 */
	private void parseActionCallPrima(Method method, CallParameters call) throws SintaxException {
		int [] expected = { NOT,MINUS,PLUS,INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN,RPAREN};

		switch(nextToken.getKind()) {
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case DOUBLE_LITERAL:
			case TRUE:
			case FALSE:
			case GEAR:
			case SPEED:
			case ANGLE:
			case POSITION:
			case RPM:
			case SENSOR0:
			case SENSOR1:
			case SENSOR2:
			case SENSOR3:
			case SENSOR4:
			case SENSOR5:
			case SENSOR6:
			case SENSOR7:
			case SENSOR8:
			case SENSOR9:
			case SENSOR10:
			case SENSOR11:
			case SENSOR12:
			case SENSOR13:
			case SENSOR14:
			case SENSOR15:
			case SENSOR16:
			case SENSOR17:
			case SENSOR18:
			case IDENTIFIER:
			case LPAREN:
				Expression exp = tryparseExpr(method);
				call.addParameter(exp);
				parseActionCallPrimaPrima(method,call);
				break;
			case RPAREN:
				break;
		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <ActionCallPrimaPrima>
	 * @param method
	 * @param call 
	 * @throws SintaxException
	 */
	private void parseActionCallPrimaPrima(Method method, CallParameters call) throws SintaxException {
		int [] expected = { COMMA,RPAREN};

		switch(nextToken.getKind()) {
		case COMMA:
			match(COMMA);
			Expression exp = tryparseExpr(method);
			call.addParameter(exp);
			parseActionCallPrimaPrima(method,call);
			break;
		case RPAREN:
			break;
		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	private Expression tryparseExpr(Method method) {
		int [] lsync = { };
		int [] rsync = { RPAREN, SEMICOLON, COMMA};
		try { return parseExpr(method); }
		catch(Exception ex){ catchError(ex);skipTo(lsync,rsync); return null;}



	}

	/**
	 * Analiza el s�mbolo <Expr>
	 * @param method
	 * @throws SintaxException
	 */
	private Expression parseExpr(Method method) throws SintaxException {
		int [] expected = { NOT,MINUS,PLUS,INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN};

		switch(nextToken.getKind()) {
		case NOT:
		case MINUS:
		case PLUS:
		case INTEGER_LITERAL:
		case DOUBLE_LITERAL:
		case TRUE:
		case FALSE:
		case GEAR:
		case SPEED:
		case ANGLE:
		case POSITION:
		case RPM:
		case SENSOR0:
		case SENSOR1:
		case SENSOR2:
		case SENSOR3:
		case SENSOR4:
		case SENSOR5:
		case SENSOR6:
		case SENSOR7:
		case SENSOR8:
		case SENSOR9:
		case SENSOR10:
		case SENSOR11:
		case SENSOR12:
		case SENSOR13:
		case SENSOR14:
		case SENSOR15:
		case SENSOR16:
		case SENSOR17:
		case SENSOR18:
		case IDENTIFIER:
		case LPAREN:

		    Expression exp = tryAndExpr(method);
			exp = parseExprPrima(exp,method);
			return exp;
	default:
		throw new SintaxException(nextToken, expected);
	}

	}

	private Expression actionOrExpression(Token tk,Expression exp1, Expression exp2) {
		verifyBooleanTypes(tk,exp1,exp2);
		int type = Type.BOOLEAN_TYPE;
		int op = BinaryExpression.OR;
		Expression exp = new BinaryExpression(type, op, exp1, exp2);
		return exp;
	}


	/**
	 * Analiza el s�mbolo <ExprPrima>
	 * @param method
	 * @param exp
	 * @throws SintaxException
	 */
	private Expression parseExprPrima(Expression exp, Method method) throws SintaxException {
		int [] expected = { OR, COMMA, RPAREN, FLECHA, SEMICOLON};

		switch(nextToken.getKind()) {
		case OR:
			match(OR);
			Token tk = prevToken;
			Expression andExp = tryAndExpr(method);
			Expression orExp = actionOrExpression(tk, exp,andExp);
			Expression retExp = parseExprPrima(orExp,method);
			return retExp;
		case COMMA:
		case RPAREN:
		case FLECHA:
		case SEMICOLON:
			return exp;
		default:
			throw new SintaxException(nextToken, expected);
		}

	}

	private Expression tryAndExpr(Method method) {
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR };
		try { return parseAndExpr(method); }
		catch(Exception ex) { catchError(ex); skipTo(lsync,rsync); return null;}
	}


	/**
	 * Analiza el s�mbolo <AndExpr>
	 * @param method
	 * @return
	 * @throws SintaxException
	 */
	private Expression parseAndExpr(Method method) throws SintaxException {
		int [] expected = { NOT,MINUS,PLUS,INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN};

		switch(nextToken.getKind()) {
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case DOUBLE_LITERAL:
			case TRUE:
			case FALSE:
			case GEAR:
			case SPEED:
			case ANGLE:
			case POSITION:
			case RPM:
			case SENSOR0:
			case SENSOR1:
			case SENSOR2:
			case SENSOR3:
			case SENSOR4:
			case SENSOR5:
			case SENSOR6:
			case SENSOR7:
			case SENSOR8:
			case SENSOR9:
			case SENSOR10:
			case SENSOR11:
			case SENSOR12:
			case SENSOR13:
			case SENSOR14:
			case SENSOR15:
			case SENSOR16:
			case SENSOR17:
			case SENSOR18:
			case IDENTIFIER:
			case LPAREN:
				Expression exp = tryparseRelExpr(method);
				Expression exp2 = parseAndExprPrima(exp,method);
				return exp2;

		default:
		throw new SintaxException(nextToken, expected);
		}

	}



	private Expression actionAndExpression(Token tk, Expression exp1, Expression exp2) {
		verifyBooleanTypes(tk,exp1,exp2);
		int type = Type.BOOLEAN_TYPE;
		int op = BinaryExpression.AND;
		Expression exp = new BinaryExpression(type, op, exp1, exp2);
		return exp;
	}

	/**
	 * Analiza el s�mbolo <AndExprPrima>
	 * @param method
	 * @param exp3
	 * @return
	 * @throws SintaxException
	 */
	private Expression parseAndExprPrima(Expression exp, Method method) throws SintaxException {
		int [] expected = {AND, OR, COMMA,RPAREN,FLECHA,SEMICOLON};

		switch(nextToken.getKind()) {
			case AND:
				match(AND);
				Token tk = prevToken;
				Expression relExp = tryparseRelExpr(method);

				Expression andExp =  actionAndExpression(tk,exp,relExp);
				Expression retExp = parseAndExprPrima(andExp, method);
				return retExp;
			case OR:
			case COMMA:
			case RPAREN:
			case FLECHA:
			case SEMICOLON:
				return exp;
		default:
		throw new SintaxException(nextToken, expected);
		}

	}

	private Expression tryparseRelExpr(Method method) {
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND };
		try { return parseRelExpr(method); }
		catch(Exception ex) { catchError(ex); skipTo(lsync,rsync); return null;}
	}

	/**
	 * Analiza el s�mbolo <RelExpr>
	 * @param method
	 * @return
	 * @throws SintaxException
	 */
	private Expression parseRelExpr(Method method) throws SintaxException {
		int [] expected = { NOT,MINUS,PLUS,INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN};

		switch(nextToken.getKind()) {
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case DOUBLE_LITERAL:
			case TRUE:
			case FALSE:
			case GEAR:
			case SPEED:
			case ANGLE:
			case POSITION:
			case RPM:
			case SENSOR0:
			case SENSOR1:
			case SENSOR2:
			case SENSOR3:
			case SENSOR4:
			case SENSOR5:
			case SENSOR6:
			case SENSOR7:
			case SENSOR8:
			case SENSOR9:
			case SENSOR10:
			case SENSOR11:
			case SENSOR12:
			case SENSOR13:
			case SENSOR14:
			case SENSOR15:
			case SENSOR16:
			case SENSOR17:
			case SENSOR18:
			case IDENTIFIER:
			case LPAREN:
				Expression exp = tryparseSumExpr(method);
				Expression retExp = parseRelExprPrima(exp,method);
				return retExp;

		default:
		throw new SintaxException(nextToken, expected);
		}


	}


	private Expression actionRelExpression(Token tk, int op, Expression exp1, Expression exp2) {
		verifyRelationTypes(tk,op,exp1,exp2);
		int type = Type.BOOLEAN_TYPE;
		Expression exp = new BinaryExpression(type, op, exp1, exp2);
		return exp;
	}

	/**
	 * Analiza el s�mbolo <RelExprPrima>
	 * @param method
	 * @param exp
	 * @throws SintaxException
	 */
	private Expression parseRelExprPrima(Expression exp, Method method) throws SintaxException {
		int [] expected = { EQ, NE, GT,GE,LT,LE,AND,OR,COMMA,RPAREN,FLECHA,SEMICOLON};

		switch(nextToken.getKind()) {
		case EQ:
		case NE:
		case GT:
		case GE:
		case LT:
		case LE:
			Token tk = nextToken;
			int op = parseRelOp();
			Expression sumExp = tryparseSumExpr(method);
			Expression retExp = actionRelExpression(tk,op,exp,sumExp);
			return retExp;
		case AND:
		case OR:
		case COMMA:
		case RPAREN:
		case FLECHA:
		case SEMICOLON:
			return exp;
		default:
		throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <RelOp>
	 * @return
	 * @throws SintaxException
	 */
	private int parseRelOp() throws SintaxException {
		int [] expected = { EQ, NE, GT,GE,LT,LE};
		switch(nextToken.getKind()) {
			case EQ:
				match(EQ);
				return BinaryExpression.EQ;
			case NE:
				match(NE);
				return BinaryExpression.NEQ;
			case GT:
				match(GT);
				return BinaryExpression.GT;
			case GE:
				match(GE);
				return BinaryExpression.GE;
			case LT:
				match(LT);
				return BinaryExpression.LT;

			case LE:
				match(LE);
				return BinaryExpression.LE;
		default:
		throw new SintaxException(nextToken, expected);
		}

	}


	private Expression tryparseSumExpr(Method method) {
		  int[] lsync = { };
		  int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE };
		  try { return parseSumExpr(method); }
		  catch(Exception ex) { catchError(ex); skipTo(lsync,rsync); return null;}
	}

	private Expression actionUnaryExpression(Token tk, int op, Expression exp) {
		switch(op) {
			case UnaryExpression.NONE:
				return exp;
			case UnaryExpression.NOT:
				verifyBooleanType(tk,exp);
				return new UnaryExpression(Type.BOOLEAN_TYPE,op,exp);
			case UnaryExpression.MINUS:
				verifyNumberType(tk,exp);
				return new UnaryExpression(exp.getType(),op,exp);

			case UnaryExpression.PLUS:
				verifyNumberType(tk,exp);
				return exp;
		}
		return exp;
	}

	/**
	 * Analiza el s�mbolo <SumExpr>
	 * @param method
	 * @return
	 * @throws SintaxException
	 */
	private Expression parseSumExpr(Method method) throws SintaxException {
		int [] expected = { NOT,MINUS,PLUS,INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN};

		switch(nextToken.getKind()) {
		case NOT:
		case MINUS:
		case PLUS:
		case INTEGER_LITERAL:
		case DOUBLE_LITERAL:
		case TRUE:
		case FALSE:
		case GEAR:
		case SPEED:
		case ANGLE:
		case POSITION:
		case RPM:
		case SENSOR0:
		case SENSOR1:
		case SENSOR2:
		case SENSOR3:
		case SENSOR4:
		case SENSOR5:
		case SENSOR6:
		case SENSOR7:
		case SENSOR8:
		case SENSOR9:
		case SENSOR10:
		case SENSOR11:
		case SENSOR12:
		case SENSOR13:
		case SENSOR14:
		case SENSOR15:
		case SENSOR16:
		case SENSOR17:
		case SENSOR18:
		case IDENTIFIER:
		case LPAREN:
			Token tk = nextToken;
			int op = parseUnOp();
			Expression prodExp = tryparseProdExpr(method);
			Expression unExp = actionUnaryExpression(tk, op, prodExp);
			Expression retExp = parseSumExprPrima(unExp,method);
			return retExp;

		default:
		throw new SintaxException(nextToken, expected);
		}


	}


	private Expression actionSumExpression(Token tk,int op, Expression exp1, Expression exp2) {
		verifyNumberTypes(tk,exp1,exp2);
		int type = exp1.getType();
		if(exp2.getType()==Type.DOUBLE_TYPE) type = Type.DOUBLE_TYPE;
		Expression exp = new BinaryExpression(type, op, exp1, exp2);
		return exp;
	}

	/**
	 * Analiza el s�mbolo <SumExprPrima>
	 * @param method
	 * @param unExp
	 * @return
	 * @throws SintaxException
	 */
	private Expression parseSumExprPrima(Expression exp, Method method) throws SintaxException {
		int [] expected = { MINUS,PLUS,EQ, NE, GT,GE,LT,LE,AND,OR,COMMA,RPAREN,FLECHA,SEMICOLON};

		switch(nextToken.getKind()) {
		case MINUS:
		case PLUS:
			Token tk = nextToken;
			int op = parseSumOp();
			Expression prodExp = tryparseProdExpr(method);
			Expression sumExp = actionSumExpression(tk,op,exp,prodExp);
			Expression retExp = parseSumExprPrima(sumExp,method);
			return retExp;
		case EQ:
		case NE:
		case GT:
		case GE:
		case LT:
		case LE:
		case AND:
		case OR:
		case COMMA:
		case RPAREN:
		case FLECHA:
		case SEMICOLON:
			return exp;
		default:
		throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <UnOp>
	 * @return
	 * @throws SintaxException
	 */
	private int parseUnOp() throws SintaxException {
		int [] expected = { NOT,MINUS,PLUS,INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN};

		switch(nextToken.getKind()) {
		case NOT:
			match(NOT);
			return UnaryExpression.NOT;
		case MINUS:
			match(MINUS);
			return UnaryExpression.MINUS;
		case PLUS:
			match(PLUS);
			return UnaryExpression.PLUS;
		case INTEGER_LITERAL:
		case DOUBLE_LITERAL:
		case TRUE:
		case FALSE:
		case GEAR:
		case SPEED:
		case ANGLE:
		case POSITION:
		case RPM:
		case SENSOR0:
		case SENSOR1:
		case SENSOR2:
		case SENSOR3:
		case SENSOR4:
		case SENSOR5:
		case SENSOR6:
		case SENSOR7:
		case SENSOR8:
		case SENSOR9:
		case SENSOR10:
		case SENSOR11:
		case SENSOR12:
		case SENSOR13:
		case SENSOR14:
		case SENSOR15:
		case SENSOR16:
		case SENSOR17:
		case SENSOR18:
		case IDENTIFIER:
		case LPAREN:
			return UnaryExpression.NONE;
		default:
		throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <SumOp>
	 * @return
	 * @throws SintaxException
	 */
	private int parseSumOp() throws SintaxException {
		int [] expected = { MINUS,PLUS};

		switch(nextToken.getKind()) {
		case MINUS:
			match(MINUS);
			return BinaryExpression.MINUS;
		case PLUS:
			match(PLUS);
			return BinaryExpression.PLUS;
		default:
		throw new SintaxException(nextToken, expected);
		}


	}


	private Expression tryparseProdExpr(Method method) {
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE, PLUS, MINUS };
		try { return parseProdExpr(method); }
		catch(Exception ex) { catchError(ex); skipTo(lsync,rsync); return null; }
	}

	/**
	 * Analiza el s�mbolo <ProdExpr>
	 * @param method
	 * @throws SintaxException
	 */
	private Expression parseProdExpr(Method method) throws SintaxException {
		int [] expected = {INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN};

		switch(nextToken.getKind()) {
		case INTEGER_LITERAL:
		case DOUBLE_LITERAL:
		case TRUE:
		case FALSE:
		case GEAR:
		case SPEED:
		case ANGLE:
		case POSITION:
		case RPM:
		case SENSOR0:
		case SENSOR1:
		case SENSOR2:
		case SENSOR3:
		case SENSOR4:
		case SENSOR5:
		case SENSOR6:
		case SENSOR7:
		case SENSOR8:
		case SENSOR9:
		case SENSOR10:
		case SENSOR11:
		case SENSOR12:
		case SENSOR13:
		case SENSOR14:
		case SENSOR15:
		case SENSOR16:
		case SENSOR17:
		case SENSOR18:
		case IDENTIFIER:
		case LPAREN:
			Expression factor = tryparseFactor(method);
			Expression retExp = parseProdExprPrima(factor,method);
			return retExp;
		default:
		throw new SintaxException(nextToken, expected);
		}

	}


	private Expression actionProdExpression(Token tk,int op, Expression exp1, Expression exp2) {
		verifyNumberTypes(tk,exp1,exp2);
		int type = exp1.getType();
		if(exp2.getType()==Type.DOUBLE_TYPE) type = Type.DOUBLE_TYPE;
		Expression exp = new BinaryExpression(type, op, exp1, exp2);
		return exp;

	}

	/**
	 * Analiza el s�mbolo <ProdExprPrima>
	 * @param method
	 * @param factor
	 * @return
	 * @throws SintaxException
	 */
	private Expression parseProdExprPrima(Expression exp, Method method) throws SintaxException {
		int [] expected = {PROD,DIV,MOD, MINUS,PLUS,EQ, NE, GT,GE,LT,LE,AND,OR,COMMA,RPAREN,FLECHA,SEMICOLON};

		switch(nextToken.getKind()) {
		case PROD:
		case DIV:
		case MOD:
			Token tk = nextToken;
			int op = parseMultOp();
			Expression factor = tryparseFactor(method);
			Expression multExp = actionProdExpression(tk,op,exp,factor);
			Expression retExp = parseProdExprPrima(multExp,method);
			return retExp;
		case MINUS:
		case PLUS:
		case EQ:
		case NE:
		case GT:
		case GE:
		case LT:
		case LE:
		case AND:
		case OR:
		case COMMA:
		case RPAREN:
		case FLECHA:
		case SEMICOLON:
			return exp;
		default:
		throw new SintaxException(nextToken, expected);
		}

	}

	/**
	 * Analiza el s�mbolo <MultOp>
	 * @return
	 * @throws SintaxException
	 */
	private int parseMultOp() throws SintaxException {
		int [] expected = {PROD,DIV,MOD};

		switch(nextToken.getKind()) {
		case PROD:
			match(PROD);
			return BinaryExpression.PROD;
		case DIV:
			match(DIV);
			return BinaryExpression.DIV;
		case MOD:
			match(MOD);
			return BinaryExpression.MOD;
		default:
		throw new SintaxException(nextToken, expected);
		}


	}


	private Expression tryparseFactor(Method method) {
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE, PLUS, MINUS, PROD, DIV, MOD };
		try { return parseFactor(method); }
		catch(Exception ex) { catchError(ex); skipTo(lsync,rsync); return null; }
	}

	/**
	 * Analiza el s�mbolo <Factor>
	 * @param method
	 * @throws SintaxException
	 */
	private Expression parseFactor(Method method) throws SintaxException {
		int [] expected = {INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN};

		Expression exp = null;

		switch(nextToken.getKind()) {
			case INTEGER_LITERAL:
			case DOUBLE_LITERAL:
			case TRUE:
			case FALSE:
				Token tk = parseLiteral(method);
				if(tk.getKind() == INTEGER_LITERAL)
				{
					exp = new IntegerLiteralExpression(tk.getLexeme());
				}
				else if(tk.getKind() == DOUBLE_LITERAL)
				{
					exp = new DoubleLiteralExpression(tk.getLexeme());
				}
				else exp = new BooleanLiteralExpression(tk.getLexeme());
				break;
			case GEAR:
			case SPEED:
			case ANGLE:
			case POSITION:
			case RPM:
			case SENSOR0:
			case SENSOR1:
			case SENSOR2:
			case SENSOR3:
			case SENSOR4:
			case SENSOR5:
			case SENSOR6:
			case SENSOR7:
			case SENSOR8:
			case SENSOR9:
			case SENSOR10:
			case SENSOR11:
			case SENSOR12:
			case SENSOR13:
			case SENSOR14:
			case SENSOR15:
			case SENSOR16:
			case SENSOR17:
			case SENSOR18:
				exp = parseInputVar(method);
				break;
			case IDENTIFIER:
				exp = parseReference(method);
				break;
			case LPAREN:
				match(LPAREN);
				exp = tryparseExpr(method);
				match(RPAREN);
				break;
		default:
		throw new SintaxException(nextToken, expected);
		}

		return exp;

	}


	/**
	 * Analiza el s�mbolo <Litera>
	 * @param method
	 * @throws SintaxException
	 */
	private Token parseLiteral(Method method) throws SintaxException {
		int [] expected = {INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE};

		switch(nextToken.getKind()) {
		case INTEGER_LITERAL:
			match(INTEGER_LITERAL);
			return prevToken;

		case DOUBLE_LITERAL:
			match(DOUBLE_LITERAL);
			return prevToken;

		case TRUE:
			match(TRUE);
			return prevToken;

		case FALSE:
			match(FALSE);
			return prevToken;

		default:
		throw new SintaxException(nextToken, expected);
		}


	}

	/**
	 * Analiza el s�mbolo <Reference>
	 * @param method
	 * @throws SintaxException
	 */
	private Expression parseReference(Method method) throws SintaxException {
		int [] expected = {IDENTIFIER};

		switch(nextToken.getKind()) {
		case IDENTIFIER:
			match(IDENTIFIER);

			Token tid = prevToken;
			Expression exp = parseReferencePrima(tid,method);
			return exp;
		default:
		throw new SintaxException(nextToken, expected);
		}


	}

	private void verifyUnknownVariable(Token tk, Method method) {
		if(method.getVariable(tk.getLexeme()) == null) {

			if(controller.getInner(tk.getLexeme())== null)
			{
				int errorcode = SemanticException.UNKNOWN_VARIABLE_EXCEPTION;
				catchError(new SemanticException(errorcode,tk));
			}

		}
	}

	/**
	 * Analiza el s�mbolo <ReferencePrima>
	 * @param method
	 * @param tid
	 * @throws SintaxException
	 */
	private Expression parseReferencePrima(Token tid, Method method) throws SintaxException {
		int [] expected = { LPAREN,PROD,DIV,MOD, MINUS,PLUS,EQ, NE, GT,GE,LT,LE,AND,OR,COMMA,RPAREN,FLECHA,SEMICOLON};

		CallParameters call = null;

		switch(nextToken.getKind()) {
		case LPAREN:
			call = parseMethodCall(method);

			verifyUnknownMethod(tid,call,method);

			Perception perception = controller.getPerception(tid.getLexeme(),call);

			if(perception == null)
				{
					if(controller.getAction(tid.getLexeme(),call) != null)
					{
					 return new CallExpression(controller.getAction(tid.getLexeme(),call), call);
					}
					else return new CallExpression(new Method(Type.MISMATCH_TYPE,tid.getLexeme()),call);
				}
			else  return new CallExpression(perception, call);
		case PROD:
		case DIV:
		case MOD:
		case MINUS:
		case PLUS:
		case EQ:
		case NE:
		case GT:
		case GE:
		case LT:
		case LE:
		case AND:
		case OR:
		case COMMA:
		case RPAREN:
		case FLECHA:
		case SEMICOLON:
			
			verifyUnknownVariable(tid,method);

			Variable var = method.getVariable(tid.getLexeme());
			if(var == null)
				{
					if( controller.getInner(tid.getLexeme()) != null)
					{
						Inner inner = controller.getInner(tid.getLexeme());
						var = new Variable(inner.getType(), inner.getName());
						return new VariableExpression(var);
					}
					else return new VariableExpression(new Variable(Type.MISMATCH_TYPE, tid.getLexeme()));

				}
			else return new VariableExpression(var);
		default:
		throw new SintaxException(nextToken, expected);
		}


	}

	/**
	 * Analiza el s�mbolo <MethodCall>
	 * @param method
	 * @throws SintaxException
	 */
	private CallParameters parseMethodCall(Method method) throws SintaxException {
		int [] expected = { LPAREN};

		switch(nextToken.getKind()) {
		case LPAREN:
			match(LPAREN);
			CallParameters param = new CallParameters();
			parseMethodCallPrima(param,method);
			match(RPAREN);
			return param;
		default:
		throw new SintaxException(nextToken, expected);
		}


	}

	/**
	 * Analiza el s�mbolo <MethodCallPrima>
	 * @param method
	 * @param param
	 * @throws SintaxException
	 */
	private void parseMethodCallPrima(CallParameters param, Method method) throws SintaxException {
		int [] expected = { NOT, MINUS,PLUS,INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN, RPAREN};

		switch(nextToken.getKind()) {
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case DOUBLE_LITERAL:
			case TRUE:
			case FALSE:
			case GEAR:
			case SPEED:
			case ANGLE:
			case POSITION:
			case RPM:
			case SENSOR0:
			case SENSOR1:
			case SENSOR2:
			case SENSOR3:
			case SENSOR4:
			case SENSOR5:
			case SENSOR6:
			case SENSOR7:
			case SENSOR8:
			case SENSOR9:
			case SENSOR10:
			case SENSOR11:
			case SENSOR12:
			case SENSOR13:
			case SENSOR14:
			case SENSOR15:
			case SENSOR16:
			case SENSOR17:
			case SENSOR18:
			case IDENTIFIER:
			case LPAREN:
				Expression exp = tryparseExpr(method);
				param.addParameter(exp);
				parseMethodCallPrimaPrima(param,method);
				break;
			case RPAREN:
				break;
	default:
	throw new SintaxException(nextToken, expected);
	}

	}

	/**
	 * Analiza el s�mbolo <MethodCallPrimaPrima>
	 * @param method
	 * @param param
	 * @throws SintaxException
	 */
	private void parseMethodCallPrimaPrima(CallParameters param, Method method) throws SintaxException {
		int [] expected = { COMMA, RPAREN};

		switch(nextToken.getKind()) {
		case COMMA:
			match(COMMA);
			Expression exp = tryparseExpr(method);
			param.addParameter(exp);
			parseMethodCallPrimaPrima(param,method);
			break;
		case RPAREN:
			break;
		default:
		throw new SintaxException(nextToken, expected);
		}

	}

}
