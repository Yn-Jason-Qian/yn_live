package com.live.exception.file;

public class FileOperateErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5916633422762013567L;

	public FileOperateErrorException() {
		super();
	}

	public FileOperateErrorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FileOperateErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileOperateErrorException(String message) {
		super(message);
	}

	public FileOperateErrorException(Throwable cause) {
		super(cause);
	}


}
