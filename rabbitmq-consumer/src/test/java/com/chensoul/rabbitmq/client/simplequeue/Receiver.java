package com.chensoul.rabbitmq.client.simplequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Receiver {

	public static void main(String[] args) throws Exception {
		test1();

		test2();
	}

	public static void test1() throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("123456");

		Connection connection = connectionFactory.newConnection();

		Channel channel = connection.createChannel();

		String queueName = "simple.queue";
		//	durable 是否持久化消息
		channel.queueDeclare(queueName, false, false, false, null);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//	参数：队列名称、是否自动ACK、Consumer
		channel.basicConsume(queueName, true, consumer);
		//	循环获取消息
		while (true) {
			//	获取消息，如果没有消息，这一步将会一直阻塞
			Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("收到消息：" + msg);
		}
	}

	public static void test2() throws InterruptedException, IOException, TimeoutException {
		//1 创建一个ConnectionFactory, 并进行配置
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("123456");

		//2 通过连接工厂创建连接
		Connection connection = connectionFactory.newConnection();

		//3 通过connection创建一个Channel
		Channel channel = connection.createChannel();

		//4 声明（创建）一个队列
		String queueName = "simple.queue";
		channel.queueDeclare(queueName, true, false, false, null);

		//5 创建消费者
		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

		//6 设置Channel
		channel.basicConsume(queueName, true, queueingConsumer);

		while (true) {
			//7 获取消息
			Delivery delivery = queueingConsumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.err.println("消费端: " + msg);
			Map<String, Object> headers = delivery.getProperties().getHeaders();
			System.err.println("headers get my1 value: " + headers.get("my1"));
			//Envelope envelope = delivery.getEnvelope();
		}
	}
}
