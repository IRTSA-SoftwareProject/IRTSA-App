package com.swinburne.irtsa.irtsa.server;

import io.reactivex.Observable;

public final class Server {
  private static final Server instance = new Server();

  private Connection connection = new Connection("ws://192.168.43.121:8765");

  public static Observable<Connection.Status> status = instance.connection.status;
  public static MessageObservable messages = new MessageObservable(instance.connection.messages);

  public static void connect() {
    instance.connection.pollForConnection();
  }

  public static void send(Message message) {
    instance.connection.send(message.toJson());
  }

  public static Connection.Status getStatus() {
    return instance.connection.getStatus();
  }
}
