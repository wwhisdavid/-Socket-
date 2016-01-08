package com.wwhisdavid.server;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.crypto.Data;

import org.omg.CORBA.DATA_CONVERSION;

import com.wwhisdavid.service.ReceiveMessageService;
import com.wwhisdavid.service.impl.ReceiveMessageServiceImpl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyChannelHandle extends ChannelHandlerAdapter {
	private ReceiveMessageService service = new ReceiveMessageServiceImpl();
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		ByteBuf buf =  (ByteBuf)msg;
//		byte[] req = new byte[buf.readableBytes()];
//		buf.readBytes(req);
//		String body = new String(req, "utf-8");
		String body = (String)msg;
		
		System.out.println("The Server recerive:" + body);
		service.insert2mysql(body); // 失败抛异常
		
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
	
	
	
	
	
}
