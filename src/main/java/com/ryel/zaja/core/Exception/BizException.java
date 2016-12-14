package com.ryel.zaja.core.exception;

/**
 * 自定义异常类
 *
 */
public class BizException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
    private String code;

    public BizException(String message) {
        this.message = message;
    }

    public BizException(Throwable throwable, String code, String message) {
        super(throwable);
        this.code = code;
        this.message = message;
    }

    public BizException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(final String p_messageKey, Throwable p_throwable) {
        super(p_messageKey,p_throwable);
    }

    public BizException(String code, final String p_messageKey, Throwable p_throwable) {
        super(p_messageKey,p_throwable);
        this.code = code;
        this.message = p_messageKey;
    }

    @Override
	public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BizException{");
        sb.append("message='").append(message).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
