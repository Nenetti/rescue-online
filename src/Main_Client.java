
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import module.ClassFile;
import module.ClassReader;
import module.ModuleConfig;
import module.NodeFX;
import module.ModuleConfig.AgentConfig;





public class Main_Client extends Application {

	List<ComboBox<String>> comboBoxs;
	ComboBox<String> detector;
	ComboBox<String> search;
	ComboBox<String> extAction;
	ComboBox<String> extActionMove;
	ComboBox<String> commandExecutor;
	ComboBox<String> commandExecutorScout;
	ComboBox<String> clustering;
	ComboBox<String> pathPlanning;

	Button at_Button;
	Button fb_Button;
	Button pf_Button;
	Button ac_Button;
	Button fs_Button;
	Button po_Button;

	private static final String AT_BUTTON_ID="AT_Button";
	private static final String FB_BUTTON_ID="FB_Button";
	private static final String PF_BUTTON_ID="PF_Button";
	private static final String AC_BUTTON_ID="AC_Button";
	private static final String FS_BUTTON_ID="FS_Button";
	private static final String PO_BUTTON_ID="PO_Button";

	String selectButtonID;

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		NodeFX nodeFX=NodeFX.readFXML("./stage.fxml");

		at_Button=nodeFX.getButton(AT_BUTTON_ID);
		fb_Button=nodeFX.getButton(FB_BUTTON_ID);
		pf_Button=nodeFX.getButton(PF_BUTTON_ID);
		ac_Button=nodeFX.getButton(AC_BUTTON_ID);
		fs_Button=nodeFX.getButton(FS_BUTTON_ID);
		po_Button=nodeFX.getButton(PO_BUTTON_ID);


		detector=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		search=(ComboBox<String>)nodeFX.getNode("Search_Box");
		extAction=(ComboBox<String>)nodeFX.getNode("ExtAction_Box");
		extActionMove=(ComboBox<String>)nodeFX.getNode("ActionExtMove_Box");
		//commandExecutor=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		//commandExecutorScout=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		clustering=(ComboBox<String>)nodeFX.getNode("Clustering_Box");
		pathPlanning=(ComboBox<String>)nodeFX.getNode("PathPlanning_Box");
		
		comboBoxs=Arrays.asList(detector, search, extAction, extActionMove, clustering, pathPlanning);

		List<ClassFile> classFiles=ClassReader.ClassRead(System.getProperty("user.home")+"/git/sample-master/src");
		ModuleConfig config=new ModuleConfig(classFiles);

		at_Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button source=(Button)event.getSource();
				//saveConfig(config, selectButtonID);
				switch (source.getId()) {
				case AT_BUTTON_ID:
					break;
				case FB_BUTTON_ID:
					break;
				case PF_BUTTON_ID:
					break;
				case AC_BUTTON_ID:
					break;
				case FS_BUTTON_ID:
					break;
				case PO_BUTTON_ID:
					break;
				}
			}
		});













		setConfig(config);










		Scene scene=new Scene(nodeFX.root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void changeConfig(ModuleConfig config, String agentID) {
		AgentConfig agentConfig=null;
		switch (agentID) {
		case AT_BUTTON_ID:
			agentConfig=config.at;
			break;
		case FB_BUTTON_ID:
			agentConfig=config.fb;
			break;
		case PF_BUTTON_ID:
			agentConfig=config.pf;
			break;
		case AC_BUTTON_ID:
			break;
		case FS_BUTTON_ID:
			break;
		case PO_BUTTON_ID:
			break;
		}
		/*
		if(agentConfig!=null) {
			if(detector.getItems().contains(agentConfig.detector.className)) detector.getSelectionModel().select(agentConfig.detector.className);
		}*/
	}

	public void saveConfig(ModuleConfig config, String agentID) {
		ClassFile detectorClass=config.detectors.get(detector.getSelectionModel().getSelectedItem());
		ClassFile searchClass=config.detectors.get(search.getSelectionModel().getSelectedItem());
		ClassFile extActionClass=config.detectors.get(extAction.getSelectionModel().getSelectedItem());
		ClassFile extActionMoveClass=config.detectors.get(extActionMove.getSelectionModel().getSelectedItem());
		ClassFile commandExecutorClass=config.detectors.get(commandExecutor.getSelectionModel().getSelectedItem());
		ClassFile commandExecutorScoutClass=config.detectors.get(commandExecutorScout.getSelectionModel().getSelectedItem());
		ClassFile clusteringClass=config.detectors.get(clustering.getSelectionModel().getSelectedItem());
		ClassFile pathPlanningClass=config.detectors.get(pathPlanning.getSelectionModel().getSelectedItem());
		switch (agentID) {
		case AT_BUTTON_ID:
			config.at.set(detectorClass, searchClass, extActionClass, extActionMoveClass, commandExecutorClass, commandExecutorScoutClass, clusteringClass, pathPlanningClass);
			break;
		case FB_BUTTON_ID:
			config.fb.set(detectorClass, searchClass, extActionClass, extActionMoveClass, commandExecutorClass, commandExecutorScoutClass, clusteringClass, pathPlanningClass);
			break;
		case PF_BUTTON_ID:
			config.pf.set(detectorClass, searchClass, extActionClass, extActionMoveClass, commandExecutorClass, commandExecutorScoutClass, clusteringClass, pathPlanningClass);
			break;
		case AC_BUTTON_ID:
			break;
		case FS_BUTTON_ID:
			break;
		case PO_BUTTON_ID:
			break;
		}
	}

	public void setConfig(ModuleConfig config) {
		detector.getItems().clear();
		detector.getItems().addAll(config.detectors.keySet());
		detector.getSelectionModel().select("SampleRoadDetector");
		search.getItems().clear();
		search.getItems().addAll(config.searchs.keySet());
		search.getSelectionModel().selectFirst();
		extAction.getItems().clear();
		extAction.getItems().addAll(config.extActions.keySet());
		extAction.getSelectionModel().selectFirst();
		extActionMove.getItems().clear();
		extActionMove.getItems().addAll(config.extActions.keySet());
		extActionMove.getSelectionModel().selectFirst();
		//commandExecutor.getItems().clear();
		//commandExecutor.getItems().addAll(config..keySet());
		clustering.getItems().clear();
		clustering.getItems().addAll(config.clusterings.keySet());
		clustering.getSelectionModel().selectFirst();
		pathPlanning.getItems().clear();
		pathPlanning.getItems().addAll(config.pathPlannings.keySet());
		pathPlanning.getSelectionModel().selectFirst();
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