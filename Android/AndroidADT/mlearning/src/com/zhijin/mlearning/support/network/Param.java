package com.zhijin.mlearning.support.network;

/**
 * 
 * @author XingRongJing
 * 
 */
public class Param
{

	private Class cls;
	/** 操作类的hashcode **/
	private int hashCode;
	/** 操作符 **/
	private int operator;
	/** 操作结果 **/
	private Object result;
	/** 操作方法 **/
	private int method;

	public Param()
	{

	}

	public Class getCls()
	{
		return cls;
	}

	public void setCls(Class cls)
	{
		this.cls = cls;
	}

	public Param(int hashcode)
	{
		this.hashCode = hashcode;
	}

	public int getHashCode()
	{
		return hashCode;
	}

	public void setHashCode(int hashCode)
	{
		this.hashCode = hashCode;
	}

	public int getOperator()
	{
		return operator;
	}

	public void setOperator(int operator)
	{
		this.operator = operator;
	}

	public Object getResult()
	{
		return result;
	}

	public void setResult(Object result)
	{
		this.result = result;
	}

	public int getMethod()
	{
		return method;
	}

	public void setMethod(int method)
	{
		this.method = method;
	}
}
