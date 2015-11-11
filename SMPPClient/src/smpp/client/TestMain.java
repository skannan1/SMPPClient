package smpp.client;

/**
 * This is a sample Java implementation of SMPP client
 * 
 * @author Suresh Kannan
 *
 */


public class TestMain {

	public static void main(String[] args) throws Exception {
		int port = 2775;
		String host = "localhost";
		int timeout = 3000;
		String systemId = "smppclient1";
		String password = "password";
		byte addrTon = 0;
		byte addrNpi = 0;
		byte dataCoding = 3;
		String addrRange = "11*";
		String sourceAddress = "9329";

		int enquiryInterval = 3000;
		boolean enquiryEnabled = true;
		SMPPSender sender = new JsmppSMSSender();
		sender.setHost(host);
		sender.setPort(port);
		sender.setTimeout(timeout);
		sender.setAddrNpi(addrNpi);
		sender.setAddrTon(addrTon);
		sender.setAddrRange(addrRange);
		sender.setSystemId(systemId);
		sender.setPassword(password);
		sender.setEnquiryInterval(enquiryInterval);
		sender.setEnquiryEnabled(enquiryEnabled);
		sender.setDataCoding(dataCoding);
		
		SMPPRequest smtpRequest = new SMPPRequest();
	    smtpRequest.setCallback("9132214348");
	    smtpRequest.setDestinationAddress("9133381443");
	    String message = "Hello, this is a test SMPP message";
	    if (message.length() > 160) {
	    	smtpRequest.setLongMessage(message);
	    } else {
	    	smtpRequest.setShortMessage(message);
	    }
	    smtpRequest.setSourceAddress(sourceAddress);
	    sender.sendMessage(smtpRequest);
	}

}
