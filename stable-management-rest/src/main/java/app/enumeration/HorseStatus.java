package app.enumeration;

public enum HorseStatus {
    HEALTHY(1), SICK(2), TRAINING(3), SOLD(4);

    private int status;

    HorseStatus(int status){
        this.status = status;
    }
}
