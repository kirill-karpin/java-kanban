package com.tracker.result;

import java.util.ArrayList;
import java.util.Collection;

public class Result {

  private final Collection<String> messages = new ArrayList<>();
  private Boolean success = true;
  private Object data;

  public Object getData() {
    return data;
  }

  public Boolean isSuccess() {
    return success;
  }

  public void addError(String message) {
    success = false;
    messages.add(message);
  }

  public String getErrorMessage() {
    return String.join("\n", messages);
  }

  public void setData(Object data) {
    this.data = data;
  }
}
