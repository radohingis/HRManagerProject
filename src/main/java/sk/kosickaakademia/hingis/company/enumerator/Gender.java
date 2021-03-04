package sk.kosickaakademia.hingis.company.enumerator;

public enum Gender {
    male(0), female(1), other(2);

    private final int value;

    Gender(int value) {
        this.value  = value;
    }

    public int getValue() {
        return value;
    }
}