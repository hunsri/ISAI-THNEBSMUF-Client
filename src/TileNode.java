public class TileNode {
    private TileNode parent = null;

    private int posX;
    private int posY;

    private Direction facing;

    public TileNode(TileNode parent, int posX, int posY, Direction facing) {
        this.parent = parent;
        
        this.posX = posX;
        this.posY = posY;

        this.facing = facing;
    }

    public TileNode(TileNode parent, Position p, Direction facing) {
        this.parent = parent;
        
        this.posX = p.x;
        this.posY = p.y;

        this.facing = facing;
    }

    public Position getPosition() {
        return new Position(posX, posY);
    }

    public Direction getFacing() {
        return facing;
    }

    public TileNode getParentNode() {
        return parent;
    }

    @Override
    public String toString() {
        return "TileNode{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", facing=" + facing +
                ", parent=" + (parent != null ? parent.getPosition() : "null") +
                '}';
    }
}
