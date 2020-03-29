/** This file is released under the Apache License 2.0. See the LICENSE file for details. **/
package com.eazyftw.imgurapi.model.gallery;

import java.util.ArrayList;

import com.eazyftw.imgurapi.model.Trophy;
import com.eazyftw.imgurapi.util.Utils;
import com.google.gson.annotations.SerializedName;

/**
 * It's possible to access a set of counters about a user's
 * activity.
 * @author Evan Zimmerman
 *
 */

public class GalleryProfile {

	
	/**
	 * Total number of comments this user has made in the gallery
	 * @return the commentCount
	 */
	public int getCommentCount() {
		return commentCount;
	}

	/**
	 * Total number of items favorited by this user in the gallery
	 * @return the favoriteCount
	 */
	public int getFavoriteCount() {
		return favoriteCount;
	}

	/**
	 * Total number of images submitted by this user
	 * @return the submissionCount
	 */
	public int getSubmissionCount() {
		return submissionCount;
	}

	/**
	 * A list of the trophies the user has earned
	 * @return the trophies
	 */
	public ArrayList<Trophy> getTrophies() {
		return trophies;
	}

	// ===================================================
	@SerializedName("total_gallery_comments")
	private int commentCount;
	@SerializedName("total_gallery_favorites")
	private int favoriteCount;
	@SerializedName("total_gallery_submissions")
	private int submissionCount;
	private ArrayList<Trophy> trophies;
	
	@Override
	public String toString() {
		return Utils.toString( this );
	}

} // class AccountSettings
