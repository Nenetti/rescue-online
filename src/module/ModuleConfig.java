package module;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ConstraintsBase;

public class ModuleConfig {

	public static enum Module{
		AT,
		FB,
		PF,
		AC,
		FS,
		PO;
	}

	private static final String AGENT_PANEL_ID = "AgentPanel";
	private static final String CENTER_PANEL_ID = "CenterPanel";

	public AnchorPane agentPanel;
	public AnchorPane centerPanel;

	public HashSet<ComboBox<String>> comboBoxs;

	public ComboBox<String> detector;
	public ComboBox<String> search;
	public ComboBox<String> extAction;
	public ComboBox<String> extActionMove;
	public ComboBox<String> commandExecutor;
	public ComboBox<String> commandExecutorScout;
	public ComboBox<String> detectorClustering;
	public ComboBox<String> detectorPathPlanning;
	public ComboBox<String> extActionPathPlanning;
	public ComboBox<String> extActionMovePathPlanning;
	public ComboBox<String> searchPathPlanning;
	public ComboBox<String> searchClustering;
	public ComboBox<String> targetAllocator;
	public ComboBox<String> commandPicker;

	public TabPane tabPane;
	public Tab at_Tab;
	public Tab fb_Tab;
	public Tab pf_Tab;
	public Tab ac_Tab;
	public Tab fs_Tab;
	public Tab po_Tab;
	public Tab exec_Tab;

	public HashMap<String, ClassFile> detectors = new HashMap<>();
	private HashMap<String, ClassFile> humanDetectors = new HashMap<>();
	private HashMap<String, ClassFile> buildingDetectors = new HashMap<>();
	private HashMap<String, ClassFile> roadDetectors = new HashMap<>();

	public HashMap<String, ClassFile> extActions = new HashMap<>();

	public HashMap<String, ClassFile> searchs = new HashMap<>();

	public HashMap<String, ClassFile> clusterings = new HashMap<>();
	private HashMap<String, ClassFile> dynamicClusterings = new HashMap<>();
	private HashMap<String, ClassFile> staticClusterings = new HashMap<>();

	public HashMap<String, ClassFile> pathPlannings = new HashMap<>();

	public HashMap<String, ClassFile> commandExecutors = new HashMap<>();
	private HashMap<String, ClassFile> commandExecutors_CommandPolice = new HashMap<>();
	private HashMap<String, ClassFile> commandExecutors_CommandAmbulance = new HashMap<>();
	private HashMap<String, ClassFile> commandExecutors_CommandFire = new HashMap<>();
	public HashMap<String, ClassFile> commandExecutors_CommandScout = new HashMap<>();

	public HashMap<String, ClassFile> targetAllocators = new HashMap<>();
	private HashMap<String, ClassFile> fireTargetAllocators = new HashMap<>();
	private HashMap<String, ClassFile> ambulanceTargetAllocators = new HashMap<>();
	private HashMap<String, ClassFile> policeTargetAllocators = new HashMap<>();

	public HashMap<String, ClassFile> commandPickers = new HashMap<>();


	private HashMap<Module, AgentConfig> agentConfigs = new HashMap<>();
	private HashMap<Module, CenterConfig> centerConfigs = new HashMap<>();

	public ModuleConfig(NodeFX nodeFX, List<ClassFile> files) {

		agentConfigs.put(Module.AT, new AgentConfig("AT"));
		agentConfigs.put(Module.FB, new AgentConfig("FB"));
		agentConfigs.put(Module.PF, new AgentConfig("PF"));
		centerConfigs.put(Module.AC, new CenterConfig("AC"));
		centerConfigs.put(Module.FS, new CenterConfig("FS"));
		centerConfigs.put(Module.PO, new CenterConfig("PO"));


		setupModuleList(files);
		setupConfig(System.getProperty("user.home")+"/git/sample-master/config/module_sample.cfg");

		setupGUI(nodeFX);
		updateConfig(Module.AT);

		changeConfig(Module.AT);
		updateGUI(Module.AT);
		//asdsaasdaaasa

	}

