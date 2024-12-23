package com.tracker.server;

import com.tracker.result.Result;

public class HandlerResult extends Result {

  private Integer code = 200;


  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

}
