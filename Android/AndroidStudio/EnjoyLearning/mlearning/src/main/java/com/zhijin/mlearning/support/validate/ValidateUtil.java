package com.zhijin.mlearning.support.validate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import android.util.Log;

import com.zhijin.mlearning.support.annotation.Validates;
import com.zhijin.mlearning.support.sqlite.ObjectOptionUtil;

public class ValidateUtil
{
	private static String validateInfo = "";

	public static boolean validateObj(Object obj)
	{
		boolean flag = true;
		ObjectOptionUtil objOptionUtil = new ObjectOptionUtil();
		if (obj != null) {
			Class cls = obj.getClass();
			// 获得全类名(包名+类名)
			String allName = cls.getName();
			// 获得类名
			String className = allName.substring(allName.lastIndexOf('.') + 1, allName.length());
			// 获得类里声明的字段。
			Field[] fa = cls.getDeclaredFields();
			int count = fa.length;
			String fields[] = new String[count];
			Map<String, String> map = new HashMap<String, String>();
			for (int j = 0; j < count; j++) {
				// 属性名
				String fieldName = fa[j].getName();
				String validateType = null;
				Object value = objOptionUtil.getFieldVlaue(obj, fieldName);
				boolean isExist = fa[j].isAnnotationPresent(Validates.class);
				if (isExist) {
					Validates valida = fa[j].getAnnotation(Validates.class);
					validateInfo = valida.info();
					validateType = valida.type();
				}
				if (validateType != null) {
					// 然后通过验证组件验证信息并赋值
					if (validateValue(validateInfo, validateType, value) == false) {
						flag = false;
						map.put(fieldName, validateInfo);
					} else {
						if (flag) {
							flag = true;
						}
					}
				}
			}

			if (flag == false) {
				// 在赋完值之后，判断是否有错误信息，并显示
				try {
					Method method = cls.getDeclaredMethod("setErrors", Map.class);
					method.invoke(obj, map);
				} catch (Exception e) {
					Log.e("info", "error" + e.getMessage());
				}
			}
		}
		return flag;
	}

