package com.swinburne.irtsa.irtsa.server;

import com.google.gson.Gson;

import io.reactivex.Observable;

/**
 * The Server class is a singleton that initiates and manages a persistent connection to the
 * micro-controller. The actual connection logic is contained within the Connection class.
 */
public final class Server {
  private static final Gson gson = new Gson();
  private static final Server instance = new Server();

  private Connection connection = new Connection("ws://10.0.0.1:8765");

  /**
   * Observable of the status of the connection.
   */
  public static Observable<Status> status = instance.connection.status;

  /**
   * A message observable constructed from the string observable contained on the
   * connection instance.
   */
  public static MessageObservable messages = new MessageObservable(instance.connection.messages);

  /**
   * Starts the persistent connection to the microcontroller. You should only need to call this once
   * when your app starts.
   */
  public static void connect() {
    instance.connection.pollForConnection();
  }

  /**
   * Tells the connection to send a Message instance to the server.
   * @param message Message instance to send to the server.
   * @param <T> Type of message.
   */
  public static <T extends Message> void send(T message) {
    instance.connection.send(gson.toJson(message));
  }

  /**
   * Utility function for getting the current status in case you don't want to use the status
   * observable.
   * @return The current status
   */
  public static Status getStatus() {
    return instance.connection.getStatus();
  }
}
