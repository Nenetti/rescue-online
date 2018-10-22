package module;

import java.io.File;

public class Path {



	public static String getTargetPath(String path, String target) {
		File file=new File(path);
		if(file.exists()) {
			if(isTargetFile(file, target)) {
				return file.getAbsolutePath();
			}
			for(File f:file.listFiles()) {
				if(f.isDirectory()&&isTargetFile(f, target)) {
					return f.getAbsolutePath();
				}
			}
		}
		return null;
	}

	private static boolean isTargetFile(File file, String target) {
		File[] files=file.listFiles();
		if(files!=null) {
			for(File f:files) {
				if(f.getName().equals(target)) {
					return true;
				}
			}
		}
		return false;
	}










}