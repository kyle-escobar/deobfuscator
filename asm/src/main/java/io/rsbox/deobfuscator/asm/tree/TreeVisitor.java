/**
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

import io.rsbox.deobfuscator.asm.cfg.*;
import io.rsbox.deobfuscator.asm.cfg.Block;
import io.rsbox.deobfuscator.asm.cfg.FlowGraph;
import org.jetbrains.annotations.NotNull;

/**
 * TreeVisitor performs a traversal of a tree. It does so by having a method of
 * every kind of node in the tree. This abstract class performs default
 * operations for each kind of node visited. It must be subclasses to perform a
 * more interesting traversal.
 * 
 * @see Node
 * @see Tree
 * 
 * @see PrintVisitor
 * @see ReplaceVisitor
 * 
 */
public abstract class TreeVisitor {
	public static final int FORWARD = 0;

	public static final int REVERSE = 1;

	boolean prune;

	int direction;

	public TreeVisitor() {
		this(TreeVisitor.FORWARD);
	}

	public TreeVisitor(final int direction) {
		this.direction = direction;
	}

	/**
	 * @param prune
	 *            Is the tree pruned during traversal?
	 */
	public void setPrune(final boolean prune) {
		this.prune = prune;
	}

	public boolean prune() {
		return prune;
	}

	/**
	 * @return The direction in which the tree is traversed.
	 */
	public int direction() {
		return direction;
	}

	/**
	 * Returns <tt>true</tt> if the traversal traverses in the forward
	 * direction?
	 */
	public boolean forward() {
		return direction == TreeVisitor.FORWARD;
	}

	public boolean reverse() {
		return direction == TreeVisitor.REVERSE;
	}

	public void visitFlowGraph(@NotNull final FlowGraph graph) {
		graph.visitChildren(this);
	}

	public void visitBlock(@NotNull final Block block) {
		block.visitChildren(this);
	}

	public void visitTree(@NotNull final Tree tree) {
		visitNode(tree);
	}

	public void visitExprStmt(@NotNull final ExprStmt stmt) {
		visitStmt(stmt);
	}

	public void visitIfStmt(@NotNull final IfStmt stmt) {
		visitStmt(stmt);
	}

	public void visitIfCmpStmt(@NotNull final IfCmpStmt stmt) {
		visitIfStmt(stmt);
	}

	public void visitIfZeroStmt(@NotNull final IfZeroStmt stmt) {
		visitIfStmt(stmt);
	}

	public void visitInitStmt(@NotNull final InitStmt stmt) {
		visitStmt(stmt);
	}

	public void visitGotoStmt(@NotNull final GotoStmt stmt) {
		visitStmt(stmt);
	}

	public void visitLabelStmt(@NotNull final LabelStmt stmt) {
		visitStmt(stmt);
	}

	public void visitMonitorStmt(@NotNull final MonitorStmt stmt) {
		visitStmt(stmt);
	}

	public void visitPhiStmt(@NotNull final PhiStmt stmt) {
		visitStmt(stmt);
	}

	public void visitCatchExpr(@NotNull final CatchExpr expr) {
		visitExpr(expr);
	}

	public void visitDefExpr(@NotNull final DefExpr expr) {
		visitExpr(expr);
	}

	public void visitStackManipStmt(@NotNull final StackManipStmt stmt) {
		visitStmt(stmt);
	}

	public void visitPhiCatchStmt(@NotNull final PhiCatchStmt stmt) {
		visitPhiStmt(stmt);
	}

	public void visitPhiJoinStmt(@NotNull final PhiJoinStmt stmt) {
		visitPhiStmt(stmt);
	}

	public void visitRetStmt(@NotNull final RetStmt stmt) {
		visitStmt(stmt);
	}

	public void visitReturnExprStmt(@NotNull final ReturnExprStmt stmt) {
		visitStmt(stmt);
	}

	public void visitReturnStmt(@NotNull final ReturnStmt stmt) {
		visitStmt(stmt);
	}

	public void visitAddressStoreStmt(@NotNull final AddressStoreStmt stmt) {
		visitStmt(stmt);
	}

