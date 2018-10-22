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
import module.Path;

public class Main_Server {



	public static String DEFAULT_SERVER_PATH=System.getProperty("user.home")+"/git/rcrs-server-master";
	public static String DEFAULT_SOURCE_PATH=System.getProperty("user.home")+"/git/";

	private final static String HOME=System.getProperty("user.home");


	public static void main(String[] args) throws Exception{

		String server=DEFAULT_SERVER_PATH;

		//while(true) {
		ServerSocket serverSocket=new ServerSocket(9999);
		System.out.println("接続待機中...");
		Socket socket=serverSocket.accept();
		System.out.println("接続完了");
		BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String line;
		String user=null;
		String map=null;
		while((line=reader.readLine())!=null) {
			int index=line.lastIndexOf(":");
			if(index!=-1) {
				user=line.substring(0, index).trim();
				map=line.substring(index+1).trim();		
			}
		}
		System.out.println(user+" ::: "+map);
		if(user!=null&&map!=null) {
			ProcessBuilder builder=new ProcessBuilder("bash", "git_clone.sh", DEFAULT_SOURCE_PATH, user).inheritIO();
			Process process=builder.start();
			process.waitFor();
		}
		socket.close();
		serverSocket.close();
	}

/*
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
	}*/

	/*
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
	}*/

	private static void duration(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}