package com.wwhisdavid.client;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyClientHandle extends ChannelHandlerAdapter {
	private static final Logger logger = Logger.getLogger(NettyClientHandle.class.getName());
	private final ByteBuf firstMessage;
	
	public NettyClientHandle(){
		byte[] req = "hello".getBytes();
		firstMessage = Unpooled.buffer(req.length);
		firstMessage.writeBytes(req);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warning("Unexcepted exception from downstream:"+ cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("发送:"+firstMessage);
		ctx.writeAndFlush(firstMessage);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf)msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "utf-8");
		System.out.println(body);
	}
	
	
}
