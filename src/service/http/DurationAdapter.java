package service.http;

//адаптер для сериализации/десериализации Duration в JSON без этого падало в инсомнии в 500(((
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toSeconds());
        }
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            return null;
        }
        return Duration.ofSeconds(in.nextLong());
    }
}

