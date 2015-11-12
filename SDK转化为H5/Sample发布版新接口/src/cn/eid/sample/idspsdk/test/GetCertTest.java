package cn.eid.sample.idspsdk.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.security.cert.X509Certificate;

import com.trimps.eid.sdk.defines.base.ByteResult;
import com.trimps.eid.sdk.defines.common.CertInfo;
import com.trimps.eid.sdk.defines.common.ErrorCode;
import com.trimps.eid.sdk.idspapi.core.DeviceReader;
import android.text.format.DateFormat;
import cn.eid.sample.idspsdk.common.BaseApiTest;
import cn.eid.sample.idspsdk.common.ConverterUtil;

/**
 * 
 * 获取证书
 * <br/>
 * <br/>获取证书前，需知道其容器名 获取证书后，可对证书进行解析处理以获得所需信息
 * <br/>容器内有交换密钥和签名密钥，交换密钥用于加解密，签名密钥用于签名验签，当前容器内只有签名密钥。
 * <br/>Constants.TEID_KEY_TYPE有两个值，当值为TEID_SIGN则使用签名密钥；值为TEID_KEYEX则使用交换密钥
 */
public class GetCertTest extends BaseApiTest {

	public GetCertTest(DeviceReader reader) {
		super(reader);
		
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ResultData perform() {

		ResultData result = new ResultData();
		
		long ret = ErrorCode.ERR_SUCCESS;
		try {
			
			ret = deviceReader.openDevice();
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {

			appendMore("openDevice 失败", ret);
			result.isOK = false;
			result.more = buildMore();
			return result;

		}
		
		appendMore("openDevice 成功");
		

		ByteResult cert = new ByteResult();
		try {
			
			ret = deviceReader.getCert(cert);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ErrorCode.ERR_SUCCESS != ret) {
			
			appendMore("getCert 失败", ret);
			
			result.isOK = false;
			result.more = buildMore();
			
			closeDevice(deviceReader);
			
			return result;

		} else {

			result.isOK = true;
			
			HashMap<String, String> certData = readCertificateInformation(cert.data);
			if (null != certData && certData.size() > 0) {
				
				appendMore("getCert 成功，证书信息如下：");
				
				Iterator iter = certData.entrySet().iterator();
				while (iter.hasNext()) {
					
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					Object val = entry.getValue();
					appendMore("\"" + key + "\"：	" + val);
					
				}

			} else {

				appendMore("getCert 成功，证书解析失败!");
				result.isOK = false;

			}

		}
		
		CertInfo certInfo = new CertInfo();
		try {
			
			ret = deviceReader.getCert(certInfo);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ret == ErrorCode.ERR_SUCCESS) {

			appendMore("getCert 成功");
			result.isOK = true;
			
			printCertInfo(certInfo);

		} else {
	
			appendMore("getCert 失败", ret);
			result.isOK = false;
			
		}
		
			
		closeDevice(deviceReader);
		result.more = buildMore();
		
		return result;
		
	}

	private HashMap<String, String> readCertificateInformation(byte[] buf) {

		HashMap<String, String> hashMap = new HashMap<String, String>();

		try {

			InputStream input = new ByteArrayInputStream(buf);
			Certificate certificate = CertificateFactory.getInstance("X.509", "BC").generateCertificate(input);

			X509Certificate X509certificates = X509Certificate .getInstance(certificate.getEncoded());
			
			String version = convertCertVersion(X509certificates.getVersion());
			
			String issuerDN = X509certificates.getIssuerDN().toString();

			String endDate = DateFormat.format("yyyy-MM-dd HH:mm:ss E", X509certificates.getNotAfter()).toString();
			String beginDate = DateFormat.format("yyyy-MM-dd HH:mm:ss E", X509certificates.getNotBefore()).toString();

			String serialNumber = X509certificates.getSerialNumber().toString(16);
			String sigAlgName = X509certificates.getSigAlgName();
			String sigAlgOID = X509certificates.getSigAlgOID();
			byte[] sigAlgParams = X509certificates.getSigAlgParams();
			String subjectDN = X509certificates.getSubjectDN().getName();
			
			hashMap.put("version", version); // 证书的版本号
			hashMap.put("issuerDN", issuerDN);// 特殊的编号
			hashMap.put("beginDate", beginDate);// 返回证书最后的有效期
			hashMap.put("endDate", endDate);// 返回证书的开始日期
			hashMap.put("serialNumber", serialNumber);// 返回证书的序列号
			hashMap.put("sigAlgName", sigAlgName);// 返回证书的签名
			hashMap.put("sigAlgOID", sigAlgOID);// 返回OID签名算法从证书
			
			if(sigAlgParams != null) {
			
				hashMap.put("sigAlgParams", ConverterUtil.getHexString(
					sigAlgParams, sigAlgParams.length));
			}else{
				hashMap.put("sigAlgParams", null);
			}
			
			hashMap.put("subjectDN", subjectDN);

			return hashMap;

		} catch (Exception e) {

			// recordLog(CustomUtil.LogMode.ERROR, "readCertificateInformation",
			// e.getMessage(), true);
			// new RuntimeException("证书异常，请稍后再试");

			if (e != null)
				e.printStackTrace();

		}

		return null;
	}
	
	
	String convertCertVersion(int index) {
		
		String ret = null;
		switch (index) {
		
		case 0:
			ret = "v1";
			break;
			
		case 1:
			ret = "v2";
			break;
		
		case 2:
			ret = "v3";
			break;
			
		default:
			break;
			
		}
		
		if (null == ret) {
			
			return String.valueOf(index);
			
		}
		
		return ret;
		
	}
	
	
	void printCertInfo(CertInfo info) {
		
		appendMore("证书颁发者（issuer）：	" + info.getIssuer());
		appendMore("证书颁发者序列号（issuerSN）：	" + info.getIssuerSN());
		appendMore("证书主体（subject）：	" + info.getSubject());
		appendMore("证书序列号（sn）：	" + info.getSN());
		appendMore("证书版本号（version）：	" + info.getVersion());
		appendMore("证书有效期（period）：	" + info.getBeginDate() + "~" + info.getEndDate());
		appendMore("证书签名算法（sigAlgName）：	" + info.getSigAlgName());
		appendMore("证书签名算法OID（sigAlgOID）：	" + info.getSigAlgOID());
		appendMore("证书签名算法参数（sigAlgParams）：	" + info.getSigAlgParams());
		
	}

}
