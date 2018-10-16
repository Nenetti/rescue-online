import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main_Server {
	

	public static String SERVER_PATH=System.getProperty("user.home")+"/git/rcrs-server-master";
	public static String SOURCE_PATH=System.getProperty("user.home")+"/git/sample-master";

	
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws Exception{
		ServerSocket serverSocket=new ServerSocket(9999);
		Socket socket=serverSocket.accept();
		
		BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String line;
		while((line=reader.readLine())!=null) {
			System.out.println(line);
		}
		System.out.println("終了");
		
		//run("../maps/gml/test/map", "../maps/gml/test/config");
	}
	
	public static void run(String mapDataPath, String mapConfigPath) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			try {
				Runtime.getRuntime().exec("bash "+SERVER_PATH+"/boot/kill.sh");
			} catch (IOException e) {
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