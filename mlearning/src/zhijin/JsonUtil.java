package zhijin;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	// 根据键，值得到一个json对象的字符串
	public static String string4json(String key, String value) {
		JSONObject object = new JSONObject();
		object.put(key, value);
		return object.toString();
	}

	// 根据指定的class和json得到一个实体对象
	public static Object getObjectJsonString(String jsonString, Class pojoCalss) {
		Object pojo;
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		pojo = JSONObject.toBean(jsonObject, pojoCalss);
		return pojo;
	}

	// 根据指定的class和json对象得到一个实体对象
	public static Object getObjectJsonString(JSONObject jsonObject,
			Class pojoCalss) {
		Object pojo;
		pojo = JSONObject.toBean(jsonObject, pojoCalss);
		return pojo;
	}

	// 获得某一对象List的Json
	public static String getJsonList(List li) {
		return JSONArray.fromObject(li).toString();
	}

	// 获得某一对象的Json
	public static String getJsonObject(Object obj) {
		return JSONObject.fromObject(obj).toString();
	}

	// 获得某一对象的List
	public static List getList4Json(String jsonString, Class pojoClass) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;
		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			pojoValue = JSONObject.toBean(jsonObject, pojoClass);
			list.add(pojoValue);

		}
		return list;

	}

	// 获得多个对象的数组
	public static Object[] getStringArray4Json(String jsonString) {
		JSONObject jsonObj = JSONObject.fromObject(jsonString);
		JSONArray jsonarr = jsonObj.getJSONArray("item");
		return (Object[]) jsonarr.toArray();
	}

}
