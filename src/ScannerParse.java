import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class ScannerParse {
	
	public static SExpression d = new SExpression("Atom", new Token("NIL","Atom"));
	
	public static void main(String[] args) throws IOException {
		
//		Scanner sc = new Scanner(System.in);
//		
//		while(sc.hasNextLine()){
//			scanner(sc.nextLine());
//		}
		
		try (BufferedReader br = new BufferedReader(new FileReader("input.txt")))
		{
 
			String currentLine;
			
			while ((currentLine = br.readLine()) != null) {
				//System.out.println("Sending to Scanner >>> "+ currentLine);
				scanner(currentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		parse();
		
		while(it<allTokens.size()&&!Error){
			it--;
			Token t = getToken();
			if(t.getType()=="New")
			parse();
		}
		
		int i = 0;
		
		while(i<allSExpression.size()){
			//System.out.print("SExpression "+(i+1)+": ");
			//printSExpression(allSExpression.get(i));
			//System.out.println(" ");
			//System.out.print("Interpreter "+(i+1)+": ");
			d = MyInterpreter.myInterpreter(allSExpression.get(i),d);
			//printSExpression(MyInterpreter.cons(allSExpression.get(0),allSExpression.get(1)));
			//printSExpression(MyInterpreter.cdr(allSExpression.get(i)));
			System.out.println("");
			i++;
		}
	}
	
	public static void printSExpression(SExpression s) {
		if(s.child.getClass() == Token.class){
			System.out.print(((Token) s.child).getVal());
		}
		else{
			System.out.print("(");
			CSExpression cs = (CSExpression) s.child;
			printSExpression(cs.left);
			System.out.print(" . ");
			printSExpression(cs.right);
			System.out.print(")");
		}
	}
	
	public static void printOutput(SExpression s){
		if(s!=null){
			if(s.child.getClass() == Token.class){
				System.out.print(((Token) s.child).getVal());
			}
			else{
				if(checkRightForNil(((CSExpression)s.child).right)){
					CSExpression cs = (CSExpression) s.child;
					System.out.print("(");
					printOutput(cs.left);
					printList(cs.right);
					System.out.print(")");
				}
				else{
					System.out.print("(");
					CSExpression cs = (CSExpression) s.child;
					printOutput(cs.left);
					System.out.print(" . ");
					printOutput(cs.right);
					System.out.print(")");
				}
			}
		}
	}
	
	private static void printList(SExpression s){
		if(s.child.getClass() == Token.class){
			if(!((Token) s.child).getVal().equals("NIL")){
				System.out.print(" "+((Token) s.child).getVal());
			}
		}
		else{
			CSExpression cs = (CSExpression) s.child;
			System.out.print(" ");
			printOutput(cs.left);
			printList(cs.right);
		}
	}
	
	public static Boolean checkRightForNil(SExpression s){
		if(s.child.getClass() == Token.class){
			if(((Token) s.child).getVal().equals("NIL")){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return checkRightForNil(((CSExpression)s.child).right);
		}
	}

	static boolean Error = false;
	static int openParanCount = 0;
	static boolean newLine = true;
	static List<Token> allTokens = new ArrayList<Token>();
	static List<SExpression> allSExpression = new ArrayList<SExpression>();
	
	private static void scanner(String currentLine) {
		
		StringTokenizer st = new StringTokenizer(currentLine);
		
	     while (st.hasMoreTokens()) {
	         
	         char token[] = st.nextToken().toUpperCase().toCharArray();
	         
	        	 switch(token[0]) { 
		         	case '(': 	newLine = true;
		         				allTokens.add(new Token("(","LeftParan"));
		         				//System.out.println("New Token-> (");
		         				openParanCount++;
		         				if(token.length>1){
		         					String temp = new String(token);
		         					scanner(temp.substring(1));
		         				}
		         				break;
		         	case ')': 	newLine = true;
					         	allTokens.add(new Token(")","RightParan"));
					         	openParanCount--;
			     				//System.out.println("New Token-> )");
					         	if(token.length>1){
			     					String temp = new String(token);
			     					scanner(temp.substring(1));
			     				}
		         				//System.out.println("Right Paran");
	 							break;
		         	case '.': 	newLine = true;
		         				allTokens.add(new Token(".","Dot"));
		         				//System.out.print(" .");
					         	if(token.length>1){
					         		if(token[1]==')'||token[1]=='('){
					         			String temp = new String(token);
				     					scanner(temp.substring(1));
					         		}
					         		else
					         		allTokens.add(new Token("ERROR: Parsing failed, '"+token[1]+"' cannot be after Dot.","Error"));
					         		//System.out.print("<-- Error at parsing, '"+token[1]+"' cannot be after Dot.");
			     				}
								break;
		         	case '+':case '-':case '0': case '1':case '2':case '3':
		         	case '4':case '5': case '6':case '7':case '8':case '9':
		         				//System.out.print("Numeric Atom -> ");
//				         		if(openParanCount == 0){
//		     						System.out.print("\n");
//		     					}
		         				newLine = true;
		         				int start = 0;
		         				boolean check = false;
		         				StringBuilder sb = new StringBuilder();
		 
		         				if(token[0]=='+'||token[0]=='-'){
		         					if(token.length>1){
		         						start = 1;
		         						if(token[0]=='-'){
		         							sb.append("-");
		         						}
		         					}
		         				}
		         				
	         					for(int j = start; j<token.length; j++){	     //HANDLE +(    						
	         						if(token[j]>47 && token[j]<58){
	         							check = true;
	         							sb.append(token[j]);
			         					//i = token.length;
		         					}
	         						else if (token[j]==')'||token[j]=='('){
	         							allTokens.add(new Token(sb.toString(),"Atom"));
	         							check = false;
	         							String temp = new String(token);
			         					scanner(temp.substring(j));
	         							//System.out.println("\nRight Paran");
	         							break;
	         						}
		         					else{
		         						//i=j-1;
		         						allTokens.add(new Token("ERROR: Parsing failed, '"+token[j]+"' cannot be a number.","Error"));
		         						//System.out.print("<-- Error at parsing, '"+token[j]+"' cannot be a number.");
		         						break;
		         					}
		         				}
	         					if(check)
	         					allTokens.add(new Token(sb.toString(),"Atom"));
		         				//System.out.print('\n');
								break;
		         	default: 	//if(openParanCount == 0){
					         	//	System.out.print("\n");
			 					//}
		         				if(token[0]>64 && token[0]<91){
		         					newLine = true;
		         					boolean check2=true;
		         					//System.out.print(" ");
		         					//System.out.print("Literal Atom -> ");
		         					StringBuilder sb2 = new StringBuilder();
		         					//System.out.print(token[0]);
		         					sb2.append(token[0]);
					         		for(int j = 1; j<token.length; j++){
			         					if(token[j]==')'||token[j]=='('){
			         						allTokens.add(new Token(sb2.toString(),"Atom"));
			         						check2 = false;
			         						String temp = new String(token);
				         					scanner(temp.substring(j));
			         						//System.out.println("\nRight Paran");
			         						break;
			         					}
			         					else if((token[j]>64 && token[j]<91)||(token[j]>47 && token[j]<58)) {
			         						//System.out.print(token[j]);
			         						sb2.append(token[j]);
			         					}
			         					else{
			         						allTokens.add(new Token("ERROR: Parsing failed, '"+token[j]+"' cannot be a literal.","Error"));
			         						//System.out.print("<-- Error at parsing, '"+token[j]+"' cannot be a literal.");
			         						break;
			         					}
			         				}
					         		if(check2)
					         		allTokens.add(new Token(sb2.toString(),"Atom"));
					         		//System.out.print(" ");
					         		//System.out.print('\n');
		         				}
		         				else{
		         					allTokens.add(new Token("ERROR: Interpreter failed at Scanner: '"+token[0]+"' is not valid.","Error"));
		         					//System.out.print("<-- Interpreter failed at Scanner: '"+token[1]+"' is not valid.");
		         				}
		         				break;
		         }
	        	 if(openParanCount == 0 && newLine){
	        		 	allTokens.add(new Token("NewExp","New"));
						newLine = false;
	        	 }
	        	 //System.out.print(" ");
	        // }
	     }
		
	}

	
	//static Iterator<Token> it;
	
	static int it = 0;
	
	private static Token getToken(){
		//it = allTokens.iterator();
		if(it <  allTokens.size()){
			Token tk = allTokens.get(it);
			it++;
			//System.out.println("Sending->"+tk.getVal());
			return tk;		
		}
		return null;
	}
	
/*	First Set

	F(S) = {atom, '('}
	F(E) = {atom, '('}
	F(X) = {atom, '(', ')'}
	F(Y) = {'.', atom, '(', ')'}
	F(R) = {atom, '(', 'ϵ'}

	Follow Set

	FL(S) = {'$'}
	FL(E) = {'.', atom, '(', ')', '$'}
	FL(X) = {'.', atom, '(', ')', '$'}
	FL(Y) = {'.', atom, '(', ')', '$'}
	FL(R) = {')', '$'}


	Parsing Table
			.		atom		(		)		$
	S 				S->E		S->E
	E				E->atom		E->(X
	X				X->EY		X->EY	X->)
	Y	   Y->.E)   Y->R)		Y->R)   Y->R)
	R				R->ER		R->ER	R->ϵ		R->ϵ */
	
	static String [][] table = {
			{null,"E","E",null,null},
			{null,"Atom","(X",null,null},
			{null,"EY","EY",")",null},
			{".E)","R)","R)","R)",null},
			{null,"ER","ER","_","_"}
	};
	
	static String nonTerms = "SEXYR";
	static String[] terms = {"Dot","Atom","LeftParan","RightParan","$"};
	static Stack<Token> stack = new Stack<Token>();
	
	private static void parse() {
		
		stack = new Stack<Token>();
		
		Token token =  getToken();
		
		Token top = new Token("$","$");
		
		stack.push(top);
		stack.push(new Token("S","NT"));
		
		while(true) { 
	
			try{
				top = stack.pop();
			}
			catch(Exception e){
				System.out.println("ERROR: Expression do not match the grammar");
				System.exit(0);
				Error = true;
				//top = null;
				return;
			}
			
			if(token==null){
				//System.out.println("Grammar Rejected");
				//return false;
				break;
			}
			
			if(token.getType()=="New"){ 
				break;
			}
			
			if(top.getType()=="NT"){					//NT---> Non Terminal
				
				String rule = findRule(top,token);
				//System.out.println(rule);
				if(rule==null){
					//System.out.println("Grammar failed, no grammar");
					break;
					//return false;
				}
				else if(rule=="Atom"){
					stack.push(token);
				}
				else{
					applyRule(rule);						//IF NULL THROW ERROR
				}
				
			}
			else if(top.getType()=="Atom"||top.getType()=="Dot"||top.getType()=="LeftParan"||top.getType()=="RightParan"){
				if(!top.getVal().equals(token.getVal())){
					System.out.println("ERROR: GRAMMAR Failed wrong Terminal");
					System.exit(0);
					Error = true;
					//return false;
					break;
				}
				else{
					//System.out.println("Matching terminal-->"+token.getVal());
					addToTree(token);
					token = getToken();
					// GET NEXT TOKEN and store in token
				}
			}	
		}
		//System.out.println(top.getVal());
		if(top.getType()=="$"){
			//System.out.println("Grammar Accepted");
			addTreeToList();
			//return true;
		}
		else{
			System.out.println("ERROR: Expression do not satisfy the grammar");
			System.exit(0);
			Error = true;
		}
		
	}
	
	public static void applyRule(String rule){
		
		for(int i = rule.length()-1; i>=0; i--){
			char ch = rule.charAt(i);
			switch(ch) {
				case '.': stack.push(new Token(".","Dot"));
				break;
				case '(': stack.push(new Token("(","LeftParan"));
				break;
				case ')': stack.push(new Token(")","RightParan"));
				break;
				case '_': //System.out.println("Rule space found");
				break;
				default: stack.push(new Token(String.valueOf(ch),"NT"));
				//System.out.println("Defualt-->>>" + ch);
				break;
			}
		}
		
	}
	
	public static String findRule(Token top, Token token) {
		
		int row = nonTerms.indexOf(top.getVal());
		int col = -1;
		
		for(int i = 0; i<terms.length; i++){
			if(terms[i]==token.getType()){
				col = i;
				break;
			}
		}
		
		if(col != -1){
			String rule = table[row][col];
			
			if(rule==null){
				System.out.println("ERROR: No grammar rule found for given expression");
				System.exit(0);
			}
		
			return rule;
		}
		else{
			if(token.getType()=="Error"){
				System.out.println(token.getVal());
				System.exit(0);
			}
			else{
				System.out.println("ERROR: "+token.getType()+" rule not found");
				System.exit(0);
			}
			return null;
		}
	}
	
	static CSExpression currentTree;
	static SExpression	lastNode;
	static CSExpression oldTree;
	static CSExpression temp;
	static Boolean lastDot = false;
	static int ExtraParan = 0;
	
	public static void addToTree(Token t){
		if(t==null){
			currentTree = null;
		}
		if(t.getType()=="Atom"){
			if(ExtraParan > 0) ExtraParan -= 1;
			lastNode = new SExpression("Atom",t);
			if(!lastDot){
				if(currentTree!=null){
					if(currentTree.left!=null){
						currentTree.right = new SExpression("New",null); 
						if(oldTree == null){
							oldTree = currentTree;
						}
						temp = currentTree;
						currentTree = (CSExpression) currentTree.right.child;
						currentTree.parent = temp;
					}
					currentTree.left = lastNode;
				}
			}
			else{
				currentTree.right = lastNode;
			}
			lastDot = false;
		}
		else if(t.getType()=="LeftParan"){
			
			if(currentTree==null){
				currentTree = new CSExpression();
			}
			else{
				if(currentTree.left == null){
					currentTree.left = new SExpression("New",null);
					if(oldTree == null){
						oldTree = currentTree;
					}
					temp = currentTree;
					currentTree = (CSExpression) currentTree.left.child;
					currentTree.parent = temp;
				}
				else{
					currentTree.right = new SExpression("New",null); 
					if(oldTree == null){
						oldTree = currentTree;
					}
					temp = currentTree;
					currentTree = (CSExpression) currentTree.right.child;
					currentTree.parent = temp;
					
					// One more subtree because of Left Parenthesis at right child
					if(!lastDot){
						currentTree.left = new SExpression("New",null);
						temp = currentTree;
						currentTree = (CSExpression) currentTree.left.child;
						currentTree.parent = temp;
					}
					lastDot = false;
				}
			}
		}
		else if(t.getType()=="RightParan"){
			if(currentTree.left == null){
				if(currentTree.parent!=null){
					temp = currentTree.parent;
					if(temp.left.child == currentTree){
						temp.left = null;
					}
					else{
						temp.right = null;
					}
					currentTree = temp;
					if(currentTree.left == null){
						currentTree.left = new SExpression("Atom", new Token("NIL","Atom"));
					}
					else{
						currentTree.right = new SExpression("Atom", new Token("NIL","Atom"));
					}
				}
				else{
					currentTree = null;
					lastNode = new SExpression("Atom",new Token("NIL","Atom"));
				}
			}
			else if(currentTree!= null && currentTree.right == null && ExtraParan == 0){
				currentTree.right = new SExpression("Atom", new Token("NIL","Atom"));
			}
	
			if(ExtraParan > 0) ExtraParan -= 1;
			
			while(currentTree!=null && currentTree.parent != null && currentTree.right != null){
				currentTree = currentTree.parent;
				if(currentTree.right != null && currentTree.parent != null){
					currentTree = currentTree.parent;
					//ExtraParan += 1;
				}
			}
			
		}
		else if(t.getType()=="Dot"){
			lastDot = true;
		}
	}
	
	public static void addTreeToList(){
		
		if(currentTree != null && currentTree.right == null && ExtraParan == 0){
			currentTree.right = new SExpression("Atom", new Token("NIL","Atom"));
		}
		
		if(oldTree != null){
			allSExpression.add(new SExpression(oldTree));
		}
		else if(currentTree != null){
			allSExpression.add(new SExpression(currentTree));
		}
		else if(lastNode != null){
			allSExpression.add(lastNode);
		}
		
		lastNode = null;
		currentTree = null;
		oldTree = null;
		lastDot = false;
		ExtraParan = 0;
	}
}