package com.wwhisdavid.client.nomal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientThread implements Runnable {

	private BufferedReader bf;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 1.创建客户端socket
				Socket socket = null;
				try {
					socket = new Socket("localhost", 12345);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					bf = new BufferedReader(new FileReader("demo.txt"));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BufferedWriter bw = null;
				try {
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String line = null;
				try {
					while( ( line = bf.readLine()) != null){
						bw.write(line);
						bw.newLine();
						bw.flush();
						System.out.println("send success!");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// 4.通知服务器，传输结束
				try {
					socket.shutdownOutput();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// 5.接收服务器反馈
				BufferedReader brClient = null;
				try {
					brClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					String client = brClient.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//  关闭客户端
				try {
					brClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

}
