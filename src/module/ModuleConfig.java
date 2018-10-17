package module;

import java.util.HashMap;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;


public class ModuleConfig {

	private static final String AT_BUTTON_ID="AT_Button";
	private static final String FB_BUTTON_ID="FB_Button";
	private static final String PF_BUTTON_ID="PF_Button";
	private static final String AC_BUTTON_ID="AC_Button";
	private static final String FS_BUTTON_ID="FS_Button";
	private static final String PO_BUTTON_ID="PO_Button";
	private static final String AGENT_PANEL_ID="AgentPanel";
	private static final String CENTER_PANEL_ID="CenterPanel";

	public String selectButtonID=AT_BUTTON_ID;

	public AnchorPane agentPanel;
	public AnchorPane centerPanel;

	public ComboBox<String> detector;
	public ComboBox<String> search;
	public ComboBox<String> extAction;
	public ComboBox<String> extActionMove;
	public ComboBox<String> commandExecutor;
	public ComboBox<String> commandExecutorScout;
	public ComboBox<String> clustering;
	public ComboBox<String> pathPlanning;
	public ComboBox<String> targetAllocator;
	public ComboBox<String> commandPicker;

	public Button at_Button;
	public Button fb_Button;
	public Button pf_Button;
	public Button ac_Button;
	public Button fs_Button;
	public Button po_Button;

	public HashMap<String, ClassFile> detectors=new HashMap<>();
	private HashMap<String, ClassFile> humanDetectors=new HashMap<>();
	private HashMap<String, ClassFile> buildingDetectors=new HashMap<>();
	private HashMap<String, ClassFile> roadDetectors=new HashMap<>();

	public HashMap<String, ClassFile> extActions=new HashMap<>();

	public HashMap<String, ClassFile> searchs=new HashMap<>();
	public HashMap<String, ClassFile> commandPickers=new HashMap<>();

	public HashMap<String, ClassFile> clusterings=new HashMap<>();
	private HashMap<String, ClassFile> dynamicClusterings=new HashMap<>();
	private HashMap<String, ClassFile> staticClusterings=new HashMap<>();

	public HashMap<String, ClassFile> pathPlannings=new HashMap<>();

	public HashMap<String, ClassFile> targetAllocators=new HashMap<>();
	public HashMap<String, ClassFile> fireTargetAllocators=new HashMap<>();
	public HashMap<String, ClassFile> ambulanceTargetAllocators=new HashMap<>();
	public HashMap<String, ClassFile> policeTargetAllocators=new HashMap<>();

	public HashMap<String, ClassFile> tacticsFireStations=new HashMap<>();
	public HashMap<String, ClassFile> tacticsPoliceOffices=new HashMap<>();
	public HashMap<String, ClassFile> tacticsFireBrigades=new HashMap<>();
	public HashMap<String, ClassFile> tacticsAmbulanceCentres=new HashMap<>();
	public HashMap<String, ClassFile> tacticsAmbulanceTeams=new HashMap<>();
	public HashMap<String, ClassFile> tacticsPoliceForces=new HashMap<>();

	private HashMap<String, AgentConfig> agentConfigs=new HashMap<>();
	private HashMap<String, CenterConfig> centerConfigs=new HashMap<>();

	public ModuleConfig(NodeFX nodeFX, List<ClassFile> files) {

		setupModuleList(files);
		setupGUI(nodeFX);
		setupConfig();

		saveConfig(AT_BUTTON_ID);
		saveConfig(FB_BUTTON_ID);
		saveConfig(PF_BUTTON_ID);

	}

