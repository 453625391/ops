package com.meitianhui.common.exception;

/**
 * 业务异常类</br>
 * 对违反业务逻辑的操作抛出这个异常。属于应用级异常。<br>
 * 这类异常应由操作员判断如何处理。最终以异常码的信息返给请求方<br>
 * 
 * @author Tiny
 * @since 2015年12月17日
 */
public class BusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**返回数据**/
	private String code;
	/**返回数据**/
	private String msg;

	/**
	 * 自定义参数描述
	 * @param code
	 * @param msg
	 */
	public BusinessException(String code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
