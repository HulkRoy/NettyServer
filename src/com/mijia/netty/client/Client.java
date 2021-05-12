package com.mijia.netty.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {

	public static String tcpSendToServerWithResponse(String ip, int port, String msg) {
		String rev = "";
		Socket s = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			s = new Socket(ip, port);
			os = s.getOutputStream();
			is = s.getInputStream();
			os.write(msg.getBytes());
			os.flush();
			rev = readFromServer(is, s);
		} catch (Exception e) {
			rev = "failed";
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
				if (s != null)
					s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rev;
	}

	public static String readFromServer(InputStream in, Socket socket) {
		String msg = "";
		try {
			socket.setSoTimeout(2000);
			byte[] buffer = new byte[1024];
			int len = in.read(buffer);
			msg = new String(buffer, 0, len);
		} catch (SocketTimeoutException e) {
			// 从服务器端接收数据有个时间限制（系统自设，也可以自己设置），超过了这个时间，便会抛出该异常
			System.out.println("Time out, No response");
			msg = "timeout";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	public static void main(String[] args) {
		System.out.println(Client.tcpSendToServerWithResponse("47.93.207.49", 8004,
				"S=0&Action=GoodsNotice&MsgId=0&Timer=0&Imei=sendTo862991528710001&ChannelIndex=0&SaleId=0&E=0"));
	}
}
