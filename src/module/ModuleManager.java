package module;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import module.ModuleManager.AgentConfig.CommandExecutor;
import module.ModuleManager.AgentConfig.CommandExecutorScout;
import module.ModuleManager.AgentConfig.Detector;
import module.ModuleManager.AgentConfig.ExtAction;
import module.ModuleManager.AgentConfig.ExtActionMove;
import module.ModuleManager.AgentConfig.Search;

public class ModuleManager {

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
	private String sourcePath;

	public AnchorPane agentPanel;
	public AnchorPane centerPanel;

	public HashSet<ComboBox<String>> comboBoxs;

	public ComboBox<String> detector;
	public ComboBox<String> detectorClustering;
	public ComboBox<String> detectorPathPlanning;
	public ComboBox<String> search;
	public ComboBox<String> searchClustering;
	public ComboBox<String> searchPathPlanning;
	public ComboBox<String> extAction;
	public ComboBox<String> extActionPathPlanning;
	public ComboBox<String> extActionMove;
	public ComboBox<String> extActionMovePathPlanning;
	public ComboBox<String> commandExecutor;
	public ComboBox<String> commandExecutorPathPlanning;
	public ComboBox<String> commandExecutorExtAction;
	public ComboBox<String> commandExecutorActionExtMove;
	public ComboBox<String> commandExecutorScout;
	public ComboBox<String> commandExecutorScoutPathPlanning;
	public ComboBox<String> commandExecutorScoutExtAction;

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
	public Button save_Button;

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

	public ModuleManager(NodeFX nodeFX, String path) {
		this.sourcePath=path;
		setupGUI(nodeFX);
	}

	public void start() {

		List<ClassFile> classFiles=ClassReader.ClassRead(sourcePath);
		
		agentConfigs.put(Module.AT, new AgentConfig("AT"));
		agentConfigs.put(Module.FB, new AgentConfig("FB"));
		agentConfigs.put(Module.PF, new AgentConfig("PF"));
		centerConfigs.put(Module.AC, new CenterConfig("AC"));
		centerConfigs.put(Module.FS, new CenterConfig("FS"));
		centerConfigs.put(Module.PO, new CenterConfig("PO"));

		setupModuleList(classFiles);
		setupConfig(sourcePath+"/config/"+"module.cfg");
		updateConfig(Module.AT);
		changeConfig(Module.AT);
		updateGUI(Module.AT);
	}

	/******************************************************************************************************************************************************
	 * 
	 * JAVFX関連の初期設定
	 * 
	 * @param nodeFX
	 */

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
		save_Button = nodeFX.getButton("Save");

		detector = (ComboBox<String>) nodeFX.getNode("Detector_Box");
		detectorClustering = (ComboBox<String>) nodeFX.getNode("Clustering_Box");
		detectorPathPlanning = (ComboBox<String>) nodeFX.getNode("DetectorPathPlanning_Box");

		search = (ComboBox<String>) nodeFX.getNode("Search_Box");
		searchClustering = (ComboBox<String>) nodeFX.getNode("SearchClustering_Box");
		searchPathPlanning = (ComboBox<String>) nodeFX.getNode("SearchPathPlanning_Box");

		extAction = (ComboBox<String>) nodeFX.getNode("ExtAction_Box");
		extActionPathPlanning = (ComboBox<String>) nodeFX.getNode("ActionExtPathPlanning_Box");
		extActionMove = (ComboBox<String>) nodeFX.getNode("ActionExtMove_Box");
		extActionMovePathPlanning = (ComboBox<String>) nodeFX.getNode("ActionExtMovePathPlanning_Box");

		commandExecutor = (ComboBox<String>) nodeFX.getNode("CommandExecutor_Box");
		commandExecutorPathPlanning = (ComboBox<String>) nodeFX.getNode("CommandExecutorPathPlanning_Box");
		commandExecutorExtAction = (ComboBox<String>) nodeFX.getNode("CommandExecutorExtAction_Box");
		commandExecutorActionExtMove = (ComboBox<String>) nodeFX.getNode("CommandExecutorActionExtMove_Box");

