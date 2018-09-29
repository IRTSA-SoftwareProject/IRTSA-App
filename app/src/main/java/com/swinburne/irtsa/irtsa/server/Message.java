package com.swinburne.irtsa.irtsa.server;

import com.google.gson.Gson;

import java.util.AbstractMap;
import java.util.HashMap;

public class Message {
  private static final Gson gson = new Gson();

  public String type;
  public Object body;

  public Message() { }

  public Message(String type, Object body) {
    this.type = type;
    this.body = body;
  }

  public static Message fromJson(String message) {
    return gson.fromJson(message, Message.class);
  }

  public String toJson() {
    return gson.toJson(this);
  }

  public AbstractMap getBodyHash() {
    return (AbstractMap) this.body;
  }
}
