package smpp.client;

import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.MessageType;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * This is a sample Java implementation of SMPP client
 * 
 * @author Suresh Kannan
 *
 */

public class MessageReceiverListenerImpl implements MessageReceiverListener {
	public void onAcceptDeliverSm(DeliverSm deliverSm)
			throws ProcessRequestException {

		if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
			// this message is delivery receipt
			try {
				DeliveryReceipt delReceipt = deliverSm
						.getShortMessageAsDeliveryReceipt();

				// lets cover the id to hex string format
				long id = Long.parseLong(delReceipt.getId()) & 0xffffffff;
				String messageId = Long.toString(id, 16).toUpperCase();

				/*
				 * you can update the status of your submitted message on the
				 * database based on messageId
				 */

				System.out.println("Receiving delivery receipt for message '"
						+ messageId + " ' from " + deliverSm.getSourceAddr()
						+ " to " + deliverSm.getDestAddress() + " : "
						+ delReceipt);
			} catch (InvalidDeliveryReceiptException e) {
				System.err.println("Failed getting delivery receipt");
				e.printStackTrace();
			}
		} else {
			// this message is regular short message

			/*
			 * you can save the incoming message to database.
			 */

			System.out.println("Receiving message : "
					+ new String(deliverSm.getShortMessage()));
		}
	}

	public void onAcceptAlertNotification(AlertNotification alertNotification) {
	}

	public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
			throws ProcessRequestException {
		return null;
	}

}