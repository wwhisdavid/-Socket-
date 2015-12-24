package com.wwhisdavid.server.nomal;

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
	
	public ServerThread(Socket socket) {
		// TODO Auto-generated constructor stub
		this.s = socket;
	}
	@Override
	public void run() {
		try {
				// 封装流
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				System.out.println("break1");
				BufferedWriter bw = new BufferedWriter(new FileWriter("demo2.txt"));
				System.out.println("break2");
				String line = null;
				while ((line = br.readLine()) != null) { // 阻塞
					bw.write(line);
					bw.newLine();
					bw.flush();
					System.out.println("receive!");
				}
				// 给出反馈
				BufferedWriter bwServer = new BufferedWriter(
						new OutputStreamWriter(s.getOutputStream()));
				bwServer.write("文件上传成功");
				bwServer.newLine();
				bwServer.flush();

				// 释放资源
				bw.close();
				s.close();
		} catch (IOException e) {
			
		}

	}

}
