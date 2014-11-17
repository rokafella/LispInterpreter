public class MyInterpreter {

	public static SExpression Dlist;
	
	public static SExpression myInterpreter(SExpression exp, SExpression d) {
		Dlist = d;
		try{
			//ScannerParse.printSExpression(eval(exp, new SExpression("Atom", new Token("NIL","Atom")), d));
			ScannerParse.printOutput(eval(exp, new SExpression("Atom", new Token("NIL","Atom")), d));
			return Dlist;
		}
		catch(Exception e){
			System.out.print(e.getMessage());
			return new SExpression("Atom", new Token("NIL","Atom"));
		}
	}

	private static SExpression eval(SExpression exp, SExpression a, SExpression d) throws Exception {
		if(isAtom(exp)){
			if(isTrue(exp)){
				return exp;
			}
			else if(isNull(exp)){
				return exp;
			}
			else if(isInt(exp)){
				return exp;
			}
			else if(bound(exp,a)){
				return getval(exp,a);
			}
			else{
				throw new Exception("ERROR: Broken at eval, unbound variable: "+((Token)exp.child).getVal());
			}
		}
		else{
			if(((Token)car(exp).child).getVal().equals("QUOTE")){
				return car(cdr(exp));
			}
			else if(((Token)car(exp).child).getVal().equals("COND")){
				return evcon(cdr(exp),a,d);
			}
			else if(((Token)car(exp).child).getVal().equals("DEFUN")){
				d = addToDList(cdr(exp),d);
				Dlist = d;
				return car(cdr(exp));
			}
			else {
				return apply(car(exp),evlist(cdr(exp),a,d),a,d);
			}
		}
	}

	private static SExpression addToDList(SExpression exp, SExpression d) throws Exception {
		
		return cons(cons(car(exp),cons(car(cdr(exp)),car(cdr(cdr(exp))))),d);
		
	}

	private static SExpression evcon(SExpression x, SExpression a, SExpression d) throws Exception {
		if(isNull(x)){
			throw new Exception("ERROR: Broken at eval, No condition matched evcon");
		}
		else if(isTrue(eval(car(car(x)),a,d))){
			return eval(car(cdr(car(x))),a,d);
		}
		else{
			return evcon(cdr(x),a,d);
		}
	}

	private static SExpression apply(SExpression f, SExpression x, SExpression a, SExpression d) throws Exception {
		if(isAtom(f)){
			if(((Token)f.child).getVal().equals("CAR")){
				return car(car(x));
			}
			else if(((Token)f.child).getVal().equals("CDR")){
				return cdr(car(x));
			}
			else if(((Token)f.child).getVal().equals("CONS")){
				return cons(car(x),car(cdr(x)));
			}
			else if(((Token)f.child).getVal().equals("ATOM")){
				if(isNull(cdr(x))){
					if(isAtom(car(x))){
						return new SExpression("Atom", new Token("T","Atom"));
					}
					else{
						return new SExpression("Atom", new Token("NIL","Atom"));
					}
				}
				else{
					throw new Exception("ERROR: Broken at apply, Atom is a unary function");
				}
			}
			else if(((Token)f.child).getVal().equals("EQ")){
				if(isNull(cdr(cdr(x)))){
					if(eq(car(x),car(cdr(x)))){
						return new SExpression("Atom", new Token("T","Atom"));
					}
					else{
						return new SExpression("Atom", new Token("NIL","Atom"));
					}
				}
				else{
					throw new Exception("ERROR: Broken at apply, EQ is a binary function");
				}
			}
			else if(((Token)f.child).getVal().equals("INT")){
				if(isNull(cdr(x))){
					if(isInt(car(x))){
						return new SExpression("Atom", new Token("T","Atom"));
					}
					else{
						return new SExpression("Atom", new Token("NIL","Atom"));
					}
				}
				else{
					throw new Exception("ERROR: Broken at apply, INT is a unary function");
				}
			}
			else if(((Token)f.child).getVal().equals("NULL")){
				if(isNull(cdr(x))){
					if(isNull(car(x))){
						return new SExpression("Atom", new Token("T","Atom"));
					}
					else{
						return new SExpression("Atom", new Token("NIL","Atom"));
					}
				}
				else{
					throw new Exception("ERROR: Broken at apply, NULL is a unary function");
				}
			}
			else if(((Token)f.child).getVal().equals("PLUS")){
				if(isNull(cdr(cdr(x)))){
					return plus(car(x),car(cdr(x)));
				}
				else{
					throw new Exception("ERROR: Broken at apply, Plus is a binary function");
				}
			}
			else if(((Token)f.child).getVal().equals("MINUS")){
				if(isNull(cdr(cdr(x)))){
					return minus(car(x),car(cdr(x)));
				}
				else{
					throw new Exception("ERROR: Broken at apply, Minus is a binary function");
				}
			}
			else if(((Token)f.child).getVal().equals("TIMES")){
				if(isNull(cdr(cdr(x)))){
					return times(car(x),car(cdr(x)));
				}
				else{
					throw new Exception("ERROR: Broken at apply, Times is a binary function");
				}
			}
			else if(((Token)f.child).getVal().equals("QUOTIENT")){
				if(isNull(cdr(cdr(x)))){
					return quotient(car(x),car(cdr(x)));
				}
				else{
					throw new Exception("ERROR: Broken at apply, Quotient is a binary function");
				}
			}
			else if(((Token)f.child).getVal().equals("REMAINDER")){
				if(isNull(cdr(cdr(x)))){
					return remainder(car(x),car(cdr(x)));
				}
				else{
					throw new Exception("ERROR: Broken at apply, Remainder is a binary function");
				}
			}
			else if(((Token)f.child).getVal().equals("LESS")){
				if(isNull(cdr(cdr(x)))){
					return less(car(x),car(cdr(x)));
				}
				else{
					throw new Exception("ERROR: Broken at apply, Less is a binary function");
				}
			}
			else if(((Token)f.child).getVal().equals("GREATER")){
				if(isNull(cdr(cdr(x)))){
					return greater(car(x),car(cdr(x)));
				}
				else{
					throw new Exception("ERROR: Broken at apply, Greater is a binary function");
				}
			}
			else{
				if(bound(f,d)){
					return eval(cdr(getval(f,d)), addpairs(car(getval(f,d)),x,a), d);
				}
				else{
					throw new Exception("ERROR: Broken at apply, function: "+((Token)f.child).getVal()+" not defined");
				}
			}
		}
		else{
			throw new Exception("ERROR: Broken at apply, function should be an atom");
		}
	}
	
	private static SExpression addpairs(SExpression x, SExpression y, SExpression z) throws Exception {
		if(isNull(x) || isNull(y)){
			return z;
		}
		else{
			return addpairs(cdr(x),cdr(y),cons(cons(car(x),car(y)),z));
		}
	}

	private static Boolean bound(SExpression x, SExpression z) throws Exception{
		if(isNull(z)){
			return false;
		}
		else{
			if(eq(x,car(car(z)))){
				return true;
			}
			else{
				return bound(x,cdr(z));
			}
		}
	}
	
	private static SExpression getval(SExpression x, SExpression z) throws Exception{
		if(eq(x,car(car(z)))){
			return cdr(car(z)); 
		}
		else{
			return getval(x,cdr(z));
		}
	}
	
	private static SExpression greater(SExpression x, SExpression y) throws Exception {
		if(isInt(x)&&isInt(y)){
			if(Integer.parseInt(((Token)x.child).getVal()) > Integer.parseInt(((Token)y.child).getVal())){
				return new SExpression("Atom", new Token("T","Atom"));
			}
			else{
				return new SExpression("Atom", new Token("NIL","Atom"));
			}
		}
		else{
			throw new Exception("ERROR: Broken at eval, only integers can be compared");
		}
	}
	
	private static SExpression less(SExpression x, SExpression y) throws Exception {
		if(isInt(x)&&isInt(y)){
			if(Integer.parseInt(((Token)x.child).getVal()) < Integer.parseInt(((Token)y.child).getVal())){
				return new SExpression("Atom", new Token("T","Atom"));
			}
			else{
				return new SExpression("Atom", new Token("NIL","Atom"));
			}
		}
		else{
			throw new Exception("ERROR: Broken at eval, only integers can be compared");
		}
	}

	private static SExpression remainder(SExpression x, SExpression y) throws Exception {
		if(isInt(x)&&isInt(y)){
			int result = Integer.parseInt(((Token)x.child).getVal()) % Integer.parseInt(((Token)y.child).getVal());
			return new SExpression("Atom", new Token(String.valueOf(result),"Atom"));
		}
		else{
			throw new Exception("ERROR: Broken at eval, only integers can be divided");
		}
	}
	
	private static SExpression quotient(SExpression x, SExpression y) throws Exception {
		if(isInt(x)&&isInt(y)){
			int result = Integer.parseInt(((Token)x.child).getVal()) / Integer.parseInt(((Token)y.child).getVal());
			return new SExpression("Atom", new Token(String.valueOf(result),"Atom"));
		}
		else{
			throw new Exception("ERROR: Broken at eval, only integers can be divided");
		}
	}
	
	private static SExpression times(SExpression x, SExpression y) throws Exception {
		if(isInt(x)&&isInt(y)){
			int result = Integer.parseInt(((Token)x.child).getVal()) * Integer.parseInt(((Token)y.child).getVal());
			return new SExpression("Atom", new Token(String.valueOf(result),"Atom"));
		}
		else{
			throw new Exception("ERROR: Broken at eval, only integers can be multiplied");
		}
	}
	
	private static SExpression minus(SExpression x, SExpression y) throws Exception {
		if(isInt(x)&&isInt(y)){
			int result = Integer.parseInt(((Token)x.child).getVal()) - Integer.parseInt(((Token)y.child).getVal());
			return new SExpression("Atom", new Token(String.valueOf(result),"Atom"));
		}
		else{
			throw new Exception("ERROR: Broken at eval, only integers can be subtracted");
		}
	}
	
	private static SExpression plus(SExpression x, SExpression y) throws Exception {
		if(isInt(x)&&isInt(y)){
			int result = Integer.parseInt(((Token)x.child).getVal()) + Integer.parseInt(((Token)y.child).getVal());
			return new SExpression("Atom", new Token(String.valueOf(result),"Atom"));
		}
		else{
			throw new Exception("ERROR: Broken at eval, only integers can be added");
		}
	}

	private static boolean eq(SExpression x, SExpression y) {
		try{
			if(((Token)x.child).getVal().equals(((Token)y.child).getVal())){
				return true;
			}
			else{
				return false;
			}
		}
		catch (Exception e){
			return false;
		}
	}

	private static SExpression evlist(SExpression x, SExpression a, SExpression d) throws Exception {
		if(isNull(x)){
			return new SExpression("Atom", new Token("NIL","Atom"));
		}
		else{
			return cons(eval(car(x),a,d), evlist(cdr(x),a,d)); 	
		}
	}

	public static SExpression cons(SExpression x, SExpression y) {
		SExpression z = new SExpression("New",null);
		((CSExpression)z.child).left = x;
		((CSExpression)z.child).right = y;
		return z;
	}

	private static boolean isNull(SExpression x) {
		if(isAtom(x)){
			if(((Token)x.child).getVal().equals("NIL")){
				return true;
			}
		}
		return false;
	}
	
	private static boolean isTrue(SExpression x) {
		if(isAtom(x)){
			if(((Token)x.child).getVal().equals("T")){
				return true;
			}
		}
		return false;
	}

	public static SExpression car(SExpression s) throws Exception{
		if(s.child.getClass() != Token.class){
			CSExpression cs = (CSExpression) s.child;
			if(cs.left!=null){
				return cs.left;	
			}
			else{
				return new SExpression("Atom", new Token("NIL","Atom"));
			}
		}
		else{
			throw new Exception("ERROR: Car is undefined for atoms");
		}
	}
	
	public static SExpression cdr(SExpression s) throws Exception{
		if(s.child.getClass() != Token.class){
			CSExpression cs = (CSExpression) s.child;
			if(cs.right!=null){
				return cs.right;	
			}
			else{
				return new SExpression("Atom", new Token("NIL","Atom"));
			}
		}
		else{
			throw new Exception("ERROR: Cdr is undefined for atoms");
		}
	}
	
	private static boolean isInt(SExpression exp) {
		try{
			Integer.parseInt(((Token)exp.child).getVal());
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static boolean isAtom(SExpression exp) {
		if(exp.child.getClass() == Token.class){
			return true;
		}
		else{
			return false;
		}
	}
}
