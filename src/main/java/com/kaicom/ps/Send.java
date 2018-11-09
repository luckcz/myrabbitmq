package com.kaicom.ps;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	private static final String EXCHANGE_NAME = "test_exchange_fanout";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection conn = ConnectionUtils.getConnection();
		Channel channel = conn.createChannel();
		//声名交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout"); //分发

		//发送消息
		String msg = "hello world";

		channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());

		System.out.println("send : "+msg);

		channel.close();

		conn.close();
	}
}
