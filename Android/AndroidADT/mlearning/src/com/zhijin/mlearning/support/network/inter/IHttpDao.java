package com.zhijin.mlearning.support.network.inter;

/**
 * @author XingRongJing
 * 
 * @param <I>
 *            输入参数
 * @param <O>
 *            输出参数
 * @param <E>
 *            解析对象，如json、xml字符串
 */
public interface IHttpDao<O, E>
{

	/**
	 * 处理请求参数返回输出参数
	 * 
	 * @param e
	 * @return 成功/失败
	 */
	public boolean process(E e);

	/**
	 * 操作成功
	 * 
	 * @param o
	 */
	public void success(O o);

	/**
	 * 操作失败
	 * 
	 * @param o
	 * @param error
	 */
	public void fails(O o);

	/**
	 * 获取结果对象
	 * 
	 * @return
	 */
	public O getResult();
}
