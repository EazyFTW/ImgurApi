/** This file is released under the Apache License 2.0. See the LICENSE file for details. **/
package com.eazyftw.imgurapi;

import java.io.IOException;
import java.util.List;

import com.eazyftw.imgurapi.model.ImgurResponseWrapper;
import com.eazyftw.imgurapi.model.Notification;
import com.eazyftw.imgurapi.model.NotificationList;
import com.eazyftw.imgurapi.util.ImgurApiException;
import com.eazyftw.imgurapi.util.ImgurAuthException;
import com.google.gson.GsonBuilder;

import retrofit.Call;
import retrofit.Response;


/**
 * 
 * Access user-specific notifications
 * @author Evan Zimmerman
 */
public class NotificationService {

	/**
	 * Specifically, returns only the notifications that someone
	 * has replied to the user in comments or somewhere.
	 * <p>
        * <b>ACCESS: AUTHENTICATED USER</b>
	 * @param onlyNew true if the request is for only the unviewed notifications
	 * @return A list of Notification objects
	 * @throws ImgurApiException something failed
	 */
	public List<Notification> listReplyNotifications(
			boolean onlyNew ) throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 401 );
		} // if
		
		Call<ImgurResponseWrapper<NotificationList>> call =
				client.getApi().listNotifications( onlyNew );
		try {
			Response<ImgurResponseWrapper<NotificationList>> res = call.execute();
			ImgurResponseWrapper<NotificationList> out = res.body();
			client.throwOnWrapperError( res );
			return out.getData().getReplyNotifications();
			
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listReplyNotifications


	/**
	 * Returns the currently-logged-in user's list of message notifications
	 * <p>
        * <b>ACCESS: AUTHENTICATED USER</b>
	 * @param onlyNew whether to get non-viewed notifications
	 * @return the list of notifications
	 * @throws ImgurApiException it couldn't be done
	 */
	public List<Notification> listMessageNotifications(
			boolean onlyNew ) throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 401 );
		} // if
		
		Call<ImgurResponseWrapper<NotificationList>> call =
				client.getApi().listNotifications( onlyNew );
		try {
			Response<ImgurResponseWrapper<NotificationList>> res = call.execute();
			ImgurResponseWrapper<NotificationList> out = res.body();
			client.throwOnWrapperError( res );
			return out.getData().getMessageNotifications();
			
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listMessageNotifications

	/**
	 * Returns information about a specific notification
	 * <p>
        * <b>ACCESS: AUTHENTICATED USER</b>
	 * @param id the id of the notification to return
	 * @return a Notification object
	 * @throws ImgurApiException something failed
	 */
	public Notification getNotification( long id ) throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 401 );
		} // if
		
		Call<ImgurResponseWrapper<Notification>> call =
				client.getApi().getNotification( id );
		try {
			Response<ImgurResponseWrapper<Notification>> res = call.execute();
			ImgurResponseWrapper<Notification> out = res.body();
			client.throwOnWrapperError( res );
			return out.getData();
			
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	}
	
	/**
	 * Marks a notification as having been viewed
	 * <p>
        * <b>ACCESS: AUTHENTICATED USER</b>
	 * @param id the id of the notification to mark viewed
	 * @return true if successful... false probably means it was already marked viewed
	 * @throws ImgurApiException something failed
	 */
	public boolean markNotificiationViewed( long id ) throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 401 );
		} // if
		
		Call<ImgurResponseWrapper<Boolean>> call =
				client.getApi().markNotificationViewed( id );
		try {
			Response<ImgurResponseWrapper<Boolean>> res = call.execute();
			ImgurResponseWrapper<Boolean> out = res.body();
			client.throwOnWrapperError( res );
			return out.getData();
			
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	}
	
	
	
	
	
	// ================================================
	protected NotificationService(ImgurApiClient imgurClient, GsonBuilder gsonBuilder ) {
		this.client = imgurClient;
	} // constructor

	
	private ImgurApiClient client = null;

}
