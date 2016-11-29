package com.jtanks.controller;

public class Message {

	private final String message;

	public Message(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
	
}
