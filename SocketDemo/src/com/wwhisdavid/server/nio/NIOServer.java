package com.wwhisdavid.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.swing.plaf.synth.SynthStyle;

public class NIOServer {
	public static SelectorLoop connectionBell;
	public static SelectorLoop readBell;
	public boolean isReading = false;

	public static void main(String[] args) throws IOException {
		NIOServer server = new NIOServer();
		System.out.println("main1");
		server.startServer();
		System.out.println("main2");
	}

	// 1.启动服务器
	private void startServer() throws IOException {
		connectionBell = new SelectorLoop();
		readBell = new SelectorLoop();

		// 开启监听
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false); // 非阻塞

		ServerSocket socket = ssc.socket();
		socket.bind(new InetSocketAddress("localhost", 12345));

		// 注册连接事件
		System.out.println("register before");
		ssc.register(connectionBell.getSelector(), SelectionKey.OP_ACCEPT);
		System.out.println("register after");
		new Thread(connectionBell).start();
	}

	// 内部类，处理轮询
	public class SelectorLoop implements Runnable {
		private Selector selector;
		private ByteBuffer buffer = ByteBuffer.allocate(1024);

		public SelectorLoop() throws IOException {
			this.selector = Selector.open();
		}

		public void run() {
			System.out.println("before try");
			while (true) {
				try {
					// 阻塞,只有当至少一个注册的事件发生的时候才会继续.
					this.selector.select();

					Set<SelectionKey> selectKeys = this.selector.selectedKeys();
					Iterator<SelectionKey> it = selectKeys.iterator();
					while (it.hasNext()) {
						SelectionKey key = it.next();
						it.remove();
						// 处理事件. 可以用多线程来处理.
						this.dispatch(key);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void dispatch(SelectionKey key) throws IOException, InterruptedException {
			if (key.isAcceptable()) { // 连接操作
				// 获取对应key的SocketChannel
				ServerSocketChannel ss = (ServerSocketChannel) key.channel();
				SocketChannel sc = ss.accept(); // 可指定是否阻塞

				sc.configureBlocking(false);
				sc.register(readBell.getSelector(), SelectionKey.OP_READ);// 注册在读取selector上

				// 单线程
				synchronized (NIOServer.this) {
					if (!NIOServer.this.isReading) {
						NIOServer.this.isReading = true;
						new Thread(readBell).start();
					}
				}
			} else if (key.isReadable()) { // 读取事件
				SocketChannel sc = (SocketChannel) key.channel();
				// 写数据到buff
				int count = sc.read(buffer);
				if (count < 0) { // 客户端已断开
					key.cancel();
					sc.close();
					return;
				}
				// 切换buffer到读状态,内部指针归位.
				buffer.flip();
				String msg = Charset.forName("UTF-8").decode(buffer).toString();
				System.out.println("Server received [" + msg + "] from client address:" + sc.getRemoteAddress());

				//Thread.sleep(1000);
				// echo back.
				sc.write(ByteBuffer.wrap(msg.getBytes(Charset.forName("UTF-8"))));

				// 清空buffer
				buffer.clear();
			}
		}

		public Selector getSelector() {
			return selector;
		}

		public void setSelector(Selector selector) {
			this.selector = selector;
		}
	}

}
