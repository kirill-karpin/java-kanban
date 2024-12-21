package com.tracker.server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class LocalDateAdapter extends TypeAdapter<LocalDateTime> {

  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
    if (localDate != null) {
      jsonWriter.value(localDate.format(dtf));
    } else {
      jsonWriter.nullValue();
    }

  }

  @Override
  public LocalDateTime read(JsonReader jsonReader) throws IOException {
    if (jsonReader.peek() == JsonToken.NULL) {
      jsonReader.nextNull();
      return null;
    }
    return LocalDateTime.parse(jsonReader.nextString(), dtf);
  }
}
