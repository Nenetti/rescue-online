package module;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class ModuleSubscriber {
	
	public String sourcePath;

	public ModuleSubscriber(String sourcePath) {
		this.sourcePath=sourcePath;
	}
	
	
	public void subscribeModuleFile(int port) {
		try {
			while(true) {
				ServerSocket serverSocket=new ServerSocket(port);
				Socket socket=serverSocket.accept();
				BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line;
				String header=reader.readLine();
				ClassFile classFile=toClassFile(header);
				classFile.file.getParentFile().mkdirs();
				classFile.file.createNewFile();
				BufferedWriter writer=new BufferedWriter(new FileWriter(classFile.file));
				while((line=reader.readLine())!=null) {
					writer.write(line);
					writer.newLine();
				}
				writer.close();
				ModuleSocket.waitClosed(socket);
				serverSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ClassFile toClassFile(String header) {
		int index=header.lastIndexOf(".");
		String className=header.substring(index);
		String packagePath=header.substring(0, index);
		String filePath=sourcePath+"/src/"+header.replaceAll("\\.", "/")+"java";
		return new ClassFile(new File(filePath), packagePath, className, null);
	}
	
	
	
	
	
}