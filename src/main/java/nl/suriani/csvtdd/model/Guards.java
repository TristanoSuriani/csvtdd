package nl.suriani.csvtdd.model;

public interface Guards {
    static void isNotNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Value is required");
        }
    }

    static void isNotEmpty(String s) {
        if (s.trim().isEmpty()) {
            throw new IllegalArgumentException("Value cannot be empty");
        }
    }

    static void isZeroOrPositive(Integer i) {
        if (i < 0) {
            throw new IllegalArgumentException("Value should be > 0 but is '" + i + "'");
        }
    }
}
