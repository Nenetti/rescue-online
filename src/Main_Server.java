import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import module.ModuleSocket;

public class Main_Server {


	public static String SERVER_PATH=System.getProperty("user.home")+"/git/rcrs-server-master";
	public static String SOURCE_PATH=System.getProperty("user.home")+"/git/sample-master";










	public static void main(String[] args) throws Exception{
		while(true) {
			ServerSocket serverSocket=new ServerSocket(9999);
			Socket socket=serverSocket.accept();
			System.out.println("接続");
			BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			while((line=reader.readLine())!=null) {
				System.out.println(line);
			}
			System.out.println("終了");
			ModuleSocket.waitClosed(socket);
			serverSocket.close();
			System.out.println("ソケット閉じ");
		}

		//run("../maps/gml/test/map", "../maps/gml/test/config");
	}

	public static void run(String mapDataPath, String mapConfigPath) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			try {
				Process killProcess=new ProcessBuilder("bash", SERVER_PATH+"/boot/kill.sh").inheritIO().start();
				System.out.println("\u001b[41m\nプロセス強制終了\u001b[0m\n");
				killProcess.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));

		Process serverProcess=new ProcessBuilder("bash", SERVER_PATH+"/boot.sh", mapDataPath, mapConfigPath).inheritIO().start();
		duration(2000);
		Process sourceProcess=new ProcessBuilder("bash", SOURCE_PATH+"/boot.sh").inheritIO().start();
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