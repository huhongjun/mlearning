package com.zhijin.mlearning.support.network.inter;

import com.zhijin.mlearning.support.network.Param;

/**
 * 每个请求(本地/远程)操作的回调接口
 * 
 * @author XingRongJing
 * 
 */
public interface ICallback
{

	public void notifySuccessSaveLearningProgress(Param out);

	public void notifySuccessFindLearningProgress(Param out);

	public void onFails(Param out);
}
