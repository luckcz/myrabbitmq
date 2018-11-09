package com.kaicom.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	private static final String QUEUE_NAME = "test_simple_queue";
	public static void main(String[] args) throws IOException, TimeoutException {
		//获取一个连接
		Connection connection = ConnectionUtils.getConnection();
		//从连接中获取一个通道
		Channel createChannel = connection.createChannel();
		//创建消息队列
		createChannel.queueDeclare(QUEUE_NAME, false, false, false, null);

		String msg = "你在哪啊";

		//发送消息到队列中
		createChannel.basicPublish("", QUEUE_NAME, null, msg.getBytes());

		System.out.println("发送消息"+msg+"成功");

		createChannel.close();
		connection.close();
	}
}
