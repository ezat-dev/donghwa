package com.ace.util;

import java.util.HashMap;
import java.util.Map;

public class RecipeMap {
	
	Map<String, String> rMap = new HashMap<String, String>();
	
	public RecipeMap() {
		rMapSet();
	}
	
	public String rMapRtn(String key) {
		String value = "";
		value = rMap.get(key);		
		
		return value;
	}
	
	public void rMapSet() {
		rMap.put("recipe.getProcess_step0()","DONGHWA.PLC.RECIPE.SEGMENT_0.PROCESS_STEP_0");
		rMap.put("recipe.getTime_0()","DONGHWA.PLC.RECIPE.SEGMENT_0.TIME_0");
		rMap.put("recipe.getTemperature_0()","DONGHWA.PLC.RECIPE.TEMPERATURE_0");
	}
	
}
