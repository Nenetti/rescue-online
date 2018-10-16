package module;

import java.util.HashMap;
import java.util.List;


public class ModuleConfig {

	public HashMap<String, ClassFile> humanDetectors=new HashMap<>();
	public HashMap<String, ClassFile> buildingDetectors=new HashMap<>();
	public HashMap<String, ClassFile> roadDetectors=new HashMap<>();
	
	public HashMap<String, ClassFile> extActions=new HashMap<>();
	
	public HashMap<String, ClassFile> searchs=new HashMap<>();
	public HashMap<String, ClassFile> commandPickers=new HashMap<>();
	
	public HashMap<String, ClassFile> dynamicClusterings=new HashMap<>();
	public HashMap<String, ClassFile> staticClusterings=new HashMap<>();
	
	public HashMap<String, ClassFile> pathPlannings=new HashMap<>();
	
	public HashMap<String, ClassFile> fireTargetAllocators=new HashMap<>();
	public HashMap<String, ClassFile> ambulanceTargetAllocators=new HashMap<>();
	public HashMap<String, ClassFile> policeTargetAllocators=new HashMap<>();
	
	public HashMap<String, ClassFile> tacticsFireStations=new HashMap<>();
	public HashMap<String, ClassFile> tacticsPoliceOffices=new HashMap<>();
	public HashMap<String, ClassFile> tacticsFireBrigades=new HashMap<>();
	public HashMap<String, ClassFile> tacticsAmbulanceCentres=new HashMap<>();
	public HashMap<String, ClassFile> tacticsAmbulanceTeams=new HashMap<>();
	public HashMap<String, ClassFile> tacticsPoliceForces=new HashMap<>();


	public ModuleConfig(List<ClassFile> files) {
		for(ClassFile classFile: files) {
			System.out.println(classFile);
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
	}




	public class AgentConfig {

		public ClassFile detector;
		public ClassFile search;
		public ClassFile extAction;
		public ClassFile extActionMove;
		public ClassFile commandExecutor;
		public ClassFile commandExecutorScout;
		public ClassFile clustering;
		public ClassFile pathPlanning;

		public AgentConfig() {

		}

	}

}