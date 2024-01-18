package com.chensoul.rabbitmq.client.simplequeue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Sender {
	public static void main(String[] args) throws Exception {
		test1();

		test2();
	}

	public static void test1() throws IOException, TimeoutException {
		//	1 创建ConnectionFactory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("123456");

		//	2 创建Connection
		Connection connection = connectionFactory.newConnection();
		//	3 创建Channel
		Channel channel = connection.createChannel();
		//	4 声明
		String queueName = "simple.queue";
		//	参数: queue名字,是否持久化,独占的queue（仅供此连接）,不使用时是否自动删除, 其他参数
		channel.queueDeclare(queueName, false, false, false, null);

		for (int i = 0; i < 5; i++) {
			String msg = "Hello World RabbitMQ " + i;
			channel.basicPublish("", queueName, null, msg.getBytes());
		}
	}

	public static void test2() throws IOException, TimeoutException {
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

		Map<String, Object> headers = new HashMap<>();
		headers.put("my1", "111");
		headers.put("my2", "222");

		AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
			.deliveryMode(2)
			.contentEncoding("UTF-8")
			.expiration("10000")
			.headers(headers)
			.build();

		//4 通过Channel发送数据
		for (int i = 0; i < 5; i++) {
			String msg = "Hello RabbitMQ!";
			//1 exchange   2 routingKey
			channel.basicPublish("", "simple.queue", properties, msg.getBytes());
		}

		//5 记得要关闭相关的连接
		channel.close();
		connection.close();
	}

}
