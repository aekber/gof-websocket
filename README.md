# About

Game of three with web socket by Ali Ekber Celik.This application developed under Ubuntu 16.04 x64 OS with Java 8.


Technology stack is;

*Java 8
*Tyrus(Java WebSocket reference implementation)
*Maven
*Junit


## Build from source

$> mvn clean install


## Start the server

Open a terminal and run the following command in the `target` directory:


$> java -cp "aekber-lieferando-1.0-SNAPSHOT.jar:lib/*" com.eko.lieferando.assignment.player1.Player1


_If you are on Windows, make sure to change the classpath separator to ; instead of :_


## Launch a client

Open a second terminal and run the following command in the `target` directory:


$> java -cp "aekber-lieferando-1.0-SNAPSHOT.jar:lib/*" com.eko.lieferando.assignment.player2.Player2


_If you are on Windows, make sure to change the classpath separator to ; instead of :_
