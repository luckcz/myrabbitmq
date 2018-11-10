package com.kaicom.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class Recv {
	private static final String QUEUE_NAME = "test_simple_queue";
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		//获取连接
		Connection conn = ConnectionUtils.getConnection();

		//创建频道
		Channel createChannel = conn.createChannel();

		//队列声名
		createChannel.queueDeclare(QUEUE_NAME, false, false, false, null);

		DefaultConsumer consumer = new DefaultConsumer(createChannel){
			@Override
			public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope, com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
				String msg = new String(body,"utf-8");
				System.out.println("new api recv:"+msg);
			}
		};
		//监听队列
		createChannel.basicConsume(QUEUE_NAME, true, consumer);
	}

	//老版本的api
	public static void oldRec() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException, TimeoutException{
		//获取连接
		Connection conn = ConnectionUtils.getConnection();

		//创建频道
		Channel createChannel = conn.createChannel();

		//定义队列的消费者
		QueueingConsumer consumer = new QueueingConsumer(createChannel);

		//监听队列
		createChannel.basicConsume(QUEUE_NAME, true, consumer);

		while(true){

			Delivery nextDelivery = consumer.nextDelivery();
			String getMsg = new String(nextDelivery.getBody());
			System.out.println("recive msg : "+getMsg);
		}
	}
}
