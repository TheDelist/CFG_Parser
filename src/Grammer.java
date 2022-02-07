import java.util.ArrayList;

public class Grammer {
	public String nonTerminal;
	public ArrayList<Terminal> terminals;
	
	public Grammer(String nonTerminal) {
		this.nonTerminal = nonTerminal;
		this.terminals = new ArrayList<Terminal>();
	}
	
	public String getNonTerminal() {
		return nonTerminal;
	}
	public void setNonTerminal(String nonTerminal) {
		this.nonTerminal = nonTerminal;
	}
	
}
