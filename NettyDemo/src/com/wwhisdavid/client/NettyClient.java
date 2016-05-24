package com.wwhisdavid.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyClient {
	private static int i = 0;

	public void connect(int port, String host, String msg) throws InterruptedException {
		// 配置客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ByteBuf delimiter = Unpooled.copiedBuffer("$".getBytes());
							ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new NettyClientHandle(msg));
						}
					});
			ChannelFuture future = b.connect(host, port).sync();
			future.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void testConcurrent() throws InterruptedException {

		for (; i < 1;i++) {
			Thread.sleep(100);
			new Thread(new Runnable() {

				public void run() {

					int port = 12345;
					try {
						// Random r = new Random();
						// int j = r.nextInt(10000);
						long startTime;
						startTime = System.currentTimeMillis() / 1000;
						String temp1 = "1D0B4FAFED5F4E4E4612ECD54E3386E7#1&" + startTime;
						String temp3 = "&20.7&20.7&9&9&9$";
						StringBuffer buffer = new StringBuffer();
						buffer.append(temp1);
						buffer.append(temp3);

						// System.out.println(buffer.toString());
						new NettyClient().connect(port, "127.0.0.1", buffer.toString());
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();

		}

	}

	public static void main(String[] args) throws InterruptedException {

		// if (args != null && args.length > 0) {
		// port = Integer.valueOf(args[0]);
		// }

//		send("hello java!");
		testConcurrent();
	}
	
	public static void send(String msg) throws InterruptedException {
		new Thread(new Runnable() {
			
			
			@Override
			public void run() {
				int port = 12345;
				// TODO Auto-generated method stub
				try {
					new NettyClient().connect(port, "127.0.0.1", msg);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
