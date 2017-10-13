package com.zhijin.mlearning.app.dao.http;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zhijin.mlearning.app.CoreApp;
import com.zhijin.mlearning.support.annotation.Lazy;
import com.zhijin.mlearning.support.network.Param;
import com.zhijin.mlearning.support.network.inter.IHttpDao;

public abstract class HttpBaseDao implements IHttpDao<Param, String>
{

	protected Map<String, Map<String, Integer>> nameMap = new HashMap<String, Map<String, Integer>>();
	protected Param mParam;

	@Override
	public boolean process(String e)
	{
		return false;
	}

	@Override
	public void success(Param o)
	{
		CoreApp.getApp().notifyOperationSuccess(o);
	}

	@Override
	public void fails(Param o)
	{
		CoreApp.getApp().notifyOperationFails(o);
	}

	@Override
	public Param getResult()
	{
		return this.mParam;
	}

	/**
	 * 组建url参数
	 * 
	 * @param url
	 * @param paramsMap
	 * @return
	 */
	protected String buildUrlParams(String url, Map paramsMap)
	{
		try {
			String params = "";
			if (url.indexOf("?") != -1) {
				params = "&";
			} else {
				params = "?";
			}
			Set<Map.Entry<String, Object>> entrys = paramsMap.entrySet();
			Map.Entry<String, Object> entry = null;
			String key = "";
			Object value = "";
			Iterator<Map.Entry<String, Object>> it = entrys.iterator();
			while (it.hasNext()) {
				entry = (Map.Entry<String, Object>) it.next();
				key = entry.getKey();
				// value = entry.getValue();
				value = URLEncoder.encode((String) entry.getValue(), "utf-8");
				params += key + "=" + value + "&";
			}
			return url + params;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * 获得传入的对象需要延迟加载的属性名
	 * 
	 * @param selectObject
	 *            查询的对象
	 * @return 属性的名称集合
	 */
	protected List<String> getFkObjName(Object selectObject)
	{
		if (selectObject != null) {
			Map<String, Integer> fieldNameMap = new HashMap<String, Integer>();
			List<String> fkObjNames = new ArrayList<String>();
			Class objClass = selectObject.getClass();
			// 获得类里声明的字段。
			Field[] fa = objClass.getDeclaredFields();
			int count = fa.length;
			for (int j = 0; j < count; j++) {
				// 属性名
				String fieldName = fa[j].getName();
				boolean isExist = fa[j].isAnnotationPresent(Lazy.class);
				if (isExist) {
					Lazy valida = fa[j].getAnnotation(Lazy.class);
					if (valida.isLazy()) {
						fkObjNames.add(fieldName);
						if (nameMap != null && nameMap.containsKey(objClass.getName())) {
							if (nameMap.get(objClass.getName()) != null
									&& nameMap.get(objClass.getName()).containsKey(fieldName)) {
								int number = nameMap.get(objClass.getName()).get(fieldName);
								if (number < 2) {
									number++;
									nameMap.get(objClass.getName()).put(fieldName, number);
								} else {
									return fkObjNames;
								}
							} else {
								fieldNameMap.put(fieldName, 1);
								nameMap.put(objClass.getName(), fieldNameMap);
							}
						} else {
							fieldNameMap.put(fieldName, 1);
							nameMap.put(objClass.getName(), fieldNameMap);
						}
						Object childObj;
						try {
							childObj = Class.forName(fa[j].getType().getName()).newInstance();
							List<String> list = getFkObjName(childObj);
							if (list != null) {
								for (int i = 0; i < list.size(); i++) {
									String fkFieldName = list.get(i);
									if (fkFieldName.contains(".")) {
									} else {
										fkObjNames.add(fieldName + "." + fkFieldName);
									}
								}
							}
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return fkObjNames;
		} else {
			return null;
		}
	}

}
