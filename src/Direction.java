public enum Direction {
    NORTH, WEST, SOUTH, EAST;

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

    public static Direction vectorDirection(Position a, Position b) {
        
        if (a.x == b.x) {
            if (a.y == b.y + 1) return NORTH;
            if (a.y == b.y - 1) return SOUTH;
        } else if (a.y == b.y) {
            if (a.x == b.x + 1) return WEST;
            if (a.x == b.x - 1) return EAST;
        }
        throw new IllegalArgumentException("Positions are not adjacent or not aligned. \nA: "+a.toString()+"\nB: "+b.toString());
    }
}