	@SuppressWarnings("incomplete-switch")
	private void setupConfig(String filePath) {
		HashMap<String, String> map=ModuleReader.read(filePath);
		AgentConfig at=agentConfigs.get(Module.AT);
		AgentConfig fb=agentConfigs.get(Module.FB);
		AgentConfig pf=agentConfigs.get(Module.PF);

		at.detector=detectors.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.HumanDetector")));
		at.search=searchs.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.Search")));
		at.extAction=extActions.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.ActionTransport")));
		at.extActionMove=extActions.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.ActionExtMove")));
		at.commandExecutor=commandExecutors.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.CommandExecutorAmbulance")));
		at.commandExecutorScout=commandExecutors_CommandScout.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.CommandExecutorScout")));

		fb.detector=detectors.get(ClassFile.toClassName(map.get("TacticsFireBrigade.BuildingDetector")));
		fb.search=searchs.get(ClassFile.toClassName(map.get("TacticsFireBrigade.Search")));
		fb.extAction=extActions.get(ClassFile.toClassName(map.get("TacticsFireBrigade.ActionFireFighting")));
		fb.extActionMove=extActions.get(ClassFile.toClassName(map.get("TacticsFireBrigade.ActionExtMove")));
		fb.commandExecutor=commandExecutors.get(ClassFile.toClassName(map.get("TacticsFireBrigade.CommandExecutorFire")));
		fb.commandExecutorScout=commandExecutors_CommandScout.get(ClassFile.toClassName(map.get("TacticsFireBrigade.CommandExecutorScout")));

		pf.detector=detectors.get(ClassFile.toClassName(map.get("TacticsPoliceForce.RoadDetector")));
		pf.search=searchs.get(ClassFile.toClassName(map.get("TacticsPoliceForce.Search")));
		pf.extAction=extActions.get(ClassFile.toClassName(map.get("TacticsPoliceForce.ActionExtClear")));
		pf.extActionMove=extActions.get(ClassFile.toClassName(map.get("TacticsPoliceForce.ActionExtMove")));
		pf.commandExecutor=commandExecutors.get(ClassFile.toClassName(map.get("TacticsPoliceForce.CommandExecutorPolice")));
		pf.commandExecutorScout=commandExecutors_CommandScout.get(ClassFile.toClassName(map.get("TacticsPoliceForce.CommandExecutorScout")));

		at.detectorClustering=clusterings.get(ClassFile.toClassName(map.get(at.detector.className+"."+"Clustering")));
		fb.detectorClustering=clusterings.get(ClassFile.toClassName(map.get(fb.detector.className+"."+"Clustering")));
		pf.detectorPpathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(pf.detector.className+"."+"PathPlanning")));

		at.searchPathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(at.search.className+"."+"PathPlanning.Ambulance")));
		at.searchClustering=clusterings.get(ClassFile.toClassName(map.get(at.search.className+"."+"Clustering.Ambulance")));
		fb.searchPathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(fb.search.className+"."+"PathPlanning.Fire")));
		fb.searchClustering=clusterings.get(ClassFile.toClassName(map.get(fb.search.className+"."+"Clustering.Fire")));
		pf.searchPathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(pf.search.className+"."+"PathPlanning.Police")));
		pf.searchClustering=clusterings.get(ClassFile.toClassName(map.get(pf.search.className+"."+"Clustering.Police")));
		
		at.extActionPathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(at.extAction.className+"."+"PathPlanning")));
		fb.extActionPathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(fb.extAction.className+"."+"PathPlanning")));
		pf.extActionPathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(pf.extAction.className+"."+"PathPlanning")));
		
		at.extActionMovePathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(at.extActionMove.className+"."+"PathPlanning")));
		fb.extActionMovePathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(fb.extActionMove.className+"."+"PathPlanning")));
		pf.extActionMovePathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(pf.extActionMove.className+"."+"PathPlanning")));
		
		at.extActionMovePathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(at.extActionMove.className+"."+"PathPlanning")));
		fb.extActionMovePathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(fb.extActionMove.className+"."+"PathPlanning")));
		pf.extActionMovePathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(pf.extActionMove.className+"."+"PathPlanning")));

	}


	private void setupModuleList(List<ClassFile> files) {
		for (ClassFile classFile : files) {
			switch (classFile.superClass) {
			// Detector系
			case "HumanDetector":
				humanDetectors.put(classFile.className, classFile);
				break;
			case "BuildingDetector":
				buildingDetectors.put(classFile.className, classFile);
				break;
			case "RoadDetector":
				roadDetectors.put(classFile.className, classFile);
				break;
				// パスプラ系
			case "PathPlanning":
				pathPlannings.put(classFile.className, classFile);
				break;
				// クラスタリング系
			case "DynamicClustering":
				dynamicClusterings.put(classFile.className, classFile);
				break;
			case "StaticClustering":
				staticClusterings.put(classFile.className, classFile);
				break;
				// アロケーター系
			case "FireTargetAllocator":
				fireTargetAllocators.put(classFile.className, classFile);
				break;
			case "AmbulanceTargetAllocator":
				ambulanceTargetAllocators.put(classFile.className, classFile);
				break;
			case "PoliceTargetAllocator":
				policeTargetAllocators.put(classFile.className, classFile);
				break;
				// サーチ系
			case "Search":
				searchs.put(classFile.className, classFile);
				break;
				// アクション系
			case "ExtAction":
				extActions.put(classFile.className, classFile);
				break;
				// コマンド系
			case "CommandPicker":
				commandPickers.put(classFile.className, classFile);
				break;
			default:
				// CommmandExecutorとかAbstractLoaderとか
				if (classFile.superClass.contains("CommandExecutor")) {
					if (classFile.superClass.contains("CommandAmbulance")) {
						commandExecutors_CommandAmbulance.put(classFile.className, classFile);
					} else if (classFile.superClass.contains("CommandFire")) {
						commandExecutors_CommandFire.put(classFile.className, classFile);
					} else if (classFile.superClass.contains("CommandPolice")) {
						commandExecutors_CommandPolice.put(classFile.className, classFile);
					} else if (classFile.superClass.contains("CommandScout")) {
						commandExecutors_CommandScout.put(classFile.className, classFile);
					}
				}
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
		commandExecutors.putAll(commandExecutors_CommandAmbulance);
		commandExecutors.putAll(commandExecutors_CommandFire);
		commandExecutors.putAll(commandExecutors_CommandPolice);
	}

	private void updateConfig(Module module) {

		for(ComboBox<String> box: comboBoxs) {
			box.getItems().clear();
		}
		switch (module) {
		case AT:
			detector.getItems().addAll(humanDetectors.keySet());
			commandExecutor.getItems().addAll(commandExecutors_CommandAmbulance.keySet());
			break;
		case FB:
			detector.getItems().addAll(buildingDetectors.keySet());
			commandExecutor.getItems().addAll(commandExecutors_CommandFire.keySet());
			break;
		case PF:
			detector.getItems().addAll(roadDetectors.keySet());
			commandExecutor.getItems().addAll(commandExecutors_CommandPolice.keySet());
			break;
		case AC:
			targetAllocator.getItems().addAll(ambulanceTargetAllocators.keySet());
			break;
		case FS:
			targetAllocator.getItems().addAll(fireTargetAllocators.keySet());
			break;
		case PO:
			targetAllocator.getItems().addAll(policeTargetAllocators.keySet());
			break;
		}
		search.getItems().addAll(searchs.keySet());
		extAction.getItems().addAll(extActions.keySet());
		extActionMove.getItems().addAll(extActions.keySet());
		commandExecutorScout.getItems().addAll(commandExecutors_CommandScout.keySet());
		detectorClustering.getItems().addAll(clusterings.keySet());
		searchClustering.getItems().addAll(clusterings.keySet());
		searchPathPlanning.getItems().addAll(pathPlannings.keySet());
		detectorPathPlanning.getItems().addAll(pathPlannings.keySet());
		extActionPathPlanning.getItems().addAll(pathPlannings.keySet());
		extActionMovePathPlanning.getItems().addAll(pathPlannings.keySet());
		commandPicker.getItems().addAll(commandPickers.keySet());
	}

	@SuppressWarnings("unchecked")
	private void setupGUI(NodeFX nodeFX) {
		at_Tab = nodeFX.getTab(Module.AT.toString());
		fb_Tab = nodeFX.getTab(Module.FB.toString());
		pf_Tab = nodeFX.getTab(Module.PF.toString());
		ac_Tab = nodeFX.getTab(Module.AC.toString());
		fs_Tab = nodeFX.getTab(Module.FS.toString());
		po_Tab = nodeFX.getTab(Module.PO.toString());
		agentPanel = nodeFX.getAnchorPane(AGENT_PANEL_ID);
		centerPanel = nodeFX.getAnchorPane(CENTER_PANEL_ID);
		exec_Tab = nodeFX.getTab("Execute");

		detector = (ComboBox<String>) nodeFX.getNode("Detector_Box");
		search = (ComboBox<String>) nodeFX.getNode("Search_Box");
		extAction = (ComboBox<String>) nodeFX.getNode("ExtAction_Box");
		extActionMove = (ComboBox<String>) nodeFX.getNode("ActionExtMove_Box");
		commandExecutor = (ComboBox<String>) nodeFX.getNode("CommandExecutor_Box");
		commandExecutorScout = (ComboBox<String>) nodeFX.getNode("CommandExecutorScout_Box");
		detectorClustering = (ComboBox<String>) nodeFX.getNode("Clustering_Box");
		searchClustering = (ComboBox<String>) nodeFX.getNode("SearchClustering_Box");
		detectorPathPlanning = (ComboBox<String>) nodeFX.getNode("DetectorPathPlanning_Box");
		extActionPathPlanning = (ComboBox<String>) nodeFX.getNode("ActionExtPathPlanning_Box");
		extActionMovePathPlanning = (ComboBox<String>) nodeFX.getNode("ActionExtMovePathPlanning_Box");
		searchPathPlanning = (ComboBox<String>) nodeFX.getNode("SearchPathPlanning_Box");
		commandPicker = (ComboBox<String>) nodeFX.getNode("CommandPicker_Box");
		targetAllocator = (ComboBox<String>) nodeFX.getNode("TargetAllocator_Box");
		tabPane = nodeFX.getTabPane("TabPane");

		comboBoxs=nodeFX.getAllNode(ComboBox.class).stream().map(t->(ComboBox<String>)t).collect(Collectors.toCollection(HashSet::new));
				
		comboBoxs.stream().forEach(t -> t.setStyle(t.getStyle()+" "+"-fx-font: 15px \"System\";"));
		agentPanel.setVisible(true);
		centerPanel.setVisible(false);
		setupEventHandler();
	}

	private void setupEventHandler() {
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
				Module oldModule=Module.valueOf(oldTab.getId());
				Module newModule=Module.valueOf(newTab.getId());
				saveConfig(oldModule);
				updateConfig(newModule);
				changeConfig(newModule);
				updateGUI(newModule);
			}
		});
	}
	
	private void updateGUI(Module module) {
		if(module==Module.AT||module==Module.FB||module==Module.PF) {
			switch (module) {
			case AT:
			case FB:
				detectorClustering.setDisable(false);
				detectorPathPlanning.setDisable(true);
				break;
			case PF:
				detectorClustering.setDisable(true);
				detectorPathPlanning.setDisable(false);
				break;
			}
			agentPanel.setVisible(true);
			centerPanel.setVisible(false);
		}else {
			agentPanel.setVisible(false);
			centerPanel.setVisible(true);
		}
	}

	private void changeConfig(Module module) {
		AgentConfig agentConfig = agentConfigs.get(module);
		CenterConfig centerConfig = centerConfigs.get(module);
		if (agentConfig != null) {
			set(detector, agentConfig.detector);
			set(search, agentConfig.search);
			set(extAction, agentConfig.extAction);
			set(extActionMove, agentConfig.extActionMove);
			set(commandExecutor, agentConfig.commandExecutor);
			set(commandExecutorScout, agentConfig.commandExecutorScout);
			set(detectorClustering, agentConfig.detectorClustering);
			set(searchClustering, agentConfig.searchClustering);
			set(detectorPathPlanning, agentConfig.detectorPpathPlanning);
			set(extActionPathPlanning, agentConfig.extActionPathPlanning);
			set(extActionMovePathPlanning, agentConfig.extActionMovePathPlanning);
			set(searchPathPlanning, agentConfig.searchPathPlanning);
		} else if (centerConfig != null) {
			set(targetAllocator, centerConfig.targetAllocator);
			set(commandPicker, centerConfig.commandPicker);
		}
	}

	private void set(ComboBox<String> box, ClassFile classFile) {
		if (classFile != null) {
			box.getSelectionModel().select(classFile.className);
			if (box.getSelectionModel().getSelectedIndex() == -1) {
				box.getSelectionModel().select(null);
			}
		} else {
			box.getSelectionModel().select(null);
		}
	}

	private void saveConfig(Module module) {
		AgentConfig agentConfig = agentConfigs.get(module);
		CenterConfig centerConfig = centerConfigs.get(module);
		if (agentConfig != null) {
			agentConfig.set(
					detectors.get(detector.getSelectionModel().getSelectedItem()),
					searchs.get(search.getSelectionModel().getSelectedItem()),
					extActions.get(extAction.getSelectionModel().getSelectedItem()),
					extActions.get(extActionMove.getSelectionModel().getSelectedItem()),
					commandExecutors.get(commandExecutor.getSelectionModel().getSelectedItem()),
					commandExecutors_CommandScout.get(commandExecutorScout.getSelectionModel().getSelectedItem()),
					clusterings.get(detectorClustering.getSelectionModel().getSelectedItem()),
					clusterings.get(searchClustering.getSelectionModel().getSelectedItem()),
					pathPlannings.get(detectorPathPlanning.getSelectionModel().getSelectedItem()),
					pathPlannings.get(extActionPathPlanning.getSelectionModel().getSelectedItem()),
					pathPlannings.get(extActionMovePathPlanning.getSelectionModel().getSelectedItem()),
					pathPlannings.get(searchPathPlanning.getSelectionModel().getSelectedItem()));
		} else if (centerConfig != null) {
			centerConfig.set(
					targetAllocators.get(targetAllocator.getSelectionModel().getSelectedItem()),
					commandPickers.get(commandPicker.getSelectionModel().getSelectedItem()));
		}
	}

	public void output() {
		for(Module agent: agentConfigs.keySet()) {
			AgentConfig config=agentConfigs.get(agent);

		}
		HashMap<String, String> map =new HashMap<>();
		for(Module agent: agentConfigs.keySet()) {
			AgentConfig config=agentConfigs.get(agent);
			String tactics=null;
			String detector=null;
			String extAction=null;
			String commandExecutor=null;
			String extActionMove="ActionExtMove";
			String search="Search";
			String commandExecutorScout="CommandExecutorScout";
			switch (agent) {
			case AT:
				tactics="TacticsAmbulanceTeam";
				detector="HumanDetector";
				extAction="ActionTransport";
				commandExecutor="CommandExecutorAmbulance";
				break;
			case FB:
				tactics="TacticsFireBrigade";
				detector="BuildingDetector";
				extAction="ActionFireFighting";
				commandExecutor="CommandExecutorFire";
				break;
			case PF:
				tactics="TacticsPoliceForce";
				detector="RoadDetector";
				extAction="ActionExtClear";
				commandExecutor="CommandExecutorPolice";
				break;
			}
			map.put(tactics+"."+detector, config.detector.toOutputFormat());
			map.put(tactics+"."+search, config.search.toOutputFormat());
			map.put(tactics+"."+extAction, config.extAction.toOutputFormat());
			map.put(tactics+"."+extActionMove, config.extActionMove.toOutputFormat());
			map.put(tactics+"."+commandExecutor, config.commandExecutor.toOutputFormat());
			map.put(tactics+"."+commandExecutorScout, config.commandExecutorScout.toOutputFormat());
			map.put(tactics+"."+detector, config.detector.toOutputFormat());
			map.put(tactics+"."+detector, config.detector.toOutputFormat());
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
		public ClassFile detectorClustering;
		public ClassFile searchClustering;
		public ClassFile detectorPpathPlanning;
		public ClassFile extActionPathPlanning;
		public ClassFile extActionMovePathPlanning;
		public ClassFile searchPathPlanning;

		public AgentConfig(String name) {
			this.name = name;
		}

		public void set(ClassFile detector, ClassFile search, ClassFile extAction, ClassFile extActionMove,
				ClassFile commandExecutor, ClassFile commandExecutorScout, ClassFile detectorClustering, ClassFile searchClustering,
				ClassFile detectorPpathPlanning, ClassFile extActionPathPlanning, ClassFile extActionMovePathPlanning, ClassFile searchPathPlanning) {
			this.detector = detector;
			this.search = search;
			this.extAction = extAction;
			this.extActionMove = extActionMove;
			this.commandExecutor = commandExecutor;
			this.commandExecutorScout = commandExecutorScout;
			this.detectorClustering = detectorClustering;
			this.searchClustering = searchClustering;
			this.detectorPpathPlanning=detectorPpathPlanning;
			this.extActionPathPlanning=extActionPathPlanning;
			this.extActionMovePathPlanning=extActionMovePathPlanning;
			this.searchPathPlanning=searchPathPlanning;
		}
		
		public class ExtAction {
			public ClassFile classFile;
			public ClassFile pathPlanning;
			public ClassFile clustering;
		}
		
		public class CommandExecutor{
			public ClassFile classFile;
			public ClassFile pathPlanning;
			public ClassFile clustering;
		}
		
		public class Detector {
			public ClassFile classFile;
			public ClassFile pathPlanning;
			public ClassFile clustering;
		}
	}

	public class CenterConfig {

		public String name;
		public ClassFile targetAllocator;
		public ClassFile commandPicker;

		public CenterConfig(String name) {
			this.name = name;
		}

		public void set(ClassFile targetAllocator, ClassFile commandPicker) {
			this.targetAllocator = targetAllocator;
			this.commandPicker = commandPicker;
		}
	}

}