package smpp.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.Session;

/**
 * This is a sample Java implementation of SMPP client
 * 
 * @author Suresh Kannan
 *
 */

class JsmppSMSSender implements SMPPSender {
	private static final String US_ASCII = "US-ASCII";
	private static SMPPSession mySession;
	private int enquiryInterval = 15000;
	private boolean enquiryEnabled = false;
	private String host;
	private int port;
	private long timeout;
	private byte addrTon;
	private byte addrNpi;
	private String addrRange;
	private String systemId;
	private String password;
	private String pduDegree;
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat df = new SimpleDateFormat("MM-dd, hh:mm:ss");
	private byte destAddrTon;
	private byte desAddrNpi;
	protected synchronized void closeConnection() {
		if (mySession == null)
			return;
		try {
			mySession.unbindAndClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mySession = null;
	}

	protected synchronized void connect() throws Exception {
		if (mySession != null
				&& (mySession.getSessionState().isBound() || mySession
						.getSessionState().equals(SessionState.OPEN))) {
			System.out
					.println("Connection already established..no new connection will be opened");
			return;
		}
		try {
			mySession = new SMPPSession();
			mySession.setTransactionTimer(this.timeout);
			mySession.setEnquireLinkTimer(this.enquiryInterval);
			mySession.setMessageReceiverListener(new MessageReceiverListenerImpl());
			mySession.connectAndBind(this.host, this.port, BindType.BIND_TX,
					this.systemId, this.password, null,
					TypeOfNumber.valueOf(this.addrTon),
					NumberingPlanIndicator.valueOf(this.addrNpi), "11*",
					this.timeout);
			mySession.addSessionStateListener(new SessionStateListenerImpl(
					mySession));
		} catch (IOException ex) {
			throw new Exception("SMPP server connection cannot be established",
					ex);
		}
		System.out.println("New connection object: " + mySession.getSessionId());
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		if ((((this.host != null) || (host == null)))
				&& (((this.host == null) || (this.host.equals(host)))))
			return;
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public String getPduDegree() {
		return pduDegree;
	}

	public void setPduDegree(String pduDegree) {
		this.pduDegree = pduDegree;
	}

	public void setPort(int port) {
		if (this.port == port)
			return;
		this.port = port;
	}

	public long getTimeout() {
		return this.timeout;
	}

	public void setTimeout(long timeout) {
		if (this.timeout == timeout)
			return;
		this.timeout = timeout;
	}

	public byte getAddrTon() {
		return this.addrTon;
	}

	public void setAddrTon(byte addrTon) {
		if (this.addrTon == addrTon)
			return;
		this.addrTon = addrTon;
	}

	public byte getAddrNpi() {
		return this.addrNpi;
	}

	public void setAddrNpi(byte addrNpi) {
		if (this.addrNpi == addrNpi)
			return;
		this.addrNpi = addrNpi;
	}

	public String getAddrRange() {
		return this.addrRange;
	}

	public void setAddrRange(String addrRange) {
		if ((((this.addrRange != null) || (addrRange == null)))
				&& (((this.addrRange == null) || (this.addrRange
						.equals(addrRange)))))
			return;
		this.addrRange = addrRange;
	}

	public String getSystemId() {
		return this.systemId;
	}

	public void setSystemId(String systemId) {
		if ((((this.systemId != null) || (systemId == null)))
				&& (((this.systemId == null) || (this.systemId.equals(systemId)))))
			return;
		this.systemId = systemId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		if ((((this.password != null) || (password == null)))
				&& (((this.password == null) || (this.password.equals(password)))))
			return;
		this.password = password;
	}

	public void setEnquiryInterval(int enquiryInterval) {
		this.enquiryInterval = enquiryInterval;
	}

	public int getEnquiryInterval() {
		return this.enquiryInterval;
	}

	public void setEnquiryEnabled(boolean enquiryEnabled) {
		this.enquiryEnabled = enquiryEnabled;
	}

	public boolean isEnquiryEnabled() {
		return this.enquiryEnabled;
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(super.getClass().getSimpleName());
		buffer.append("\nConnectivity parameters:");
		buffer.append("\nhostname:");
		buffer.append(this.host);
		buffer.append("\nport:");
		buffer.append(this.port);
		buffer.append("\ntimeout:");
		buffer.append(this.timeout);
		buffer.append("\naddrTon:");
		buffer.append(this.addrTon);
		buffer.append("\naddrNpi:");
		buffer.append(this.addrNpi);
		buffer.append("\naddrRange:");
		buffer.append(this.addrRange);
		buffer.append("\nsystemId:");
		buffer.append(this.systemId);
		buffer.append("\npassword:");
		buffer.append(this.password);
		buffer.append("\nconnection object:");
		if (mySession != null)
			buffer.append(mySession.getSessionId());
		else
			buffer.append("none");

		return buffer.toString();
	}

	protected Session getConnection() {
		return mySession;
	}

	private synchronized void validateConnection() throws Exception {
		if (mySession != null)
			cal.setTimeInMillis(mySession.getLastActivityTimestamp());
		if (mySession.getSessionState().isBound()
				|| mySession.getSessionState().equals(SessionState.OPEN)) {
			System.out
					.println("SMPP Connection open..last reported activity on "
							+ df.format(cal.getTime()));
			return;
		}
		System.out
				.println("SMPP session is not bound and open..will reconnect.!");
		System.out.println("Last reported activity on "
				+ df.format(cal.getTime()));
		try {
			mySession.unbindAndClose();
		} catch (Exception ex) {
			System.out
					.println("Unbind failed..perhaps connection already closed."
							+ ex.getMessage());
			try {
				mySession.close();
			} catch (Exception ex1) {
			}
		}
		mySession = null;
		connect();
	}

	public String submitShortMessage(SMPPRequest request) throws Exception {
		if (mySession == null)
			connect();
		validateConnection();
		String messageId = null;
		boolean ascii;
		if (request.getLongMessage() == null) {
			ascii = isAsciiText(request.getShortMessage());
			messageId = mySession.submitShortMessage(null, TypeOfNumber
					.valueOf(this.addrTon), NumberingPlanIndicator
					.valueOf(this.addrNpi), request.getSourceAddress(),
					TypeOfNumber.valueOf(destAddrTon), NumberingPlanIndicator
							.valueOf(desAddrNpi), request
							.getDestinationAddress(), new ESMClass(), (byte) 0,
					(byte) 1, null, null, new RegisteredDelivery(
							SMSCDeliveryReceipt.DEFAULT), (byte) 0,
					new GeneralDataCoding(ascii ? Alphabet.ALPHA_DEFAULT : Alphabet.ALPHA_UCS2), (byte) 0,
					ascii ? request.getShortMessage().getBytes(US_ASCII)
							: getEncodedMessage(request.getShortMessage()),
					new OptionalParameter[0]);
		} else {
			ascii = isAsciiText(request.getLongMessage());
			byte[] byteMsg = ascii ? request.getLongMessage()
					.getBytes(US_ASCII) : getEncodedMessage(request
					.getLongMessage());
			OptionalParameter tlvArray[] = new OptionalParameter[1];
			OptionalParameter.OctetString tlv = new OptionalParameter.OctetString(
					(short) 0x0424, byteMsg);
			tlvArray[0] = tlv;
			messageId = mySession.submitShortMessage(null, TypeOfNumber
					.valueOf(this.addrTon), NumberingPlanIndicator
					.valueOf(this.addrNpi), request.getSourceAddress(),
					TypeOfNumber.valueOf(destAddrTon), NumberingPlanIndicator
							.valueOf(desAddrNpi), request
							.getDestinationAddress(), new ESMClass(), (byte) 0,
					(byte) 1, null, null, new RegisteredDelivery(
							SMSCDeliveryReceipt.DEFAULT), (byte) 0,
					new GeneralDataCoding(ascii ?  Alphabet.ALPHA_DEFAULT : Alphabet.ALPHA_UCS2), (byte) 0,
					new byte[0], tlvArray);
		}
		return messageId;
	}

	private boolean isAsciiText(String message) {
		CharsetEncoder asciiEncoder = Charset.forName(US_ASCII).newEncoder();
		return asciiEncoder.canEncode(message);
	}

	private byte[] getEncodedMessage(String message) {
		Charset charset = Charset.forName("ISO-8859-1");
		CharsetEncoder encoder = charset.newEncoder();
		String returnMessage = message;
		try {
			ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(message));
			return bbuf.array();
		} catch (CharacterCodingException e) {
			// could not convert..just return the same message.
		}
		return returnMessage.getBytes();
	}

	public String sendMessage(SMPPRequest request) throws Exception {
		return submitShortMessage(request);
	}

	public String sendMessage(SMPPRequest sms, String debugFolder,
			String messageId) throws Exception {
		return null;
	}

	public void setDestAddrTon(byte destAddrTon) {
		this.destAddrTon = destAddrTon;
	}

	public void setDestAddrNpt(byte destAddrNpi) {
		this.desAddrNpi = destAddrNpi;
	}

	public void setDataCoding(byte dataCoding) {
	}

}