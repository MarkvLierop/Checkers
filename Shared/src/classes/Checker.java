package classes;

import enums.CheckerType;

import java.io.Serializable;

public class Checker implements Serializable
{
    private CheckerType checkerType;

    public CheckerType getCheckerType() {
        return checkerType;
    }

    public void setCheckerType(CheckerType checkerType) {
        this.checkerType = checkerType;
    }
}
