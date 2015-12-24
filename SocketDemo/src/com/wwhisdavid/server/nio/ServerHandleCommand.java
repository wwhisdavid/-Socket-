package com.wwhisdavid.server.nio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class ServerHandleCommand implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(11111);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Socket s = null;
		try {
			s = ss.accept();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
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
}