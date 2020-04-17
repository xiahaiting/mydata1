package cn.cf.common.creditpay.shanghai;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;

//import cn.cf.entry.BankInfo;
import cn.cf.property.PropertyConfig;


public class PostUtils {

	protected String sendData = "";

	protected String http = "http";

	protected String split = "&";

	protected String account = "";

	protected String sessionid = "";

	protected String serialNo = "";

	protected java.net.HttpURLConnection urlConnection = null;

	protected String sendDataForBatchQueryBalanceCurrentOp = "";// 账户余额查询
	protected String transferInner1_1Op = "transferInner1_1Op";// 行内公转公转账

	protected String CebankUserLogonOpForSign = ""; // 登录


	/**
	 * 登陆交易
	 */
	public String logon() {
		this.sendData = this.CebankUserLogonOpForSign;
		System.out.println(CebankUserLogonOpForSign);
		String responsorUrl = this.http + "://" + PropertyConfig.getProperty("sh_api_ip") + ":" + PropertyConfig.getProperty("sh_pre_port")+ "/CM/APISessionReqServlet?";// 登陆
		System.out.println(responsorUrl);
		try {
			java.net.URL aURL = new java.net.URL(responsorUrl);

			urlConnection = (java.net.HttpURLConnection) aURL.openConnection();

			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("User-Agent", "compatible; MSIE 5.0;");
			// urlConnection.setRequestProperty("User-Agent", "HTTP");
			urlConnection.connect();
			if (sendData != null && sendData.trim().length() != 0) {
				urlConnection.getOutputStream().write(sendData.getBytes());
			}

			urlConnection.getResponseCode();
			int contentLen = urlConnection.getContentLength();

			java.io.DataInputStream in = new java.io.DataInputStream(urlConnection.getInputStream());

			byte buffer[] = new byte[contentLen];

			int len = 0;

			while (len < contentLen) {
				int remainedLen = contentLen - len;
				if (remainedLen > 1024)
					remainedLen = 1024;
				int readLen = in.read(buffer, len, remainedLen);
				if (readLen == -1 || readLen == 0) {
					break;
				}

				len = len + readLen;
			}

			urlConnection.disconnect();
			urlConnection = null;
			String a = new String(buffer, 0, contentLen);
			System.out.println(a);
			return a;

		} catch (Throwable e) {
			e.printStackTrace();
			if (urlConnection != null) {
				try {
					urlConnection.disconnect();
					urlConnection = null;
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		} finally {
			if (urlConnection != null) {
				try {
					urlConnection.disconnect();
					urlConnection = null;
				} catch (Exception ee) {
				}
			}
		}
		return "";
	}


	public static String BOS_URLencode(String orgStr) {
		String newStr = null;
		try {
			newStr = java.net.URLEncoder.encode(orgStr, "GBK");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		return newStr;
	}



	/**
	 * 一般交易
	 */
	public String hiscomm(String sendDatatemp) {
		this.sendData = sendDatatemp;
		String responsorUrl = this.http + "://" + PropertyConfig.getProperty("sh_api_ip") + ":" + PropertyConfig.getProperty("sh_pre_port") + "/CM/APIReqServlet?";

		try {
			System.out.println(sendDatatemp);
			java.net.URL aURL = new java.net.URL(responsorUrl);

			urlConnection = (java.net.HttpURLConnection) aURL.openConnection();

			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			// urlConnection.setRequestProperty("User-Agent", "MSIE");
			urlConnection.setRequestProperty("User-Agent", "compatible; MSIE 5.0;");
			urlConnection.connect();
			if (sendData != null && sendData.trim().length() != 0) {
				OutputStream output = urlConnection.getOutputStream();
				output.write(sendData.getBytes("GBK"));
				output.flush();
				output.close();
			}

			 urlConnection.getResponseCode();
			int contentLen = urlConnection.getContentLength();

			java.io.DataInputStream in = new java.io.DataInputStream(urlConnection.getInputStream());

			byte buffer[] = new byte[contentLen];

			int len = 0;
			long start = System.currentTimeMillis();
			while (len < contentLen) {
				int remainedLen = contentLen - len;
				if (remainedLen > 1024)
					remainedLen = 1024;

				int readLen = in.read(buffer, len, remainedLen);

				if (readLen == -1 || readLen == 0) {
					break;
				}

				len = len + readLen;
			}
			long end = System.currentTimeMillis();
			System.out.println("处理完成，耗时：" + (end - start) + "毫秒");
			System.out.println("total nums=" + contentLen);
			urlConnection.disconnect();
			urlConnection = null;
			String a = new String(buffer, 0, contentLen, "GBK");
			return a;

		} catch (Throwable e) {
			e.printStackTrace();
			if (urlConnection != null) {
				try {
					urlConnection.disconnect();
					urlConnection = null;
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		} finally {
			if (urlConnection != null) {
				try {
					urlConnection.disconnect();
					urlConnection = null;
				} catch (Exception ee) {
				}
			}
		}
		return "";
	}

	public String post(String method, String params,String userID) {
		String longonresult = null;
		String result = null;
		// 登录
		serialNo = String.valueOf(Math.round(Math.random() * 10000)) + System.currentTimeMillis();
		CebankUserLogonOpForSign = "opName=CebankUserLogon1_1Op"
				+ split
				+ "reqData="
				+ BOS_URLencode(cn.cf.common.creditpay.shanghai.SignOpStep.getNodeValue((new SignOpStep("<?xml version=\"1.0\" encoding=\"GBK\"?>"
				+ "<BOSEBankData>" + "<opReq>" + "<serialNo>" + serialNo + "</serialNo>" + "<reqTime>"
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "</reqTime>" + "<ReqParam>" + "<userID>"
				+ PropertyConfig.getProperty("sh_user_id")+ "</userID>" + "<userPWD>" +  PropertyConfig.getProperty("sh_user_pwd") + "</userPWD>" + "</ReqParam>" + "</opReq>"
				+ "</BOSEBankData>")).sign(), "signed_data"));
		longonresult = logon();
		System.out.println("帐号登陆结果：=" + longonresult + "=");
		if (null == longonresult || "".equals(longonresult) || longonresult.substring(0, 1).equals("<")) {// 没有
			System.out.println("没有登陆正确:" + longonresult);
			result = "loginFail"+longonresult;
		} else {
			// 截取sessionid
			sessionid = longonresult.substring(0, 40);
			// 生成交易序列号
			serialNo = String.valueOf(Math.round(Math.random() * 10000)) + System.currentTimeMillis();
			try {
				String data = "dse_sessionId=" + sessionid + split
						+ "opName=" + method + split + "reqData=" + params;
				result = hiscomm(data);


			} catch (Exception e) {
				System.out.println("银行返回信息异常:" + result);
			}
		}
		return result;
	}

//	public static void main(String[] arg) {
//		PostUtils postUtils=new PostUtils();
//		XmlUtils xmlUtils=new XmlUtils();
//		//BankBaseService bankBaseService=new BankShanghaiServiceImpl();
//		//bankBaseService.searchBankCreditAmount(null,null);
//		String aurl="MCAgrmtContrQuery1_1Op";//额度查询
//		String burl="PusReqExInfo1_1Op";//放款条件维护结果
//		String curl="applyFinancingLoan3_1Op";//放款申请
//		String durl="qryLoanApplyRes1_1Op";//放款申请结果查询
//		String d2url="loanApplyResSync1_2Op";//贷款信息查询
//		String eurl="repaymentResSync1_1Op";//还款查询
//		String furl="FeeCharge1_1Op";//费用代扣03003827355
//		String a=	xmlUtils.searchLimitAmt("49415651");
//	    String b=xmlUtils.applyLoanCondition("TBA30110059219090902266","49415651","1","SXO201909041004380757987"
//			,800.0,"2019-11-04",
//			"化纤汇授信客户","化纤汇第三方客户");
//	    String c=xmlUtils.applyLoan(KeyUtils.getFlow(6),"TBA30110059219090902266","CON20190909627800005"
//			,"49415651","03003827347","20191111","SXO201909041004380757987",800.0);
//	    String d=xmlUtils.searchLoan("20190919102147845350");
//	    String d2=xmlUtils.searchloanApply("LD1926272093","","49415651","20190909","20191113");
//	    String e=xmlUtils.searchRepayment("LD1926272093","49415651","20180804","20191124");
//	    String f=xmlUtils.agentPay("","","","");
//		System.out.println(postUtils.post(burl,b,"49415651"));
//	  	System.out.println(postUtils.post(curl,c,"49415651"));
//
//     System.out.println(postUtils.post(eurl,e,"49415651"));
//
//
//	}
}
