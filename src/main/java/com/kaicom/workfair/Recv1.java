package com.kaicom.workfair;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;

public class Recv1 {
	private static final String QUEUE_NAME = "test-work-queue-fair";
	public static void main(String[] args) throws IOException, TimeoutException {
		//获取连接
		Connection connection = ConnectionUtils.getConnection();

		//获取通道
		final Channel channel = connection.createChannel();

		//声名队列
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);

		channel.basicQos(1);//保证一次只分发一个

		//定义一个消费者
		DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
			//消息到达触发这个方法
			@Override
			public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope, com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
				String msg = new String(body,"utf-8");
				System.out.println("消费者1 接受的消息"+msg);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					System.out.println("done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		boolean autoAck = false ; //自动应答改成false
		channel.basicConsume(QUEUE_NAME, autoAck, defaultConsumer);
	}
}
