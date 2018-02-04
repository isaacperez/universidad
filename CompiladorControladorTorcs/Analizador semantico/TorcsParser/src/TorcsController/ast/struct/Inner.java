package TorcsController.ast.struct;

import TorcsController.ast.Type;
import TorcsController.ast.expression.BooleanLiteralExpression;
import TorcsController.ast.expression.DoubleLiteralExpression;
import TorcsController.ast.expression.Expression;
import TorcsController.ast.expression.IntegerLiteralExpression;


public class Inner {

	private String name;

	private int type;
	
	private Expression literal;
	
	public Inner(String name, int type, Expression literal){
		this.name = name;
		this.type = type;
		this.literal = literal;
	}
	
	public String getName(){
		return name;
		
	}
	
	public int getType(){
		return type;
	}
	
	public Expression getLiteral(){
		return literal;
	}

	public String toJava() {

		String innerDecl="";
		
		switch(type){
			case Type.BOOLEAN_TYPE: innerDecl="private boolean";
				break;
			case Type.DOUBLE_TYPE: innerDecl="private double";
				break;
			case Type.INT_TYPE: innerDecl="private int";
				break;
		}
		
		innerDecl = innerDecl + " " + name + " = ";
		
		if(literal != null)
		{
			switch(literal.getType()){
			case Type.BOOLEAN_TYPE: 
				if(((BooleanLiteralExpression)literal).getValue()) innerDecl = innerDecl + " " + "true";
				else innerDecl = innerDecl + " " + "false";
				break;
			case Type.DOUBLE_TYPE: innerDecl = innerDecl + ((DoubleLiteralExpression)literal).getValue();
				break;
			case Type.INT_TYPE: innerDecl = innerDecl + ((IntegerLiteralExpression)literal).getValue();
				break;
			}
		}
		
		innerDecl = innerDecl + ";";
		
		return innerDecl;
	}
	

}
