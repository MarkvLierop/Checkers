package domain.enums;

import com.google.gson.Gson;

public enum  PlayerNumber {
    ONE,
    TWO;

    public String toJson()
    {
        return new Gson().toJson(this);
    }
}
