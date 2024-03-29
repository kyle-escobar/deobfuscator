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

import io.rsbox.deobfuscator.asm.cfg.*;
import io.rsbox.deobfuscator.asm.cfg.Block;
import io.rsbox.deobfuscator.asm.ssa.SSAConstructionInfo;

/**
 * <tt>PhiJoinStmt</tt> represents a phi-function inserted into a control flow
 * graph during conversion of variables to static single-assignment form. A
 * <tt>PhiJoinStmt</tt> at a point of control flow convergence.
 * 
 * @see SSAConstructionInfo SSAConstructionInfo
 */
public class PhiJoinStmt extends PhiStmt {
	Map operands; // Operands to a PhiStmt (mapping between a Block

	// and a VarExpr occurring at that block)
	Block block; // Basic block containing this PhiJoinStmt

	/**
	 * Constructor.
	 * 
	 * @param target
	 *            The target of this PhiStmt.
	 * @param block
	 *            The basic Block in which this PhiJoinStmt resides.
	 */
	public PhiJoinStmt(final VarExpr target, final Block block) {
		super(target);

		this.block = block;
		this.operands = new HashMap();

		final Iterator preds = block.graph().preds(block).iterator();

		while (preds.hasNext()) {
			final Block pred = (Block) preds.next();
			final VarExpr operand = (VarExpr) target.clone();
			operands.put(pred, operand);
			operand.setParent(this);
			operand.setDef(null);
		}
	}

	/**
	 * Set the operand to this PhiJoinStmt for a given Block to a given
	 * expression.
	 * 
	 * @param block
	 * 
	 * @param expr
	 * 
	 */
	public void setOperandAt(final Block block, final Expr expr) {
		final Expr operand = (Expr) operands.get(block);

		if (operand != null) {
			operand.cleanup();
		}

		if (expr != null) {
			operands.put(block, expr);
			expr.setParent(this);
		} else {
			operands.remove(block);
		}
	}

	/**
	 * Returns the occurrence of the variable with which this PhiJoinStmt is
	 * concerned (usually represented by a VarExpr) at a given block.
	 * 
	 * @param block
	 *            The block at which an occurrence of the variable occurs.
	 * 
	 * @see VarExpr
	 */
	public Expr operandAt(final Block block) {
		return (Expr) operands.get(block);
	}

	/**
	 * Returns the number of operands that this PhiJoinStmt has.
	 */
	public int numOperands() {
		return block.graph().preds(block).size();
	}

	/**
	 * Returns the predacessor nodes (in the CFG not dominator graph) of the
	 * block in which this PhiJoinStmt occurs.
	 */
	public Collection preds() {
		return block.graph().preds(block);
	}

	/**
	 * Returns the operands of this PhiJoinStmt. They are usually of type
	 * VarExpr.
	 * 
	 * @see VarExpr
	 */
	public Collection operands() {
		if (operands != null) {
			operands.keySet().retainAll(preds());
			return operands.values();
		}

		return new ArrayList();
	}

	public void visitForceChildren(final TreeVisitor visitor) {
		if (visitor.reverse()) {
			target.visit(visitor);
		}

		final Iterator e = operands().iterator();

		while (e.hasNext()) {
			final Expr operand = (Expr) e.next();
			operand.visit(visitor);
		}

		if (!visitor.reverse()) {
			target.visit(visitor);
		}
	}

	public void visit(final TreeVisitor visitor) {
		visitor.visitPhiJoinStmt(this);
	}
}
