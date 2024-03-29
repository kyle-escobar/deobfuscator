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

import io.rsbox.deobfuscator.asm.cfg.*;
import io.rsbox.deobfuscator.asm.cfg.Subroutine;

/**
 * Associated with an AddressStoreStmt is a Subroutine whose address (offset in
 * the instruction sequence) is to be stored. Addresses may be loaded (using
 * <i>astore</i>), but cannot be reloaded. Therefore, AddressStoreStmt is
 * needed to differentiate between a regular (object reference) <i>astore</i>
 * which is modeled by a LocalExpr.
 * 
 * @see Tree#visit_astore
 * @see Subroutine
 * @see LocalExpr
 */
public class AddressStoreStmt extends Stmt {
	Subroutine sub;

	/**
	 * Constructor.
	 * 
	 * @param sub
	 * 
	 */
	public AddressStoreStmt(final Subroutine sub) {
		this.sub = sub;
	}

	public Subroutine sub() {
		return sub;
	}

	public void visitForceChildren(final TreeVisitor visitor) {
	}

	public void visit(final TreeVisitor visitor) {
		visitor.visitAddressStoreStmt(this);
	}

	public Object clone() {
		return copyInto(new AddressStoreStmt(sub));
	}
}
