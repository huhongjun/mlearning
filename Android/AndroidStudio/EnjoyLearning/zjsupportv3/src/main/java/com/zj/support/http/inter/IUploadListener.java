package com.zj.support.http.inter;

/**
 * 上层上传回调监听-遵循接口分离原则
 * 
 * @author XingRongJing
 */
public interface IUploadListener {

	public void onUploadTransfered(String path, long total,
			long transferedLength);

	public void onUploadSuccess(String path);

	public void onUploadFails(String path, String error);
}
