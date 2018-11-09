package com.kaicom.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv2 {
	private static final String QUEUE_NAME = "test_queue_topic_2";
	private static final String EXCHANGE_NAME = "test_exchange_topic";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection conn = ConnectionUtils.getConnection();
		final Channel channel = conn.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);
		String routingKey = "goods.#";
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, routingKey);

		DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String getMsg = new String(body,"utf-8");
				System.out.println("[2]  recv :"+getMsg);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					System.out.println("[2]  done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		boolean autoAct = false ; //自动应答 false

		channel.basicConsume(QUEUE_NAME,autoAct, defaultConsumer);
	}
}
