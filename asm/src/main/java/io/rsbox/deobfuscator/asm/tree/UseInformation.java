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
 * UseInformation stores information about a use of a local variable.
 * 
 * @author Thomas VanDrunen
 */
public class UseInformation {

	int type;

	int type0s;

	int type1s;

	int type0_x1s;

	int type0_x2s;

	int type1_x1s;

	int type1_x2s;

	public UseInformation() {

		type = 2; // assume type > 1 unless discovered otherwise
		type0s = 0;
		type1s = 0;
		type0_x1s = 0;
		type0_x2s = 0;
		type1_x1s = 0;
		type1_x2s = 0;
	}
}
