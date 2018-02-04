package TorcsController.ast.struct;

import java.util.ArrayList;

import TorcsController.ast.expression.BinaryExpression;
import TorcsController.ast.expression.BooleanLiteralExpression;
import TorcsController.ast.expression.CallExpression;
import TorcsController.ast.expression.DoubleLiteralExpression;
import TorcsController.ast.expression.Expression;
import TorcsController.ast.expression.IntegerLiteralExpression;
import TorcsController.ast.expression.PerceptionReferenceExpression;
import TorcsController.ast.expression.UnaryExpression;
import TorcsController.ast.expression.VariableExpression;

public class Rule {

	private Expression expression;
	
	private ArrayList<CallExpression> methodCall;
	
	public Rule(Expression expression){
		this.expression = expression;
		this.methodCall = new ArrayList<CallExpression>();
	}
	
	public void addCallExpression(CallExpression methoCall){
		this.methodCall.add(methoCall);
	}
	
	public ArrayList<CallExpression> getCallExpression(){
		return methodCall;
	}
	
	public Expression getExpression(){
		return this.expression;
	}

	public ArrayList<String> toJava() {
		ArrayList<String> cadena = new ArrayList<String>();
		
		String cabecera="if( ";
		
		if(expression.getClass().equals(BinaryExpression.class) )
		{
			cabecera = cabecera + ((BinaryExpression)expression).toJava();
		}
		else if(expression.getClass().equals(BooleanLiteralExpression.class) )
		{
			cabecera = cabecera +  ((BooleanLiteralExpression)expression).toJava();
		}
		else if(expression.getClass().equals(CallExpression.class) )
		{
			cabecera = cabecera + ((CallExpression)expression).toJava();
		}
		else if(expression.getClass().equals(DoubleLiteralExpression.class) )
		{
			cabecera = cabecera + ((DoubleLiteralExpression)expression).toJava();
		}
		else if(expression.getClass().equals(IntegerLiteralExpression.class) )
		{
			cabecera = cabecera + ((IntegerLiteralExpression)expression).toJava();
		}
		else if(expression.getClass().equals(PerceptionReferenceExpression.class) )
		{
			cabecera = cabecera + ((PerceptionReferenceExpression)expression).toJava();
		}
		else if(expression.getClass().equals(UnaryExpression.class) )
		{
			cabecera = cabecera + ((UnaryExpression)expression).toJava();
		}
		else if(expression.getClass().equals(VariableExpression.class) )
		{
			cabecera = cabecera + ((VariableExpression)expression).toJava();
		}
		
		cadena.add(cabecera + ") {");
		
		int size = methodCall.size();
		
		for(int i = 0; i<size; i++)
		{
			cadena.add("\t"+methodCall.get(i).toJava()+";");
		}
		
		cadena.add("}");
		
		return cadena;
		
	}
	
}
