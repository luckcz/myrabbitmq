package com.kaicom.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class BatchSend {
	private static final String QUEUE_NAME = "test_queue_confirm_2";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection conn = ConnectionUtils.getConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.confirmSelect();
		for (int i = 0; i < 10; i++) {
			String msg = "confirm batch send!!!";
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			System.out.println("send "+msg);
		}

		if(channel.waitForConfirms()){
			System.out.println("send message sucess......");
		}else{
			System.out.println("send message fail......");
		}
		channel.close();
		conn.close();
	}
}
