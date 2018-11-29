package domain.enums;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import domain.interfaces.IToJson;

public enum  PlayerNumber implements IToJson {
    ONE,
    TWO;

    public String toJson()
    {
        return new Gson().toJson(this);
    }
}
