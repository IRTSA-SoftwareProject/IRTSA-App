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

/**
 * The Connection class manages a low level websocket connection to the microcontroller. You
 * wouldn't normally need to instantiate this class directly. Instead prefer to use the Server
 * class at it provides a more easy to use interface.
 */
class Connection extends WebSocketListener {
  /**
   * How often (in ms) to try to reconnect while trying to open a socket.
   */
  private static final int POLL_INTERVAL = 5000;

  /**
   * Normal closure code. Used when the user initiates the closing of the socket.
   */
  private static final int NORMAL_CLOSURE_STATUS = 1000;

  /**
   * Reference to the listener that will be called on socket events. This class itself implements
   * the listener interface to make it easier to update the status.
   */
  private final WebSocketListener listener = this;

  /**
   * Instance of the OkHttpClient that will be used to open sockets.
   */
  private OkHttpClient client = new OkHttpClient();

  /**
   * Open socket request that is instantiated in the constructor.
   */
  private Request request;

  /**
   * Handler used to schedule reconnect attempts in the future.
   */
  private Handler handler = new Handler();

  /**
   * The currently open socket, or null if there isn't one.
   */
  private WebSocket socket;

  /**
   * Observable subject for messages received from the server.
   */
  private PublishSubject<String> messagesSubject = PublishSubject.create();

  /**
   * Observable subject for the current status of the connection. This uses a Behaviour subject so
   * it can track the current state of the connection.
   */
  private BehaviorSubject<Status> statusSubject =
          BehaviorSubject.createDefault(Status.NOT_CONNECTED);

  /**
   * Public messages observable. Downcast from the internal messages subject.
   */
  public Observable<String> messages = messagesSubject;

  /**
   * Public status observable. Downcast from the internal status subject.
   */
  public Observable<Status> status = statusSubject;


  /**
   * Constructs a new connection instance to the given url.
   * @param url The URL to connect the websocket to.
   */
  Connection(String url) {
    request = new Request.Builder().url(url).build();
  }

  /**
   * Starts attempting to connect to the server. Will retry indefinitely until a successful
   * connection is made.
   */
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

  /**
   * Sends string message to the server.
   * @param message The message to send. This should be a JSON string.
   */
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

  /**
   * Utility function for getting the current connection status in case you don't want to use the
   * observable.
   * @return The current connection status.
   */
  public Status getStatus() {
    return statusSubject.getValue();
  }
}
