package smpp.client;

/**
 * This is a sample Java implementation of SMPP client
 * 
 * @author Suresh Kannan
 *
 */

public abstract interface SMPPSender
{
  public abstract String sendMessage(SMPPRequest paramSMTPRequest)
    throws Exception;

  public abstract String sendMessage(SMPPRequest paramSMTPRequest, String paramString1, String paramString2)
    throws Exception;

  public abstract void setPassword(String paramString);

  public abstract String getPassword();

  public abstract void setSystemId(String paramString);

  public abstract String getSystemId();

  public abstract void setAddrRange(String paramString);

  public abstract String getAddrRange();

  public abstract void setAddrNpi(byte paramByte);

  public abstract byte getAddrNpi();

  public abstract void setAddrTon(byte paramByte);

  public abstract byte getAddrTon();

  public abstract void setTimeout(long paramLong);

  public abstract long getTimeout();

  public abstract void setPort(int paramInt);

  public abstract int getPort();

  public abstract void setHost(String paramString);

  public abstract String getHost();

  public abstract void setEnquiryInterval(int paramInt);

  public abstract int getEnquiryInterval();

  public abstract void setEnquiryEnabled(boolean paramBoolean);

  public abstract boolean isEnquiryEnabled();

  public abstract void setDestAddrTon(byte destAddrTon);

  public abstract void setDestAddrNpt(byte destAddrNpi);

  public abstract void setDataCoding(byte dataCoding); 

}
