package com.tracker.server;

import com.sun.net.httpserver.HttpExchange;
import com.tracker.exception.RequestException;

public interface HandlerInterface {

  HandlerResult doRequest(HttpExchange exchange) throws RequestException;
}
