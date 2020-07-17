package org.egg.utils;
 
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
 
/**
 * 签名认证工具类
 * @author Weijun Hu
 * @since 2016年7月9日 上午10:19:36
 */
public class SignUtil {
 
	/**
	 * 加密/校验流程如下：
		<li>1. 将token、timestamp、nonce三个参数进行字典序排序</li>
		<li>2. 将三个参数字符串拼接成一个字符串进行sha1加密</li>
		<li>3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信</li>
	 * @param token
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 * @date 2016年7月9日 上午10:19:36
	 * @url http://mp.weixin.qq.com/wiki/17/2d4265491f12608cd170a95559800f2d.html
	 */
	public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
		String[] arr = new String[] { token, timestamp, nonce };
		// 将token,timestamp,nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		content = null;
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}
 
	/**
	 * 将字节数组转换为十六进制字符串
	 * @param byteArray
	 * @return
	 * @date 2016年7月9日 上午10:20:53
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}
 
	/**
	 * 将字节转换为十六进制字符串
	 * @param mByte
	 * @return
	 * @date 2016年7月9日 上午10:20:59
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];
		String s = new String(tempArr);
		return s;
	}
}