	public void visitStoreExpr(@NotNull final StoreExpr expr) {
		visitExpr(expr);
	}

	public void visitJsrStmt(@NotNull final JsrStmt stmt) {
		visitStmt(stmt);
	}

	public void visitSwitchStmt(@NotNull final SwitchStmt stmt) {
		visitStmt(stmt);
	}

	public void visitThrowStmt(@NotNull final ThrowStmt stmt) {
		visitStmt(stmt);
	}

	public void visitStmt(@NotNull final Stmt stmt) {
		visitNode(stmt);
	}

	public void visitSCStmt(@NotNull final SCStmt stmt) {
		visitStmt(stmt);
	}

	public void visitSRStmt(@NotNull final SRStmt stmt) {
		visitStmt(stmt);
	}

	public void visitArithExpr(@NotNull final ArithExpr expr) {
		visitExpr(expr);
	}

	public void visitArrayLengthExpr(@NotNull final ArrayLengthExpr expr) {
		visitExpr(expr);
	}

	public void visitMemExpr(@NotNull final MemExpr expr) {
		visitDefExpr(expr);
	}

	public void visitMemRefExpr(@NotNull final MemRefExpr expr) {
		visitMemExpr(expr);
	}

	public void visitArrayRefExpr(@NotNull final ArrayRefExpr expr) {
		visitMemRefExpr(expr);
	}

	public void visitCallExpr(@NotNull final CallExpr expr) {
		visitExpr(expr);
	}

	public void visitCallMethodExpr(@NotNull final CallMethodExpr expr) {
		visitCallExpr(expr);
	}

	public void visitCallStaticExpr(@NotNull final CallStaticExpr expr) {
		visitCallExpr(expr);
	}

	public void visitCastExpr(@NotNull final CastExpr expr) {
		visitExpr(expr);
	}

	public void visitConstantExpr(@NotNull final ConstantExpr expr) {
		visitExpr(expr);
	}

	public void visitFieldExpr(@NotNull final FieldExpr expr) {
		visitMemRefExpr(expr);
	}

	public void visitInstanceOfExpr(@NotNull final InstanceOfExpr expr) {
		visitExpr(expr);
	}

	public void visitLocalExpr(@NotNull final LocalExpr expr) {
		visitVarExpr(expr);
	}

	public void visitNegExpr(@NotNull final NegExpr expr) {
		visitExpr(expr);
	}

	public void visitNewArrayExpr(@NotNull final NewArrayExpr expr) {
		visitExpr(expr);
	}

	public void visitNewExpr(@NotNull final NewExpr expr) {
		visitExpr(expr);
	}

	public void visitNewMultiArrayExpr(@NotNull final NewMultiArrayExpr expr) {
		visitExpr(expr);
	}

	public void visitCheckExpr(@NotNull final CheckExpr expr) {
		visitExpr(expr);
	}

	public void visitZeroCheckExpr(@NotNull final ZeroCheckExpr expr) {
		visitCheckExpr(expr);
	}

	public void visitRCExpr(@NotNull final RCExpr expr) {
		visitCheckExpr(expr);
	}

	public void visitUCExpr(@NotNull final UCExpr expr) {
		visitCheckExpr(expr);
	}

	public void visitReturnAddressExpr(@NotNull final ReturnAddressExpr expr) {
		visitExpr(expr);
	}

	public void visitShiftExpr(@NotNull final ShiftExpr expr) {
		visitExpr(expr);
	}

	public void visitStackExpr(@NotNull final StackExpr expr) {
		visitVarExpr(expr);
	}

	public void visitVarExpr(@NotNull final VarExpr expr) {
		visitMemExpr(expr);
	}

	public void visitStaticFieldExpr(@NotNull final StaticFieldExpr expr) {
		visitMemRefExpr(expr);
	}

	public void visitExpr(@NotNull final Expr expr) {
		visitNode(expr);
	}

	public void visitNode(@NotNull final Node node) {
		node.visitChildren(this);
	}
}
