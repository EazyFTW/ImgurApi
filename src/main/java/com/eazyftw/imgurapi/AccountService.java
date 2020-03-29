/** This file is released under the Apache License 2.0. See the LICENSE file for details. **/
package com.eazyftw.imgurapi;

import java.io.IOException;
import java.util.List;

import com.eazyftw.imgurapi.model.Account;
import com.eazyftw.imgurapi.model.AccountSettings;
import com.eazyftw.imgurapi.model.Album;
import com.eazyftw.imgurapi.model.ChangedAccountSettings;
import com.eazyftw.imgurapi.model.Comment;
import com.eazyftw.imgurapi.model.Image;
import com.eazyftw.imgurapi.model.ImgurResponseWrapper;
import com.eazyftw.imgurapi.model.gallery.GalleryItem;
import com.eazyftw.imgurapi.model.gallery.GalleryItemProxy;
import com.eazyftw.imgurapi.model.gallery.GalleryProfile;
import com.eazyftw.imgurapi.util.ImgurApiException;
import com.eazyftw.imgurapi.util.ImgurAuthException;
import com.google.gson.GsonBuilder;

import retrofit.Call;
import retrofit.Response;


/**
 * 
 * Manages the user's account, settings, favorites, etc
 * <p>
 * See <a href="http://api.imgur.com/endpoints/account">Imgur Accounts</a> for API details
 *
 * @author Evan Zimmerman
 */
public class AccountService {

