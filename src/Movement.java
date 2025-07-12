public enum Movement {
    STRAIGHT, LEFT, RIGHT;

    public static Movement directionToMovement(Direction facingDirection, Direction desiredDirection) {
        // Calculate the relative direction: 0 = straight, 1 = right, 3 = left (modulo 4 handles wrap-around)
        int diff = (desiredDirection.ordinal() - facingDirection.ordinal() + 4) % 4;

        switch (diff) {
            case 0: return STRAIGHT;
            case 1: return LEFT;
            case 3: return RIGHT;
            default: throw new IllegalArgumentException("Invalid movement: cannot turn backwards");
        }
    }
}
