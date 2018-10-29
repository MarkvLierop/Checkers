package classes;

import enums.CheckerType;

import java.io.Serializable;

public class Checker implements Serializable, Comparable
{
    private CheckerType checkerType;
    private int location;

    public CheckerType getCheckerType() {
        return checkerType;
    }
    public void setCheckerType(CheckerType checkerType) {
        this.checkerType = checkerType;
    }

    public int getLocation() {
        return location;
    }
    public void setLocation(int location) {
        this.location = location;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
