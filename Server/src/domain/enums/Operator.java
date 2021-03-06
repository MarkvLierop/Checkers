package domain.enums;

public enum Operator {
    ADDITION("+") {
        @Override public int apply(int value1, int value2) {
            return value1 + value2;
        }
    },
    SUBTRACTION("-") {
        @Override public int apply(int value1, int value2) {
            return value1 - value2;
        }
    },
    BIGGER_THAN(">"){
        @Override public int apply(int value1, int value2) {
            return (value1 > value2) ? 1 : 0;
        }
    },
    SMALLER_THAN("<"){
        @Override public int apply(int value1, int value2) {
            return (value1 < value2) ? 1 : 0;
        }
    };

    private final String text;

    Operator(String text) {
        this.text = text;
    }

    public abstract int apply(int value1, int value2);

    @Override public String toString() {
        return text;
    }
}
