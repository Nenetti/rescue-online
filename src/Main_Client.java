
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import module.ClassFile;
import module.ClassReader;
import module.ModuleConfig;
import module.NodeFX;





public class Main_Client extends Application {
	
	
	ComboBox<String> detector;
	ComboBox<String> search;
	ComboBox<String> extAction;
	ComboBox<String> extActionMove;
	ComboBox<String> commandExecutor;
	ComboBox<String> commandExecutorScout;
	ComboBox<String> clustering;
	ComboBox<String> pathPlanning;
	
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		NodeFX nodeFX=NodeFX.readFXML("./stage.fxml");
		
		Button at_Button=nodeFX.getButton("AT_Button");
		Button fb_Button=nodeFX.getButton("FB_Button");
		Button pf_Button=nodeFX.getButton("PF_Button");
		Button ac_Button=nodeFX.getButton("AC_Button");
		Button fs_Button=nodeFX.getButton("FS_Button");
		Button po_Button=nodeFX.getButton("PO_Button");
		
		
		detector=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		search=(ComboBox<String>)nodeFX.getNode("Search_Box");
		extAction=(ComboBox<String>)nodeFX.getNode("ExtAction_Box");
		extActionMove=(ComboBox<String>)nodeFX.getNode("ActionExtMove_Box");
		//commandExecutor=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		//commandExecutorScout=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		clustering=(ComboBox<String>)nodeFX.getNode("Clustering_Box");
		pathPlanning=(ComboBox<String>)nodeFX.getNode("PathPlanning_Box");
		
		List<ClassFile> classFiles=ClassReader.ClassRead(System.getProperty("user.home")+"/git/sample-master/src");
		ModuleConfig config=new ModuleConfig(classFiles);
		setConfig(config);
		
		Scene scene=new Scene(nodeFX.root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void setConfig(ModuleConfig config) {
		detector.getItems().clear();
		detector.getItems().addAll(config.humanDetectors.keySet());
		detector.getItems().addAll(config.buildingDetectors.keySet());
		detector.getItems().addAll(config.roadDetectors.keySet());
		search.getItems().clear();
		search.getItems().addAll(config.searchs.keySet());
		extAction.getItems().clear();
		extAction.getItems().addAll(config.extActions.keySet());
		extActionMove.getItems().clear();
		extActionMove.getItems().addAll(config.extActions.keySet());
		//commandExecutor.getItems().clear();
		//commandExecutor.getItems().addAll(config..keySet());
		clustering.getItems().clear();
		clustering.getItems().addAll(config.dynamicClusterings.keySet());
		clustering.getItems().addAll(config.staticClusterings.keySet());
		pathPlanning.getItems().clear();
		pathPlanning.getItems().addAll(config.pathPlannings.keySet());
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}
	
	public void connect() throws Exception{
		InetSocketAddress inetSocketAddress=new InetSocketAddress("localhost", 9999);
		Socket socket=new Socket();
		socket.connect(inetSocketAddress, 10000);
		
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		write(writer, new File("Main_Client.java"));
		socket.close();
	}
	
	public void write(BufferedWriter writer, File file) throws Exception{
		BufferedReader reader=new BufferedReader(new FileReader(file));
		String line;
		while((line=reader.readLine())!=null) {
			writer.write(line);
			writer.newLine();
		}
		writer.close();
		reader.close();
	}
	
}