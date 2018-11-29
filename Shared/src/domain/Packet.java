package domain;

import domain.enums.Action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Packet implements Serializable {
    private Action action;
    private String[] parameters;

    public Packet(Action action, String[] parameters)
    {
        this.action = action;
        this.parameters = parameters;
    }

    public Action getAction() {
        return action;
    }

    public String[] getParameters() {
        return parameters;
    }
}
