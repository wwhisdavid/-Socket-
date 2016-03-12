package com.wwhisdavid.transfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.plaf.synth.SynthStyle;

public class ServerThread implements Runnable {

	private Socket s;
	private String data;
	
	public ServerThread(Socket socket, String data) {
		// TODO Auto-generated constructor stub
		this.s = socket;
		this.data = data;
	}
	@Override
	public void run() {
		try {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bw.write(data);
				bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			s.shutdownOutput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String receive = br.readLine();
			System.out.println(receive);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
