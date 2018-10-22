package module;

import java.awt.Button;
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

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;





public class ModulePublisher {

	
	public static void gitPush() {
		
		String branch, user, password;
		
		Alert dialog=new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Git セットアップ");
		dialog.setHeaderText("");
		GridPane grid=new GridPane();
		TextField user_field=new TextField();
		TextField branch_field=new TextField();
		PasswordField passwordField=new PasswordField();
		grid.add(new Label("Branch: "), 1, 1);
		grid.add(branch_field, 2, 1);
		grid.add(new Label("  <- ブランチ名"), 3, 1);
		
		grid.add(new Label(), 1, 2);
		
		grid.add(new Label("Username: "), 1, 3);
		grid.add(user_field, 2, 3);
		grid.add(new Label("  <- デフォ Ri--one"), 3, 3);
		
		grid.add(new Label(), 1, 4);
		
		grid.add(new Label("Password: "), 1, 5);
		grid.add(passwordField, 2, 5);
		grid.add(new Label("  <- gitのパスワード"), 3, 5);
		dialog.getDialogPane().setContent(grid);
		
		HashMap<String, String> map=readPreviousConfig("config.cfg");
		branch=map.get("Branch");
		user=map.get("Username");
		branch_field.setText(branch);
		user_field.setText(user);
		
		while(dialog.showAndWait().get()==ButtonType.OK) {
			branch=branch_field.getText();
			user=user_field.getText();
			password=passwordField.getText();
			if(branch.length()>0&&user.length()>0&&password.length()>0) {
				try {
					String shell=System.getProperty("user.home")+"/git/sample-nenetti/git_https.sh";
					ProcessBuilder processBuilder=new ProcessBuilder("bash", shell, branch, "0.1", user, password).inheritIO();
					Process process=processBuilder.start();
					int code=process.waitFor();
					if(code==0) {
						System.out.println("\n アップロード 完了");
					}else {
						System.out.println("\n アップロード 失敗");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				map.put("Branch", branch);
				map.put("Username", user);
				return;
			}
		}
		return;
	}
	
	private static HashMap<String, String> readPreviousConfig(String filePath) {
		HashMap<String, String> map=new HashMap<>();
		try {
			File file=new File(filePath);
			if(file.exists()) {
				BufferedReader reader=new BufferedReader(new FileReader(file));
				String line;
				while((line=reader.readLine())!=null) {
					int index=line.lastIndexOf(":");
					if(index!=-1) {
						String key=line.substring(0, index).trim();
						String value=line.substring(index+1).trim();
						map.put(key, value);
					}
				}
				reader.close();
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public void startServer(String host, int port, String user, String map) {
		try {
			InetSocketAddress address=new InetSocketAddress(host, port);
			Socket socket=new Socket();
			socket.connect(address, 1000000);
			
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(user+" : "+map);
			writer.flush();
			while(!socket.isClosed()) {
				
			}
			
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	public void publishModuleFile(HashSet<ClassFile> set, String host, int port) {
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
	}*/
}