import lenz.htw.thnebsmuf.net.NetworkClient;
import lenz.htw.thnebsmuf.net.Update;

public class Bot {
    private int myID;

    private NetworkClient client;
    private BotType botType;

    private Position previousPosition;

    private Position botPosition;

    private Direction facingDirection;

    public Bot(NetworkClient client, BotType botType) {
        this.client = client;
        this.botType = botType;

        myID = client.getMyPlayerNumber();
        botPosition = fetchStartPosition();
        botPosition.x += 1; //Bot will move one step to the east before we can act
        previousPosition = new Position(botPosition.x, botPosition.y);

        facingDirection = Direction.EAST;  // bots always start facing east
    }

    public void update(Update u) {
        if(u.player != myID)
            return;

        if(u.bot != botType.ordinal())
            return;

        if(botPosition.x != u.x || botPosition.y != u.y) {
            previousPosition.x = botPosition.x;
            previousPosition.y = botPosition.y;
            
            botPosition.x = u.x;
            botPosition.y = u.y;
        }

        if(!previousPosition.equals(botPosition)){
            facingDirection = Direction.vectorDirection(previousPosition, botPosition);
        }
    }

    private Position fetchStartPosition() {
        return new Position(client.getStartX(myID, botType.ordinal()), client.getStartY(myID, botType.ordinal()));
    }

    public Position getPosition() {
        return botPosition;
    }

    public BotType getBotType() {
        return botType;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }
}
