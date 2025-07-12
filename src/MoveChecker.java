public class MoveChecker {


    public static boolean doesMoveReachDestination(Bot bot, int move, Position expectedPosition){
        Position nextActualPosition = new Position(bot.getPosition().x, bot.getPosition().y);
        
        Axis axis = Axis.getAxisOf(bot.getFacingDirection());
        int botPositionOnAxis;
        boolean positiveDirection;

        if(axis == Axis.X) {
            botPositionOnAxis = bot.getPosition().x;
        } else {
            botPositionOnAxis = bot.getPosition().y;
        }

        if(bot.getFacingDirection() == Direction.SOUTH || bot.getFacingDirection() == Direction.EAST) {
            positiveDirection = true;
        } else {
            positiveDirection = false;
        }

        int lookupForwardSteps = positiveDirection ? 1 : -1;

        boolean hasReachedTurningPoint = false;

        if(positiveDirection) {
            if(botPositionOnAxis + lookupForwardSteps >= Math.abs(move)) {
                hasReachedTurningPoint = true;
            }
        } else {
            if(botPositionOnAxis + lookupForwardSteps <= Math.abs(move)) {
                hasReachedTurningPoint = true;
            }
        }

        if(!hasReachedTurningPoint) {
            nextActualPosition = nextPosition(bot.getPosition(), bot.getFacingDirection());
            boolean ret = expectedPosition.equals(nextActualPosition);
            inform(ret, expectedPosition, nextActualPosition);
            return ret;
        }

        // Turning Point has been reached
        boolean isMovePositive = move >= 0;

        Direction movingDirection;

        if(Axis.X == axis) {
            if(isMovePositive) {
                movingDirection = Direction.SOUTH;
            } else {
                movingDirection = Direction.NORTH;
            }
        } else { // Y
            if(isMovePositive) {
                movingDirection = Direction.EAST;
            } else {
                movingDirection = Direction.WEST;
            }
        }

        nextActualPosition = nextPosition(bot.getPosition(), movingDirection);
        boolean ret = expectedPosition.equals(nextActualPosition);
        inform(ret, expectedPosition, nextActualPosition);
        return ret;
    }

    private static void inform(boolean result, Position expected, Position willActuallyBe) {
        if(!result) {
            StringBuilder sb = new StringBuilder().append("Expected Position: ").append(expected);
            sb.append(" BUT GOT: ").append(willActuallyBe);

            System.err.println(sb.toString());
        }else {
            System.out.println("All clear for next move to: "+expected);
        }
    }

    private static Position nextPosition(Position current, Direction d){
        switch (d) {
            case NORTH:
                return new Position(current.x, current.y-1);
            case WEST:
                return new Position(current.x-1, current.y);
            case SOUTH:
                return new Position(current.x, current.y+1);
            case EAST:
                return new Position(current.x+1, current.y);
            default:
                return null;
        }
    }
}
