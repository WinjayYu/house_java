package com.ryel.zaja.controller.editor;


import org.apache.commons.lang.StringUtils;

import java.beans.PropertyEditorSupport;

public class CustomIntegerEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text)
			throws IllegalArgumentException {
		if (text != null && text instanceof String && StringUtils.isNotBlank(text)) {
			text=text.toString().replaceAll(",", "");
			super.setValue(Integer.valueOf(text));
		} else {
			super.setValue(null);
		}
	}

	@Override
	public String getAsText() {
		Object obj=super.getValue();
		String text=null;
		if (obj != null && obj instanceof String && StringUtils.isNotBlank(text)) {
			text=obj.toString().replaceAll(",", "");
		}
		return text;
	}

}
