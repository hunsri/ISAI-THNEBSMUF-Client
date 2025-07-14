import java.util.ArrayList;
import java.util.LinkedList;

public class Pathfinder {

    private int consumptionCounter = 0;

    private Field field;

    private TileNodeTree tree;

    private Bot bot;

    private DestinationFinder destinationFinder;

    // contains the route that the pathfinder found
    private LinkedList<TileNode> plannedRoute = new LinkedList<TileNode>();

    // and as map representation, used to signal other pathfinders where they can't go
    // true represents that the tile is in use
    private boolean[][] plannedRouteOnMap = new boolean[Field.SIZE][Field.SIZE];

    public static ArrayList<Pathfinder> teamedFinders = new ArrayList<Pathfinder>();

    public Pathfinder(Bot bot, Field field) {
        this.bot = bot;
        this.field = field;

        destinationFinder = new DestinationFinder(bot, field);
        tree = new TileNodeTree(this, bot.getPosition(), destinationFinder.getDestination());
        tree.buildTree();

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

    public Move getNextMove() {
        Move ret = new Move(bot.getBotType(), 0);

        TileNode nextTile = nextDirection();
        
        if(nextTile == null) {
            return null;
        }
        consumptionCounter += 1; 

        Direction vec;
        try {
            vec = Direction.vectorDirection(bot.getPosition(), nextTile.getPosition());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }

        // move back
        if(Direction.getOpposite(bot.getFacingDirection()) == vec) {
            System.err.println("Attempted to move backwards!");
            return null;
        }

        // moving straight 
        if(vec == bot.getFacingDirection()) {
            if(bot.getFacingDirection() == Direction.NORTH || bot.getFacingDirection() == Direction.WEST) {
                ret.moveAt = 0;
            } else {
                ret.moveAt = Field.SIZE-1;
            }

            MoveChecker.doesMoveReachDestination(bot, ret.moveAt, nextTile.getPosition());
            return ret;
        }

        // moving to the sides

        // if we are facing a positive direction apply 1, otherwise -1
        int step = (bot.getFacingDirection() == Direction.SOUTH || bot.getFacingDirection() == Direction.EAST ? 1: -1);

        int myPointOnAxis;

        if(Axis.getAxisOf(bot.getFacingDirection())== Axis.X) {
            myPointOnAxis = bot.getPosition().x;
        } else {
            myPointOnAxis = bot.getPosition().y;
        }

        // if the moving vector is positive apply 1, otherwise -1
        int mult = (vec == Direction.SOUTH || vec == Direction.EAST ? 1: -1);

        ret.moveAt = (myPointOnAxis + step) * mult;
        MoveChecker.doesMoveReachDestination(bot, ret.moveAt, nextTile.getPosition());
        return ret;
    }

    private TileNode nextDirection() {
        if(plannedRoute.size()>0) {
            return plannedRoute.removeLast();
        } else {
            return null;
        }
    }

    public void markDebug(int x, int y) {
        field.markDebug(x, y);
    }

    /**
     * Attempting to find a new path to the target.
     * Target gets determined automatically.
     * 
     * @param end
     */
    public void refresh(boolean planAhead) {
        consumptionCounter = 0;
        plannedRoute.clear();
        plannedRouteOnMap = new boolean[Field.SIZE][Field.SIZE];

        Position pos = planAhead ? bot.getPositionAhead() : bot.getPosition();

        destinationFinder.updateDestination();
        tree = new TileNodeTree(this, pos, destinationFinder.getDestination());
        tree.buildTree();
    }

    /**
     * Returns how many steps of the path have been consumed
     * Counter gets incremented each time {@link #getNextMove()} is called
     *  
     * @return Steps taken since the start of the calculation
     */
    public int getConsumptionCounter() {
        return consumptionCounter;
    }

}
