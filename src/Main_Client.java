
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





public class Main_Client extends Application {


	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		NodeFX nodeFX=NodeFX.readFXML("./stage.fxml");

		List<ClassFile> classFiles=ClassReader.ClassRead(System.getProperty("user.home")+"/git/sample-master/src");
		ModuleManager manager=new ModuleManager(nodeFX, classFiles);
		
		Scene scene=new Scene(nodeFX.root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) throws Exception {
		launch(args);
		//connect();
	}

	public static void connect() throws Exception{
		InetSocketAddress inetSocketAddress=new InetSocketAddress("localhost", 9999);
		Socket socket=new Socket();
		socket.connect(inetSocketAddress, 10000);
		BufferedInputStream input=new BufferedInputStream(new FileInputStream(new File("Main_Client.java")));
		System.out.println(input.available());
		Thread.sleep(1000);
		BufferedOutputStream outputStream=new BufferedOutputStream(socket.getOutputStream());
		byte[] bs=new byte[input.available()];
		input.read(bs);
		outputStream.write(bs);
		outputStream.flush();
		System.out.println(input.available());

		//BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		//write(writer, new File("Main_Client.java"));
		
		
		//socket.close();
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