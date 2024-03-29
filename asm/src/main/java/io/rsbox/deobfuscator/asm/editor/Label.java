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

package io.rsbox.deobfuscator.asm.editor;

/**
 * <tt>Label</tt> is used to label an instruction. <tt>Label</tt>s are used
 * to preserve the location of branch targets. A <tt>Label</tt> consists of an
 * index into the code array and a <tt>boolean</tt> that determines whether or
 * not it starts a basic block.
 * 
 * @author Nate Nystrom (<a
 *         href="mailto:nystrom@cs.purdue.edu">nystrom@cs.purdue.edu</a>)
 */
public class Label {
	public static boolean TRACE = false;

	private int index;

	private boolean startsBlock;

	private String comment; // Comment with Label

	/**
	 * Constructor.
	 * 
	 * @param index
	 *            A unique index for the label. For instance, its offset in the
	 *            instruction array.
	 */
	public Label(final int index) {
		this(index, false);
	}

	/**
	 * Constructor.
	 * 
	 * @param index
	 *            The index of this label into the instruction array
	 * @param startsBlock
	 *            True if the label is the first instruction in a basic block,
	 *            false if not.
	 */
	public Label(final int index, final boolean startsBlock) {
		this.index = index;
		this.startsBlock = startsBlock;

		// if(Label.TRACE) {
		// try {
		// throw new Exception("Creating a new label: " + this);
		// } catch(Exception ex) {
		// ex.printStackTrace(System.out);
		// }
		// }
	}

	/**
	 * Sets the comment associated with this <tt>Label</tt>.
	 */
	public void setComment(final String comment) {
		this.comment = comment;
	}

	/**
	 * Set if the label starts a block.
	 * 
	 * @param startsBlock
	 *            True if the label starts a block, false if not.
	 */
	public void setStartsBlock(final boolean startsBlock) {
		this.startsBlock = startsBlock;
	}

	/**
	 * Check if the label starts a block.
	 * 
	 * @return True if the label starts a block, false if not.
	 */
	public boolean startsBlock() {
		return startsBlock;
	}

	/**
	 * Get the index of the label.
	 * 
	 * @return The index of the label.
	 */
	public int index() {
		return index;
	}

	/**
	 * Hash the label.
	 * 
	 * @return The hash code.
	 */
	public int hashCode() {
		return index;
	}

	/**
	 * Check if an object is equal to this label.
	 * 
	 * @param obj
	 *            The object to compare against.
	 * @return true if equal, false if not.
	 */
	public boolean equals(final Object obj) {
		return ((obj instanceof Label) && (((Label) obj).index == index));
	}

	/**
	 * Convert the label to a string.
	 * 
	 * @return A string representation of the label.
	 */
	public String toString() {
		if (comment != null) {
			return "label_" + index + " (" + comment + ")";
		} else {
			return "label_" + index;
		}
	}
}
