package com.wwhisdavid.transfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.plaf.synth.SynthStyle;

public class ServerThread implements Runnable {

	private Socket s;
	private File data;
	private StringBuffer stringBuffer;
	private Boolean sendOK;

	public ServerThread(Socket socket, File data, Boolean isReceive) {
		// TODO Auto-generated constructor stub
		this.s = socket;
		this.sendOK = isReceive;
		this.data = data;
	}

	@Override
	public void run() {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			FileReader fileReader = new FileReader(data);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line = null;
			stringBuffer = new StringBuffer();
			stringBuffer.append("whu&" + data.getName() + "\n");
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + "\n");
			}
			bufferedReader.close();
		
			bw.write(stringBuffer.toString());
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
			if (receive.equals("receive!")) {
				sendOK = true;
			}
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