	/**
	 * Given an account name, return the Account object for it.
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName the name of the account
	 * @return Account object
	 * @throws ImgurApiException something went pear-shaped
	 */
	public Account getAccount( String userName ) throws ImgurApiException {
		Call<ImgurResponseWrapper<Account>> call =
				client.getApi().getAccount( userName );

		try {
			Response<ImgurResponseWrapper<Account>> res = call.execute();
			ImgurResponseWrapper<Account> out = res.body();
			client.throwOnWrapperError( res );

			return out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // getAccount


	/**
	 * Given an account name and a page number (starting at 0),
	 * return a list of GalleryItems that user has favorited.
	 * GalleryItem is the superclass for GalleryImage and
	 * GallerAlbum, so cast the results as appropriate for access
	 * to more specific fields.  Defaults to sorting by newest.
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName the userName to return gallery items for
	 * @param page page number of results to return, starting at 0
	 * @return a list of gallery items
	 * @throws ImgurApiException something went wrong
	 */
	public List<GalleryItem> listGalleryFavorites( String userName,
			int page ) throws ImgurApiException {

		return listGalleryFavorites( userName, page, Account.GallerySort.newest);
	} // listFavorites

	/**
	 * Given an account name, a sort, and a page number (starting at 0),
	 * return a list of GalleryItems that user has favorited in the gallery.
	 * GalleryItem is the superclass for GalleryImage and
	 * GallerAlbum, so cast the results as appropriate for access
	 * to more specific fields.
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName name of the user to get favorites for
	 * @param sort the sort direction for results
	 * @param page the page number to return starting at 0
	 * @return a list of gallery items
	 * @throws ImgurApiException something went wrong
	 */
	public List<GalleryItem> listGalleryFavorites( String userName,
			int page,
			Account.GallerySort sort
			) throws ImgurApiException {
		Call<ImgurResponseWrapper<List<GalleryItemProxy>>> call =
				client.getApi().listAccountGalleryFavorites( userName, page, sort );

		try {
			Response<ImgurResponseWrapper<List<GalleryItemProxy>>> res = call.execute();
			ImgurResponseWrapper<List<GalleryItemProxy>> out = res.body();

			client.throwOnWrapperError( res );

			return client.galleryService().convertToGalleryItems( out.getData() );
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listFavorites


	/**
	 * Return a list of GalleryItems this user has favorited.
	 * GalleryItem is the superclass for GalleryImage and
	 * GallerAlbum, so cast the results as appropriate for access
	 * to more specific fields.
	 * <p>
     * <b>ACCESS: AUTHENTICATED USER</b>
	 * @return a list of gallery items
	 * @throws ImgurApiException something failed
	 */
	public List<GalleryItem> listFavorites() throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 401 );
		} // if

		Call<ImgurResponseWrapper<List<GalleryItemProxy>>> call =
				client.getApi().listAccountFavorites( userName );

		try {
			Response<ImgurResponseWrapper<List<GalleryItemProxy>>> res = call.execute();
			ImgurResponseWrapper<List<GalleryItemProxy>> out = res.body();

			client.throwOnWrapperError( res );

			return client.galleryService().convertToGalleryItems( out.getData() );
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listFavorites

	/**
	 * Given an account name, a sort, and a page number (starting at 0),
	 * return a list of GalleryItems that user has submitted in the gallery.
	 * GalleryItem is the superclass for GalleryImage and
	 * GallerAlbum, so cast the results as appropriate for access
	 * to more specific fields.
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName name of the user to get favorites for
	 * @param page the page number to return starting at 0
	 * @return a list of gallery items
	 * @throws ImgurApiException something failed
	 */
	public List<GalleryItem> listSubmissions( String userName,
			int page ) throws ImgurApiException {
		Call<ImgurResponseWrapper<List<GalleryItemProxy>>> call =
				client.getApi().listAccountSubmissions( userName, page );

		try {
			Response<ImgurResponseWrapper<List<GalleryItemProxy>>> res = call.execute();
			ImgurResponseWrapper<List<GalleryItemProxy>> out = res.body();

			client.throwOnWrapperError( res );

			return client.galleryService().convertToGalleryItems( out.getData() );
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listSubmissions

	/**
	 * Return the settings on the currently authenticated account.
	 * <p>
     * <b>ACCESS: AUTHENTICATED USER</b>
	 * @return The account's settings
	 * @throws ImgurApiException something failed
	 */
	public AccountSettings getAccountSettings() throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 403 );
		} // if

		Call<ImgurResponseWrapper<AccountSettings>> call =
				client.getApi().getAccountSettings( userName );

		try {
			Response<ImgurResponseWrapper<AccountSettings>> res = call.execute();
			ImgurResponseWrapper<AccountSettings> out = res.body();

			client.throwOnWrapperError( res );

			return out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // getAccountSettings

	/**
	 * Saves the settings on the currently authenticated account.
	 * <p>
     * <b>ACCESS: AUTHENTICATED USER</b>
	 * @param settings the settings to save
	 * @throws ImgurApiException something failed
	 */
	public void setAccountSettings( ChangedAccountSettings settings ) throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 403 );
		} // if

		Call<ImgurResponseWrapper<Object>> call =
				client.getApi().setAccountSettings( userName, settings );

		try {
			Response<ImgurResponseWrapper<Object>> res = call.execute();
			client.throwOnWrapperError( res );
			
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // setAccountSettings

	/**
	 * Return the gallery profile for a user
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName the userName for the account to return the profile of
	 * @return The account's GalleryProfile
	 * @throws ImgurApiException something failed
	 */
	public GalleryProfile getGalleryProfile( String userName) throws ImgurApiException {
		Call<ImgurResponseWrapper<GalleryProfile>> call =
				client.getApi().getAccountGalleryProfile( userName );

		try {
			Response<ImgurResponseWrapper<GalleryProfile>> res = call.execute();
			ImgurResponseWrapper<GalleryProfile> out = res.body();

			client.throwOnWrapperError( res );

			return out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // getGalleryProfile

	/**
	 * Returns whether or not the currently-authenticated account's
	 * email address has been verified.
	 * <p>
     * <b>ACCESS: AUTHENTICATED USER</b>
	 * @return The account's GalleryProfile
	 * @throws ImgurApiException something failed
	 */
	public boolean isVerified() throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 403 );
		} // if

		Call<ImgurResponseWrapper<Boolean>> call =
				client.getApi().isAccountVerified( userName );

		try {
			Response<ImgurResponseWrapper<Boolean>> res = call.execute();
			ImgurResponseWrapper<Boolean> out = res.body();

			client.throwOnWrapperError( res );
			return out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // isVerified

	/**
	 * Initiates re-sending of the verification email.
	 * <p>
     * <b>ACCESS: AUTHENTICATED USER</b>
	 * @return whether or not a verification email was sent
	 * @throws ImgurApiException something failed
	 */
	public boolean sendVerificationEmail() throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 403 );
		} // if

		Call<ImgurResponseWrapper<Boolean>> call =
				client.getApi().sendAccountVerificationEmail( userName );

		try {
			Response<ImgurResponseWrapper<Boolean>> res = call.execute();

			ImgurResponseWrapper<Boolean> out = res.body();
			client.throwOnWrapperError( res );
			
			return out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // sendVerificationEmail

	/**
	 * Returns a paged list of albums associated with the given
	 * userName, paged 50 at a time.
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName the name of the user to fetch albums for
	 * @param page the page number, starting at 0
	 * @return A list of Album objects
	 * @throws ImgurApiException something failed
	 */
	public List<Album> listAlbums(
			String userName,
			int page ) throws ImgurApiException {

		Call<ImgurResponseWrapper<List<Album>>> call =
				client.getApi().listAccountAlbums( userName, page );

		try {
			Response<ImgurResponseWrapper<List<Album>>> res = call.execute();
			ImgurResponseWrapper<List<Album>> out = res.body();
			client.throwOnWrapperError( res );
			return out.getData();
			
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listAlbums

	/**
	 * Returns a list of album IDs associated with the given
	 * userName, paged 50 at a time
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName the name of the user to fetch album ids for
	 * @param page The page number to fetch, starting at 0
	 * @return A list of album IDs (integers)
	 * @throws ImgurApiException something failed
	 */
	public List<String> listAlbumIds(
			String userName,
			int page ) throws ImgurApiException {

		Call<ImgurResponseWrapper<List<String>>> call =
				client.getApi().listAccountAlbumIds( userName, page );
		try {
			Response<ImgurResponseWrapper<List<String>>> res = call.execute();
			ImgurResponseWrapper<List<String>> out = res.body();
			client.throwOnWrapperError( res );
			return out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listAlbumIds

	/**
	 * Returns the total number of Albums the given user
	 * owns.
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName The name of the user to fetch an album count for
	 * @return Integer the number of albums the user owns
	 * @throws ImgurApiException something failed
	 */
	public int getAlbumCount(
				String userName ) throws ImgurApiException {

		Call<ImgurResponseWrapper<Integer>> call =
				client.getApi().getAccountAlbumCount( userName );

		try {
			Response<ImgurResponseWrapper<Integer>> res = call.execute();
			ImgurResponseWrapper<Integer> out = res.body();

			client.throwOnWrapperError( res );
			return out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // getAlbumCount

	/**
	 * Returns a paged list of comments associated with the given
	 * userName, paged 50 at a time, sorted by newest-first
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName the name of the user to fetch comments for
	 * @param page the page number, starting at 0
	 * @return A list of Comment objects
	 * @throws ImgurApiException something failed
	 */
	public List<Comment> listComments(
			String userName,
			int page ) throws ImgurApiException {
		return listComments( userName, Comment.Sort.Newest, page );
	} // listComments
	
	/**
	 * Returns a paged list of comments associated with the given
	 * userName, paged 50 at a time.
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName the name of the user to fetch comments for
	 * @param sort a sort direction
	 * @param page the page number, starting at 0
	 * @return A list of Comment objects
	 * @throws ImgurApiException something failed
	 */
	public List<Comment> listComments(
			String userName,
			Comment.Sort sort,
			int page ) throws ImgurApiException {

		Call<ImgurResponseWrapper<List<Comment>>> call =
				client.getApi().listAccountComments( userName, sort, page );

		try {
			Response<ImgurResponseWrapper<List<Comment>>> res = call.execute();
			ImgurResponseWrapper<List<Comment>> out = res.body();
			client.throwOnWrapperError( res );
			return out.getData();
			
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listComments
	
	/**
	 * Returns a list of comment IDs associated with the given
	 * userName, paged 50 at a time
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName the name of the user to fetch comment ids for
	 * @param sort a sort direction
	 * @param page The page number to fetch, starting at 0
	 * @return A list of comment IDs (integers)
	 * @throws ImgurApiException something failed
	 */
	public List<Integer> listCommentIds(
			String userName,
			Comment.Sort sort,
			int page ) throws ImgurApiException {

		Call<ImgurResponseWrapper<List<Integer>>> call =
				client.getApi().listAccountCommentIds( userName, sort, page );
		try {
			Response<ImgurResponseWrapper<List<Integer>>> res = call.execute();
			ImgurResponseWrapper<List<Integer>> out = res.body();
			client.throwOnWrapperError( res );
			return out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listCommentIds

	/**
	 * Returns the total number of Comments the given user
	 * owns.
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @param userName The name of the user to fetch a comment count for
	 * @return int the number of comments the user owns
	 * @throws ImgurApiException something failed
	 */
	public int getCommentCount(
				String userName ) throws ImgurApiException {
		Call<ImgurResponseWrapper<Integer>> call =
				client.getApi().getAccountCommentCount( userName );

		try {
			Response<ImgurResponseWrapper<Integer>> res = call.execute();
			ImgurResponseWrapper<Integer> out = res.body();

			client.throwOnWrapperError( res );
			return (Integer)out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // getCommentCount
	
	/**
	 * Returns a paged list of images associated with the current
	 * user, paged 50 at a time.
	 * <p>
     * <b>ACCESS: AUTHENTICATED USER</b>
	 * @param page the page number, starting at 0
	 * @return A list of Image objects
	 * @throws ImgurApiException something failed
	 */
	public List<Image> listImages( int page ) throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 401 );
		} // if

		Call<ImgurResponseWrapper<List<Image>>> call =
				client.getApi().listAccountImages( userName, page );

		try {
			Response<ImgurResponseWrapper<List<Image>>> res = call.execute();
			ImgurResponseWrapper<List<Image>> out = res.body();
			client.throwOnWrapperError( res );
			return out.getData();
			
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listImages
	
	/**
	 * Returns a list of image IDs associated with the current
	 * user, paged 50 at a time
	 * <p>
     * <b>ACCESS: AUTHENTICATED USER</b>
	 * @param page The page number to fetch, starting at 0
	 * @return A list of Image IDs (strings)
	 * @throws ImgurApiException something failed
	 */
	public List<String> listImageIds( int page ) throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 401 );
		} // if

		Call<ImgurResponseWrapper<List<String>>> call =
				client.getApi().listAccountImageIds( userName, page );
		try {
			Response<ImgurResponseWrapper<List<String>>> res = call.execute();
			ImgurResponseWrapper<List<String>> out = res.body();
			client.throwOnWrapperError( res );
			return out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // listImageIds
	
	/**
	 * Returns the total number of Images the current user owns.
	 * <p>
     * <b>ACCESS: AUTHENTICATED USER</b>
	 * @return int the number of images the user owns
	 * @throws ImgurApiException something failed
	 */
	public int getImageCount() throws ImgurApiException {
		String userName = client.getAuthenticatedUserName();
		if( userName == null ) {
			throw new ImgurAuthException( "No user logged in", 401 );
		} // if

		Call<ImgurResponseWrapper<Integer>> call =
				client.getApi().getAccountImageCount( userName );

		try {
			Response<ImgurResponseWrapper<Integer>> res = call.execute();
			ImgurResponseWrapper<Integer> out = res.body();

			client.throwOnWrapperError( res );
			return (Integer)out.getData();
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} 
	} // getImageCount
	

	// ================================================

	protected AccountService(ImgurApiClient imgurClient, GsonBuilder gsonBuilder) {
		this.client = imgurClient;
	}

	private ImgurApiClient client = null;

}
