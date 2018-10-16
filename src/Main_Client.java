
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
		
		ComboBox<String> detector=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		ComboBox<String> search=(ComboBox<String>)nodeFX.getNode("Search_Box");
		ComboBox<String> extAction=(ComboBox<String>)nodeFX.getNode("ExtAction_Box");
		ComboBox<String> extActionMove=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		ComboBox<String> commandExecutor=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		ComboBox<String> commandExecutorScout=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		ComboBox<String> clustering=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		ComboBox<String> pathPlanning=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		
		
		List<ClassFile> classFiles=ClassReader.ClassRead(System.getProperty("user.home")+"/git/sample-master/src");
		ModuleConfig config=new ModuleConfig(classFiles);
		
		
		Scene scene=new Scene(nodeFX.root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
}