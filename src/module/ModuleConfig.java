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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ConstraintsBase;
import module.ModuleConfig.AgentConfig.CommandExecutor;
import module.ModuleConfig.AgentConfig.CommandExecutorScout;
import module.ModuleConfig.AgentConfig.Detector;
import module.ModuleConfig.AgentConfig.ExtAction;
import module.ModuleConfig.AgentConfig.ExtActionMove;
import module.ModuleConfig.AgentConfig.Search;

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
	public ComboBox<String> commandExecutorPathPlanning;
	public ComboBox<String> commandExecutorScout;
	public ComboBox<String> commandExecutorScoutPathPlanning;
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
	public Button exec_Button;

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
	public HashMap<String, ClassFile> commandExecutorsScout = new HashMap<>();

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

	}

	@SuppressWarnings("incomplete-switch")
	private void setupConfig(String filePath) {
		HashMap<String, String> map=ModuleReader.read(filePath);

		for(Module module:Module.values()) {
			switch (module) {
			case AT:case FB:case PF:
				AgentConfig agent=agentConfigs.get(module);
				switch (module) {
				case AT:
					agent.detector.classFile=detectors.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.HumanDetector")));
					agent.search.classFile=searchs.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.Search")));
					agent.extAction.classFile=extActions.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.ActionTransport")));
					agent.extActionMove.classFile=extActions.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.ActionExtMove")));
					agent.commandExecutor.classFile=commandExecutors.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.CommandExecutorAmbulance")));
					agent.commandExecutorScout.classFile=commandExecutorsScout.get(ClassFile.toClassName(map.get("TacticsAmbulanceTeam.CommandExecutorScout")));
					agent.search.clustering=clusterings.get(ClassFile.toClassName(map.get(agent.search.classFile.className+"."+"Clustering.Ambulance")));
					agent.commandExecutor.extAction=extActions.get(ClassFile.toClassName(map.get(agent.commandExecutor.classFile.className+"."+"ActionTransport")));
					agent.detector.clustering=clusterings.get(ClassFile.toClassName(map.get(agent.detector.classFile.className+"."+"Clustering")));
					break;
				case FB:
					agent.detector.classFile=detectors.get(ClassFile.toClassName(map.get("TacticsFireBrigade.BuildingDetector")));
					agent.search.classFile=searchs.get(ClassFile.toClassName(map.get("TacticsFireBrigade.Search")));
					agent.extAction.classFile=extActions.get(ClassFile.toClassName(map.get("TacticsFireBrigade.ActionFireFighting")));
					agent.extActionMove.classFile=extActions.get(ClassFile.toClassName(map.get("TacticsFireBrigade.ActionExtMove")));
					agent.commandExecutor.classFile=commandExecutors.get(ClassFile.toClassName(map.get("TacticsFireBrigade.CommandExecutorFire")));
					agent.commandExecutorScout.classFile=commandExecutorsScout.get(ClassFile.toClassName(map.get("TacticsFireBrigade.CommandExecutorScout")));
					agent.search.clustering=clusterings.get(ClassFile.toClassName(map.get(agent.search.classFile.className+"."+"Clustering.Fire")));
					agent.commandExecutor.extAction=extActions.get(ClassFile.toClassName(map.get(agent.commandExecutor.classFile.className+"."+"ActionFireFighting")));
					agent.detector.clustering=clusterings.get(ClassFile.toClassName(map.get(agent.detector.classFile.className+"."+"Clustering")));
					break;
				case PF:
					agent.detector.classFile=detectors.get(ClassFile.toClassName(map.get("TacticsPoliceForce.RoadDetector")));
					agent.search.classFile=searchs.get(ClassFile.toClassName(map.get("TacticsPoliceForce.Search")));
					agent.extAction.classFile=extActions.get(ClassFile.toClassName(map.get("TacticsPoliceForce.ActionExtClear")));
					agent.extActionMove.classFile=extActions.get(ClassFile.toClassName(map.get("TacticsPoliceForce.ActionExtMove")));
					agent.commandExecutor.classFile=commandExecutors.get(ClassFile.toClassName(map.get("TacticsPoliceForce.CommandExecutorPolice")));
					agent.commandExecutorScout.classFile=commandExecutorsScout.get(ClassFile.toClassName(map.get("TacticsPoliceForce.CommandExecutorScout")));
					agent.search.clustering=clusterings.get(ClassFile.toClassName(map.get(agent.search.classFile.className+"."+"Clustering.Police")));
					agent.commandExecutor.extAction=extActions.get(ClassFile.toClassName(map.get(agent.commandExecutor.classFile.className+"."+"ActionExtClear")));
					agent.detector.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.detector.classFile.className+"."+"PathPlanning")));
					break;
				}
				agent.search.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.search.classFile.className+"."+"PathPlanning.Ambulance")));
				agent.extAction.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.extAction.classFile.className+"."+"PathPlanning")));
				agent.extActionMove.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.extActionMove.classFile.className+"."+"PathPlanning")));
				agent.commandExecutor.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.commandExecutor.classFile.className+"."+"PathPlanning")));
				agent.commandExecutor.extActionMove=extActions.get(ClassFile.toClassName(map.get(agent.commandExecutor.classFile.className+"."+"ActionExtMove")));
				//agent.commandExecutorScout.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.commandExecutorScout.classFile.className+"."+"PathPlanning")));
				//agent.commandExecutorScout.extActionMove=extActions.get(ClassFile.toClassName(map.get(agent.commandExecutorScout.classFile.className+"."+"ActionExtMove")));
				break;
			case AC:case FS:case PO:
				CenterConfig center=centerConfigs.get(module);
				switch (module) {
				case AC:
					center.targetAllocator=targetAllocators.get(ClassFile.toClassName(map.get("TacticsAmbulanceCentre.TargetAllocator")));
					center.commandPicker=commandPickers.get(ClassFile.toClassName(map.get("TacticsAmbulanceCentre.CommandPicker")));
					break;
				case FS:
					center.targetAllocator=targetAllocators.get(ClassFile.toClassName(map.get("TacticsFireStation.TargetAllocator")));
					center.commandPicker=commandPickers.get(ClassFile.toClassName(map.get("TacticsFireStation.CommandPicker")));
					break;
				case PO:
					center.targetAllocator=targetAllocators.get(ClassFile.toClassName(map.get("TacticsPoliceOffice.TargetAllocator")));
					center.commandPicker=commandPickers.get(ClassFile.toClassName(map.get("TacticsPoliceOffice.CommandPicker")));	
					break;
				}
				break;
			}
		}
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
						commandExecutorsScout.put(classFile.className, classFile);
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
		commandExecutorScout.getItems().addAll(commandExecutorsScout.keySet());
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
		exec_Button = nodeFX.getButton("Execute");

		detector = (ComboBox<String>) nodeFX.getNode("Detector_Box");
		search = (ComboBox<String>) nodeFX.getNode("Search_Box");
		extAction = (ComboBox<String>) nodeFX.getNode("ExtAction_Box");
		extActionMove = (ComboBox<String>) nodeFX.getNode("ActionExtMove_Box");
		commandExecutor = (ComboBox<String>) nodeFX.getNode("CommandExecutor_Box");
		commandExecutorScout = (ComboBox<String>) nodeFX.getNode("CommandExecutorScout_Box");
		commandExecutorPathPlanning = (ComboBox<String>) nodeFX.getNode("CommandExecutorPathPlanning_Box");
		commandExecutorScoutPathPlanning = (ComboBox<String>) nodeFX.getNode("CommandExecutorScoutPathPlanning_Box");
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
		exec_Button.setOnAction((ActionEvent e)->{
			output();
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
			set(detector, agentConfig.detector.classFile);
			set(search, agentConfig.search.classFile);
			set(extAction, agentConfig.extAction.classFile);
			set(extActionMove, agentConfig.extActionMove.classFile);
			set(commandExecutor, agentConfig.commandExecutor.classFile);
			set(commandExecutorScout, agentConfig.commandExecutorScout.classFile);
			set(detectorClustering, agentConfig.detector.clustering);
			set(searchClustering, agentConfig.search.clustering);
			set(detectorPathPlanning, agentConfig.detector.pathPlanning);
			set(extActionPathPlanning, agentConfig.extAction.pathPlanning);
			set(extActionMovePathPlanning, agentConfig.extActionMove.pathPlanning);
			set(searchPathPlanning, agentConfig.search.pathPlanning);
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
			agentConfig.detector.set(detectors.get(getSelectItem(detector)), pathPlannings.get(getSelectItem(detectorPathPlanning)), clusterings.get(getSelectItem(detectorClustering)));
			agentConfig.search.set(searchs.get(getSelectItem(search)), pathPlannings.get(getSelectItem(searchPathPlanning)), clusterings.get(getSelectItem(searchClustering)));
			agentConfig.extAction.set(extActions.get(getSelectItem(extAction)), pathPlannings.get(getSelectItem(extActionPathPlanning)));
			agentConfig.extActionMove.set(extActions.get(getSelectItem(extActionMove)), pathPlannings.get(getSelectItem(extActionMovePathPlanning)));
			agentConfig.commandExecutor.set(commandExecutors.get(getSelectItem(commandExecutor)), pathPlannings.get(getSelectItem(commandExecutorPathPlanning)), null, null);
			agentConfig.commandExecutorScout.set(commandExecutorsScout.get(getSelectItem(commandExecutorScout)), pathPlannings.get(getSelectItem(commandExecutorScoutPathPlanning)), null, null);
			
			//後日修正予定
			agentConfig.commandExecutor.extAction=agentConfig.extAction.classFile;
			agentConfig.commandExecutor.extActionMove=agentConfig.extActionMove.classFile;
		} else if (centerConfig != null) {
			centerConfig.set(
					targetAllocators.get(targetAllocator.getSelectionModel().getSelectedItem()),
					commandPickers.get(commandPicker.getSelectionModel().getSelectedItem()));
		}
	}

	private String getSelectItem(ComboBox<String> comboBox) {
		return comboBox.getSelectionModel().getSelectedItem();
	}

	public void output() {
		HashMap<String, String> map =new HashMap<>();
		for(Module module: agentConfigs.keySet()) {
			AgentConfig agent=agentConfigs.get(module);
			String tactics=null;
			String detector=null;
			String extAction=null;
			String commandExecutor=null;
			String extActionMove="ActionExtMove";
			String search="Search";
			String commandExecutorScout="CommandExecutorScout";
			String type=null;
			switch (module) {
			case AT:
				type="Ambulance";
				tactics="TacticsAmbulanceTeam";
				detector="HumanDetector";
				extAction="ActionTransport";
				commandExecutor="CommandExecutorAmbulance";
				break;
			case FB:
				type="Fire";
				tactics="TacticsFireBrigade";
				detector="BuildingDetector";
				extAction="ActionFireFighting";
				commandExecutor="CommandExecutorFire";
				break;
			case PF:
				type="Police";
				tactics="TacticsPoliceForce";
				detector="RoadDetector";
				extAction="ActionExtClear";
				commandExecutor="CommandExecutorPolice";
				break;
			}

			map.put(tactics+"."+detector, agent.detector.classFile.toOutputFormat());
			map.put(tactics+"."+search, agent.search.classFile.toOutputFormat());
			map.put(tactics+"."+extAction, agent.extAction.classFile.toOutputFormat());
			map.put(tactics+"."+extActionMove, agent.extActionMove.classFile.toOutputFormat());
			map.put(tactics+"."+commandExecutor, agent.commandExecutor.classFile.toOutputFormat());
			map.put(tactics+"."+commandExecutorScout, agent.commandExecutorScout.classFile.toOutputFormat());

			if(agent.detector.clustering!=null) map.put(agent.detector.clustering.className+"."+"Clustering", agent.detector.clustering.toOutputFormat());
			if(agent.detector.pathPlanning!=null) map.put(agent.detector.pathPlanning.className+"."+"PathPlanning", agent.detector.pathPlanning.toOutputFormat());

			map.put(agent.search.pathPlanning.className+"."+"PathPlanning"+"."+type, agent.search.pathPlanning.toOutputFormat());
			map.put(agent.search.clustering.className+"."+"Clustering"+"."+type, agent.search.clustering.toOutputFormat());

			map.put(agent.extAction.pathPlanning.className+"."+"PathPlanning", agent.extAction.pathPlanning.toOutputFormat());
			map.put(agent.extActionMove.pathPlanning.className+"."+"PathPlanning", agent.extActionMove.pathPlanning.toOutputFormat());

			map.put(agent.commandExecutor.pathPlanning.className+"."+"PathPlanning", agent.commandExecutor.classFile.toOutputFormat());
			map.put(agent.commandExecutor.extAction.className+"."+extAction, agent.commandExecutor.extAction.toOutputFormat());
			map.put(agent.commandExecutor.extActionMove.className+"."+extActionMove, agent.commandExecutor.extActionMove.toOutputFormat());
			
			
			//map.put(agent.commandExecutorScout.pathPlanning.className+"."+"PathPlanning", agent.commandExecutorScout.classFile.toOutputFormat());
			//map.put(agent.commandExecutorScout.extAction.className+"."+extAction, agent.commandExecutorScout.extAction.toOutputFormat());
			//map.put(agent.commandExecutorScout.extActionMove.className+"."+extActionMove, agent.commandExecutorScout.extActionMove.toOutputFormat());
			
		}
		
		for(String key:map.keySet()) {
			System.out.println(key+" : "+map.get(key));
		}
		
		
	}

	public static class AgentConfig {

		public String name;
		public Detector detector;
		public Search search;
		public ExtAction extAction;
		public ExtActionMove extActionMove;
		public CommandExecutor commandExecutor;
		public CommandExecutorScout commandExecutorScout;

		public AgentConfig(String name) {
			this.name = name;
			this.detector=new Detector();
			this.search=new Search();
			this.extAction=new ExtAction();
			this.extActionMove=new ExtActionMove();
			this.commandExecutor=new CommandExecutor();
			this.commandExecutorScout=new CommandExecutorScout();
		}

		public void set(Detector detector, Search search, ExtAction extAction, ExtActionMove extActionMove,
				CommandExecutor commandExecutor, CommandExecutorScout commandExecutorScout) {

			this.detector = detector;
			this.search = search;
			this.extAction = extAction;
			this.extActionMove = extActionMove;
			this.commandExecutor = commandExecutor;
			this.commandExecutorScout = commandExecutorScout;

		}

		public static class ExtAction {
			public ClassFile classFile;
			public ClassFile pathPlanning;
			public ExtAction() {
			}
			public void set(ClassFile classFile, ClassFile pathPlanning) {
				this.classFile=classFile;
				this.pathPlanning=pathPlanning;
			}
		}

		public static class ExtActionMove {
			public ClassFile classFile;
			public ClassFile pathPlanning;
			public ExtActionMove() {
			}
			public void set(ClassFile classFile, ClassFile pathPlanning) {
				this.classFile=classFile;
				this.pathPlanning=pathPlanning;
			}
		}

		public static class Search {
			public ClassFile classFile;
			public ClassFile pathPlanning;
			public ClassFile clustering;
			public Search() {
			}
			public void set(ClassFile classFile, ClassFile pathPlanning, ClassFile clustering) {
				this.classFile=classFile;
				this.pathPlanning=pathPlanning;
				this.clustering=clustering;
			}
		}

		public static class CommandExecutorScout{
			public ClassFile classFile;
			public ClassFile pathPlanning;
			public ClassFile extAction;
			public ClassFile extActionMove;		
			public CommandExecutorScout() {
			}
			public void set(ClassFile classFile, ClassFile pathPlanning, ClassFile extAction, ClassFile extActionMove) {
				this.classFile=classFile;
				this.pathPlanning=pathPlanning;
				this.extAction=extAction;
				this.extActionMove=extActionMove;
			}
		}

		public static class CommandExecutor{
			public ClassFile classFile;
			public ClassFile pathPlanning;
			public ClassFile extAction;
			public ClassFile extActionMove;		
			public CommandExecutor() {
			}
			public void set(ClassFile classFile, ClassFile pathPlanning, ClassFile extAction, ClassFile extActionMove) {
				this.classFile=classFile;
				this.pathPlanning=pathPlanning;
				this.extAction=extAction;
				this.extActionMove=extActionMove;
			}
		}

		public static class Detector {
			public ClassFile classFile;
			public ClassFile pathPlanning;
			public ClassFile clustering;
			public Detector() {
			}
			public void set(ClassFile classFile, ClassFile pathPlanning, ClassFile clustering) {
				this.classFile=classFile;
				this.pathPlanning=pathPlanning;
				this.clustering=clustering;
			}
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