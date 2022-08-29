package com.kk.grocerystore.exception;

public class DuplicateProductException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	public DuplicateProductException() {
		super();
	}
	
	public DuplicateProductException(String message) {
		super(message);
	}
}
