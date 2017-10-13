package com.zhijin.mlearning.support.network.obsever;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.zhijin.mlearning.support.network.Param;
import com.zhijin.mlearning.support.network.inter.ICallback;
import com.zhijin.mlearning.support.network.inter.IObserver;

/**
 * 监听管理者
 * 
 * @author XingRongJing
 * 
 */
public class Observer implements IObserver
{

	private static Observer mObserver;
	// private List<ICallback> mObservers = new ArrayList<ICallback>();
	private Map<Integer, ICallback> mObservers = new HashMap<Integer, ICallback>();

	private Observer()
	{
	}

	public static Observer getObserver()
	{
		if (null == mObserver) {
			mObserver = new Observer();
		}
		return mObserver;
	}

	@Override
	public void addCallback(int hashCode, ICallback callback)
	{
		// TODO Auto-generated method stub
		if (callback == null) {
			throw new NullPointerException("Observer-->addCallback()");
		}
		mObservers.put(hashCode, callback);
	}

	@Override
	public void deleteCallback(int hashCode, ICallback callback)
	{
		// TODO Auto-generated method stub
		if (callback == null) {
			throw new NullPointerException("Observer-->deleteCallback()");
		}
		mObservers.remove(hashCode);
	}

	// @Override
	// public void notifySuccessFindAll(Param out) {
	// // TODO Auto-generated method stub
	// int hashCode = out.getHashCode();
	// Set<Integer> set = mObservers.keySet();
	// Iterator<Integer> it = set.iterator();
	// while (it.hasNext()) {
	// int key = it.next();
	// if (hashCode == key) {
	// ICallback callback = mObservers.get(key);
	// callback.onSuccessFindAll(out);
	// break;
	// }
	// }
	// }

	// @Override
	// public void notifySuccessSaveOrUpdate(Param out) {
	// // TODO Auto-generated method stub
	// int hashCode = out.getHashCode();
	// Set<Integer> set = mObservers.keySet();
	// Iterator<Integer> it = set.iterator();
	// while (it.hasNext()) {
	// int key = it.next();
	// if (hashCode == key) {
	// ICallback callback = mObservers.get(key);
	// callback.onSuccessSaveOrUpdate(out);
	// break;
	// }
	// }
	// }

	@Override
	public void notifyFails(Param param)
	{
		// TODO Auto-generated method stub
		int hashCode = param.getHashCode();
		Set<Integer> set = mObservers.keySet();
		Iterator<Integer> it = set.iterator();
		while (it.hasNext()) {
			int key = it.next();
			if (hashCode == key) {
				ICallback callback = mObservers.get(key);
				callback.onFails(param);
				break;
			}
		}
	}

	@Override
	public void clearObservers()
	{
		if (null != mObservers) {
			mObservers.clear();
			mObservers = null;
		}
	}

	@Override
	public int getObserverSize()
	{
		// TODO Auto-generated method stub
		return mObservers == null ? 0 : mObservers.size();
	}

	@Override
	public void notifySuccessSaveLearningProgress(Param out)
	{
		int hashCode = out.getHashCode();
		Set<Integer> set = mObservers.keySet();
		Iterator<Integer> it = set.iterator();
		while (it.hasNext()) {
			int key = it.next();
			if (hashCode == key) {
				ICallback callback = mObservers.get(key);
				callback.notifySuccessSaveLearningProgress(out);
				break;
			}
		}
	}

	@Override
	public void notifySuccessFindLearningProgress(Param out)
	{
		int hashCode = out.getHashCode();
		Set<Integer> set = mObservers.keySet();
		Iterator<Integer> it = set.iterator();
		while (it.hasNext()) {
			int key = it.next();
			if (hashCode == key) {
				ICallback callback = mObservers.get(key);
				callback.notifySuccessFindLearningProgress(out);
				break;
			}
		}
	}
}
