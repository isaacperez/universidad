package TorcsController;



public class TorcsParser implements TokenConstants {

	/**
	 *  Analizador l�xico
	 */
	private TorcsLexer lexer;
	
	/**
	 *  Siguiente token de la cadena de entrada
	 */
	private Token nextToken;
	
	/**
	 * M�todo de an�isis de un fichero
	 * 
	 * @param filename Nombre del fichero a analizar
	 * @return Resultado del an�lisis sint�ctico
	 */
	public boolean parse(String filename) {
		try {
			this.lexer = new TorcsLexer(filename);
			this.nextToken = lexer.getNextToken();
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

		TorcsParser parser = new TorcsParser();
		if(parser.parse(args[0])) {
			System.out.println("Correcto");
		} else {
			System.out.println("Incorrecto");
		}
	}

	/**
	 * M�todo que consume un token de la cadena de entrada
	 * @param kind Tipo de token a consumir
	 * @throws SintaxException Si el tipo no coincide con el token 
	 */
	private void match(int kind) throws SintaxException {
		if(nextToken.getKind() == kind) nextToken = lexer.getNextToken();
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
				parseRules();
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
				parseInnerDecl();
				break;
			case PERCEPTION:
				parsePerceptionDecl();
				break;
			case ACTION:
				parseActionDecl();
				break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
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
				parseType();
				match(IDENTIFIER);
				match(ASSIGN);
				parseLiteral();
				match(SEMICOLON);
			break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <Type>
	 * @throws SintaxException
	 */
	private void parseType() throws SintaxException {
		int [] expected = { INT, DOUBLE, BOOLEAN};
		
		switch(nextToken.getKind()) {
		case INT:
			match(INT);
			break;
		case DOUBLE:
			match(DOUBLE);
			break;
		case BOOLEAN:
			match(BOOLEAN);
			break;
		default: 
			throw new SintaxException(nextToken, expected);
		}
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
			match(IDENTIFIER);
			parseArgumentDecl();
			parsePerceptionBody();
			break;
		default: 
			throw new SintaxException(nextToken, expected);
		}
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
				match(IDENTIFIER);
				parseArgumentDecl();
				parseActionBody();
				break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <ArgumentDecl>
	 * @throws SintaxException
	 */
	private void parseArgumentDecl() throws SintaxException {
		int [] expected = { LPAREN};
		
		switch(nextToken.getKind()) {
			case LPAREN:
				match(LPAREN);
				parseArgumentDeclPrima();
				match(RPAREN);
				break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ArgumentDeclPrima>
	 * @throws SintaxException
	 */
	private void parseArgumentDeclPrima() throws SintaxException {
		int [] expected = { INT, DOUBLE, BOOLEAN, RPAREN};
		
		switch(nextToken.getKind()) {
			case INT:
			case DOUBLE:
			case BOOLEAN:
				parseArgument();
				parseArgumentDeclPrimaPrima();
				break;
			case RPAREN:
				break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <ArgumentDeclPrimaPrima>
	 * @throws SintaxException
	 */
	private void parseArgumentDeclPrimaPrima() throws SintaxException {
		int [] expected = { COMMA, RPAREN};
		
		switch(nextToken.getKind()) {
			case COMMA:
				match(COMMA);
				parseArgument();
				parseArgumentDeclPrimaPrima();
				break;
			case RPAREN:	
				break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <Argument>
	 * @throws SintaxException
	 */
	private void parseArgument() throws SintaxException {
		int [] expected = { INT, DOUBLE, BOOLEAN};
		
		switch(nextToken.getKind()) {
			case INT:
			case DOUBLE:
			case BOOLEAN:
				parseType();
				match(IDENTIFIER);
				break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PerceptionBody>
	 * @throws SintaxException
	 */
	private void parsePerceptionBody() throws SintaxException {
		int [] expected = { LBRACE};
		
		switch(nextToken.getKind()) {
			case LBRACE:
				match(LBRACE);
				parsePerceptionBodyPrima();
				match(RBRACE);
				break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <PerceptionBodyPrima>
	 * @throws SintaxException
	 */
	private void parsePerceptionBodyPrima() throws SintaxException {
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
				parsePcptStatement();
				parsePerceptionBodyPrima();
				break;
			case RBRACE:
				break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <ActionBody>
	 * @throws SintaxException
	 */
	private void parseActionBody() throws SintaxException {
		int [] expected = { LBRACE};
		
		switch(nextToken.getKind()) {
		case LBRACE:
			match(LBRACE);
			parseActionBodyPrima();
			match(RBRACE);
			break;
		default: 
			throw new SintaxException(nextToken, expected);
	}		
		
		
	}
	
	/**
	 * Analiza el s�mbolo <ActionBodyPrima>
	 * @throws SintaxException
	 */
	private void parseActionBodyPrima() throws SintaxException {
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
			parseActStatement();
			parseActionBodyPrima();
			
			break;
		case RBRACE:
			
			break;
		default: 
			throw new SintaxException(nextToken, expected);
	}
		
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
				parseRulesPrima();
				match(RBRACE);
				break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <RulesPrima>
	 * @throws SintaxException
	 */
	private void parseRulesPrima() throws SintaxException {
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
				parseRule();
				parseRulesPrima();
				break;
			case RBRACE:
				break;
				
			default: 
				throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <InputVar>
	 * @throws SintaxException
	 */
	private void parseInputVar() throws SintaxException {
		int [] expected = {GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18};
		
		switch(nextToken.getKind()) {
			case GEAR:
				match(GEAR);
				break;
			case SPEED:
				match(SPEED);
				break;
			case ANGLE:
				match(ANGLE);
				break;
			case POSITION:
				match(POSITION);
				break;
			case RPM:
				match(RPM);
				break;
			case SENSOR0:
				match(SENSOR0);
				break;
			case SENSOR1:
				match(SENSOR1);
				break;
			case SENSOR2:
				match(SENSOR2);
				break;
			case SENSOR3:
				match(SENSOR3);
				break;
			case SENSOR4:
				match(SENSOR4);
				break;
			case SENSOR5:
				match(SENSOR5);
				break;
			case SENSOR6:
				match(SENSOR6);
				break;
			case SENSOR7:
				match(SENSOR7);
				break;
			case SENSOR8:
				match(SENSOR8);
				break;
			case SENSOR9:
				match(SENSOR9);
				break;
			case SENSOR10:
				match(SENSOR10);
				break;
			case SENSOR11:
				match(SENSOR11);
				break;
			case SENSOR12:
				match(SENSOR12);
				break;
			case SENSOR13:
				match(SENSOR13);
				break;
			case SENSOR14:
				match(SENSOR14);
				break;
			case SENSOR15:
				match(SENSOR15);
				break;
			case SENSOR16:
				match(SENSOR16);
				break;
			case SENSOR17:
				match(SENSOR17);
				break;
			case SENSOR18:
				match(SENSOR18);
				break;
				
			default: 
				throw new SintaxException(nextToken, expected);
	}		
		
	}
	
	/**
	 * Analiza el s�mbolo <OutputVar>
	 * @throws SintaxException
	 */
	private void parseOutputVar() throws SintaxException {
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
		
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptStatement>
	 * @throws SintaxException
	 */
	private void parsePcptStatement() throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,IF,WHILE,TRUE,FALSE,LBRACE};
		
		switch(nextToken.getKind()) {
		case INT:	
		case DOUBLE:		
		case BOOLEAN:			
			parsePcptDecl();
			break;
		case IDENTIFIER:
			parsePcptAssignStm();
			break;
		case IF:
			parsePcptIfStm();
			break;
		case WHILE:
			parsePcptWhileStm();
			break;
		case TRUE:
			parsePcptTrueStm();
			break;
		case FALSE:
			parsePcptFalseStm();
			break;
		case LBRACE:
			parsePcptBlockStm();
			break;
			
		default: 
			throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptDecl>
	 * @throws SintaxException
	 */
	private void parsePcptDecl() throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN};
		
		switch(nextToken.getKind()) {
			case INT:	
			case DOUBLE:		
			case BOOLEAN:			
				parseType();
				match(IDENTIFIER);
				parsePcptDeclPrima();
				match(SEMICOLON);
				
				break;
			default: 
				throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptDeclPrima>
	 * @throws SintaxException
	 */
	private void parsePcptDeclPrima() throws SintaxException {
		int [] expected = { ASSIGN,SEMICOLON};
		
		switch(nextToken.getKind()) {
		case ASSIGN:
			match(ASSIGN);
			parseExpr();
			break;
		case SEMICOLON:
			break;
		default: 
			throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptIfStm>
	 * @throws SintaxException
	 */
	private void parsePcptIfStm() throws SintaxException {
		int [] expected = { IF};
		
		switch(nextToken.getKind()) {
		case IF:
			match(IF);
			match(LPAREN);
			parseExpr();
			match(RPAREN);
			parsePcptStatement();
			parsePcptIfStmPrima();
		break;
		
		default: 
			throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptIfStmPrima>
	 * @throws SintaxException
	 */
	private void parsePcptIfStmPrima() throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,IF,WHILE,TRUE,FALSE,LBRACE,RBRACE,ELSE};
		
		switch(nextToken.getKind()) {
		case ELSE:
			match(ELSE);
			parsePcptStatement();
			break;
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
			break;
		default: 
			throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptWhileStm>
	 * @throws SintaxException
	 */
	private void parsePcptWhileStm() throws SintaxException {
		int [] expected = { WHILE};
		
		switch(nextToken.getKind()) {
		case WHILE:
			match(WHILE);
			match(LPAREN);
			parseExpr();
			match(RPAREN);
			parsePcptStatement();
			break;
			
		default: 
			throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptAssignStm>
	 * @throws SintaxException
	 */
	private void parsePcptAssignStm() throws SintaxException {
		int [] expected = { IDENTIFIER};
		
		switch(nextToken.getKind()) {
		case IDENTIFIER:
			match(IDENTIFIER);
			match(ASSIGN);
			parseExpr();
			match(SEMICOLON);
			break;
			
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptTrueStm>
	 * @throws SintaxException
	 */
	private void parsePcptTrueStm() throws SintaxException {
		int [] expected = { TRUE};
		
		switch(nextToken.getKind()) {
		case TRUE:
			match(TRUE);
			match(SEMICOLON);
			break;
			
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptFalseStm>
	 * @throws SintaxException
	 */
	private void parsePcptFalseStm() throws SintaxException {
		int [] expected = { FALSE};
		
		switch(nextToken.getKind()) {
		case FALSE:
			match(FALSE);
			match(SEMICOLON);
			break;
			
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptBlockStm>
	 * @throws SintaxException
	 */
	private void parsePcptBlockStm() throws SintaxException {
		int [] expected = { LBRACE};
		
		switch(nextToken.getKind()) {
		case LBRACE:
			match(LBRACE);
			parsePcptBlockStmPrima();
			match(RBRACE);
			break;
			
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <PcptBlockStmPrima>
	 * @throws SintaxException
	 */
	private void parsePcptBlockStmPrima() throws SintaxException {
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
			parsePcptStatement();
			parsePcptBlockStmPrima();
			break;
		case RBRACE:
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
	}
	
	/**
	 * Analiza el s�mbolo <ActStatement>
	 * @throws SintaxException
	 */
	private void parseActStatement() throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN,IDENTIFIER,GEAR,ACCELERATE, BRAKE, STEERING,IF, WHILE,LBRACE};
		
		switch(nextToken.getKind()) {
			case INT:
			case DOUBLE:
			case BOOLEAN:
				parseActDecl();
				break;
			case IDENTIFIER:
			case GEAR:
			case ACCELERATE:
			case BRAKE:
			case STEERING:
				parseActAssignStm();
				break;
			case IF:
				parseActIfStm();
				break;
			case WHILE:
				parseActWhileStm();
				break;
			case LBRACE:
				parseActBlockStm();
				break;
			default: 
				throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <ActDecl>
	 * @throws SintaxException
	 */
	private void parseActDecl() throws SintaxException {
		int [] expected = { INT,DOUBLE,BOOLEAN};
		
		switch(nextToken.getKind()) {
		case INT:
		case DOUBLE:
		case BOOLEAN:
			parseType();
			match(IDENTIFIER);
			parseActDeclPrima();
			match(SEMICOLON);
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
	}
	
	/**
	 * Analiza el s�mbolo <ActDeclPrima>
	 * @throws SintaxException
	 */
	private void parseActDeclPrima() throws SintaxException {
		int [] expected = { ASSIGN, SEMICOLON};
		
		switch(nextToken.getKind()) {
			case ASSIGN:
				match(ASSIGN);
				parseExpr();
				break;
			case SEMICOLON:
				break;
			default: 
				throw new SintaxException(nextToken, expected);	
		}
	}
	
	/**
	 * Analiza el s�mbolo <ActIfStm>
	 * @throws SintaxException
	 */
	private void parseActIfStm() throws SintaxException {
		int [] expected = { IF};
		
		switch(nextToken.getKind()) {
		case IF:
			match(IF);
			match(LPAREN);
			parseExpr();
			match(RPAREN);
			parseActStatement();
			parseActIfStmPrima();
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
	}
	
	/**
	 * Analiza el s�mbolo <ActIfStmPrima>
	 * @throws SintaxException
	 */
	private void parseActIfStmPrima() throws SintaxException {
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
			break;
		case ELSE:
			match(ELSE);
			parseActStatement();
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
	}
	
	/**
	 * Analiza el s�mbolo <ActWhileStm>
	 * @throws SintaxException
	 */
	private void parseActWhileStm() throws SintaxException {
		int [] expected = { WHILE};
		
		switch(nextToken.getKind()) {
		case WHILE:
			match(WHILE);
			match(LPAREN);
			parseExpr();
			match(RPAREN);
			parseActStatement();
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
	}
	
	/**
	 * Analiza el s�mbolo <ActAssignStm>
	 * @throws SintaxException
	 */
	private void parseActAssignStm() throws SintaxException {
		int [] expected = {IDENTIFIER,GEAR,ACCELERATE, BRAKE, STEERING};
		
		switch(nextToken.getKind()) {
		case IDENTIFIER:
		case GEAR:
		case ACCELERATE:
		case BRAKE:
		case STEERING:
			parseActAssignStmPrima();
			match(ASSIGN);
			parseExpr();
			match(SEMICOLON);
			
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <ActAssignStmPrima>
	 * @throws SintaxException
	 */
	private void parseActAssignStmPrima() throws SintaxException {
		int [] expected = {IDENTIFIER,GEAR,ACCELERATE, BRAKE, STEERING};
		
		switch(nextToken.getKind()) {
		case IDENTIFIER:
			match(IDENTIFIER);
			break;
		case GEAR:
		case ACCELERATE:
		case BRAKE:
		case STEERING:
			parseOutputVar();
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <ActBlockStm>
	 * @throws SintaxException
	 */
	private void parseActBlockStm() throws SintaxException {
		int [] expected = { LBRACE};
		
		switch(nextToken.getKind()) {
		case LBRACE:
			match(LBRACE);
			parseActBlockStmPrima();
			match(RBRACE);
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
		
	}
	
	/**
	 * Analiza el s�mbolo <ActBlockStmPrima>
	 * @throws SintaxException
	 */
	private void parseActBlockStmPrima() throws SintaxException {
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
			parseActStatement();
			parseActBlockStmPrima();
			break;
		case RBRACE:
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
	}
	
	/**
	 * Analiza el s�mbolo <Rule>
	 * @throws SintaxException
	 */
	private void parseRule() throws SintaxException {
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
			parseExpr();
			match(FLECHA);
			parseActionCall();
			parseRulePrima();
			match(SEMICOLON);
			break;
		default: 
			throw new SintaxException(nextToken, expected);
}		

	}
	
	/**
	 * Analiza el s�mbolo <RulePrima>
	 * @throws SintaxException
	 */
	private void parseRulePrima() throws SintaxException {
		int [] expected = { COMMA, SEMICOLON};
		
		switch(nextToken.getKind()) {
		case COMMA:
			match(COMMA);
			parseActionCall();
			parseRulePrima();
			break;
		case SEMICOLON:
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <ActionCall>
	 * @throws SintaxException
	 */
	private void parseActionCall() throws SintaxException {
		int [] expected = { IDENTIFIER};
		
		switch(nextToken.getKind()) {
		case IDENTIFIER:
			match(IDENTIFIER);
			match(LPAREN);
			parseActionCallPrima();
			match(RPAREN);
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <ActionCallPrima>
	 * @throws SintaxException
	 */
	private void parseActionCallPrima() throws SintaxException {
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
				parseExpr();
				parseActionCallPrimaPrima();
				break;
			case RPAREN:
				break;
		default: 
			throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <ActionCallPrimaPrima>
	 * @throws SintaxException
	 */
	private void parseActionCallPrimaPrima() throws SintaxException {
		int [] expected = { COMMA,RPAREN};
		
		switch(nextToken.getKind()) {
		case COMMA:
			match(COMMA);
			parseExpr();
			parseActionCallPrimaPrima();
			break;
		case RPAREN:
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <Expr>
	 * @throws SintaxException
	 */
	private void parseExpr() throws SintaxException {
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
			parseAndExpr();
			parseExprPrima();
			break;

	default: 
		throw new SintaxException(nextToken, expected);
	}
		
	}
	
	/**
	 * Analiza el s�mbolo <ExprPrima>
	 * @throws SintaxException
	 */
	private void parseExprPrima() throws SintaxException {
		int [] expected = { OR, COMMA, RPAREN, FLECHA, SEMICOLON};
		
		switch(nextToken.getKind()) {
		case OR:
			match(OR);
			parseAndExpr();
			parseExprPrima();
			break;
		case COMMA:
		case RPAREN:
		case FLECHA:
		case SEMICOLON:
			break;
		default: 
			throw new SintaxException(nextToken, expected);	
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <AndExpr>
	 * @throws SintaxException
	 */
	private void parseAndExpr() throws SintaxException {
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
				parseRelExpr();
				parseAndExprPrima();
				break;

		default: 
		throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <AndExprPrima>
	 * @throws SintaxException
	 */
	private void parseAndExprPrima() throws SintaxException {
		int [] expected = {AND, OR, COMMA,RPAREN,FLECHA,SEMICOLON};
		
		switch(nextToken.getKind()) {
			case AND:
				match(AND);
				parseRelExpr();
				parseAndExprPrima();
				break;
			case OR:
			case COMMA:
			case RPAREN:
			case FLECHA:
			case SEMICOLON:
				break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <RelExpr>
	 * @throws SintaxException
	 */
	private void parseRelExpr() throws SintaxException {
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
				parseSumExpr();
				parseRelExprPrima();
			break;

		default: 
		throw new SintaxException(nextToken, expected);
		}
		
		
	}
	
	/**
	 * Analiza el s�mbolo <RelExprPrima>
	 * @throws SintaxException
	 */
	private void parseRelExprPrima() throws SintaxException {
		int [] expected = { EQ, NE, GT,GE,LT,LE,AND,OR,COMMA,RPAREN,FLECHA,SEMICOLON};
		
		switch(nextToken.getKind()) {
		case EQ:
		case NE:
		case GT:
		case GE:
		case LT:
		case LE:
			parseRelOp();
			parseSumExpr();
			break;
		case AND:
		case OR:
		case COMMA:
		case RPAREN:
		case FLECHA:
		case SEMICOLON:
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <RelOp>
	 * @throws SintaxException
	 */
	private void parseRelOp() throws SintaxException {
		int [] expected = { EQ, NE, GT,GE,LT,LE};
		switch(nextToken.getKind()) {
			case EQ:
				match(EQ);
				break;
			case NE:
				match(NE);
				break;
			case GT:
				match(GT);
				break;
			case GE:
				match(GE);
				break;
			case LT:
				match(LT);
				break;
			case LE:
				match(LE);
				break;	
		default: 
		throw new SintaxException(nextToken, expected);
		}
			
	}
	
	/**
	 * Analiza el s�mbolo <SumExpr>
	 * @throws SintaxException
	 */
	private void parseSumExpr() throws SintaxException {
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
			parseUnOp();
			parseProdExpr();
			parseSumExprPrima();
			break;

		default: 
		throw new SintaxException(nextToken, expected);
		}
		
		
	}
	
	/**
	 * Analiza el s�mbolo <SumExprPrima>
	 * @throws SintaxException
	 */
	private void parseSumExprPrima() throws SintaxException {
		int [] expected = { MINUS,PLUS,EQ, NE, GT,GE,LT,LE,AND,OR,COMMA,RPAREN,FLECHA,SEMICOLON};
		
		switch(nextToken.getKind()) {
		case MINUS:
		case PLUS:
			parseSumOp();
			parseProdExpr();
			parseSumExprPrima();
			break;
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
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
			
	}
	
	/**
	 * Analiza el s�mbolo <UnOp>
	 * @throws SintaxException
	 */
	private void parseUnOp() throws SintaxException {
		int [] expected = { NOT,MINUS,PLUS,INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN};
		
		switch(nextToken.getKind()) {
		case NOT:
			match(NOT);
			break;
		case MINUS:
			match(MINUS);
			break;
		case PLUS:
			match(PLUS);
			break;
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
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <SumOp>
	 * @throws SintaxException
	 */
	private void parseSumOp() throws SintaxException {
		int [] expected = { MINUS,PLUS};
		
		switch(nextToken.getKind()) {
		case MINUS:
			match(MINUS);
			break;
		case PLUS:
			match(PLUS);
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
			
		
	}
	
	/**
	 * Analiza el s�mbolo <ProdExpr>
	 * @throws SintaxException
	 */
	private void parseProdExpr() throws SintaxException {
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
			parseFactor();
			parseProdExprPrima();
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <ProdExprPrima>
	 * @throws SintaxException
	 */
	private void parseProdExprPrima() throws SintaxException {
		int [] expected = {PROD,DIV,MOD, MINUS,PLUS,EQ, NE, GT,GE,LT,LE,AND,OR,COMMA,RPAREN,FLECHA,SEMICOLON};
		
		switch(nextToken.getKind()) {
		case PROD:
		case DIV:
		case MOD:
			parseMultOp();
			parseFactor();
			parseProdExprPrima();
			break;
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
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <MultOp>
	 * @throws SintaxException
	 */
	private void parseMultOp() throws SintaxException {
		int [] expected = {PROD,DIV,MOD};
		
		switch(nextToken.getKind()) {
		case PROD:
			match(PROD);
			break;
		case DIV:
			match(DIV);
			break;
		case MOD:
			match(MOD);
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
		
	}
	
	/**
	 * Analiza el s�mbolo <Factor>
	 * @throws SintaxException
	 */
	private void parseFactor() throws SintaxException {
		int [] expected = {INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE,GEAR,SPEED,ANGLE,POSITION,RPM,SENSOR0
				, SENSOR1, SENSOR2, SENSOR3,SENSOR4,SENSOR5,SENSOR6,SENSOR7,SENSOR8,SENSOR9,SENSOR10,SENSOR11,SENSOR12,SENSOR13,
				SENSOR14,SENSOR15,SENSOR16,SENSOR17,SENSOR18, IDENTIFIER, LPAREN};
		
		switch(nextToken.getKind()) {
			case INTEGER_LITERAL:
			case DOUBLE_LITERAL:
			case TRUE:
			case FALSE:
				parseLiteral();
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
				parseInputVar();
				break;
			case IDENTIFIER:
				parseReference();
				break;
			case LPAREN:
				match(LPAREN);
				parseExpr();
				match(RPAREN);
				break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
	}
	
	/**
	 * Analiza el s�mbolo <Litera>
	 * @throws SintaxException
	 */
	private void parseLiteral() throws SintaxException {
		int [] expected = {INTEGER_LITERAL,DOUBLE_LITERAL,TRUE,FALSE};
		
		switch(nextToken.getKind()) {
		case INTEGER_LITERAL:
			match(INTEGER_LITERAL);
			break;
		case DOUBLE_LITERAL:
			match(DOUBLE_LITERAL);
			break;
		case TRUE:
			match(TRUE);
			break;
		case FALSE:
			match(FALSE);
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
		
	}
	
	/**
	 * Analiza el s�mbolo <Reference>
	 * @throws SintaxException
	 */
	private void parseReference() throws SintaxException {
		int [] expected = {IDENTIFIER};
		
		switch(nextToken.getKind()) {
		case IDENTIFIER:
			match(IDENTIFIER);
			parseReferencePrima();
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
		
	}
	
	/**
	 * Analiza el s�mbolo <ReferencePrima>
	 * @throws SintaxException
	 */
	private void parseReferencePrima() throws SintaxException {
		int [] expected = { LPAREN,PROD,DIV,MOD, MINUS,PLUS,EQ, NE, GT,GE,LT,LE,AND,OR,COMMA,RPAREN,FLECHA,SEMICOLON};
		
		switch(nextToken.getKind()) {
		case LPAREN:
			parseMethodCall();
			break;
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
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
		
	}
	
	/**
	 * Analiza el s�mbolo <MethodCall>
	 * @throws SintaxException
	 */
	private void parseMethodCall() throws SintaxException {
		int [] expected = { LPAREN};
		
		switch(nextToken.getKind()) {
		case LPAREN:
			match(LPAREN);
			parseMethodCallPrima();
			match(RPAREN);
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
		
	}
	
	/**
	 * Analiza el s�mbolo <MethodCallPrima>
	 * @throws SintaxException
	 */
	private void parseMethodCallPrima() throws SintaxException {
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
				parseExpr();
				parseMethodCallPrimaPrima();
				break;
			case RPAREN:
				break;
	default: 
	throw new SintaxException(nextToken, expected);
	}
		
	}
	
	/**
	 * Analiza el s�mbolo <MethodCallPrimaPrima>
	 * @throws SintaxException
	 */
	private void parseMethodCallPrimaPrima() throws SintaxException {
		int [] expected = { COMMA, RPAREN};
		
		switch(nextToken.getKind()) {
		case COMMA:
			match(COMMA);
			parseExpr();
			parseMethodCallPrimaPrima();
			break;
		case RPAREN:
			break;
		default: 
		throw new SintaxException(nextToken, expected);
		}
		
	}
	
}
