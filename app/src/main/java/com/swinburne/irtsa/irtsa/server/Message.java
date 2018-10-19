package com.swinburne.irtsa.irtsa.server;

import com.google.gson.Gson;

/**
 * This is a simple base class for messages. Messages sent to the server should extend this class
 * and provide a type value.
 */
public class Message {
  private static final Gson gson = new Gson();

  /**
   * The type is used to determine what action to take with each message, and is required.
   */
  public String type;

  /**
   * A default constructor is required to unserialize using GSON.
   */
  public Message() { }
}
