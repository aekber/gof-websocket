package com.eko.lieferando.assignment.player2;

import java.io.IOException;
import java.util.Date;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.eko.lieferando.assignment.model.Message;
import com.eko.lieferando.assignment.model.MessageDecoder;
import com.eko.lieferando.assignment.model.MessageEncoder;
import com.eko.lieferando.assignment.util.NumberEngine;

@ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class Player2Endpoint {

	private Session userSession = null;

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Client connection established");
		this.userSession = session;
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("Client connection closed");
		this.userSession = null;
	}

	@OnMessage
	public void onMessage(Message message) throws IOException {

		if (message.getContent().equals("OK")) {
			System.out.println("You lost !!!");
			cleanupAndShutdown(0);
			return;
		}

		int current = Integer.parseInt(message.getContent());
		System.out.println("Received number: " + current);

		Message msg = new Message();
		msg.setSender("Client");
		msg.setReceived(new Date());

		int next = NumberEngine.getNextNumber(current);
		
		if (next == 1) {
			System.out.println("You win !!!!!");
			msg.setContent("OK");
			sendMessage(msg);
			cleanupAndShutdown(0);
			return;
		}
				
		msg.setContent(Integer.toString(next));
		sendMessage(msg);
	}

	private void cleanupAndShutdown(int exitStatus) throws IOException {
		this.userSession.close();
		//System.exit(exitStatus);
	}

	public void sendMessage(Message message) {
		this.userSession.getAsyncRemote().sendObject(message);
	}

}