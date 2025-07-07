import java.awt.*;

public enum Trails {
    NONE(1000, new Color(255, 0, 255)), OWN(1001, new Color(0, 0, 255)), OWN_PASSABLE(1002, new Color(0, 255, 255)), OWN_ROOMLESS(1003, new Color(0, 255, 128)), ENEMY(1004, new Color(255, 0, 0));

    private final int value;
    private final Color color;

    Trails(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public static Trails getTrail(int value) {
        for (Trails t : values()) {
            if (t.getValue() == value) {
                return t;
            }
        }
        return NONE; // or throw an exception if not found
    }
}
