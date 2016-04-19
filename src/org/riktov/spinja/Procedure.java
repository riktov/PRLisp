package org.riktov.spinja;

//import java.util.List ;
//import java.util.ArrayList ;

/**
 * A LispProcedure can be a compound procedure (lambda), a primitive procedure, or a special operation
 * @author Paul RICHTER &lt;riktov@freeshell.de&gt;
 *
 */
abstract class LispProcedure extends LispObject {
	abstract LispObject apply(LispList argsToApply) ;
    public LispList processArguments(LispList argForms, Environment evalEnv) {
    	//System.out.println("LispProcedure.processArguments() : argForms: " + argForms) ;
		return argForms.listOfValues(evalEnv) ;
	}
    /**
     * requireArgumentCount - 
     * @param count - number of arguments this function requires
     * @param argForms - the actual passed arguments
     * @param procName - the function name, used for the error message
     * @return Normally returns true, otherwise throw a restart condition
     */
	protected boolean requireArgumentCount(int count, LispList argForms, String procName) {
		int len = argForms.length() ;
		if(len != count) {
			new LispRestarter().offerRestarts("The procedure " + procName + " has been called with " + len + " arguments; it requires exactly " + count + " argument(s).") ;
			throw new LispAbortEvaluationException() ;
		}	
		return true ;
	}

	protected boolean requireArgumentCountAtLeast(int count, LispList argForms, String procName) {
		int len = argForms.length() ;
		if(len < count) {
			new LispRestarter().offerRestarts("The procedure " + procName + " has ben called with " + len + " arguments; it requires at least " + count + " argument(s).") ;
			throw new LispAbortEvaluationException() ;
		}	
		return true ;
	}

}

class CompoundProcedure extends LispProcedure {
	//private LispObject formalParams; //may be a list, or an Atom for list-binding
	private Bindable formalParams; //may be a list, or an Atom for list-binding
	private LispList body;
	private Environment env;
	// constructors
	/**
	 * Constructor
	 * @param formalParams
	 * @param body
	 * @param env
	 */
	/*
	public CompoundProcedure(Bindable formalParams, LispList body, Environment env) {
		this.formalParams = formalParams;
		this.body = body;
		this.env = env;
	}
	*/
	
	public CompoundProcedure(ConsCell formalParams, LispList body, Environment env) {
		this.formalParams = new ParameterList(formalParams) ;
		this.body = body;
		this.env = env;		
	}	

	public CompoundProcedure(SymbolAtom formalParams, LispList body, Environment env) {
		this.formalParams = new ParameterSymbol(formalParams) ;
		this.body = body;
		this.env = env;		
	}	

	public CompoundProcedure(NilAtom formalParams, LispList body, Environment env) {
		this.formalParams = new ParameterNull() ;
		this.body = body;
		this.env = env;		
	}
	
	public CompoundProcedure(LispList formalParams, ConsCell body, Environment env) {
		if(formalParams.isNull()) {
			this.formalParams = new ParameterNull() ;
		} else {
			this.formalParams = new ParameterList((ConsCell)formalParams) ;
		}
		this.body = body;
		this.env = env;				
	}

	// implementation of LispObject
	/**
	 * Copying CLISP notation
	 */
	@Override public String toString() {
		ConsCell paramsAndBody = new ConsCell((LispObject)formalParams, (LispObject) body) ;
		return "#<FUNCTION :LAMBDA " + paramsAndBody.toString(true) + ">"; 
		}

	/**
	 * accessors
	 */
	Bindable formalParams() { return formalParams ; }//can be a list, or nil, or a rest-bound atom
	LispList body() { return body ; }	

	/**
	 * APPLY Evaluate in turn each subform of the procedure body, in an environment created by extending
	 * the procedure's own environment with the argument bindings.
	 * 
	 * @param argVals
	 *            This is an array of objects (evaluated) that is passed to the
	 *            method.
	 * @return LispObject The value of the last form in the procedure body
	 * @throws LispAbortEvaluationException 
	 */
	public LispObject apply(LispList argForms) {
		if(!formalParams.matchCount(argForms)) {
			new LispRestarter().offerRestarts("Invalid argument count") ;
		}
		Environment newEnv = new ChildEnvironment(env);
		formalParams.bindValues(argForms, newEnv) ;

		return body.evalSequence(newEnv) ;
	}	
}

/**
 * Bindable provides functionality for function parameters, which may be
 * a list, or a SymbolAtom (bound to a list), or nil.
 * @author paul
 *
 */
interface Bindable {
	void bindValues(LispList values, Environment env) ;
	boolean isAtom();
	boolean matchCount(LispList values) ;
}

class ParameterList extends ConsCell implements Bindable {
	public ParameterList(LispObject car, LispObject cdr) {
		super(car, cdr);
	}

	public ParameterList(LispObject []objects) {
		super(objects);
	}

	public ParameterList(ConsCell params) {
		super(params.car, params.cdr) ;
	}

	@Override
	public void bindValues(LispList values, Environment env) {
		LispObject currentParam = this ;
		LispList currentVal = values ;	// ConsCell or NilAtom
		
		while(!currentParam.isNull()) {
			ConsCell currentValCell = (ConsCell)currentVal ;
			LispObject val = currentValCell.car() ;
			
			String sym ;
			//we still need to handle (define (myfunc foo bar . baz)...
			if(currentParam.isAtom()) {
				sym = currentParam.toString() ;
				ParameterSymbol ps = new ParameterSymbol(sym) ;
				ps.bindValues(currentVal, env) ;
				return ;
			} else {
				ConsCell currentParamCell = (ConsCell)currentParam ;
				sym = currentParamCell.car().toString() ;
			
				env.intern(sym, val) ;
			
				currentVal = (LispList) currentValCell.cdr() ;
				currentParam = currentParamCell.cdr() ;
			}
		}
	}
	
	public boolean matchCount(LispList values) {
		return (values.length() == length()) ;
	}
}

class ParameterSymbol extends SymbolAtom implements Bindable {
	public ParameterSymbol(String s) {
		super(s);
	}

	public ParameterSymbol(SymbolAtom s) {
		super(s.toString());
	}
@Override
	public void bindValues(LispList values, Environment env) {
		env.intern(toString(), (LispObject) values) ;		
	}

	public boolean isAtom() {
		return super.isAtom() ;
	}

	public boolean matchCount(LispList values) {
		return true ;
	}
}

class ParameterNull implements Bindable {
	public void bindValues(LispList values, Environment env) {
		// do nothing
	}

	@Override
	public boolean isAtom() {
		return false;
	}

	public boolean matchCount(LispList values) {
		return (values.length() == 0) ;
	}
}