		commandExecutorScout = (ComboBox<String>) nodeFX.getNode("CommandExecutorScout_Box");
		commandExecutorScoutPathPlanning = (ComboBox<String>) nodeFX.getNode("CommandExecutorScoutPathPlanning_Box");
		commandExecutorScoutExtAction = (ComboBox<String>) nodeFX.getNode("CommandExecutorScoutExtAction_Box");

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
			//ModulePublisher.gitPush();
		});
		save_Button.setOnAction((ActionEvent e)->{
			output();
		});
	}

	/******************************************************************************************************************************************************
	 * 
	 * 選択項目ごとでGUIの設定を変更
	 * 
	 * @param module
	 */

	private void updateGUI(Module module) {
		if(module==Module.AT||module==Module.FB||module==Module.PF) {
			switch (module) {
			case AT:
			case FB:
				detectorClustering.setDisable(false);
				detectorPathPlanning.setDisable(true);
				commandExecutorScoutExtAction.setDisable(true);
				break;
			case PF:
				detectorClustering.setDisable(true);
				detectorPathPlanning.setDisable(false);
				commandExecutorScoutExtAction.setDisable(false);
				break;
			}
			agentPanel.setVisible(true);
			centerPanel.setVisible(false);
		}else {
			agentPanel.setVisible(false);
			centerPanel.setVisible(true);
		}
	}


	/******************************************************************************************************************************************************
	 * 
	 * モジュールコンフィグの Sample の読み込み
	 * 
	 * @param filePath
	 */

	@SuppressWarnings("incomplete-switch")
	private void setupConfig(String filePath) {
		HashMap<String, String> map=ModuleReader.read(filePath);

		for(Module module:Module.values()) {
			switch (module) {
			case AT:case FB:case PF:
				AgentConfig agent=agentConfigs.get(module);
				String tactics=null;
				String detector=null;
				String extAction=null;
				String agentType=null;

				switch (module) {
				case AT:
					tactics="TacticsAmbulanceTeam";
					detector="HumanDetector";
					extAction="ActionTransport";
					agentType="Ambulance";
					break;
				case FB:
					tactics="TacticsFireBrigade";
					detector="BuildingDetector";
					extAction="ActionFireFighting";
					agentType="Fire";
					break;
				case PF:
					tactics="TacticsPoliceForce";
					detector="RoadDetector";
					extAction="ActionExtClear";
					agentType="Police";
					break;
				}
				agent.detector.classFile=detectors.get(ClassFile.toClassName(map.get(tactics+"."+detector)));
				agent.detector.clustering=clusterings.get(ClassFile.toClassName(map.get(agent.detector.classFile.className+"."+"Clustering")));
				agent.detector.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.detector.classFile.className+"."+"PathPlanning")));

				agent.search.classFile=searchs.get(ClassFile.toClassName(map.get(tactics+".Search")));
				agent.search.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.search.classFile.className+"."+"PathPlanning.Ambulance")));
				agent.search.clustering=clusterings.get(ClassFile.toClassName(map.get(agent.search.classFile.className+"."+"Clustering."+agentType)));

				agent.extAction.classFile=extActions.get(ClassFile.toClassName(map.get(tactics+"."+extAction)));
				agent.extAction.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.extAction.classFile.className+"."+"PathPlanning")));

				agent.extActionMove.classFile=extActions.get(ClassFile.toClassName(map.get(tactics+".ActionExtMove")));
				agent.extActionMove.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.extActionMove.classFile.className+"."+"PathPlanning")));

				agent.commandExecutor.classFile=commandExecutors.get(ClassFile.toClassName(map.get(tactics+".CommandExecutor"+agentType)));
				agent.commandExecutor.extAction=extActions.get(ClassFile.toClassName(map.get(agent.commandExecutor.classFile.className+"."+extAction)));
				agent.commandExecutor.extActionMove=extActions.get(ClassFile.toClassName(map.get(agent.commandExecutor.classFile.className+"."+"ActionExtMove")));
				agent.commandExecutor.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.commandExecutor.classFile.className+"."+"PathPlanning")));

				agent.commandExecutorScout.classFile=commandExecutorsScout.get(ClassFile.toClassName(map.get(tactics+".CommandExecutorScout")));
				agent.commandExecutorScout.extAction=extActions.get(ClassFile.toClassName(map.get(agent.commandExecutorScout.classFile.className+"."+extAction)));
				agent.commandExecutorScout.pathPlanning=pathPlannings.get(ClassFile.toClassName(map.get(agent.commandExecutorScout.classFile.className+"."+"PathPlanning")));
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

	/******************************************************************************************************************************************************
	 * 
	 * 各クラスファイルをスーパークラスから分類
	 * 
	 * @param files
	 */

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

	/******************************************************************************************************************************************************
	 * 
	 * コンボボックス内のアイテムをエージェンごとで更新
	 * 
	 * @param module
	 */

	private void updateConfig(Module module) {

		for(ComboBox<String> box: comboBoxs) {
			box.getItems().clear();
		}		
		switch (module) {
		case AT:case FB:case PF:
			HashMap<String, ClassFile> detectors=null;
			HashMap<String, ClassFile> commandExecutors=null;
			switch (module) {
			case AT:
				detectors=humanDetectors;
				commandExecutors=commandExecutors_CommandAmbulance;
				break;
			case FB:
				detectors=buildingDetectors;
				commandExecutors=commandExecutors_CommandFire;
				break;
			case PF:
				detectors=roadDetectors;
				commandExecutors=commandExecutors_CommandPolice;
				break;
			}
			detector.getItems().addAll(detectors.keySet());
			detectorClustering.getItems().addAll(clusterings.keySet());
			detectorPathPlanning.getItems().addAll(pathPlannings.keySet());

			search.getItems().addAll(searchs.keySet());
			searchClustering.getItems().addAll(clusterings.keySet());
			searchPathPlanning.getItems().addAll(pathPlannings.keySet());

			extAction.getItems().addAll(extActions.keySet());
			extActionPathPlanning.getItems().addAll(pathPlannings.keySet());
			extActionMove.getItems().addAll(extActions.keySet());
			extActionMovePathPlanning.getItems().addAll(pathPlannings.keySet());

			commandExecutor.getItems().addAll(commandExecutors.keySet());
			commandExecutorPathPlanning.getItems().addAll(pathPlannings.keySet());
			commandExecutorExtAction.getItems().addAll(extActions.keySet());
			commandExecutorActionExtMove.getItems().addAll(extActions.keySet());

			commandExecutorScout.getItems().addAll(commandExecutorsScout.keySet());
			commandExecutorScoutPathPlanning.getItems().addAll(pathPlannings.keySet());
			commandExecutorScoutExtAction.getItems().addAll(extActions.keySet());
			break;
		case AC:case FS:case PO:
			HashMap<String, ClassFile> targetAllocators=null;
			switch (module) {
			case AC:
				targetAllocators=ambulanceTargetAllocators;
				break;
			case FS:
				targetAllocators=fireTargetAllocators;
				break;
			case PO:
				targetAllocators=policeTargetAllocators;
				break;
			}
			targetAllocator.getItems().addAll(targetAllocators.keySet());
			commandPicker.getItems().addAll(commandPickers.keySet());
			break;
		}
	}

	/******************************************************************************************************************************************************
	 * 
	 * 保存データをコンボボックスに反映
	 * 
	 * @param module
	 */

	private void changeConfig(Module module) {
		AgentConfig agentConfig = agentConfigs.get(module);
		CenterConfig centerConfig = centerConfigs.get(module);
		if (agentConfig != null) {
			set(detector, agentConfig.detector.classFile);
			set(detectorClustering, agentConfig.detector.clustering);
			set(detectorPathPlanning, agentConfig.detector.pathPlanning);

			set(search, agentConfig.search.classFile);
			set(searchClustering, agentConfig.search.clustering);
			set(searchPathPlanning, agentConfig.search.pathPlanning);

			set(extAction, agentConfig.extAction.classFile);
			set(extActionPathPlanning, agentConfig.extAction.pathPlanning);
			set(extActionMove, agentConfig.extActionMove.classFile);
			set(extActionMovePathPlanning, agentConfig.extActionMove.pathPlanning);

			set(commandExecutor, agentConfig.commandExecutor.classFile);
			set(commandExecutorPathPlanning, agentConfig.commandExecutor.pathPlanning);
			set(commandExecutorExtAction, agentConfig.commandExecutor.extAction);
			set(commandExecutorActionExtMove, agentConfig.commandExecutor.extActionMove);
			set(commandExecutorScout, agentConfig.commandExecutorScout.classFile);
			set(commandExecutorScoutPathPlanning, agentConfig.commandExecutorScout.pathPlanning);
			set(commandExecutorScoutExtAction, agentConfig.commandExecutorScout.extAction);
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

	/******************************************************************************************************************************************************
	 * 
	 * 設定した各モジュールを保存
	 * 
	 * @param module
	 */

	private void saveConfig(Module module) {
		switch (module) {
		case AT:case FB:case PF:
			AgentConfig agentConfig = agentConfigs.get(module);
			agentConfig.detector.set(detectors.get(getSelectItem(detector)), pathPlannings.get(getSelectItem(detectorPathPlanning)), clusterings.get(getSelectItem(detectorClustering)));
			agentConfig.search.set(searchs.get(getSelectItem(search)), pathPlannings.get(getSelectItem(searchPathPlanning)), clusterings.get(getSelectItem(searchClustering)));
			agentConfig.extAction.set(extActions.get(getSelectItem(extAction)), pathPlannings.get(getSelectItem(extActionPathPlanning)));
			agentConfig.extActionMove.set(extActions.get(getSelectItem(extActionMove)), pathPlannings.get(getSelectItem(extActionMovePathPlanning)));
			agentConfig.commandExecutor.set(commandExecutors.get(getSelectItem(commandExecutor)), pathPlannings.get(getSelectItem(commandExecutorPathPlanning)), extActions.get(getSelectItem(commandExecutorExtAction)), extActions.get(getSelectItem(commandExecutorActionExtMove)));
			agentConfig.commandExecutorScout.set(commandExecutorsScout.get(getSelectItem(commandExecutorScout)), pathPlannings.get(getSelectItem(commandExecutorScoutPathPlanning)), extActions.get(getSelectItem(commandExecutorScoutExtAction)));
			break;
		case AC:case FS:case PO:
			CenterConfig centerConfig = centerConfigs.get(module);
			centerConfig.set(
					targetAllocators.get(targetAllocator.getSelectionModel().getSelectedItem()),
					commandPickers.get(commandPicker.getSelectionModel().getSelectedItem()));
			break;
		}
	}

	private String getSelectItem(ComboBox<String> comboBox) {
		return comboBox.getSelectionModel().getSelectedItem();
	}

	/******************************************************************************************************************************************************
	 * 
	 * 保存データを外部に保存する
	 * 
	 */

	public void output() {
		HashMap<String, String> map =new HashMap<>();
		HashSet<ClassFile> set=new HashSet<>();
		for(Module module: Module.values()) {
			String tactics=null;
			String detector=null;
			String extAction=null;
			String agentType=null;
			switch (module) {
			case AT:case FB:case PF:
				AgentConfig agent=agentConfigs.get(module);
				switch (module) {
				case AT:
					tactics="TacticsAmbulanceTeam";
					detector="HumanDetector";
					extAction="ActionTransport";
					agentType="Ambulance";
					break;
				case FB:
					tactics="TacticsFireBrigade";
					detector="BuildingDetector";
					extAction="ActionFireFighting";
					agentType="Fire";
					break;
				case PF:
					tactics="TacticsPoliceForce";
					detector="RoadDetector";
					extAction="ActionExtClear";
					agentType="Police";
					break;
				}
				map.put(tactics+"."+detector, agent.detector.classFile.toOutputFormat());
				if(agent.detector.clustering!=null) map.put(agent.detector.classFile.className+"."+"Clustering", agent.detector.clustering.toOutputFormat());
				if(agent.detector.pathPlanning!=null) map.put(agent.detector.classFile.className+"."+"PathPlanning", agent.detector.pathPlanning.toOutputFormat());

				map.put(tactics+"."+"Search", agent.search.classFile.toOutputFormat());
				map.put(agent.search.classFile.className+"."+"PathPlanning"+"."+agentType, agent.search.pathPlanning.toOutputFormat());
				map.put(agent.search.classFile.className+"."+"Clustering"+"."+agentType, agent.search.clustering.toOutputFormat());

				map.put(tactics+"."+extAction, agent.extAction.classFile.toOutputFormat());
				map.put(agent.extAction.classFile.className+"."+"PathPlanning", agent.extAction.pathPlanning.toOutputFormat());
				map.put(tactics+"."+"ActionExtMove", agent.extActionMove.classFile.toOutputFormat());
				map.put(agent.extActionMove.classFile.className+"."+"PathPlanning", agent.extActionMove.pathPlanning.toOutputFormat());

				map.put(tactics+"."+"CommandExecutor"+agentType, agent.commandExecutor.classFile.toOutputFormat());
				map.put(agent.commandExecutor.classFile.className+"."+"PathPlanning", agent.commandExecutor.pathPlanning.toOutputFormat());
				map.put(agent.commandExecutor.classFile.className+"."+extAction, agent.commandExecutor.extAction.toOutputFormat());
				map.put(agent.commandExecutor.classFile.className+"."+"ActionExtMove", agent.commandExecutor.extActionMove.toOutputFormat());

				map.put(tactics+"."+"CommandExecutorScout", agent.commandExecutorScout.classFile.toOutputFormat());
				map.put(agent.commandExecutorScout.classFile.className+"."+"PathPlanning", agent.commandExecutorScout.pathPlanning.toOutputFormat());
				if(agent.commandExecutorScout.extAction!=null) map.put(agent.commandExecutorScout.classFile.className+"."+extAction, agent.commandExecutorScout.extAction.toOutputFormat());
				set.addAll(agent.getClassFiles());
				break;

			case AC:case FS:case PO:
				CenterConfig center=centerConfigs.get(module);
				String targetAllocator=null;
				String commandPicker=null;
				switch (module) {
				case AC:
					targetAllocator="TacticsAmbulanceCentre.TargetAllocator";
					commandPicker="TacticsAmbulanceCentre.CommandPicker";
					break;
				case FS:
					targetAllocator="TacticsFireStation.TargetAllocator";
					commandPicker="TacticsFireStation.CommandPicker";
					break;
				case PO:
					targetAllocator="TacticsPoliceOffice.TargetAllocator";
					commandPicker="TacticsPoliceOffice.CommandPicker";
					break;
				}
				map.put(targetAllocator, center.targetAllocator.toOutputFormat());
				map.put(commandPicker, center.commandPicker.toOutputFormat());
				set.addAll(center.getClassFiles());
				break;
			}
		}
		map.put("Team.Name", "rescue-online");
		
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File(sourcePath+"/config/"+"module.cfg")));
			for(String key:map.keySet()) {
				writer.write(key+" : "+map.get(key));
				writer.newLine();
			}
			writer.flush();
			writer.close();
			System.out.println("\nコンフィグを保存しました\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/******************************************************************************************************************************************************/
	//
	//以下モデュール関係のクラス
	//
	/******************************************************************************************************************************************************/

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


		public static class CommandExecutorScout{
			public ClassFile classFile;
			public ClassFile pathPlanning;
			public ClassFile extAction;
			public CommandExecutorScout() {
			}
			public void set(ClassFile classFile, ClassFile pathPlanning, ClassFile extAction) {
				this.classFile=classFile;
				this.pathPlanning=pathPlanning;
				this.extAction=extAction;
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

		public HashSet<ClassFile> getClassFiles(){
			HashSet<ClassFile> set=new HashSet<>();
			set.add(detector.classFile);
			if(detector.clustering!=null) set.add(detector.clustering);
			if(detector.pathPlanning!=null) set.add(detector.pathPlanning);

			set.add(search.classFile);
			set.add(search.clustering);
			set.add(search.pathPlanning);

			set.add(extAction.classFile);
			set.add(extAction.pathPlanning);
			set.add(extActionMove.classFile);
			set.add(extActionMove.pathPlanning);

			set.add(commandExecutor.classFile);
			set.add(commandExecutor.pathPlanning);
			set.add(commandExecutor.extAction);
			set.add(commandExecutor.extActionMove);

			set.add(commandExecutorScout.classFile);
			set.add(commandExecutorScout.pathPlanning);
			if(commandExecutorScout.extAction!=null) set.add(commandExecutorScout.extAction);
			return set;
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

		public HashSet<ClassFile> getClassFiles(){
			HashSet<ClassFile> set=new HashSet<>();
			set.add(targetAllocator);
			set.add(commandPicker);
			return set;
		}
	}

}