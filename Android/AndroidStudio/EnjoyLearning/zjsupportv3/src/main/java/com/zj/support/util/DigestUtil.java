/**
 * Copyright 2011-2013 The Kuali Foundation Licensed under the Educational
 * Community License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.zj.support.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具
 * 
 * @author XingRongJing
 */
public class DigestUtil {

	/**
	 * 32位MD5加密
	 * 
	 * @param val
	 * @return
	 */
	public static String getMD5(String val) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(val.getBytes());
			byte[] m = md5.digest();// 加密
			return getString(m);
		} catch (NoSuchAlgorithmException e) {
			return "error";
		}
	}

	private static String getString(byte[] b) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int a = b[i];
			if (a < 0)
				a += 256;
			if (a < 16)
				buf.append("0");
			buf.append(Integer.toHexString(a));

		}
		return buf.toString(); // 32位
	}
}
