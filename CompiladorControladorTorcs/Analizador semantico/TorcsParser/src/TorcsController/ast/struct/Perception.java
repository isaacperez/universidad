package TorcsController.ast.struct;

import java.util.ArrayList;

import TorcsController.ast.Type;
import TorcsController.ast.statement.BlockStatement;
import TorcsController.ast.statement.CallStatement;
import TorcsController.ast.statement.IfStatement;
import TorcsController.ast.statement.ReturnStatement;
import TorcsController.ast.statement.Statement;
import TorcsController.ast.statement.WhileStatement;
import TorcsController.TokenConstants;
import TorcsController.ast.statement.AssignStatement;


public class Perception extends Method implements TokenConstants {

	
	/**
	 * Cuerpo del perception
	 */
	private BlockStatement perceptionBody;

	/**
	 * Constructor
	 * @param name
	 */
	public Perception(int type,String name){
		super(type,name);
		this.perceptionBody = null;
	}
	
	
	public BlockStatement getPerceptionBody(){
		return perceptionBody;
	}

	
	public void setPerceptionBody ( BlockStatement perceptionBody){
		this.perceptionBody = perceptionBody;
	}
	
	public void addPerceptionStatement (Statement stm){
		perceptionBody.addStatement(stm);
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
		if(args.equals("")) cadena.add("private boolean "+getName()+"(SensorModel sensors"+args+") {");
		else cadena.add("private boolean "+getName()+"(SensorModel sensors, "+args+") {");
		
		
		
		// Cuerpo de la funcion
		Statement[] statementCuerpo = perceptionBody.getStatementList();
		
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
