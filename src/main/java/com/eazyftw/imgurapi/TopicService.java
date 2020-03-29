/** This file is released under the Apache License 2.0. See the LICENSE file for details. **/
package com.eazyftw.imgurapi;

import java.io.IOException;
import java.util.List;

import com.eazyftw.imgurapi.model.ImgurResponseWrapper;
import com.eazyftw.imgurapi.model.Topic;
import com.eazyftw.imgurapi.model.gallery.GalleryItem;
import com.eazyftw.imgurapi.model.gallery.GalleryItemProxy;
import com.eazyftw.imgurapi.util.ImgurApiException;
import com.google.gson.GsonBuilder;

import retrofit.Call;
import retrofit.Response;


/**
 * 
 * API service for topics, which provide access to categorized content
 * like "funny" and "aww."
 * <p>
 * See <a href="https://api.imgur.com/endpoints/topic">Imgur documentation</a>.
 *
 * @author Evan Zimmerman
 */
public class TopicService {

	/**
	 * Return a list of the default topics available on Imgur.
	 * <p>
     * <b>ACCESS: ANONYMOUS</b>
	 * @return list of Topic objects
	 * @throws ImgurApiException d'oh
	 */
	public List<Topic> listDefaultTopics() throws ImgurApiException {
		
		Call<ImgurResponseWrapper<List<Topic>>> call =
				client.getApi().listDefaultTopics();

		try {
			Response<ImgurResponseWrapper<List<Topic>>> res = call.execute();
			ImgurResponseWrapper<List<Topic>> out = res.body();

			client.throwOnWrapperError( res );
			List<Topic> list = out.getData();
			
			// convert all their #)(*$)#% proxy items into real items
			// TODO: REFACTOR THIS
			for( Topic topic : list ) {
				GalleryItemProxy proxy = topic.getItemProxy();
				if( proxy == null ) {
					continue;
				} // if
				topic.setConvertedItem( client.galleryService()
						.convertProxyItem( proxy ) );
			} // for
			return list;
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} // try-catch
	} 

	/**
	 * Return a list of GalleryItems pertinent to this topic
	 * <p>
        * <b>ACCESS: ANONYMOUS</b>
	 * @param topicId the id or URL-formatted name of the topic
	 * @param sort the sort direction - Viral | Time | Top
	 * @param window the windowing mechanism if Top
	 * @param page the page number, starting at 0
	 * @return a list of GalleryItem objects
	 * @throws ImgurApiException I think I need to lie down
	 */
	public List<GalleryItem> listTopic(
			int topicId,
			GalleryItem.Sort sort,
			GalleryItem.Window window,
			int page ) throws ImgurApiException {
	
		String sortStr = sort.name().toLowerCase();
		String windowStr = window.name().toLowerCase();

		Call<ImgurResponseWrapper<List<GalleryItemProxy>>> call =
				client.getApi().listTopicItems(
						topicId,
						sortStr,
						windowStr,
						page );

		try {
			Response<ImgurResponseWrapper<List<GalleryItemProxy>>> res = call.execute();
			ImgurResponseWrapper<List<GalleryItemProxy>> list = res.body();

			client.throwOnWrapperError( res );

			// TODO: Look into refactoring this
			return client.galleryService().convertToGalleryItems( list.getData() );
		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} // try-catch
		
	}
	
	// ================================================
	protected TopicService(ImgurApiClient imgurClient, GsonBuilder gsonBuilder ) {
		this.client = imgurClient;
	} // constructor

	private ImgurApiClient client = null;

}
