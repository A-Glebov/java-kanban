package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationTypeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter out, Duration duration) throws IOException {
        if (duration == null) {
            out.nullValue();
        } else {
            // Сериализуем Duration в строку, например, в формате ISO-8601
            out.value(duration.toString());
        }
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            in.nextNull();
            return null;
        } else {
            // Десериализуем строку обратно в Duration
            String durationString = in.nextString();
            return Duration.parse(durationString);
        }
    }
}

/*import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationSerializer implements JsonSerializer<Duration> {
    @Override
    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext context) {
        return context.serialize(duration.toString());
    }

   /* @Override
    public JsonElement serialize(Duration duration, Type typeOfSrc, JsonSerializationContext context) {
        // Преобразуем Duration в строку формата ISO-8601
        return context.serialize(duration.toString());
    }
}*/
