package com.wwhisdavid.client.nomal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;

import javax.swing.text.AbstractDocument.BranchElement;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		
		// 1.创建客户端socket
		Socket socket = new Socket("localhost", 12345);
		
		// 2.封装需要上传的文本，这里以为例
		BufferedReader bf = new BufferedReader(new FileReader("demo.txt"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		String line = null;
		while( ( line = bf.readLine()) != null){
			bw.write(line);
			bw.newLine();
			bw.flush();
			System.out.println("send success!");
		}
		
		// 4.通知服务器，传输结束
		socket.shutdownOutput();
		
		// 5.接收服务器反馈
		BufferedReader brClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String client = brClient.readLine();
		
		//  关闭客户端
		brClient.close();
		socket.close();
	}

}
