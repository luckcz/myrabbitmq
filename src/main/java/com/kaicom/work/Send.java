package com.kaicom.work;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	private static final String QUEUE_NAME = "test-work-queue";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		//获取连接
		Connection conn = ConnectionUtils.getConnection();
		//获取通道
		Channel channel = conn.createChannel();
		//声名队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		for(int i = 0 ; i < 50 ; i++){
			String msg = "hello"+i;
			System.out.println("[wq ] send"+msg);
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			Thread.sleep(i*20);
		}

		channel.close();
		conn.close();
	}
}
