package com.eko.lieferando.assignment.player2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.client.ClientManager;

public class Player2 {

	public static void main(String[] args) {
		ClientManager client = ClientManager.createClient();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Press any key to start...");
			scanner.nextLine();
			try {
				client.connectToServer(Player2Endpoint.class, new URI("ws://localhost:8026/ws/got"));
			} catch (DeploymentException | IOException | URISyntaxException e) {
				System.out.println("Connection problem detected,check your server...");
			}
		}

	}

}
