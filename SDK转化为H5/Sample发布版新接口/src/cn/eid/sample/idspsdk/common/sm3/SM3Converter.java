package cn.eid.sample.idspsdk.common.sm3;

public class SM3Converter {

	public static String bytesToHexString(byte[] bytes) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xff);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();

	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.length()==0) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
}
