package com.tracker.server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Duration;

class DurationAdapter extends TypeAdapter<Duration> {

  @Override
  public void write(JsonWriter out, Duration value) throws IOException {
    if (value != null) {
      out.value(value.toMinutes());
    } else {
      out.value(0);
    }

  }

  @Override
  public Duration read(JsonReader in) throws IOException {
    String value = in.nextString();
    if (value != null) {
      return Duration.ofMinutes(Integer.parseInt(value));
    }
    return null;
  }

}
