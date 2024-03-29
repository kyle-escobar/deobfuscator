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

/**
 * StackManipStmt represents the opcodes that manipulate the stack such as
 * <tt>swap</tt> and <tt>dup</tt>.
 */
public class StackManipStmt extends Stmt implements Assign {
	StackExpr[] target;

	StackExpr[] source;

	int kind;

	// 0 1 -> 1 0
	public static final int SWAP = 0;

	// 0 -> 0 0
	public static final int DUP = 1;

	// 0 1 -> 1 0 1
	public static final int DUP_X1 = 2;

	// 0 1 2 -> 2 0 1 2
	public static final int DUP_X2 = 3;

	// 0 1 -> 0 1 0 1
	public static final int DUP2 = 4;

	// 0 1 2 -> 1 2 0 1 2
	public static final int DUP2_X1 = 5;

	// 0 1 2 3 -> 2 3 0 1 2 3
	public static final int DUP2_X2 = 6;

	/**
	 * Constructor.
	 * 
	 * @param target
	 *            The new contents of the stack
	 * @param source
	 *            The old contents of the stack
	 * @param kind
	 *            The kind of stack manipulation (SWAP, DUP, etc.) to take
	 *            place.
	 */
	public StackManipStmt(final StackExpr[] target, final StackExpr[] source,
			final int kind) {
		this.kind = kind;

		this.target = target;

		for (int i = 0; i < target.length; i++) {
			this.target[i].setParent(this);
		}

		this.source = source;

		for (int i = 0; i < source.length; i++) {
			this.source[i].setParent(this);
		}
	}

	public DefExpr[] defs() {
		return target;
	}

	public StackExpr[] target() {
		return target;
	}

	public StackExpr[] source() {
		return source;
	}

	public int kind() {
		return kind;
	}

	public void visit(final TreeVisitor visitor) {
		visitor.visitStackManipStmt(this);
	}

	public void visitForceChildren(final TreeVisitor visitor) {
		if (visitor.reverse()) {
			for (int i = target.length - 1; i >= 0; i--) {
				target[i].visit(visitor);
			}

			for (int i = source.length - 1; i >= 0; i--) {
				source[i].visit(visitor);
			}
		} else {
			for (int i = 0; i < source.length; i++) {
				source[i].visit(visitor);
			}

			for (int i = 0; i < target.length; i++) {
				target[i].visit(visitor);
			}
		}
	}

	public Object clone() {
		final StackExpr[] t = new StackExpr[target.length];

		for (int i = 0; i < target.length; i++) {
			t[i] = (StackExpr) target[i].clone();
		}

		final StackExpr[] s = new StackExpr[source.length];

		for (int i = 0; i < source.length; i++) {
			s[i] = (StackExpr) source[i].clone();
		}

		return copyInto(new StackManipStmt(t, s, kind));
	}
}
