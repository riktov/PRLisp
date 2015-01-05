package org.riktov.prlisp;

import java.util.HashMap;

abstract class SpecialOperation extends LispProcedure {
	/**
	 * A special operation may choose to evaluate some arguments conditionally,
	 * rather than receiving them evaluated. So it may need the environment in
	 * which the arguments are to be evaluated,
	 */
	protected Environment argEnv;

	public LispObject apply(NilAtom n) { return n  ; }
	/**
	 * Override this if the procedure wants some arguments to be evaluated in
	 * the calling environment beforehand
	 */
	public LispObject[] ProcessArguments(LispObject[] unevaluatedArgs,
			Environment argEnv) {
		this.argEnv = argEnv; // always necessary if any args need to be
								// evaluated within apply()
		return unevaluatedArgs;
	}

	// constructors

	static HashMap<String, SpecialOperation> initialSpecials(final Environment env) {
		HashMap<String, SpecialOperation> specials = new HashMap<String, SpecialOperation>();

		/**
		 * setq - Treat the first argument as a symbol(unevaluated), and intern
		 * it with the value of the second argument
		 * TODO: Implement the paired-setq
		 */
		specials.put("setq".toUpperCase(), new SpecialOperation() {
			public LispObject apply(ConsCell argForms) {
				LispObject assignedValue = null ;

				ConsCell current = (ConsCell)argForms ;
				while(!current.cdr.isNull()) {
					String symbolName = current.car().toString().toUpperCase() ;
					current = (ConsCell)current.cdr ;
					assignedValue = current.car ;
					env.put(symbolName, assignedValue);
					current = (ConsCell)current.cdr ;
				}
				return assignedValue;
			}
		});

		/**
		 * if - Treat the first argument as a boolean(evaluated), and depending
		 * on its truth value return the value of the second or third argument.
		 */
		specials.put("if".toUpperCase(), new SpecialOperation() {
			@Override
			public LispObject apply(ConsCell argForms) {
				LispObject condition = argForms.car;
				LispObject consequent = argForms.cdr().car();
				LispObject alternate = argForms.cdr().cdr().car();

				if (!condition.eval(argEnv).isNull()) {
					return consequent.eval(argEnv);
				} else {
					return alternate.eval(argEnv);
				}
			}
		});

		// TODO: test that the skipped clause is not evaluated

		specials.put("progn".toUpperCase(), new SpecialOperation() {
			@Override
			public LispObject apply(ConsCell argForms) {
				LispObject result = null ;
				
				ConsCell current = (ConsCell)argForms ;
				while(!current.cdr.isNull()) {
					LispObject form = current.car ;
					result = form.eval(argEnv) ;
					current = (ConsCell)current.cdr ;
				}
				return result;
			}
		});

		specials.put("quote".toUpperCase(), new SpecialOperation() {
			public LispObject apply(ConsCell argForms) {
				return new ConsCell(argForms.car, new NilAtom());
			}
		});

		specials.put("lambda".toUpperCase(), new SpecialOperation() {
			public LispObject apply(ConsCell argForms) {
				ConsCell formalParamList = (ConsCell)argForms.car ;
				// LispObject body = argForms[1] ; // TODO: gather the rest not
				// just [1]

//				ConsCell argsAsLispList = formalParamList;
				ConsCell body = (ConsCell)argForms.cdr();

				String[] paramNames;

				if (formalParamList.isNull()) {
					paramNames = new String[0];
				} else {
					LispObject[] argList = ((ConsCell) formalParamList)
							.toArray();

					paramNames = new String[argList.length];

					int i;
					for (i = 0; i < argList.length; i++) {
						paramNames[i] = argList[i].toString();
					}
				}
				return new CompoundProcedure(paramNames, body, argEnv);
			}
		});

		specials.put("let".toUpperCase(), new SpecialOperation() {
			public LispObject apply(ConsCell argForms) {
				ConsCell bindingList = (ConsCell)argForms.car;
				LispObject body = argForms.cdr.car();
				
				LispObject[] bindings = bindingList.toArray();

				ChildEnvironment letEnv = new ChildEnvironment(argEnv);
				int i;
				for (i = 0; i < bindings.length; i++) {
					ConsCell binding = (ConsCell) bindings[i];
					SymbolAtom symName = (SymbolAtom) binding.car();
					LispObject value = binding.cdr().car();

					letEnv.intern(symName.toString(), value);
				}

				LispObject result = body.eval(letEnv);

				return result;
			}
		});

		// ///////////////////
		return specials;
	}
}