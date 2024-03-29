/*
 * All files in the distribution of BLOAT (Bytecode Level Optimization and
 * Analysis tool for Java(tm)) are Copyright 1997-2001 by the Purdue
 * Research Foundation of Purdue University.  All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.rsbox.deobfuscator.asm.tree;

import java.util.*;

import io.rsbox.deobfuscator.asm.util.*;
import io.rsbox.deobfuscator.asm.util.Assert;

/**
 * A PhiCatchStmt is used to handle variables that are used inside an exception
 * handler. Inside a try block a variable may be used several times. It may be
 * updated, may be invovled in a phi-function, etc. A PhiCatchStmt is placed at
 * the beginning of each expection handling (catch) block to factor together the
 * variables that are live within the protected region.
 */
public class PhiCatchStmt extends PhiStmt {
	ArrayList operands;

	/**
	 * Constructor.
	 * 
	 * @param target
	 *            Local variable to which the result of this phi-function is to
	 *            be assigned.
	 */
	public PhiCatchStmt(final LocalExpr target) {
		super(target);
		this.operands = new ArrayList();
	}

	public void visitForceChildren(final TreeVisitor visitor) {
		if (visitor.reverse()) {
			target.visit(visitor);
		}

		for (int i = 0; i < operands.size(); i++) {
			final LocalExpr expr = (LocalExpr) operands.get(i);
			expr.visit(visitor);
		}

		if (!visitor.reverse()) {
			target.visit(visitor);
		}
	}

	public void visit(final TreeVisitor visitor) {
		visitor.visitPhiCatchStmt(this);
	}

	/**
	 * Searches the list of operands for a local variable.
	 * 
	 * @param def
	 *            The local variable definition to search for.
	 * @return True, if def is found, otherwise, false.
	 */
	public boolean hasOperandDef(final LocalExpr def) {
		for (int i = 0; i < operands.size(); i++) {
			final LocalExpr expr = (LocalExpr) operands.get(i);
			if (expr.def() == def) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Add a local variable to the operand list for this phi-function.
	 * 
	 * @param operand
	 *            An operand of this phi-function.
	 */
	public void addOperand(final LocalExpr operand) {
		for (int i = 0; i < operands.size(); i++) {
			final LocalExpr expr = (LocalExpr) operands.get(i);
			Assert.isTrue(expr.def() != operand.def());
		}

		operands.add(operand);
		operand.setParent(this);
	}

	/**
	 * Returns the operands to this phi-function.
	 */
	public Collection operands() {
		if (operands == null) {
			return new ArrayList();
		}

		for (int i = 0; i < operands.size(); i++) {
			final LocalExpr ei = (LocalExpr) operands.get(i);

			for (int j = operands.size() - 1; j > i; j--) {
				final LocalExpr ej = (LocalExpr) operands.get(j);

				if (ei.def() == ej.def()) {
					ej.cleanup();
					operands.remove(j);
				}
			}
		}

		return operands;
	}

	/**
	 * Returns the number of operands to this phi-function.
	 */
	public int numOperands() {
		return operands.size();
	}

	/**
	 * Sets the value of one of this phi-function's operands.
	 * 
	 * @param i
	 *            The number parameter to set.
	 * @param expr
	 *            The new value of the parameter.
	 */
	public void setOperandAt(final int i, final Expr expr) {
		final Expr old = (Expr) operands.get(i);
		old.cleanup();
		operands.set(i, expr);
		expr.setParent(this);
	}

	/**
	 * Returns the operand at a given index.
	 */
	public Expr operandAt(final int i) {
		return (Expr) operands.get(i);
	}
}
