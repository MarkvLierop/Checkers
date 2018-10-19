package enums;

public enum Operator {
    ADDITION("+") {
        @Override public int apply(int x1, int x2) {
            return x1 + x2;
        }
    },
    SUBTRACTION("-") {
        @Override public int apply(int x1, int x2) {
            return x1 - x2;
        }
    };

    private final String text;

    Operator(String text) {
        this.text = text;
    }

    public abstract int apply(int x1, int x2);

    @Override public String toString() {
        return text;
    }
}
