package com.ryel.springBootJpa.utils.exception;

/**
 * Created by burgl on 2016/8/24.
 */
public class FileUploadException extends RuntimeException{

    // ~ Constructors -----------------------------------------------------------
    /**
     * Constructs a CloneFailedException.
     *
     * @param message description of the exception
     * @since upcoming
     */
    public FileUploadException(final String message) {
        super(message);
    }
    /**
     * Constructs a CloneFailedException.
     *
     * @param cause cause of the exception
     * @since upcoming
     */
    public FileUploadException(final Throwable cause) {
        super(cause);
    }
    /**
     * Constructs a CloneFailedException.
     *
     * @param message description of the exception
     * @param cause cause of the exception
     * @since upcoming
     */
    public FileUploadException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
