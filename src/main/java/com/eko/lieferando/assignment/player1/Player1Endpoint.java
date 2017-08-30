package com.eko.lieferando.assignment.player1;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.eko.lieferando.assignment.model.Message;
import com.eko.lieferando.assignment.model.MessageDecoder;
import com.eko.lieferando.assignment.model.MessageEncoder;
import com.eko.lieferando.assignment.util.NumberEngine;

@ServerEndpoint(value = "/got", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class Player1Endpoint {

	@OnOpen
	public void onOpen(Session session) throws IOException, EncodeException {
		System.out.println("\nServer started");
		
		Random random = new Random();
		int n = random.nextInt(1000) + 1;
		System.out.println("Generated number: " + n);

		if (n == 1) {
			System.out.println("You win !!!");
			session.close();
			return;
		}

		Message msg = new Message();
		msg.setSender("Server");
		msg.setReceived(new Date());
		msg.setContent(Integer.toString(n));
		
		session.getBasicRemote().sendObject(msg);

	}

	@OnMessage
	public void onMessage(Message message, Session session) throws IOException, EncodeException {
		
		if (message.getContent().equals("OK")) {
			System.out.println("You lost !!!");
			session.close();
			return;
		}

		int current = Integer.parseInt(message.getContent());
		System.out.println("Received number: " + current);
		
		Message msg = new Message();
		msg.setSender("Server");
		msg.setReceived(new Date());

		int next = NumberEngine.getNextNumber(current);
		
		if (next == 1) {
			System.out.println("You win !!!");
			msg.setContent("OK");
			session.getBasicRemote().sendObject(msg);
			session.close();
			return;
		}
		
		msg.setContent(Integer.toString(next));
		session.getBasicRemote().sendObject(msg);
	}

	@OnClose
	public void onClose(Session session) throws IOException, EncodeException {
		System.out.println("Server closed...\n");

	}

}