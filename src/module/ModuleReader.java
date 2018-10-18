package module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;

public class ModuleReader {



	public static HashMap<String, String> read(String filePath){
		try {
			File file=new File(filePath);
			HashMap<String, String> map=new HashMap<>();
			BufferedReader reader=new BufferedReader(new FileReader(file));
			String line;
			while((line=reader.readLine())!=null) {
				line=line.trim();
				if(!line.equals("")) {
					String[] split=line.split(":");
					String key=split[0].trim();
					String value=split[1].trim();
					map.put(key, value);
				}
			}
			reader.close();
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}