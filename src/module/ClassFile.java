package module;

import java.io.File;

public class ClassFile {
	
	
	public File file;
	public String className;
	public String superClass;
	
	
	public ClassFile(File file, String className, String superClass) {
		this.file=file;
		this.className=className;
		this.superClass=superClass;
	}
	
}