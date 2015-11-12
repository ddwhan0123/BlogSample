package cn.eid.sample.idspsdk.common;


/**
 * 在具体使用时，必须trim掉其中的所有空格。
 * @author Sober
 *
 */
public final class Commands {

	public static final String CMD_GET_CHALLENGE_04 = "0084 0000 04";
	public static final String CMD_GET_CHALLENGE_08 = "0084 0000 08";
	
	public static final String CMD_VERIFY_00 = "0020 0000 03 111111";
	public static final String CMD_VERIFY_81 = "0020 0081 03 111111";
	
	public static final String CMD_EXT_AUTH = "0082 0000 08 94E00CE6B43B6A5B";
	
	public static final String CMD_EXPORT_SEESION_KEY = "80D8 0400 0A";
	
	public static final String CMD_IMPORT_SEESION_KEY = "804A 0400 0A C1081122334455667788 00";
	
	public static final String CMD_ENC_OR_DEC = "80fa 0040 0c 00000000 1122334455667788 00";
	
	public static final String CMD_DATA_COMPRESS = "80c4 0101 0a c1081122334455667788 00";
	
	public static final String CMD_GENERATE_RSA_KEY_00 = "8042 0000 08 C0020000C2021012 00";
	public static final String CMD_GENERATE_RSA_KEY_89 = "8042 0000 08 C0020000C2021012 89";
	public static final String CMD_GENERATE_RSA_KEY = "8042 0000 08 c002 1013 c2021012";
	
	public static final String CMD_RSA_PUBLIC_KEY_CALC = "8046 0000 87 c002 1013 c18180 data";
	public static final String CMD_RSA_PUBLIC_KEY_CALC_00 = "8046 4000 87 c002 1013 c18180 data 00";
	
	public static final String CMD_RSA_PRIVATE_KEY_CALC = "8048 0000 87 c202 1012 c18180 data";
	public static final String CMD_RSA_PRIVATE_KEY_CALC_00 = "8048 4000 87 c202 1012 c18180 data 00";
	
	public static final String CMD_INSTALL_RSA_KEY = "84C6 0001 8B data";
	
	public static final String CMD_GENERATE_SM2_KEY_00 = "8040 0000 08 c0020000 c2021012 00";
	public static final String CMD_GENERATE_SM2_KEY = "8040 0000 08 c0021013 c2021012";
	
	public static final String CMD_SM2_PUBLIC_KEY_CALC = "804c 0001 48 c002 1013 c1820040 data 00";
	
	public static final String CMD_SM2_PRIVATE_KEY_CALC = "804E 0001 78 C2021312C1820070 data 00";
	
	public static final String CMD_IMPORT_SM2_KEY = "80c2 0100 46 c0020000 ca40 data";
	
	public static final String CMD_EXPORT_PUBLIC_KEY = "80C9 0001 08 C002A0F3C2021012 00";
	
	public static final String CMD_VERIFY_CERT = "80C8 0001 14 CC0221D3CD02A0F3CE02A0F2CF06101110131012";
	
	public static final String CMD_CHANGE_PIN = "805e 0100 07 111111FF111111";
	
	
	public static final String[] CMDS = new String[] {
		
		CMD_GET_CHALLENGE_04,
		CMD_GET_CHALLENGE_08, 
		
		CMD_VERIFY_00, 
		CMD_VERIFY_81,
		
		CMD_EXT_AUTH, 
		
		CMD_EXPORT_SEESION_KEY,
		CMD_IMPORT_SEESION_KEY,
		
		CMD_ENC_OR_DEC,
		
		CMD_DATA_COMPRESS,
		
		CMD_GENERATE_RSA_KEY_00,
		CMD_GENERATE_RSA_KEY_89,
		CMD_GENERATE_RSA_KEY,
		
		CMD_RSA_PUBLIC_KEY_CALC,
		CMD_RSA_PUBLIC_KEY_CALC_00,
		
		CMD_RSA_PRIVATE_KEY_CALC,
		CMD_RSA_PRIVATE_KEY_CALC_00,
		
		CMD_INSTALL_RSA_KEY,
		
		CMD_GENERATE_SM2_KEY_00,
		CMD_GENERATE_SM2_KEY,
		
		CMD_SM2_PUBLIC_KEY_CALC,
		CMD_SM2_PRIVATE_KEY_CALC,
		
		CMD_IMPORT_SM2_KEY,
		CMD_EXPORT_PUBLIC_KEY,
		CMD_VERIFY_CERT,
		CMD_CHANGE_PIN
		
};
	
}
