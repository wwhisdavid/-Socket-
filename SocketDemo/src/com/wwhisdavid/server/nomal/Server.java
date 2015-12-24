package com.wwhisdavid.server.nomal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket ss = new ServerSocket(11111);
		
		while(true){
			Socket socket = ss.accept();
			new Thread(new ServerThread(socket)).start();
		}
	}

}
