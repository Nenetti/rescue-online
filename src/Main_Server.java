import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import module.ModuleSocket;
import module.ModuleSubscriber;

public class Main_Server {

	
	
	public static String SERVER_PATH=System.getProperty("user.home")+"/git/rcrs-server-master";
	public static String SOURCE_PATH=System.getProperty("user.home")+"/git/sample-";
	
	public static void main(String[] args) throws Exception{
		run("../maps/gml/test/map", "../maps/gml/test/config");
	}
	
	public void subscribe(int port) {
		try {
			ServerSocket serverSocket=new ServerSocket(port);
			Socket socket=serverSocket.accept();
			
			BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line=reader.readLine();
			int index=line.lastIndexOf(":");
			if(index!=-1) {
				String user=line.substring(0, index).trim();
				String map=line.substring(index+1).trim();
				run(user, map);
			}
			serverSocket.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void run(String user, String mapPath) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			try {
				Process killProcess=new ProcessBuilder("bash", SERVER_PATH+"/boot/kill.sh").inheritIO().start();
				System.out.println("\u001b[41m\nプロセス強制終了\u001b[0m\n");
				killProcess.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
		
		String mapDataPath=mapPath+"/map";
		String mapConfigPath=mapPath+"/config";
		Process serverProcess=new ProcessBuilder("bash", SERVER_PATH+"/boot.sh", mapDataPath, mapConfigPath).inheritIO().start();
		duration(2000);
		Process sourceProcess=new ProcessBuilder("bash", SOURCE_PATH+user+"/boot.sh").inheritIO().start();
		serverProcess.waitFor();
		sourceProcess.waitFor();
	}

	private static void duration(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}