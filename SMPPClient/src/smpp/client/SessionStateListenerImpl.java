package smpp.client;

import org.jsmpp.extra.SessionState;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.SessionStateListener;
/**
 * This is a sample Java implementation of SMPP client
 * 
 * @author Suresh Kannan
 *
 */


public class SessionStateListenerImpl
  implements SessionStateListener
{
  private SMPPSession session;

  public SessionStateListenerImpl(SMPPSession session) {
    this.session = session;
  }

  public void onStateChange(SessionState newState, SessionState oldState, Object source) {
    if (!SessionState.CLOSED.equals(newState)) return;
    try {
    	this.session.unbindAndClose();
    } catch (Exception ex) {
      System.out.println("SMPP Session unbind failed.");
      ex.printStackTrace();
    }
    this.session = null;
  }
}