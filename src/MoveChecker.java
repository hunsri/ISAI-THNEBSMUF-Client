public class MoveChecker {


    public static boolean doesMoveReachDestination(Bot bot, int move, Position expectedPosition){
        
        Position positionAfterMove = positionAfterMove(bot, move);
        boolean result = expectedPosition.equals(positionAfterMove);
        inform(result, expectedPosition, positionAfterMove);

        return result;
    }

    private static Position positionAfterMove(Bot bot, int move) {
        
        Position nextPosition = new Position(bot.getPosition().x, bot.getPosition().y);
        
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
            nextPosition = nextPosition(bot.getPosition(), bot.getFacingDirection());
            return nextPosition;
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

        nextPosition = nextPosition(bot.getPosition(), movingDirection);
        return nextPosition;
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

    public static boolean isPositionValid(Position position, Field field, boolean ignoreBorder) {
        
        boolean outOfBounds = false;

        if(position.x >= Field.SIZE || position.x < 0) {
            outOfBounds = true;
        }
        else if(position.y >= Field.SIZE || position.y < 0) {
            outOfBounds = true;
        }

        if(outOfBounds) {
            if(!ignoreBorder) {
                return false; //Cant cross border with standard bot
            } else {
                return true; //Potential check on end of warp
            }
        }
        
        int fieldID = field.getFieldAreaId(position.x, position.y);

        if(fieldID > 0 && fieldID < 1000) {
            return true;
        }

        return false;
    }

    public static Position getPositionAhead(Bot bot) {
        Direction d = bot.getFacingDirection();

        Position p = bot.getPosition();

        switch (d) {
            case NORTH:
                return new Position(p.x, p.y-1);
            case WEST:
                return new Position(p.x-1, p.y);
            case SOUTH:
                return new Position(p.x, p.y+1);
            case EAST:
                return new Position(p.x+1, p.y);
            default:
                return null;
        }
    }

    public static Position getPositionStepsAhead(Bot bot, int amount) {
        Direction d = bot.getFacingDirection();

        Position p = bot.getPosition();

        switch (d) {
            case NORTH:
                return new Position(p.x, p.y-amount);
            case WEST:
                return new Position(p.x-amount, p.y);
            case SOUTH:
                return new Position(p.x, p.y+amount);
            case EAST:
                return new Position(p.x+amount, p.y);
            default:
                return null;
        }
    }

    private static Position getPositionToRight(Bot bot) {
        Direction d = bot.getFacingDirection();

        Position p = bot.getPosition();

        switch (d) {
            case NORTH:
                return new Position(p.x+1, p.y);
            case WEST:
                return new Position(p.x, p.y-1);
            case SOUTH:
                return new Position(p.x-1, p.y);
            case EAST:
                return new Position(p.x, p.y+1);
            default:
                return null;
        }
    }

    private static Position getPositionToLeft(Bot bot) {
        Direction d = bot.getFacingDirection();

        Position p = bot.getPosition();

        switch (d) {
            case NORTH:
                return new Position(p.x-1, p.y);
            case WEST:
                return new Position(p.x, p.y+1);
            case SOUTH:
                return new Position(p.x+1, p.y);
            case EAST:
                return new Position(p.x, p.y-1);
            default:
                return null;
        }
    }

    public static boolean isAheadInvalid(Bot b, Field f) {
        Position ahead = getPositionAhead(b);

        boolean ignoreBorder = (BotType.BORDERLESS == b.getBotType());
        boolean ignoreOwnTrail = (BotType.CLIPPING == b.getBotType());

        return !isPositionValid(ahead, f, ignoreBorder);
    }

    /**
     * Equivalent to {@link #isAheadInvalid(Bot, Field)}, when stepsAhead set to 1
     * 
     * @param b the bot
     * @param f the field
     * @param stepsAhead steps to look in advance of the bot
     * @return true if the position steps ahead is invalid, false otherwise
     */
    public static boolean isStepsAheadInvalid(Bot b, Field f, int stepsAhead) {
        Position ahead = getPositionStepsAhead(b, stepsAhead);

        boolean ignoreBorder = (BotType.BORDERLESS == b.getBotType());
        boolean ignoreOwnTrail = (BotType.CLIPPING == b.getBotType());

        return !isPositionValid(ahead, f, ignoreBorder);
    }


    /**
     * Picks a valid move in time critical scenarios
     * 
     * @param b
     * @param f
     * @return A valid move
     */
    public static Move panicMove(Bot b, Field f) {

        boolean ignoreBorder = (BotType.BORDERLESS == b.getBotType());
        boolean ignoreOwnTrail = (BotType.CLIPPING == b.getBotType());

        Move ret = new Move(b.getBotType(), 0);
        Axis axis = Axis.getAxisOf(b.getFacingDirection());

        int baseMove = (Axis.X == axis ? b.getPosition().x : b.getPosition().y);

        Position left = getPositionToLeft(b);
        Position right = getPositionToRight(b);

        int mult = 1;

        // check against positive or negative turn
        if(isPositionValid(right, f, ignoreBorder)) {
            if(Axis.Y == axis) {
                if(b.getFacingDirection() == Direction.SOUTH) {
                    mult = -1;
                }
            } else { // X
                if(b.getFacingDirection() == Direction.WEST) {
                    mult = -1;
                }
            }
        } else if (isPositionValid(left, f, ignoreBorder)) {
            if(Axis.Y == axis) {
                if(b.getFacingDirection() == Direction.NORTH) {
                    mult = -1;
                }
            } else {
                if(b.getFacingDirection() == Direction.EAST) {
                    mult = -1;
                }
            }
        }

        ret.moveAt = baseMove * mult;
        return ret;
    }
}
