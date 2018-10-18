package module;

import java.io.File;

public class ClassFile {
	
	
	public File file;
	public String packagePath;
	public String className;
	public String superClass;
	
	
	public ClassFile(File file, String packagePath, String className, String superClass) {
		this.file=file;
		this.packagePath=packagePath;
		this.className=className;
		this.superClass=superClass;
	}
	
	public String toOutputFormat() {
		return packagePath+"."+className;
	}
	
	public static String toClassName(String name) {
		return name.substring(name.lastIndexOf('.')+1);
	}
	
	@Override
	public String toString() {
		return packagePath+": "+className+" extends "+superClass;
	}
	
}