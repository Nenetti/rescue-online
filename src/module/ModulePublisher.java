package module;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;





public class ModulePublisher {
	
	private String modulePath=System.getProperty("user.home")+"/git/sample-nenetti";
	
	
	
	
	
	private void publishModuleCfg(HashMap<String, String> map, String host, int port) {
		try {
			InetSocketAddress inetSocketAddress=new InetSocketAddress(host, port);
			Socket socket=new Socket();
			socket.connect(inetSocketAddress, 10000);
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write("Team.Name : rescue-online\n");
			for(String key:map.keySet()) {
				writer.write(key+" : "+map.get(key));
				writer.newLine();
			}
			writer.flush();
			writer.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gitPush() {
		try {
			String shell=System.getProperty("user.home")+"/git/sample-nenetti/git_https.sh";
			ProcessBuilder processBuilder=new ProcessBuilder("bash", shell, "nenetti", "0.1").inheritIO();
			Process process=processBuilder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void publishModuleFile(HashSet<ClassFile> set, String host, int port) {
		gitPush();
		try {
			InetSocketAddress address=new InetSocketAddress(host, port);
			int index=1, length=set.size();
			for(ClassFile classFile:set) {
				Socket socket=new Socket();
				socket.connect(address, 10000);
				BufferedReader reader=new BufferedReader(new FileReader(classFile.file));
				BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				String header=classFile.toOutputFormat();
				String line;
				writer.write(header);
				writer.newLine();
				while((line=reader.readLine())!=null) {
					writer.write(line);
					writer.newLine();
				}
				reader.close();
				writer.flush();
				ModuleSocket.waitClosed(socket);
				System.out.println("書き込み完了: "+classFile.className+"("+index+"/"+length+")");
				socket.close();
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}