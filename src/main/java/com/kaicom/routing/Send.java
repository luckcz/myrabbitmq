package com.kaicom.routing;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	private static final String EXCHANGE_NAME = "test_exchange_direct";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection conn = ConnectionUtils.getConnection();
		Channel channel = conn.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");

		String msg = "hello direct!---info";

		String routingKey = "info";

		channel.basicPublish(EXCHANGE_NAME, routingKey,  null, msg.getBytes());
		System.out.println("发送了消息："+msg);
		channel.close();
		conn.close();
	}
}
