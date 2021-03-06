/*
 [The "BSD licence"]
 Copyright (c) 2005-2008 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.eclipse.persistence.internal.libraries.antlr.runtime.tree;

import org.eclipse.persistence.internal.libraries.antlr.runtime.Token;

import java.util.List;

public class RewriteRuleTokenStream extends RewriteRuleElementStream {

	public RewriteRuleTokenStream(TreeAdaptor adaptor, String elementDescription) {
		super(adaptor, elementDescription);
	}

	/** Create a stream with one element */
	public RewriteRuleTokenStream(TreeAdaptor adaptor,
								  String elementDescription,
								  Object oneElement)
	{
		super(adaptor, elementDescription, oneElement);
	}

	/** Create a stream, but feed off an existing list */
	public RewriteRuleTokenStream(TreeAdaptor adaptor,
								  String elementDescription,
								  List elements)
	{
		super(adaptor, elementDescription, elements);
	}

	/** Get next token from stream and make a node for it */
	public Object nextNode() {
		Token t = (Token)_next();
		return adaptor.create(t);
	}

	public Token nextToken() {
		return (Token)_next();
	}

	/** Don't convert to a tree unless they explicitly call nextTree.
	 *  This way we can do hetero tree nodes in rewrite.
	 */
	protected Object toTree(Object el) {
		return el;
	}

	protected Object dup(Object el) {
		throw new UnsupportedOperationException("dup can't be called for a token stream.");
	}
}
