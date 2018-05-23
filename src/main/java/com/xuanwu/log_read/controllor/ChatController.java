package com.xuanwu.log_read.controllor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xuanwu.log_read.entity.ChatMessage;
import com.xuanwu.log_read.entity.ChatMessage.MessageType;
import com.xuanwu.log_read.task.LogTask;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Controller
public class ChatController {

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		return chatMessage;
	}

	private Executor executor = Executors.newCachedThreadPool();

	@MessageMapping("/log.readLog")
	public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		LogTask task = new LogTask(chatMessage.getSender(), messagingTemplate, "foo");
		executor.execute(task);
		headerAccessor.getSessionAttributes().put("task", task);
	}

	@RequestMapping(value = "/send2user")
	@ResponseBody
	public ChatMessage sendMessage(String name) {
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setType(MessageType.CHAT);
		chatMessage.setContent("hello");
		chatMessage.setSender(name);
		messagingTemplate.convertAndSendToUser(name, "/queue/log", chatMessage);
		return chatMessage;
	}

}
