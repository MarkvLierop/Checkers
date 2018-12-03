package domain.utils;

import com.google.gson.*;

public class JsonUtil {
    private JsonUtil() {

    }

    public static Object getObjectFromArray(String jsonString, Class classType) {
        return new Gson().fromJson(jsonString, classType);
    }
}