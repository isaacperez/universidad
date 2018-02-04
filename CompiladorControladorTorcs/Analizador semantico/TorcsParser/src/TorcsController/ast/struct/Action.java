package TorcsController.ast.struct;

import java.util.ArrayList;

import TorcsController.ast.Type;
import TorcsController.ast.statement.AssignStatement;
import TorcsController.ast.statement.BlockStatement;
import TorcsController.ast.statement.CallStatement;
import TorcsController.ast.statement.IfStatement;
import TorcsController.ast.statement.ReturnStatement;
import TorcsController.ast.statement.Statement;
import TorcsController.ast.statement.WhileStatement;


public class Action extends Method {

	/**
	 * Cuerpo del perception
	 */
	private BlockStatement actionbody;
	
	/**
	 * Constructor
	 * @param name
	 */
	public Action(int type, String name){
		super(type,name);
		this.actionbody = null;
	}
	
	
	public BlockStatement getActionBody(){
		return actionbody;
	}
	
	public void setActionBody ( BlockStatement actionbody){
		this.actionbody = actionbody;
	}


	public ArrayList<String> toJava() {
		ArrayList<String> cadena = new ArrayList<String>();
		
		
		// Cabecera de la funcion
		String args="";
		
		ArrayList<Variable> argumentos = getArguments();
		int size= argumentos.size();
		for(int i = 0; i<size;i++)
		{
			int type = argumentos.get(i).getType();
			
			if(type == Type.BOOLEAN_TYPE)
			{
				args = args + "boolean " + argumentos.get(i).getName();
			}
			else if (type == Type.DOUBLE_TYPE)
			{
				args = args + "double " + argumentos.get(i).getName();
			}
			else if (type == Type.INT_TYPE)
			{
				args = args + "int " + argumentos.get(i).getName();
			}	
			
			if(i<(size-1)) args = args +", ";
			
		}
		if(args.equals("")) cadena.add("private void "+getName()+"(SensorModel sensors, Action action"+args+") {");
		else cadena.add("private void "+getName()+"(SensorModel sensors, Action action, "+args+") {");
		
		// Cuerpo de la funcion
		Statement[] statementCuerpo = actionbody.getStatementList();
		
		int sizeCuerpo = statementCuerpo.length;
		String tab = "\t";
		Statement tipoAnterior = null;
		for(int i = 0; i<sizeCuerpo; i++)
		{
			
			if(tipoAnterior == null || !(statementCuerpo[i].getClass().equals(tipoAnterior.getClass()))) cadena.add("");
			tipoAnterior = statementCuerpo[i];
			if(statementCuerpo[i].getClass().equals(AssignStatement.class) )
			{
				AssignStatement assign = (AssignStatement) statementCuerpo[i];
				cadena.add(tab+assign.toJava());
			}
			else if (statementCuerpo[i].getClass().equals(BlockStatement.class) )
			{
				cadena.add(tab+"{");
					ArrayList<String> block = ((BlockStatement)statementCuerpo[i]).toJava();
					
					int sizeBlock = block.size();
					
					for(int b = 0; b<sizeBlock; b++)
					{
						cadena.add(tab+block.get(b));
					}
					cadena.add(tab+"}");
				
			}
			else if (statementCuerpo[i].getClass().equals(CallStatement.class))
			{
				CallStatement call = (CallStatement) statementCuerpo[i];
				cadena.add(tab+call.toJava());
			}
			else if(statementCuerpo[i].getClass().equals(IfStatement.class))
			{
				IfStatement ifstatement = (IfStatement) statementCuerpo[i];
				
				ArrayList<String> ifS = ifstatement.toJava();
				int sizeIf = ifS.size();
				
				for(int j = 0; j<sizeIf; j++)cadena.add(tab+ifS.get(j));

			}
			else if(statementCuerpo[i].getClass().equals(ReturnStatement.class))
			{
				cadena.add(tab+ ((ReturnStatement)statementCuerpo[i]).toJava() );
			}
			else if(statementCuerpo[i].getClass().equals(WhileStatement.class))
			{
				ArrayList<String> whileblock =((WhileStatement)statementCuerpo[i]).toJava();
				
				int sizeBlock = whileblock.size();
				
				for(int b = 0; b<sizeBlock; b++)
				{
					cadena.add(tab+whileblock.get(b));
				}
			}
			
		
		}
		
		// Cierre de la funcion
		cadena.add("}");
		
		return cadena;
	}
	
}
