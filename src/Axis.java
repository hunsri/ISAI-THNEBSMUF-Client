public enum Axis {
    X, Y;

    /**
     * Returns the axis for the given direction.
     * 
     * @param d The direction whose axis is wanted
     * @return the axis
     */
    public static Axis getAxisOf(Direction d) {
        switch (d) {
            case NORTH:
                return Axis.Y;
            case SOUTH:
                return Axis.Y;
            case WEST:
                return Axis.X;
            case EAST:
                return Axis.X;
            default:
                return Axis.X; //to satisfy switch statement
        }
    }
}
