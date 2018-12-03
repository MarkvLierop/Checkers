package domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import domain.game.Move;

import java.lang.reflect.Type;

public class MoveSerializer implements JsonSerializer<Move> {
    @Override
    public JsonElement serialize(Move move, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}