	// 验证组件的内容，参数为：验证信息，即验证后给用户提示的消息;验证类型,不能是空？必须是数字？
	public static boolean validateValue(String validateinfo, String validateType, Object value)
	{
		String errorInfo = "";
		boolean flag = false;
		// 判断验证类型
		if (!validateType.equals("")) {
			String[] infos = validateinfo.split(",");// 利用“,”将需要验证的各种信息分隔
			String[] type = validateType.split(",");// 利用“,”将需要验证的各种类型分隔
			for (int i = 0; i < type.length; i++) {
				String info = infos[i];// 得到对应的验证信息
				if (type[i].equals("isNull")) {
					if (!value.toString().equals("")) {
						flag = true;
					} else {
						flag = false;
						errorInfo += info + ",";
					}
				} else if (type[i].equals("isNumber")) {
					if (checkValue(value.toString())) {
						flag = true;
					} else {
						flag = false;
						errorInfo += info + ",";
					}
				} else if (type[i].length() >= 7 && type[i].subSequence(0, 7).equals("maxSize")) {
					int maxValue = Integer.valueOf(type[i].substring(7));
					if (value.toString().length() <= maxValue) {
						flag = true;
					} else {
						flag = false;
						errorInfo += info + ",";
					}
				} else if (type[i].length() >= 7 && type[i].subSequence(0, 7).equals("minSize")) {
					int minValue = Integer.valueOf(type[i].substring(7));
					if (value.toString().length() >= minValue) {
						flag = true;
					} else {
						flag = false;
						errorInfo += info + ",";
					}
				} else if (type[i].length() >= 3 && type[i].subSequence(0, 3).equals("max")) {
					if (checkValue(value.toString())) {
						Object maxValue = convertValue(type[i].substring(3));
						Object valueNumber = convertValue(value.toString());

						if (maxValue instanceof Integer && valueNumber instanceof Integer) {
							if (!((Integer) valueNumber > (Integer) maxValue)) {
								flag = true;
							} else {
								flag = false;
								errorInfo += info + ",";
							}
						} else if (maxValue instanceof Float && valueNumber instanceof Float) {
							if (!((Float) valueNumber > (Float) maxValue)) {
								flag = true;
							} else {
								flag = false;
								errorInfo += info + ",";
							}
						} else if (maxValue instanceof Double && valueNumber instanceof Double) {
							if (!((Double) valueNumber > (Double) maxValue)) {
								flag = true;
							} else {
								flag = false;
								errorInfo += info + ",";
							}
						} else {
							flag = false;
							errorInfo += info + ",";
						}

					} else {
						flag = false;
						errorInfo += info + ",";
					}
				} else if (type[i].length() >= 3 && type[i].subSequence(0, 3).equals("min")) {
					if (checkValue(value.toString())) {
						Object minValue = convertValue(type[i].substring(3));
						Object valueNumber = convertValue(value.toString());
						if (minValue instanceof Integer && valueNumber instanceof Integer) {
							if (!((Integer) valueNumber < (Integer) minValue)) {
								flag = true;
							} else {
								flag = false;
								errorInfo += info + ",";
							}
						} else if (minValue instanceof Float && valueNumber instanceof Float) {
							if (!((Float) valueNumber < (Float) minValue)) {
								flag = true;
							} else {
								flag = false;
								errorInfo += info + ",";
							}
						} else if (minValue instanceof Double && valueNumber instanceof Double) {
							if (!((Double) valueNumber < (Double) minValue)) {
								flag = true;
							} else {
								flag = false;
								errorInfo += info + ",";
							}
						} else {
							flag = false;
							errorInfo += info + ",";
						}
					} else {
						flag = false;
						errorInfo += info + ",";
					}
				} else if (type[i].equals("isPhoneNumber")) {
					Pattern p = Pattern.compile("^((13[0-9])|(14[5,[57]])|(15[^4,\\D])|(18[0,0-9]))\\d{8}$");
					flag = p.matcher(value.toString().trim()).matches();
					if (flag == false) {
						errorInfo += info + ",";
					}
				} else if (type[i].equals("isEmail")) {
					Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
					flag = emailer.matcher(value.toString().trim()).matches();
					if (flag == false) {
						errorInfo += info + ",";
					}
				}
			}
			if (infos.length > 1 && errorInfo != null && !errorInfo.equals("")) {
				errorInfo = errorInfo.subSequence(0, errorInfo.length() - 1).toString();
			}
		}
		// 将错误信息赋给验证信息，以便其他验证组件调用
		validateInfo = errorInfo;
		return flag;
	}

	// 检测值是否可以强转为int，double，float
	private static boolean checkValue(String value)
	{
		boolean flag = true;
		try {
			System.out.println(Integer.valueOf(value).getClass());
		} catch (Exception e) {
			try {
				System.out.println(Float.valueOf(value).getClass());
			} catch (Exception ee) {
				try {
					System.out.println(Double.valueOf(value).getClass());
				} catch (Exception eee) {
					flag = false;
					Log.e("info", "error:" + eee.toString());
				}
				Log.e("info", "error:" + ee.toString());
			}
			Log.e("info", "error:" + e.toString());
		}
		return flag;
	}

	// 主要针对int，double，float型的数据
	private static Object convertValue(String value)
	{

		Object valueNumber;
		try {
			valueNumber = Integer.valueOf(value);
		} catch (Exception e) {
			try {
				valueNumber = Float.valueOf(value);
			} catch (Exception ee) {
				try {
					valueNumber = Double.valueOf(value);
				} catch (Exception eee) {
					valueNumber = 0;

					Log.e("info", "error:" + eee.toString());
				}
				Log.e("info", "error:" + ee.toString());
			}
			Log.e("info", "error:" + e.toString());
		}

		return valueNumber;
	}

}
