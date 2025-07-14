import java.util.Random;

public class DestinationFinder {

    private Bot bot;
    private Field field;

    private Position destination;

    public DestinationFinder(Bot bot, Field field) {
        this.bot = bot;
        this.field = field;
        destination = null;

        pickRandomDestination();
    }

    public void updateDestination() {
        if(!isDestinationValid()) {
            pickRandomDestination();
        }
    }

    private void pickRandomDestination() {
        Position p = new Position(1, 1);

        Random r = new Random();

        while(!isDestinationValid()) {
            p.x = r.nextInt(Field.SIZE);
            p.y = r.nextInt(Field.SIZE);
            destination = p;
        }
    }

    private boolean isDestinationValid() {
        if(destination == null){
            return false;
        }

        if(MoveChecker.isPositionValid(destination, field, false, false)) {
            return true;
        }

        return false;
    }

    public Position getDestination() {
        updateDestination();
        return destination;
    }
}
