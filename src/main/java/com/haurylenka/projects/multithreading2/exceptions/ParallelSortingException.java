package com.haurylenka.projects.multithreading2.exceptions;

public class ParallelSortingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParallelSortingException() {
		super();
	}

	public ParallelSortingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParallelSortingException(String message) {
		super(message);
	}

}
