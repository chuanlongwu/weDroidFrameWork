package com.wedroid.framework.common;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import android.util.Base64;

public class Encode {
	
	/**
	 * 字节数据转字符串专用集合
	 */
	@SuppressWarnings("unused")
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
		try {
//			BASE64Decoder base64Decoder = new BASE64Decoder();
//			byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
			byte[] buffer = Base64.decode(publicKeyStr, Base64.DEFAULT);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeySpecException e) {
		} catch (NullPointerException e) {
		}
		return null;
	}

	public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception
			 {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 字节数据转十六进制字符串
	 * 
	 * @param data
	 *            输入数据
	 * @return 十六进制内容
	 */
	public static String byteArrayToString(byte[] data) {
//		StringBuilder stringBuilder = new StringBuilder();
//		for (int i = 0; i < data.length; i++) {
//			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
//			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
//			// 取出字节的低四位 作为索引得到相应的十六进制标识符
//			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
//			if (i < data.length - 1) {
//				stringBuilder.append(' ');
//			}
//		}
//		return stringBuilder.toString();
		
		return Base64.encodeToString(data, Base64.DEFAULT);
	}

	public static void main(String[] args) {
		String encryptStr = "Test String";
		String publicKey = "SDFSDsfsdfsdfsdfsdfwe";
		try {
			// 加密
			byte[] cipher = encrypt(loadPublicKey(publicKey), encryptStr.getBytes());
			System.out.println("密文长度:" + cipher.length);
			System.out.println(byteArrayToString(cipher));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
