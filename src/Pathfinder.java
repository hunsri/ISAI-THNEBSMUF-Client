public class Pathfinder {

    private Field field;

    private TileNodeTree tree;

    public Pathfinder(Field field, Position start, Position end) {
        this.field = field;
        tree = new TileNodeTree(this, start, end);
        tree.buildTree();
    }

    public boolean canStepAt(int posX, int posY) {
        int fieldID = field.getFieldAreaId(posX, posY);

        //IDs between those values signal it is ok to step there 
        if(fieldID > 0 && fieldID < 1000) {
            return true;
        }
        return false;
    }

    public void markDebug(int x, int y) {
        field.markDebug(x, y);
    }

}
