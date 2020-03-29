/** This file is released under the Apache License 2.0. See the LICENSE file for details. **/
package com.eazyftw.imgurapi.model;

import java.util.List;

import com.eazyftw.imgurapi.util.Utils;

/**
 * This is meant to be internal, not seen by human eyes. It is a list of notifications, by message/reply type.
 * @author Evan Zimmerman
 */

public class NotificationList {
	
	
	
	/**
	 * Get the message notifications
	 * @return the messages
	 */
	public List<Notification> getMessageNotifications() {
		return messages;
	}


	/**
	 * Get the message notifications
	 * @return the reply notifications
	 */
	public List<Notification> getReplyNotifications() {
		return replies;
	}


	@Override
	public String toString() {
		return Utils.toString( this );
	}


	// ===================================================
	
	public NotificationList() { }
	
	private List<Notification> messages;
	private List<Notification> replies;
} 
