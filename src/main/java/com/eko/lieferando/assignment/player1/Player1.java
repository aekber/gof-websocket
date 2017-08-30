package com.eko.lieferando.assignment.player1;

import java.util.Scanner;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

public class Player1 {

    public static void main(String[] args) {

        Server server = new Server("localhost", 8026, "/ws", null, Player1Endpoint.class);

        try {
            server.start();
            System.out.println("Press any key to stop the server..");
            new Scanner(System.in).nextLine();
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

}
