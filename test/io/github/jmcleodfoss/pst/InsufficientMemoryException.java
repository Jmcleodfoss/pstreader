package com.jsoft.pst;

@SuppressWarnings("serial")
class InsufficientMemoryException extends Exception {
	InsufficientMemoryException(Exception e)
	{
		super(e);
	}
}
