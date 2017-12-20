package com.meitianhui.common.exception;

/**
 * 系统异常类</br>
 * 系统运行中的异常,如sql语句查询错误,数据类型转换错误等异常，这属于系统级异常<br>
 * 此异常类只捕获exception类,然后讲异常抛给controller统一处理<br>
 * 
 * @author Tiny
 * @since 2015年12月17日
 */
public class SystemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 返回数据 **/
	private String code;
	private String msg;

	/**
	 * 自定义参数描述
	 * 
	 * @param code
	 * @param msg
	 */
	public SystemException(String code, String msg, Exception e) {
		this.code = code;
		this.msg = msg;
		this.setStackTrace(e.getStackTrace());
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
