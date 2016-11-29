package com.jtanks.controller;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;


public class MessageTest {
    private static final String TEST_MESSAGE = "test message";

	@Test public void messageStringRender () {
    	Message  message = new Message(TEST_MESSAGE);
    	
    	assertEquals(TEST_MESSAGE,message.toString());
    }
}
