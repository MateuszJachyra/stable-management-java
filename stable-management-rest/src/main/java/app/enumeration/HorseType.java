package app.enumeration;

public enum HorseType {
    COLDBLOODED(1), WARMBLOODED(2);

    private int type;

    HorseType(int type) {
        this.type = type;
    }
}
