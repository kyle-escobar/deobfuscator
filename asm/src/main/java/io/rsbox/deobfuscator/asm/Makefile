# All files in the distribution of BLOAT (Bytecode Level Optimization and
# Analysis tool for Java(tm)) are Copyright 1997-2001 by the Purdue
# Research Foundation of Purdue University.  All rights reserved.
# 
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


SUBDIRS = util \
	  reflect \
	  file \
	  editor \
	  inline \
	  tree \
	  cfg \
	  ssa \
	  tbaa \
	  trans \
	  diva \
	  codegen \
	  context \
	  decorate \
	  optimize \
	  shrink \
	  strip \
	  dump \
	  tools \
	  benchmark

all:
	@for i in $(SUBDIRS) ""; do \
	    if [ "x$$i" != "x" ]; then \
		$(MAKE) -C $$i all; \
	    fi; \
	done

clean:
	@for i in $(SUBDIRS) ""; do \
	    if [ "x$$i" != "x" ]; then \
		$(MAKE) -C $$i clean; \
	    fi; \
	done

docs:
	javadoc -d ../../../../../docs -sourcepath ../../../.. \
	  io.rsbox.deobfuscator.asm.util \
	  io.rsbox.deobfuscator.asm.reflect \
	  io.rsbox.deobfuscator.asm.file \
	  io.rsbox.deobfuscator.asm.editor \
	  io.rsbox.deobfuscator.asm.tree \
	  io.rsbox.deobfuscator.asm.cfg \
	  io.rsbox.deobfuscator.asm.ssa \
	  io.rsbox.deobfuscator.asm.tbaa \
	  io.rsbox.deobfuscator.asm.trans \
	  io.rsbox.deobfuscator.asm.diva \
	  io.rsbox.deobfuscator.asm.codegen \
	  io.rsbox.deobfuscator.asm.context \
	  io.rsbox.deobfuscator.asm.decorate \
	  io.rsbox.deobfuscator.asm.optimize \
	  io.rsbox.deobfuscator.asm.shrink \
	  io.rsbox.deobfuscator.asm.strip \
	  io.rsbox.deobfuscator.asm.dump \
	  io.rsbox.deobfuscator.asm.benchmark
