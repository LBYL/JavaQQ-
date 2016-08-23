package com.lbyl.Biz;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.lbyl.Utils.ChatUtil;
import com.lbyl.Utils.LogUtil;

/**
 * 首先实现聊天室功能 服务器类： 启动服务器，循环接受客户端
 * 
 * @author LBYL
 *
 */
public class ChatServer {

	private static boolean runningState;
	private int port;
	private ServerSocket sc;
	private MainThread mThread;
	private Socket client;

	// 构造时传入端口号
	public ChatServer(int port) {
		this.port = port;
		runningState = false;
		LogUtil.log("chatServer的构造方法");
	}

	/**
	 * @param 外部设置开闭状态
	 */
	public void setRunningState(boolean runningState) {
		LogUtil.log("调用了设置服务器关闭");
		ChatServer.runningState = runningState;
	}

	/**
	 * 开启服务器
	 */
	public void setUpServer() {
		try {
			// 创建服务器

			if (runningState) {// 如果已经开了，就不响应
				return;
			}
			sc = new ServerSocket(port);
			runningState = true;
			System.out.println("------服务器创建成功！-------");

			mThread = new MainThread();
			mThread.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 返回服务器运行状态
	 * 
	 * @return
	 */
	public static boolean getRunningState() {
		return runningState;
	}

	/**
	 * 关闭服务器
	 * 
	 * @throws IOException
	 */
	public void closeServer() throws IOException {
		mThread.interrupt();
		sc.close();
		System.out.println("----------服务器已关闭！---------");
	}

	/**
	 * 循环接收客户端线程
	 * 
	 * @author LBYL
	 *
	 */
	class MainThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				// 阻塞接收客户端

				try {
					client = sc.accept();
					System.out.println("服务器收到新的socket！");
					// 创建线程
					ProcessThread st = new ProcessThread(client);
					// 维护到队列中
					ChatUtil.getStList().add(st);
					System.out.println("-------现在有" + ChatUtil.getStList().size() + "个ProcessThread--------");

					st.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("停止接收客户端");
				}
			}
		}
	}
}
