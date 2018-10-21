package com.swinburne.irtsa.irtsa.server;

import android.util.Log;

import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.operators.observable.ObservableFilter;

import java.util.Objects;

/**
 * The MessageObservable class provides some utilities for interacting with a stream of messages. It
 * handles parsing and casting plain strings from the server into Message instances that can be used
 * in the app. In normal use, you wouldn't normally need to construct the MessageObservable yourself
 * as one is already created on the Server class.
 */
public class MessageObservable extends Observable<Message> {
  private static final Gson gson = new Gson();

  private final Observable<String> messages;

  /**
   * The constructor that allows this class to convert a normal stream of messages into a stream
   * of Messages objects.
   * @param messages Observable of stringified JSON objects.
   */
  MessageObservable(Observable<String> messages) {
    this.messages = messages;
  }

  /**
   * Filters messages by a particular type. The resulting messages are still untyped which means
   * you cannot access the message body. If you would like to cast the messages at the same time,
   * try using {@link MessageObservable#castToType}.
   * @param type Message type to filter by
   * @return An observable of untyped messages that match the given type
   */
  public Observable<Message> ofType(String type) {
    return new ObservableFilter<>(this, isOfType(type));
  }

  /**
   * Filters and types messages based on a string type and a class. The class you provide has to
   * extend the {@link Message} class and is used to parse the Json string.
   * @param type Message type to filter by
   * @param classOfT Message class to be instantiated
   * @param <T> Message type
   * @return An observable of the message T
   */
  public <T extends Message> Observable<T> castToType(String type, Class<T> classOfT) {
    return new ObservableFilter<>(this.constructObservable(classOfT), isOfType(type));
  }

  @Override
  protected void subscribeActual(Observer<? super Message> observer) {
    constructObservable(Message.class).subscribe(observer);
  }

  /**
   * Constructs an {@link Message} observable from a JSON string observable.
   * @return An observable of {@link Message} objects
   */
  private <T extends Message> Observable<T> constructObservable(Class<T> classOfT) {
    return messages
        .map(json -> gson.fromJson(json, classOfT))
        .onErrorResumeNext(throwable -> {
          Log.e("SERVER", "Failed to parse message", throwable);
          return constructObservable(classOfT);
        });
  }

  /**
   * Utility function to build a predicate that will filter messages by their type.
   * @param type The message type to filter by.
   * @return A function that will return true when given a message with a matching type.
   */
  private Predicate<Message> isOfType(String type) {
    return message -> Objects.equals(message.type, type);
  }
}
