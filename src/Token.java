public class Token {
	
	private String val;
	private String type;
	
	public Token(String val, String type){
		this.val = val;
		this.type = type;
	}
	
	public Token(Token t) {
		val = t.val;
		type = t.type;
	}

	public String getVal(){
		return val;
	}
	
	public String getType(){
		return type;
	}
	
}
