package smpp.client;

/**
 * This is a sample Java implementation of SMPP client
 * 
 * @author Suresh Kannan
 *
 */

public class SMPPRequest
{
  private String sourceAddress;
  private String destinationAddress;
  private String shortMessage;
  private String longMessage;
  private String callback;

  public String getSourceAddress()
  {
    return this.sourceAddress;
  }
  public void setSourceAddress(String sourceAddress) {
    this.sourceAddress = sourceAddress;
  }
  public String getDestinationAddress() {
    return this.destinationAddress;
  }
  public void setDestinationAddress(String destinationAddress) {
    this.destinationAddress = destinationAddress;
  }
  public String getShortMessage() {
    return this.shortMessage;
  }
  public void setShortMessage(String shortMessage) {
    this.shortMessage = shortMessage;
  }
  public String getLongMessage() {
	return longMessage;
  }
  public void setLongMessage(String longMessage) {
	this.longMessage = longMessage;
  }
  public String getCallback() {
    return this.callback;
  }
  public void setCallback(String callback) {
    this.callback = callback;
  }
}