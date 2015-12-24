package com.wwhisdavid.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.wwhisdavid.server.NIOServer;

public class ConcurrentTest {

	@Test
	public void test() throws IOException {
		
			NIOServer server = new NIOServer();
	        server.initServer(12345);
	        server.listen();
		
	}

}
