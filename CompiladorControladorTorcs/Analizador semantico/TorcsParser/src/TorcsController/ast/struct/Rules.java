package TorcsController.ast.struct;

import java.util.ArrayList;

public class Rules {

	private ArrayList<Rule> rule;
	
	public Rules(){
		this.rule = new ArrayList<Rule>();
	}
	
	public void addRule(Rule rule){
		this.rule.add(rule);
	}
	
	public ArrayList<Rule> getRule(){
		return rule;
	}

	public ArrayList<String> toJava() {
		ArrayList<String> cadena = new ArrayList<String>();
		
		cadena.add("public Action control (SensorModel sensors) {");
		cadena.add("");
		cadena.add("\tAction action = new Action();");
		
		int size = rule.size();
		for(int i = 0; i<size; i++)
		{
			cadena.add("");
			ArrayList<String> cadenaRule = rule.get(i).toJava();
			
			int sizeRule = cadenaRule.size();
			for(int j=0; j<sizeRule; j++)
			{
				cadena.add("\t"+cadenaRule.get(j));
			}
		}
		
		cadena.add("");
		cadena.add("\treturn action;");
		cadena.add("}");
		return cadena;
	}
	
}
