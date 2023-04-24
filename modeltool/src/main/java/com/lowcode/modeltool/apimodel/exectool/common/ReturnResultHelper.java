package com.lowcode.modeltool.apimodel.exectool.common;

/**
 * 控制器统一返回JSON格式类
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 */
public class ReturnResultHelper {
	/**
	 *  是否成功状态
	 */
    private boolean success;

	/**
	 * 提示信息
	 */
    private String message;

	/**
	 * 返回数据对象
	 */
    private Object data;

	/**
	 * 状态码,1成功，2失败
	 */
    private Integer code = 1;

	/**
	 * 请求路径
	 */
    private String path;


	/**
	 * 登录token
	 */
    private String token;

    /**
	 * 登录tokenId
	 */
    private String tokenId;






    /**
	 * @return 获取 token属性值
	 */
	public String getToken() {
		return token;
	}



	/**
	 * @param token 设置 token 属性值为参数值 token
	 */
	public void setToken(String token) {
		this.token = token;
	}



	/**
	 * @return 获取 tokenId属性值
	 */
	public String getTokenId() {
		return tokenId;
	}



	/**
	 * @param tokenId 设置 tokenId 属性值为参数值 tokenId
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}



	/**
     *
     * 构造函数
     * @param success 是否成功
     */
    public ReturnResultHelper(boolean success) {
        this.success = success;
        this.message = success ? "ok" : "fail";
        this.code = success ? 1 : 2;
    }



    /**
     *
     * 构造函数
     * @param success 是否成功
     * @param data 返回的前端的数据对象
     */
    public ReturnResultHelper(boolean success, Object data) {
    	this.success = success;
    	this.message = success ? "ok" : "fail";
    	this.code = success ? 1 : 2;
    	this.data = data;
    }

    /**
     *
     * 构造函数
     * @param success 是否成功
     * @param message 提示消息
     * @param data 返回的前端的数据对象
     */
    public ReturnResultHelper(boolean success, String message, Object data) {
    	this.success = success;
    	this.message = message;
    	this.code = success ? 1 : 2;
    	this.data = data;
    }

    /**
     * @return boolean
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success 是否成功
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return Object
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data 返回前端的数据对象
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message 提示信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return Integer
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code 状态码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return String
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path 请求路径
     */
    public void setPath(String path) {
        this.path = path;
    }



}
