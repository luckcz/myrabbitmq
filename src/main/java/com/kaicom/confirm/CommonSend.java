package com.kaicom.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class CommonSend {
	private static final String QUEUE_NAME = "test_queue_confirm_1";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection conn = ConnectionUtils.getConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.confirmSelect();
		String msg = "hello world!!";
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		if(channel.waitForConfirms()){
			System.out.println("send message ok ......");
		}else {
			System.out.println("send message failed....");
		}
		channel.close();
		conn.close();
	}
}
