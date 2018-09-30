package com.swinburne.irtsa.irtsa.server;

import android.os.Handler;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

class Connection extends WebSocketListener {
  private static final int POLL_INTERVAL = 5000;
  private static final int NORMAL_CLOSURE_STATUS = 1000;

  private final WebSocketListener listener = this;

  private OkHttpClient client = new OkHttpClient();
  private Request request;
  private Handler handler = new Handler();
  private WebSocket socket;

  private PublishSubject<String> messagesSubject = PublishSubject.create();
  private BehaviorSubject<Status> statusSubject =
          BehaviorSubject.createDefault(Status.NOT_CONNECTED);

  public Observable<String> messages = messagesSubject;
  public Observable<Status> status = statusSubject;


  Connection(String url) {
    request = new Request.Builder().url(url).build();
  }

  public void pollForConnection() {
    statusSubject.onNext(Status.CONNECTING);
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (getStatus() == Status.CONNECTING) {
          Log.i("CONNECTION", "Attempting to make connection");
          client.newWebSocket(request, listener);
          handler.postDelayed(this, POLL_INTERVAL);
        }
      }
    }, POLL_INTERVAL);
  }

  public void send(String message) {
    if (getStatus() == Status.CONNECTED && socket != null) {
      Log.i("CONNECTION", "Sending message " + message);
      socket.send(message);
    } else {
      Log.i("CONNECTION",
              String.format("Skipped sending message because connection was not open (%s)",
                      message));
    }
  }

  @Override
  public void onOpen(WebSocket socket, Response response) {
    Log.i("CONNECTION", "Connection opened");
    statusSubject.onNext(Status.CONNECTED);
    this.socket = socket;
  }

  @Override
  public void onClosing(WebSocket socket, int code, String reason) {
    Log.i("CONNECTION", "Connection closed normally");
    socket.close(NORMAL_CLOSURE_STATUS, null);
    statusSubject.onNext(Status.CLOSED);
  }

  @Override
  public void onFailure(WebSocket socket, Throwable t, Response response) {
    if (getStatus() == Status.CONNECTED) {
      Log.e("CONNECTION", "Connection failed", t);
      statusSubject.onNext(Status.NOT_CONNECTED);
      this.socket = null;

      // Attempt to reconnect
      this.pollForConnection();
    }
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    Log.i("CONNECTION", "Received message: " + text);
    messagesSubject.onNext(text);
  }

  public Status getStatus() {
    return statusSubject.getValue();
  }
}
