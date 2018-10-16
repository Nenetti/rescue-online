package module;

import java.util.HashMap;
import java.util.List;


public class ModuleConfig {

	HashMap<String, ClassFile> roadDetector=new HashMap<>();
	HashMap<String, ClassFile> buildingDetector=new HashMap<>();
	HashMap<String, ClassFile> humanDetector=new HashMap<>();
	HashMap<String, ClassFile> search=new HashMap<>();
	HashMap<String, ClassFile> extAction=new HashMap<>();
	HashMap<String, ClassFile> extActionMove=new HashMap<>();
	HashMap<String, ClassFile> commandExecutor=new HashMap<>();
	HashMap<String, ClassFile> commandExecutorScout=new HashMap<>();
	HashMap<String, ClassFile> clustering=new HashMap<>();
	HashMap<String, ClassFile> pathPlanning=new HashMap<>();

	public ModuleConfig(List<ClassFile> files) {
		for(ClassFile classFile: files) {
			switch (classFile.superClass) {
			//Detector系
			case "HumanDetector":

				break;
			case "BuildingDetector":

				break;
			case "RoadDetector":

				break;
			//パスプラ系
			case "PathPlanning":

				break;
			//クラスタリング系
			case "DynamicClustering":

				break;
			case "StaticClustering":

				break;
				//アロケーター系
			case "FireTargetAllocator":

				break;
			case "AmbulanceTargetAllocator":

				break;
			case "PoliceTargetAllocator":

				break;
				//サーチ系
			case "Search":

				break;
				//タクティクス系
			case "TacticsFireStation":

				break;
			case "TacticsPoliceOffice":

				break;
			case "TacticsFireBrigade":

				break;
			case "TacticsAmbulanceCentre":

				break;
			case "TacticsAmbulanceTeam":

				break;
			case "TacticsPoliceForce":

				break;
				//アクション系
			case "ExtAction":

				break;
				//コマンド系
			case "CommandPicker":

				break;
			default:
				//未登録リスト
				System.out.println(classFile.superClass);
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