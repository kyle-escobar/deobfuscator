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

package io.rsbox.deobfuscator.asm.reflect;

import io.rsbox.deobfuscator.asm.file.Method;

/**
 * MethodInfo provides methods for accessing and modifying a method. It is
 * implemented by <tt>file.Method</tt>.
 * 
 * @see Method
 * 
 * @author Nate Nystrom (<a
 *         href="mailto:nystrom@cs.purdue.edu">nystrom@cs.purdue.edu</a>)
 */
public interface MethodInfo {
	/**
	 * Returns the class which declared the method.
	 */
	public ClassInfo declaringClass();

	/**
	 * Returns the index into the constant pool of the name of the method.
	 */
	public int nameIndex();

	/**
	 * Returns the index into the constant pool of the type of the method.
	 */
	public int typeIndex();

	/**
	 * Sets the index into the constant pool of the name of the method.
	 */
	public void setNameIndex(int index);

	/**
	 * Set the index into the constant pool of the type of the method.
	 * 
	 * @param index
	 *            The index into the constant pool of the type of the method.
	 */
	public void setTypeIndex(int index);

	/**
	 * Set the modifiers of the method. The values correspond to the constants
	 * in the Modifiers class.
	 * 
	 * @param modifiers
	 *            A bit vector of modifier flags for the method.
	 * @see Modifiers
	 */
	public void setModifiers(int modifiers);

	/**
	 * Get the modifiers of the method. The values correspond to the constants
	 * in the Modifiers class.
	 * 
	 * @return A bit vector of modifier flags for the method.
	 * @see Modifiers
	 */
	public int modifiers();

	/**
	 * Get the indices into the constant pool of the types of the exceptions
	 * thrown by the method.
	 * 
	 * @return The indices into the constant pool of the types of the exceptions
	 *         thrown by the method.
	 */
	public int[] exceptionTypes();

	/**
	 * Get the maximum height of the operand stack.
	 * 
	 * @return The maximum height of the operand stack.
	 */
	public int maxStack();

	/**
	 * Set the maximum height of the operand stack.
	 * 
	 * @param maxStack
	 *            The maximum height of the operand stack.
	 */
	public void setMaxStack(int maxStack);

	/**
	 * Get the maximum number of locals used in the method.
	 * 
	 * @return The maximum number of locals used in the method.
	 */
	public int maxLocals();

	/**
	 * Set the maximum number of locals used in the method.
	 * 
	 * @param maxLocals
	 *            The maximum number of locals used in the method.
	 */
	public void setMaxLocals(int maxLocals);

	/**
	 * Get the byte code array of the method.
	 * 
	 * @return The byte code array of the method.
	 */
	public byte[] code();

	/**
	 * Set the byte code array of the method.
	 * 
	 * @param code
	 *            The byte code array of the method.
	 */
	public void setCode(byte[] code);

	/**
	 * Get the line number debug info of the instructions in the method.
	 * 
	 * @return The line numbers of the instructions in the method. The array
	 *         will be of size 0 if the method has no line number debug info.
	 */
	public LineNumberDebugInfo[] lineNumbers();

	/**
	 * Set the line number debug info of the instructions in the method.
	 * 
	 * @param lineNumbers
	 *            The line numbers of the instructions in the method. The array
	 *            will be of size 0 if the method has no line number debug info.
	 */
	public void setLineNumbers(LineNumberDebugInfo[] lineNumbers);

	/**
	 * Get the local variable debug information for the method.
	 * 
	 * @return The local variables in the method. The array will be of size 0 if
	 *         the method has no local variable debug info.
	 */
	public LocalDebugInfo[] locals();

	/**
	 * Set the local variables in the method.
	 * 
	 * @param locals
	 *            The local variables in the method.
	 */
	public void setLocals(LocalDebugInfo[] locals);

	/**
	 * Get the exception handlers in the method.
	 * 
	 * @return The exception handlers in the method.
	 */
	public Catch[] exceptionHandlers();

	/**
	 * Set the exception handlers in the method.
	 * 
	 * @param exceptions
	 *            The exception handlers in the method.
	 */
	public void setExceptionHandlers(Catch[] exceptions);

	/**
	 * Creates a clone of this <tt>MethodInfo</tt> except that its declaring
	 * class does not know about it.
	 */
	public Object clone();
}
