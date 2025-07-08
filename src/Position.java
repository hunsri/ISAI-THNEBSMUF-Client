public class Position {

    public int x;
    public int y;

    public Position() {

    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position getNeighbor(Direction direction){
        Position ret = new Position();
        
        switch(direction) {
            case NORTH:
                ret.x = x;
                ret.y = y-1;
                break;
            case WEST:
                ret.x = x-1;
                ret.y = y;
                break;
            case SOUTH:
                ret.x = x;
                ret.y = y+1;
                break;
            case EAST:
                ret.x = x+1;
                ret.y = y;
                break;
        }

        return ret;
    }

    @Override
    public String toString() {
        return "Position{" + "x=" + x + ", y=" + y + '}';
    }
}
