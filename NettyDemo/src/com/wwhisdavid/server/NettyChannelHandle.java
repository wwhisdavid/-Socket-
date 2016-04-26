package com.wwhisdavid.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.crypto.Data;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.omg.CORBA.DATA_CONVERSION;

import com.wwhisdavid.entity.ANodeDetailEntity;
import com.wwhisdavid.service.ReceiveMessageService;
import com.wwhisdavid.service.impl.ReceiveMessageServiceImpl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyChannelHandle extends ChannelHandlerAdapter {
	private ReceiveMessageService service = new ReceiveMessageServiceImpl();
	private XMLParser parser;
	private FileWriter fileWriter;
	private BufferedWriter bw;
	private File file;
	private Document document;
	private Element rootElem;
	

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// ByteBuf buf = (ByteBuf)msg;
		// byte[] req = new byte[buf.readableBytes()];
		// buf.readBytes(req);
		// String body = new String(req, "utf-8");
		String body = (String) msg;
		System.out.println("The Server recerive:" + body);
		String token = body.substring(0, 3);
		System.out.println("token:" + token);
		
		if (token.contains("tx")) { // txt文件传送过来开头验证
			if (parser == null) {
				parser = new XMLParser();
			}
			String mainBody = body.split("://")[1];
			String[] names = mainBody.split("&");
			String nameToken = mainBody.split("&")[0];
			String fileName = null;
			if (names.length > 1) {
				fileName = mainBody.split("&")[1];
				System.out.println("nameToken:" + nameToken + " fileName: " + fileName);
			}
			
			if (nameToken.equals("whu")) {// 文件名
				file = new File("/Users/shiph0ne/Documents/ServerFiles/" + fileName + ".xml");
				if (file.exists()) {
					return;
				} else {
					System.out.println("create");
					file.createNewFile();
					// 分解文件名意义
					String[] nameArr = fileName.split("_");
					String id = nameArr[0];
					String timeStart = nameArr[1];
					String timeEnd = nameArr[2];
					String totalCount = nameArr[3];
				
					// 计算时间的差
					SimpleDateFormat format = new SimpleDateFormat("yyyy%MM%dd-HH:mm:ss");
					Date startDate = format.parse(timeStart);
					parser.startTimestamp = startDate.getTime();
					Date endDate = format.parse(timeEnd);
					parser.endTimestamp = endDate.getTime();

					parser.time_difference = (parser.endTimestamp - parser.startTimestamp) / Long.valueOf(totalCount);
					
					//1.在内存创建xml文档
					document = DocumentHelper.createDocument();
					rootElem = document.addElement("dataList");
				}

				if (fileName.length() == 0) {
					return;
				}

//				fileWriter = new FileWriter(file);
//				bw = new BufferedWriter(fileWriter);
			} else if (mainBody.equals("end")) {// 关闭资源
				
				FileOutputStream out = new FileOutputStream(file);
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding("utf-8");
				XMLWriter writer = new XMLWriter(out, format);
				
				writer.write(document);
				writer.close();
//				fileWriter.close();
//				bw.close();
			} else {// 一条数据
				String[] mainBodys = mainBody.split("\t");
				System.out.println(mainBodys[0] + "-" + mainBodys[1] + "-" + mainBodys[2]);
				
				Element nodeElem = rootElem.addElement("record");
				nodeElem.addAttribute("record_time", (parser.startTimestamp + parser.count * parser.time_difference) / 1000 + "");
				parser.count ++;
				nodeElem.addElement("stress-x").setText(mainBodys[0]);
				nodeElem.addElement("stress-y").setText(mainBodys[1]);
				nodeElem.addElement("stress-z").setText(mainBodys[2]);
			}
		} else
			service.insert2mysql(body); // 失败抛异常
		System.out.println(parser.startTimestamp + "--------=======" + parser.endTimestamp + "*******" + parser.time_difference);
		ByteBuf resp = Unpooled.copiedBuffer("收到了$".getBytes());
		ctx.write(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
	
	private class XMLParser{
		public long time_difference;
		public long startTimestamp;
		public long endTimestamp;
		public long count = 0; 
	}

}
