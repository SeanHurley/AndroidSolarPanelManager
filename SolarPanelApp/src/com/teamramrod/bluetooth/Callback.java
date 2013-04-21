package com.teamramrod.bluetooth;

import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;

/**
 * @author seanhurley
 * 
 *         The interface simply extends the GenericCallback and is meant to be
 *         used for the communication between the app and the solar panel. We
 *         use this so that a user knows that for this callback they must give a
 *         BaseResponse type since that is the only type which will compile. We
 *         do not want the programmer to use any other type.
 * 
 * @param <T>
 *            This callback requires a BaseResponse type object for the generic
 *            argument for the asynchronous result.
 */
public interface Callback<T extends BaseResponse> extends GenericCallback<T> {

	@Override
	public void onComplete(T response);
}
