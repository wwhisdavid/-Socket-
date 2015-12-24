package com.wwhisdavid.presstest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.wwhisdavid.client.nio.NIOClient;
import com.wwhisdavid.client.nomal.Client;
import com.wwhisdavid.client.nomal.ClientThread;

public class PressTest {

//	@Test
//	public void test1() {
//		for (int i = 0; i < 1000; i++) {
//			NIOClient client = null;
//			try {
//				client = new NIOClient(""+i);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			new Thread(client).start();;
//		}	
//	}
	@Test
	public void test2(){
		for (int i = 0; i < 200; i++) {
			new Thread(new ClientThread()).start();
		}
	}
}
