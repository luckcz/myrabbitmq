package com.kaicom.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	private static final String EXCHANGE_NAME = "test_exchange_topic";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection conn = ConnectionUtils.getConnection();

		Channel channel = conn.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "topic");

		String msg = "hi! hello!";
		String routingKey = "goods.update";
		channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
		System.out.println("send msg :---->"+msg);
		channel.close();
		conn.close();
	}
}
