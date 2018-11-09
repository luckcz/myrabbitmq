package com.kaicom.tx;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class TxSend {
	private static final String QUEUE_NAME = "test_queue_tx_1";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection conn = ConnectionUtils.getConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		try{
			String msg = "hello world!!!";
			channel.txSelect();
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			int i = 1/0;
			System.out.println("发送消息"+msg+"成功");
			channel.txCommit();
		}catch (Exception e) {
			e.printStackTrace();
			channel.txRollback();
			System.out.println("send message rollback");
		}finally {
			channel.close();
			conn.close();
		}
	}
}
