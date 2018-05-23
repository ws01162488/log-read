package com.xuanwu.log_read.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.xuanwu.log_read.entity.ChatMessage;
import com.xuanwu.log_read.entity.ChatMessage.MessageType;

public class LogTask implements Runnable {

	private String fileName;
	private SimpMessageSendingOperations messagingTemplate;
	private String user;
	private BufferedReader reader;
	private Process process;
	private volatile boolean stop;

	public LogTask(String fileName, SimpMessageSendingOperations messagingTemplate, String user) {
		this.fileName = fileName;
		this.messagingTemplate = messagingTemplate;
		this.user = user;
	}

	@Override
	public void run() {
		String command = "tail -f " + fileName;
		try {
			process = Runtime.getRuntime().exec(command);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String line;
			while (!stop && (line = reader.readLine()) != null) {
				// 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
				ChatMessage chatMessage = new ChatMessage();
				chatMessage.setType(MessageType.CHAT);
				chatMessage.setContent(line);
				chatMessage.setSender(user);
				messagingTemplate.convertAndSendToUser(user, "/queue/log", chatMessage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		stop = true;
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			process.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUser() {
		return user;
	}

}
