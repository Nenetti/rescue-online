package module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ClassReader {
	
	
	public static List<ClassFile> ClassRead(String path) {
		
		List<File> javaFiles=getJavaFile(new File(path));
		List<ClassFile> classFiles=new ArrayList<>();
		for(File f:javaFiles) {
			ClassFile classFile=analyze(f);
			if(classFile!=null) {
				classFiles.add(classFile);
			}
		}
		return classFiles;
	}
	
	
	private static List<File> getJavaFile(File file) {
		List<File> list=new ArrayList<>();
		if(file.isDirectory()) {
			File[] files=file.listFiles();
			for(File f:files) {
				if(f.isFile()&&!f.isHidden()) {
					int index=f.getName().lastIndexOf(".");
					if(index!=-1) {
						String extension=f.getName().substring(index+1);
						if(extension.equals("java")) {
							list.add(f);
						}
					}
				}else {
					list.addAll(getJavaFile(f));
				}
			}
		}
		return list;
	}
	
	private static ClassFile analyze(File file) {
		try {
			BufferedReader reader=new BufferedReader(new FileReader(file));
			
			String line;
			String packageName=null;
			String className=null;
			String superClass=null;
			
			while((line=reader.readLine())!=null) {
				if(line.contains("class")&&line.contains("extends")) {
					String[] split=line.split(" ");
					for(int i=0;i<split.length;i++) {
						switch (split[i]) {
						case "class":
							className=split[i+1];
							break;
						case "extends":
							superClass=split[i+1];
							superClass=superClass.replaceAll("\\{", "");
							break;
						}
					}
					break;
				}else if(line.contains("package ")) {
					packageName=line.substring(line.indexOf(" ")+1, line.indexOf(";"));
				}
			}
			reader.close();
			
			if(packageName!=null&&className!=null&&superClass!=null) {
				return new ClassFile(file, packageName, className, superClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
}