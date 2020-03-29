/** This file is released under the Apache License 2.0. See the LICENSE file for details. **/
package com.eazyftw.imgurapi.model;

import com.google.gson.annotations.SerializedName;

/**
 * When voting, these are the two values you can use.
 * Removing a vote means sending the same value again.
 * NOTE: As of 1.0.0, there seems to be no way to get
 * Imgur to delete a vote.
 * @author Evan Zimmerman
 * 
 */
public enum Vote {
	/**
	 * This represents an upvote
	 */
	@SerializedName("up") Up,
	/**
	 * This represents a downvote
	 */
	@SerializedName("down") Down
}
