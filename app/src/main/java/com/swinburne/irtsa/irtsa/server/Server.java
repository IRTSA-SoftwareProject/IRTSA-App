package com.swinburne.irtsa.irtsa.server;

import com.google.gson.Gson;

import io.reactivex.Observable;

public final class Server {
  private static final Gson gson = new Gson();
  private static final Server instance = new Server();

  private Connection connection = new Connection("ws://192.168.162.132:8765");

  public static Observable<Status> status = instance.connection.status;
  public static MessageObservable messages = new MessageObservable(instance.connection.messages);

  public static void connect() {
    instance.connection.pollForConnection();
  }

  public static <T extends Message> void send(T message) {
    instance.connection.send(gson.toJson(message));
  }

  public static Status getStatus() {
    return instance.connection.getStatus();
  }
}
