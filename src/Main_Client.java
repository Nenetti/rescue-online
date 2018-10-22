
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import module.ClassFile;
import module.ClassReader;
import module.ModuleManager;
import module.ModuleReader;
import module.NodeFX;
import module.Path;





public class Main_Client extends Application {

	private final static String HOME=System.getProperty("user.home");

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {

		String sourcePath=Path.getTargetPath(HOME+"/git", "git_https.sh");
		
		if(sourcePath==null) {
			System.out.println("\n モジュールファイルが見つかりませんでした \n");
			System.exit(1);
		}

		NodeFX nodeFX=NodeFX.readFXML("./stage.fxml");

		ModuleManager manager=new ModuleManager(nodeFX,sourcePath);
		manager.start();

		Scene scene=new Scene(nodeFX.root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) throws Exception {
		//launch(args);
		//connect();
		ProcessBuilder builder=new ProcessBuilder("/bin/sh", "-c", "ls || ls /home/ubuntu").inheritIO();
		//ProcessBuilder builder=new ProcessBuilder("cd", "~/git", ":", "git", "clone", "-b", user, "https://github.com/Ri--one/rescue-online.git", "sample-"+user).inheritIO();
		Process process=builder.start();
		process.waitFor();
	}

	






	public static void connect() throws Exception{
		InetSocketAddress inetSocketAddress=new InetSocketAddress("localhost", 9999);
		Socket socket=new Socket();
		socket.connect(inetSocketAddress, 1000000);
		
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		writer.write("nenetti : asaas");
		writer.flush();
		writer.close();
		socket.close();
	}

	public static void write(BufferedWriter writer, File file) throws Exception{
		BufferedReader reader=new BufferedReader(new FileReader(file));
		String line;
		while((line=reader.readLine())!=null) {
			writer.write(line);
			writer.newLine();
		}
		writer.write(line);
		writer.flush();
		reader.close();
	}

}