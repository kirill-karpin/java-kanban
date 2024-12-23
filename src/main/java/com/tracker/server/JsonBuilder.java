package com.tracker.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Duration;
import java.time.LocalDateTime;

public class JsonBuilder {

  public static Gson build() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.serializeNulls();
    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
        .registerTypeAdapter(Duration.class, new DurationAdapter());
    return gsonBuilder.create();
  }
}
