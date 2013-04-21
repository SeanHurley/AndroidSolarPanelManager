package com.teamramrod.bluetooth;

/**
 * @author seanhurley
 * 
 *         This class is a way for us the have a callback which we can pass
 *         around for our asynchronous communication. You only need to implement
 *         this interface and then pass it along to whatever asynchrounous
 *         method and eventually it will come back with the proper data
 * 
 * @param <T>
 *            The type of object which will be given back during the onComplete
 *            method of this interface
 */
public interface GenericCallback<T> {

	/**
	 * @param result
	 *            - The result from the asynchrounous method which will be
	 *            called after everything is completed
	 */
	public void onComplete(T result);
}
