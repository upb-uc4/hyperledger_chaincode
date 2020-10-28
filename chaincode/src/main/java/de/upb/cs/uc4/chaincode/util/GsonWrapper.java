package de.upb.cs.uc4.chaincode.util;

import com.google.gson.*;
import de.upb.cs.uc4.chaincode.model.Dummy;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

import java.io.Reader;
import java.lang.reflect.Type;

public class GsonWrapper {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    LocalDate.class,
                    (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> {
                        try {
                            return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
                        } catch (DateTimeParseException e) {
                            return null;
                        }
                    })
            .registerTypeAdapter(
                    LocalDate.class,
                    (JsonSerializer<LocalDate>) (date, typeOfSrc, context) -> {
                        return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
                    })
            .registerTypeAdapter(
                    Integer.class,
                    (JsonDeserializer<Integer>) (json, type, jsonDeserializationContext) -> {
                        try {
                            return json.getAsInt();
                        } catch (RuntimeException e) {
                            return null;
                        }
                    })
            .registerTypeAdapter(
                    Dummy.class,
                    (JsonSerializer<Dummy>) (dummy, typeOfSrc, context) -> {
                        return new JsonPrimitive(dummy.getContent()); // "yyyy-mm-dd"
                    })
            .registerTypeAdapter(
                    Dummy.class,
                    (JsonDeserializer<Dummy>) (json, type, jsonDeserializationContext) -> {
                        try {
                            String s = json.toString();
                            if (s.charAt(0) == '"') {
                                s = s.substring(1, s.length()-1);
                            }
                            return new Dummy(s);
                        } catch (RuntimeException e) {
                            return null;
                        }
                    })
            .registerTypeAdapter(
                    String.class,
                    (JsonDeserializer<String>) (json, type, jsonDeserializationContext) ->
                            Jsoup.clean(json.getAsJsonPrimitive().getAsString(), Whitelist.none()))
            .create();

    public static <T> T fromJson(String json, Class<T> t) throws JsonSyntaxException {
        return gson.fromJson(json, t);
    }

    public static <T> String toJson(T object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(Reader reader, Type type) {
        return gson.fromJson(reader, type);
    }

    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }
}