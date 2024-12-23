package com.tracker.exception;

public class RequestException extends Exception {

  private int code = 500;

  public RequestException(String message, int code) {
    super(message);
    this.code = code;
  }

  public RequestException(String message) {
    super(message);
  }

  public int getCode() {
    return code;
  }
}
