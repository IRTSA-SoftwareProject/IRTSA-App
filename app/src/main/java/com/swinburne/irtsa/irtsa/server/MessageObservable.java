package com.swinburne.irtsa.irtsa.server;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.internal.operators.observable.ObservableFilter;

import java.util.Objects;

public class MessageObservable extends Observable<Message> {
  private final Observable<String> messages;

  public MessageObservable(Observable<String> messages) {
    this.messages = messages;
  }

  public Observable<Message> ofType(String type) {
    return new ObservableFilter<>(this, message -> Objects.equals(message.type, type));
  }

  @Override
  protected void subscribeActual(Observer<? super Message> observer) {
    constructMessageObservable().subscribe(observer);
  }

  private Observable<Message> constructMessageObservable() {
    return messages
        .map(Message::fromJson)
        .onErrorResumeNext(throwable -> {
          Log.e("SERVER", "Failed to parse message");
          return constructMessageObservable();
        });
  }
}
