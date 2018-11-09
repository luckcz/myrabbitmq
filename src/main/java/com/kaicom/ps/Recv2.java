package com.kaicom.ps;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv2 {
	private static final String QUEUE_NAME = "test_queue_fanout_msm";
	private static final String EXCHANGE_NAME = "test_exchange_fanout";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection conn = ConnectionUtils.getConnection();
		final Channel channel = conn.createChannel();
		//声名队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//绑定队列到交换机转发器
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

		channel.basicQos(1);//一次只分发一个消息

		DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				// TODO Auto-generated method stub
				String getMsg = new String (body,"utf-8");
				System.out.println("[2] Reciv msg :"+getMsg);

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					System.out.println("[2] done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};

		boolean autoAct = false ; //自动应答 false

		channel.basicConsume(QUEUE_NAME,autoAct, defaultConsumer);
	}
}
