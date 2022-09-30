package io.rsbox.deobfuscator.asm.file;

public interface ClassSource {
	  Class loadClass(String name) throws ClassNotFoundException ;
}
