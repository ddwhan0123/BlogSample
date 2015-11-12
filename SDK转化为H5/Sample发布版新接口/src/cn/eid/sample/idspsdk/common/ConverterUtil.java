package cn.eid.sample.idspsdk.common;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;

import android.util.Base64;




public class ConverterUtil {

	// Hex help
		private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1',
				(byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
				(byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
				(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };

		// //////////////////////////////////////////////////////////////////////////
		// --------------------------------- REVISIONS
		// ------------------------------
		// Date Name Tracking # Description
		// --------- ------------------- ------------- ----------------------
		// 13SEP2011 James Shen Initial Creation
		// //////////////////////////////////////////////////////////////////////////
		/**
		 * convert a byte arrary to hex string
		 * 
		 * @param raw
		 *            byte arrary
		 * @param len
		 *            lenght of the arrary.
		 * @return hex string.
		 */
		public static String getHexString(byte[] raw, int len) {
			byte[] hex = new byte[2 * len];
			int index = 0;
			int pos = 0;

			for (byte b : raw) {
				if (pos >= len)
					break;

				pos++;
				int v = b & 0xFF;
				hex[index++] = HEX_CHAR_TABLE[v >>> 4];
				hex[index++] = HEX_CHAR_TABLE[v & 0xF];
			}

			return new String(hex);
		}

		public static byte[] getBytes(String str) {
			String digital = "0123456789ABCDEF";
			str = str.toUpperCase();
			char[] hex2char = str.toCharArray();
			byte[] bytes = new byte[str.length() / 2];
			int temp;
			for (int i = 0; i < bytes.length; i++) {
				// 其实和上面的函数是一样的 multiple 16 就是右移4位 这样就成了高4位了
				// 然后和低四位相加， 相当于 位操作"|"
				// 相加后的数字 进行 位 "&" 操作 防止负数的自动扩展. {0xff byte最大表示数}
				temp = digital.indexOf(hex2char[2 * i]) * 16;
				temp += digital.indexOf(hex2char[2 * i + 1]);
				bytes[i] = (byte) (temp & 0xff);
			}
			return bytes;

		}
		
		public static byte[] SHA1(byte[] input) {

	        try {

	            MessageDigest digest = MessageDigest.getInstance("SHA-1");
	            digest.update(input);

	            return digest.digest();

	        } catch (NoSuchAlgorithmException e) {

//	        	Log.d( TAG, e.toString());

	        }

	        return null;
	    }
		
		public static String getBASE64(byte[] data) {

			if (data == null) {

				return null;

			}

			return Base64.encodeToString(data, Base64.NO_WRAP);
		}
		
		public static int bytesToInt(byte[] intByte) {
			int fromByte = 0;
			for (int i = 1; i >= 0; i--)
			{
				int n = (intByte[i] < 0 ? (int)intByte[i] + 256 : (int)intByte[i]) << (8 * (1-i));
//				System.out.println(n);
				fromByte += n;
			}
				return fromByte;
		}
		
		public static short bytesToShort(byte[] b) {
			return (short) (b[1] & 0xff
			| (b[0] & 0xff) << 8);
			}
		
		public static ArrayList<Byte> byteToArrayList(byte[] src){
			  if (src == null)
				return null;
			ArrayList <Byte> list =new ArrayList<Byte>();
			for (int i=0; i<src.length; i++){
//				Byte b=new Byte(src[i]);
//				list.add(b);
				list.add(src[i]);
			}
		    return list;	
		}
		
		public static byte[] ArrayListToByte(ArrayList<Byte> list){
			int iSize=list.size();
			byte[] arrData=new byte[iSize];
			for (int i=0; i<iSize;i++)
			{
			  Byte b=(Byte)list.get(i);
			  arrData[i]=b.byteValue();
			}
			return arrData;
		}
		
		
		public static ArrayList<Byte> swap(byte[] src,int count) {
			ArrayList <Byte> list=new ArrayList<Byte>();
			
			byte [] arrData=new byte[count];
			for (int i=0; i<count; i++)
			   arrData[count-1-i]=src[i];
			
			for (int i=0; i<count; i++){
				Byte b=new Byte(arrData[i]);
				list.add(b);
			}		
			return list;
		}
		
		
		
		public static ArrayList<Byte> swap( int intValue,int ulLcLen ) {
			ArrayList<Byte> b = new ArrayList<Byte>();
		        for (int i = 0; i < ulLcLen; i++) {
		           b.add( (byte) (intValue >> 8 * (ulLcLen-i-1) & 0xFF));
		        }
		        return b;
		}
		
		public static String arrayListToString(ArrayList<Byte> list){
			byte[] bytes = ArrayListToByte(list);
			
			return getHexString(bytes, bytes.length);
		}
		public static boolean CompareBytes(byte[] date1,byte[] data2,int len){
			int index=date1.length<data2.length?date1.length:data2.length;
			index =len<index?len:index;
	        for (int i=0;i<index;i++)
	            if (date1[i]!=data2[i]) return false;
	        return true;
		}
		
		//公钥为大头的n和e
		public static byte[]EncryptwithPublicKey(byte[] data, PubKey pubkey){
			
			byte[] enBytes;
			try{
				PublicKey newPubKey = getPublicKey(String.valueOf(pubkey.getN()), String.valueOf(pubkey.getE()));
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");///ECB/PKCS1Padding
		
				// 加密 得到的值为公钥大头时的加密值
				cipher.init(Cipher.ENCRYPT_MODE, newPubKey);
				enBytes = cipher.doFinal(data);
				
			}catch(Exception e)
			{
				System.out.println(e.toString());
				return null;
			}
			return enBytes;
		}
		
		public static PublicKey getPublicKey(String modulus, String publicExponent)
				throws Exception {

			BigInteger m = new BigInteger(modulus, 16);
			BigInteger e = new BigInteger(publicExponent, 16);

			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(keySpec);

			return publicKey;

		}
		
		
		public static int getByteCount(byte[][] byteArray){
			
			if(null == byteArray)
				return 0;
			int count =0;
			for (int i = 0; i < byteArray.length; i++) {
				for (int j = 0; j < byteArray[i].length; j++) {
					if(byteArray[i][j] != (byte)0x00){
						count ++;
						break;
					}
				}
			}
			
			return count;
		}
		
//	    public static ArrayList<Byte> fileIndexToArrayList(TEID_FILE_MAP_INDEX fileindex)
//	    {
//	    	ArrayList<Byte> b = new ArrayList<Byte>();
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushRsa1024CertFlieIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushRsa1024PrivateKeyIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushRsa1024PublicKeyIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushRsa2048CertFlieIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushRsa2048PrivateKeyIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushRsa2048PublicKeyIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushSM2CertFlieIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushSM2PrivateKeyIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushSM2PublicKeyIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushRsa1280CertFlieIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushRsa1280PrivateKeyIndex)));
//	    	b.addAll(byteToArrayList(shortToByte(fileindex.ushRsa1280PublicKeyIndex)));
//	    	return b;
//		}
//	    public static ArrayList<Byte> infosToArrayList(TEID_STORE_MAP_RECORD[] infos)
//	    {  	
//	    	ArrayList<Byte> b = new ArrayList<Byte>();
//	    	for(int i=0;i<infos.length;i++)
//	    	{
//		    	b.addAll(byteToArrayList(infos[i].szGuid));
//		    	switch (infos[i].uchKeyExAlg)
//		    	{
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_RSA1024:
//		    			b.add((byte)0x01);
//		    			break;
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_RSA2048:
//		    			b.add((byte)0x02);
//		    			break;
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_SM2_1:
//		    			b.add((byte)0x04);
//		    			break;
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_SM2_2:
//		    			b.add((byte)0x08);
//		    			break;
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_SM2_3:
//		    			b.add((byte)0x10);
//		    			break;
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_RSA1280:
//		    			b.add((byte)0x20);
//		    			break;    			
//		    	}
//		    	switch (infos[i].uchSignAlg)
//		    	{
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_RSA1024:
//		    			b.add((byte)0x01);
//		    			break;
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_RSA2048:
//		    			b.add((byte)0x02);
//		    			break;
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_SM2_1:
//		    			b.add((byte)0x04);
//		    			break;
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_SM2_2:
//		    			b.add((byte)0x08);
//		    			break;
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_SM2_3:
//		    			b.add((byte)0x10);
//		    			break;
//		    		case  TEID_ASYMMERTIC_ALG.TEID_ALG_RSA1280:
//		    			b.add((byte)0x20);
//		    			break;    			
//		    	}
//		    	b.addAll(byteToArrayList(shortToByte(infos[i].uchKeyExCertFileID)));
//		    	b.addAll(byteToArrayList(shortToByte(infos[i].uchKeyExPrivateKeyFileID)));
//		    	b.addAll(byteToArrayList(shortToByte(infos[i].uchKeyExPublicKeyFileID)));
//		    	b.addAll(byteToArrayList(shortToByte(infos[i].uchSignCertFileID)));
//		    	b.addAll(byteToArrayList(shortToByte(infos[i].uchSignPrivateKeyFileID)));
//		    	b.addAll(byteToArrayList(shortToByte(infos[i].uchSignPublicKeyFileID)));
//		    	b.addAll(byteToArrayList(intToByte(infos[i].ulFlags)));
//	    	}
//	    	return b;
//	    }
	
}