	private void setupModuleList(List<ClassFile> files) {
		for(ClassFile classFile: files) {
			switch (classFile.superClass) {
			//Detector系
			case "HumanDetector":
				humanDetectors.put(classFile.className, classFile);
				break;
			case "BuildingDetector":
				buildingDetectors.put(classFile.className, classFile);
				break;
			case "RoadDetector":
				roadDetectors.put(classFile.className, classFile);
				break;
				//パスプラ系
			case "PathPlanning":
				pathPlannings.put(classFile.className, classFile);
				break;
				//クラスタリング系
			case "DynamicClustering":
				dynamicClusterings.put(classFile.className, classFile);
				break;
			case "StaticClustering":
				staticClusterings.put(classFile.className, classFile);
				break;
				//アロケーター系
			case "FireTargetAllocator":
				fireTargetAllocators.put(classFile.className, classFile);
				break;
			case "AmbulanceTargetAllocator":
				ambulanceTargetAllocators.put(classFile.className, classFile);
				break;
			case "PoliceTargetAllocator":
				policeTargetAllocators.put(classFile.className, classFile);
				break;
				//サーチ系
			case "Search":
				searchs.put(classFile.className, classFile);
				break;
				//タクティクス系
			case "TacticsFireStation":
				tacticsFireStations.put(classFile.className, classFile);
				break;
			case "TacticsPoliceOffice":
				tacticsPoliceOffices.put(classFile.className, classFile);
				break;
			case "TacticsFireBrigade":
				tacticsFireBrigades.put(classFile.className, classFile);
				break;
			case "TacticsAmbulanceCentre":
				tacticsAmbulanceCentres.put(classFile.className, classFile);
				break;
			case "TacticsAmbulanceTeam":
				tacticsAmbulanceTeams.put(classFile.className, classFile);
				break;
			case "TacticsPoliceForce":
				tacticsPoliceForces.put(classFile.className, classFile);
				break;
				//アクション系
			case "ExtAction":
				extActions.put(classFile.className, classFile);
				break;
				//コマンド系
			case "CommandPicker":
				commandPickers.put(classFile.className, classFile);
				break;
			default:
				//未登録リスト
				//System.out.println("判別不可: "+classFile.superClass);
				break;
			}
		}
		detectors.putAll(buildingDetectors);
		detectors.putAll(humanDetectors);
		detectors.putAll(roadDetectors);
		clusterings.putAll(dynamicClusterings);
		clusterings.putAll(staticClusterings);
		targetAllocators.putAll(ambulanceTargetAllocators);
		targetAllocators.putAll(fireTargetAllocators);
		targetAllocators.putAll(policeTargetAllocators);
	}

	private void setupConfig() {
		detector.getItems().clear();
		detector.getItems().addAll(detectors.keySet());
		detector.getSelectionModel().selectFirst();
		search.getItems().clear();
		search.getItems().addAll(searchs.keySet());
		search.getSelectionModel().selectFirst();
		extAction.getItems().clear();
		extAction.getItems().addAll(extActions.keySet());
		extAction.getSelectionModel().selectFirst();
		extActionMove.getItems().clear();
		extActionMove.getItems().addAll(extActions.keySet());
		extActionMove.getSelectionModel().selectFirst();
		//commandExecutor.getItems().clear();
		//commandExecutor.getItems().addAll(.keySet());
		clustering.getItems().clear();
		clustering.getItems().addAll(clusterings.keySet());
		clustering.getSelectionModel().selectFirst();
		pathPlanning.getItems().clear();
		pathPlanning.getItems().addAll(pathPlannings.keySet());
		pathPlanning.getSelectionModel().selectFirst();

		agentConfigs.put(AT_BUTTON_ID, new AgentConfig("AT"));
		agentConfigs.put(FB_BUTTON_ID, new AgentConfig("FB"));
		agentConfigs.put(PF_BUTTON_ID, new AgentConfig("PF"));
		centerConfigs.put(AC_BUTTON_ID, new CenterConfig("AC"));
		centerConfigs.put(FS_BUTTON_ID, new CenterConfig("FS"));
		centerConfigs.put(PO_BUTTON_ID, new CenterConfig("PO"));

	}

	@SuppressWarnings("unchecked")
	private void setupGUI(NodeFX nodeFX) {
		at_Button=nodeFX.getButton(AT_BUTTON_ID);
		fb_Button=nodeFX.getButton(FB_BUTTON_ID);
		pf_Button=nodeFX.getButton(PF_BUTTON_ID);
		ac_Button=nodeFX.getButton(AC_BUTTON_ID);
		fs_Button=nodeFX.getButton(FS_BUTTON_ID);
		po_Button=nodeFX.getButton(PO_BUTTON_ID);
		agentPanel=nodeFX.getAnchorPane(AGENT_PANEL_ID);
		centerPanel=nodeFX.getAnchorPane(CENTER_PANEL_ID);

		detector=(ComboBox<String>)nodeFX.getNode("Detector_Box");
		search=(ComboBox<String>)nodeFX.getNode("Search_Box");
		extAction=(ComboBox<String>)nodeFX.getNode("ExtAction_Box");
		extActionMove=(ComboBox<String>)nodeFX.getNode("ActionExtMove_Box");
		commandExecutor=(ComboBox<String>)nodeFX.getNode("CommandExecutor_Box");
		commandExecutorScout=(ComboBox<String>)nodeFX.getNode("CommandExecutorScout_Box");
		clustering=(ComboBox<String>)nodeFX.getNode("Clustering_Box");
		pathPlanning=(ComboBox<String>)nodeFX.getNode("PathPlanning_Box");

		agentPanel.setVisible(true);
		centerPanel.setVisible(false);
		setupEventHandler();
	}

