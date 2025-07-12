import java.util.ArrayList;
import java.util.LinkedList;

public class Pathfinder {

    private Field field;

    private TileNodeTree tree;

    private Bot bot;

    // contains the route that the pathfinder found
    private LinkedList<TileNode> plannedRoute = new LinkedList<TileNode>();

    // and as map representation, used to signal other pathfinders where they can't go
    // true represents that the tile is in use
    private boolean[][] plannedRouteOnMap = new boolean[Field.SIZE][Field.SIZE];

    public static ArrayList<Pathfinder> teamedFinders = new ArrayList<Pathfinder>();

    public Pathfinder(Bot bot, Field field, Position end) {
        this.bot = bot;

        this.field = field;
        tree = new TileNodeTree(this, bot.getPosition(), end);
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

    public Move nextMove() {

        Move ret;

        TileNode nextTurnTile = nextDirection();
        
        if(nextTurnTile == null){
            return null;
        }
        
        Movement m = Movement.directionToMovement(bot.getFacingDirection(), nextTurnTile.getFacing());

        if(m == Movement.STRAIGHT){
            if(bot.getFacingDirection() == Direction.NORTH || bot.getFacingDirection() == Direction.WEST)
                ret = new Move(bot.getBotType(), 0);
            else
                ret = new Move(bot.getBotType(), Field.SIZE-1);
        }

        int moveAt;
        Axis axis = Axis.getAxisOf(bot.getFacingDirection());
        if(axis == Axis.X) {
            moveAt = nextTurnTile.getPosition().x;
        } else {
            moveAt = nextTurnTile.getPosition().y;
        }

        Direction d = Direction.vectorDirection(bot.getPosition(), nextTurnTile.getPosition());

        if(d == Direction.NORTH || d == Direction.WEST) {
            moveAt *= -1;
        }

        ret = new Move(bot.getBotType(), moveAt);
        MoveChecker.doesMoveReachDestination(bot, moveAt, nextTurnTile.getPosition());

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

    public void refresh(Position end) {
        plannedRoute.clear();
        plannedRouteOnMap = new boolean[Field.SIZE][Field.SIZE];

        tree = new TileNodeTree(this, bot.getPosition(), end);
        tree.buildTree();
    }

    // Check whether the bot has reached the location of its turn
    //TODO replace with turn check
    // private boolean hasBotReachedTurningPoint() {

    //     Position p = field.getBotPosition(botType);

    //     if(Axis.getAxisOf(botFacingDirection) == Axis.X) {
    //         if(botFacingDirection == Direction.WEST) {
    //             if(p.x >= nextTurningPoint-1) {
    //                 return true;
    //             }
    //         } else { //EAST
    //             if(p.x <= nextTurningPoint+1) {
    //                 return true;
    //             }
    //         }
    //     } else { //Y
    //         if(botFacingDirection == Direction.SOUTH) {
    //             if(p.y >= nextTurningPoint-1) {
    //                 return true;
    //             }
    //         } else { //NORTH
    //             if(p.y <= nextTurningPoint+1) {
    //                 return true;
    //             }        
    //         }
    //     }

    //     return false;
    // }

}
