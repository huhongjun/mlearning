package com.zhijin.mlearning.support.network.inter;

import com.zhijin.mlearning.support.network.Param;

public interface IObserver
{

	public void addCallback(int hashCode, ICallback callback);

	public void deleteCallback(int hashCode, ICallback callback);

	public void notifySuccessSaveLearningProgress(Param out);

	public void notifySuccessFindLearningProgress(Param out);

	public void notifyFails(Param param);

	public void clearObservers();

	public int getObserverSize();
}
