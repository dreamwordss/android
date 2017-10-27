package com.lbl.codek3demo.taobao.bean;

import java.io.Serializable;

public class SuccessRespBean implements Serializable {

	private static final long serialVersionUID = 8950464747685845956L;

	public String code;
	public String data;
	public String version;
	public boolean isFromCache;
	public String point;
	public String num;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isValid() {
		// TODO 如果有版本号，则返回false
		return true;
	}

}
