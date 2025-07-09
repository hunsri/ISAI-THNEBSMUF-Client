public enum Direction {
    NORTH, WEST, SOUTH, EAST;

    // public steerTowards() TODO

    public static Direction getOpposite(Direction direction) {
        switch(direction) {
            case NORTH:
                return SOUTH;
            case WEST:
                return EAST;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            default:
                return EAST;
        }
    }
}
