package com.ryel.springBootJpa.controller.editor;


import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateEditor extends PropertyEditorSupport {

	public String getAsText() {
		Date value = (Date) getValue();
		if (null == value) {
			value = new Date();
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(value);
	}

	public void setAsText(String text) throws IllegalArgumentException {
		Date value = null;
		if (null != text && !text.equals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				value = df.parse(text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		setValue(value);
	}
}
