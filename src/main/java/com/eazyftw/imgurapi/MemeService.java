/** This file is released under the Apache License 2.0. See the LICENSE file for details. **/
package com.eazyftw.imgurapi;

import java.io.IOException;
import java.util.List;

import com.eazyftw.imgurapi.model.Image;
import com.eazyftw.imgurapi.model.ImgurResponseWrapper;
import com.eazyftw.imgurapi.util.ImgurApiException;
import com.google.gson.GsonBuilder;

import retrofit.Call;
import retrofit.Response;


/**
 *
 * API services for memes.
 * See <a href="https://api.imgur.com/endpoints/memegen">Imgur docs</a>
 * @author Evan Zimmerman
 */
public class MemeService {

	/**
	 * Returns a list of the default meme images.
	 * @return image list
	 * @throws ImgurApiException - well, heck.
	 */
	public List<Image> listDefaultMemes() throws ImgurApiException {
		
		Call<ImgurResponseWrapper<List<Image>>> call =
				client.getApi().listDefaultMemes();

		try {
			Response<ImgurResponseWrapper<List<Image>>> res = call.execute();
			ImgurResponseWrapper<List<Image>> out = res.body();

			client.throwOnWrapperError( res );
			return out.getData();

		} catch (IOException e) {
			throw new ImgurApiException( e.getMessage() );
		} // try-catch
	} 

	
	// ================================================
	protected MemeService(ImgurApiClient imgurClient, GsonBuilder gsonBuilder ) {
		this.client = imgurClient;
	} // constructor

	private ImgurApiClient client = null;

}
