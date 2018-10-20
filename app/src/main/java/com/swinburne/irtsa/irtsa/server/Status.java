package com.swinburne.irtsa.irtsa.server;

/**
 * Represents the status of the connection to the server.
 */
public enum Status {
  NOT_CONNECTED,
  CONNECTING,
  CONNECTED,
  CLOSED;

  @Override
  public String toString() {
    switch (this) {
      case NOT_CONNECTED: return "Not Connected";
      case CONNECTING: return "Connecting";
      case CONNECTED: return "Connected";
      case CLOSED: return "Closed";
      default: return "undefined";
    }
  }
}
