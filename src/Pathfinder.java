import java.util.ArrayList;
import java.util.LinkedList;

public class Pathfinder {

    private Field field;

    private TileNodeTree tree;

    private Direction botFacingDirection;

    // contains the route that the pathfinder found
    private LinkedList<TileNode> plannedRoute = new LinkedList<TileNode>();

    // and as map representation, used to signal other pathfinders where they can't go
    // true represents that the tile is in use
    private boolean[][] plannedRouteOnMap = new boolean[Field.SIZE][Field.SIZE];

    public static ArrayList<Pathfinder> teamedFinders = new ArrayList<Pathfinder>();

    public Pathfinder(BotType botType, Field field, Position start, Position end) {
        this.field = field;
        tree = new TileNodeTree(this, start, end);
        tree.buildTree();

        botFacingDirection = Direction.EAST; //all bots start facing east

        teamedFinders.add(this);
    }

    public boolean canStepAt(int posX, int posY) {
        int fieldID = field.getFieldAreaId(posX, posY);

        // IDs between those values signal it is ok to step there 
        if(!(fieldID > 0 && fieldID < 1000)) {
            return false;
        }

        // Another pathfinder using a tile prohibits us from going there
        for(int i = 0; i < teamedFinders.size(); i++) {
            if(teamedFinders.get(i).plannedRouteOnMap[posX][posY] == true) {
                return false;
            }
        }

        return true;        
    }

    public void addToPlannedRoute(TileNode tileNode) {
        plannedRoute.add(tileNode);
        plannedRouteOnMap[tileNode.getPosition().x][tileNode.getPosition().y] = true;
    }

    // public int nextMovement() {
    //TODO
    // }

    private Direction nextDirection() {
        if(plannedRoute.size()>0) {
            return plannedRoute.pop().getFacing();
        } else {
            return Direction.EAST;
        }
    }

    public void markDebug(int x, int y) {
        field.markDebug(x, y);
    }

    public void refresh(Position start, Position end) {
        plannedRoute.clear();
        plannedRouteOnMap = new boolean[Field.SIZE][Field.SIZE];

        tree = new TileNodeTree(this, start, end);
        tree.buildTree();
    }

}
