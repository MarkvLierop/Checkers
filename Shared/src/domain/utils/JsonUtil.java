package domain.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import domain.enums.Action;
import domain.enums.PlayerNumber;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {
    private JsonUtil() {

    }

    public static Object getObjectFromArray(String jsonString, Class classType) {
        return new Gson().fromJson(jsonString, classType);
    }
}