	private void setupEventHandler() {
		EventHandler<ActionEvent> agentEventHandler=new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button source=(Button)event.getSource();
				saveConfig(selectButtonID);
				changeConfig(source.getId());
				selectButtonID=source.getId();
			}
		};
		EventHandler<ActionEvent> centerEventHandler=new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button source=(Button)event.getSource();
				saveConfig(selectButtonID);
				changeConfig(source.getId());
				selectButtonID=source.getId();
			}
		};
		at_Button.setOnAction(agentEventHandler);
		fb_Button.setOnAction(agentEventHandler);
		pf_Button.setOnAction(agentEventHandler);
		ac_Button.setOnAction(centerEventHandler);
		fs_Button.setOnAction(centerEventHandler);
		po_Button.setOnAction(centerEventHandler);
	}

	private void changeConfig(String agentID) {
		AgentConfig agentConfig=agentConfigs.get(agentID);
		CenterConfig centerConfig=centerConfigs.get(agentID);
		
		if(agentConfig!=null) {
			set(detector, agentConfig.detector);
			set(search, agentConfig.search);
			set(extAction, agentConfig.extAction);
			set(extActionMove, agentConfig.extActionMove);
			set(commandExecutor, agentConfig.commandExecutor);
			set(commandExecutorScout, agentConfig.commandExecutorScout);
			set(clustering, agentConfig.clustering);
			set(pathPlanning, agentConfig.pathPlanning);
		}else if(centerConfig!=null){
			set(targetAllocator, centerConfig.targetAllocator);
			set(commandPicker, centerConfig.commandPicker);
		}
		
	}

	private void set(ComboBox<String> box, ClassFile classFile) {
		if(classFile!=null) {
			box.getSelectionModel().select(classFile.className);
			if(box.getSelectionModel().getSelectedIndex()==-1) {
				box.getSelectionModel().select(null);
			}
		}else {
			box.getSelectionModel().select(null);			
		}
	}

	private void saveConfig(String agentID) {
		AgentConfig agentConfig=agentConfigs.get(agentID);
		CenterConfig centerConfig=centerConfigs.get(agentID);
		
		if(agentConfig!=null) {
			agentConfig.set(
					detectors.get(detector.getSelectionModel().getSelectedItem()),
					searchs.get(search.getSelectionModel().getSelectedItem()),
					extActions.get(extAction.getSelectionModel().getSelectedItem()),
					extActions.get(extActionMove.getSelectionModel().getSelectedItem()),
					commandPickers.get(commandExecutor.getSelectionModel().getSelectedItem()),
					commandPickers.get(commandExecutorScout.getSelectionModel().getSelectedItem()),
					clusterings.get(clustering.getSelectionModel().getSelectedItem()),
					pathPlannings.get(pathPlanning.getSelectionModel().getSelectedItem())
					);
		}else if(centerConfig!=null){
			centerConfig.set(
					targetAllocators.get(targetAllocator.getSelectionModel().getSelectedItem()),
					commandPickers.get(commandPicker.getSelectionModel().getSelectedItem())
					);
		}
	}






	public class AgentConfig {

		public String name;
		public ClassFile detector;
		public ClassFile search;
		public ClassFile extAction;
		public ClassFile extActionMove;
		public ClassFile commandExecutor;
		public ClassFile commandExecutorScout;
		public ClassFile clustering;
		public ClassFile pathPlanning;

		public AgentConfig(String name) {
			this.name=name;
		}

		public void set(ClassFile detector, ClassFile search, ClassFile extAction, ClassFile extActionMove,
				ClassFile commandExecutor, ClassFile commandExecutorScout, ClassFile clustering, ClassFile pathPlanning) {
			this.detector=detector;
			this.search=search;
			this.extAction=extAction;
			this.extActionMove=extActionMove;
			this.commandExecutor=commandExecutor;
			this.commandExecutorScout=commandExecutorScout;
			this.clustering=clustering;
			this.pathPlanning=pathPlanning;
		}
	}

	public class CenterConfig {

		public String name;
		public ClassFile targetAllocator;
		public ClassFile commandPicker;

		public CenterConfig(String name) {
			this.name=name;
		}

		public void set(ClassFile targetAllocator, ClassFile commandPicker) {
			this.targetAllocator=targetAllocator;
			this.commandPicker=commandPicker;
		}
	}

}