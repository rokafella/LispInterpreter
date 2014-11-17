public class SExpression {

	//public Token token;
	//public CSExpression child;
	
	public Object child;
	
	public SExpression(String s,Token t){
		if(s.equals("Atom")){
			child = new Token(t);
		}
		else{
			child = new CSExpression();
		}
	}
	
	public SExpression(CSExpression exp){
		child = (CSExpression) exp;
	}
	
}
