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

package io.rsbox.deobfuscator.asm.inline;

import java.io.*;
import java.util.*;

/**
 * This class is used to gather statistics about inlining. Examples of such
 * statistics are the number of call sites virtual methods resolve to and the
 * number of live classes and methods.
 */
public class InlineStats {
	private String configName; // Name of configuration

	private Map morphicity; // Maps morphic number to count

	private int nLiveClasses; // Number of live classes

	private int nLiveMethods; // Number of live methods

	private int nNoPreexist; // Number of non-preexistent calls

	private int nInlined; // Number of methods inlined

	public InlineStats() {
		this.configName = "Inlining stats";
		this.morphicity = new TreeMap();
		this.nLiveClasses = 0;
		this.nLiveMethods = 0;
		this.nNoPreexist = 0;
		this.nInlined = 0;
	}

	/**
	 * Sets the configuration name for this <tt>InlineStats</tt>.
	 */
	public void setConfigName(final String configName) {
		this.configName = configName;
	}

	/**
	 * Maintains a count of the number of methods call sites resolve to. May
	 * give an idea as to how "dynamic" a program is.
	 */
	public void noteMorphicity(final int morphicity) {
		final Integer r = new Integer(morphicity);
		final Integer count = (Integer) this.morphicity.get(r);
		if (count == null) {
			this.morphicity.put(r, new Integer(1));

		} else {
			this.morphicity.put(r, new Integer(count.intValue() + 1));
		}
	}

	/**
	 * Notes that a call site's receiver is not preexistent.
	 */
	public void noteNoPreexist() {
		nNoPreexist++;
	}

	/**
	 * Notes that a method was inlined
	 */
	public void noteInlined() {
		this.nInlined++;
	}

	/**
	 * Notes the number of live methods.
	 */
	public void noteLiveMethods(final int nLiveMethods) {
		this.nLiveMethods = nLiveMethods;
	}

	/**
	 * Notes the number of live classes.
	 */
	public void noteLiveClasses(final int nLiveClasses) {
		this.nLiveClasses = nLiveClasses;
	}

	/**
	 * Print a summary of the statistics to a <tt>PrintWriter</tt>.
	 */
	public void printSummary(final PrintWriter pw) {
		pw.println("Statistics for " + this.configName + " (" + new Date()
				+ ")");
		pw.println("  Number of live classes: " + this.nLiveClasses);
		pw.println("  Number of live methods: " + this.nLiveMethods);
		pw.println("  Call site morphism:");

		final Iterator morphs = this.morphicity.keySet().iterator();
		int total = 0;
		while (morphs.hasNext()) {
			final Integer morph = (Integer) morphs.next();
			final Integer count = (Integer) this.morphicity.get(morph);
			total += count.intValue();
			pw.println("    " + morph + "\t" + count);
		}
		pw.println("  Total number of call sites: " + total);
		pw.println("  Number of non-preexistent call sites: " + nNoPreexist);
		pw.println("  Number of inlined methods: " + nInlined);

	}

}
