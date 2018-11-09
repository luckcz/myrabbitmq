package com.kaicom.confirm;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import com.kaicom.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

public class AyncSend {
	private static final String QUEUE_NAME = "QUEUE_simple_confirm_aync";

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection conn = ConnectionUtils.getConnection();
		Channel channel = conn.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		//生产者通过调用channel的confirmSelect方法将channel设置为confirm模式
		channel.confirmSelect();

		//定义一个没有handleAck的集合
		final SortedSet<Long> confirmSet = Collections.synchronizedNavigableSet(new TreeSet<Long>());

		channel.addConfirmListener(new ConfirmListener() {
			//每回调一次handleAck方法， unconfirm集合删掉相应的一条（multiple=false） 或多条（multiple=true）记录。  没有问题的
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				if(multiple){
					System.out.println("--multiple--");
					confirmSet.headSet(deliveryTag +1).clear();//用一个SortedSet, 返回此有序集合中小于end的所有元素。
				}else{
					System.out.println("--multiple false--");
					confirmSet.remove(deliveryTag);
				}

			}
			//有问题的返回
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("Nack, SeqNo: " + deliveryTag + ", multiple:" + multiple);
				if(multiple){
					System.out.println("--multiple--");
					confirmSet.headSet(deliveryTag +1).clear();//用一个SortedSet, 返回此有序集合中小于end的所有元素。
				}else{
					System.out.println("--multiple false--");
					confirmSet.remove(deliveryTag);
				}
			}
		});

		String msg = "Hello QUEUE !";
		int i = 1 ;
		while (true) {
			if(i == 19){
				int c = 1/0;
			}
			long nextSeqNo = channel.getNextPublishSeqNo();
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			confirmSet.add(nextSeqNo);
			if(i == 19){
				break;
			}
			i++;
		}
	}
}