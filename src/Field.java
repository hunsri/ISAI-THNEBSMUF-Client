import lenz.htw.thnebsmuf.net.NetworkClient;
import lenz.htw.thnebsmuf.net.Update;

public class Field {
    public final static int SIZE = 256;
    public final static int BOTS = 3;
    public final static int PLAYERS = 3;

    int[][] areaID = new int[SIZE][SIZE];

    private boolean[][] debug = new boolean[Field.SIZE][Field.SIZE];

    NetworkClient client;

    private Position defaultBot;
    private Position clippingBot;
    private Position borderlessBot;

    public Field (NetworkClient client) {
        this.client = client;
        initField();
    }

    private void initField() {
        for(int i = 0; i < SIZE; i++){

            for(int j = 0; j < SIZE; j++){
                areaID[i][j] = client.getAreaId(i, j);
            }
        }

        for(int i = 0; i < BOTS; i++) {
            int x = client.getStartX(client.getMyPlayerNumber(), i);
            int y = client.getStartY(client.getMyPlayerNumber(), i);

            areaID[x][y] = Trails.START_POSITION.getValue();
        }

        updateBotPositions();
    }

    public void updateField(Update update) {
        int value;

        if(update.player == client.getMyPlayerNumber()) {
            if(update.bot == 2) {//wall clipping bot
                value = Trails.OWN_PASSABLE.getValue();
            } else if (update.bot == 1) {
                value = Trails.OWN_ROOMLESS.getValue();
            } else {
                value = Trails.OWN.getValue();
            }
        } else {
            value = Trails.ENEMY.getValue();
        }

        areaID[update.x][update.y] = value;
        updateBotPositions();
    }

    public void markDebug(int x, int y) {
        debug[x][y] = true;
    }

    public void cleanDebug() {
        // debug = new boolean[Field.SIZE][Field.SIZE];

        for(int i = 0; i < Field.SIZE; i++) {
            for(int j = 0; j < Field.SIZE; j++) {
                debug[i][j] = false;
            }
        }
    }

    private void updateBotPositions() {
        int myPlayerID = client.getMyPlayerNumber();
        defaultBot = new Position(client.getStartX(myPlayerID, BotType.DEFAULT.ordinal()), client.getStartY(myPlayerID, BotType.DEFAULT.ordinal()));
        clippingBot = new Position(client.getStartX(myPlayerID, BotType.CLIPPING.ordinal()), client.getStartY(myPlayerID, BotType.CLIPPING.ordinal()));
        borderlessBot = new Position(client.getStartX(myPlayerID, BotType.BORDERLESS.ordinal()), client.getStartY(myPlayerID, BotType.BORDERLESS.ordinal()));
    }

    public int getFieldAreaId(int x, int y) {
        return areaID[x][y];
    }

    public Position getBotPosition(BotType botType) {
        switch (botType) {
            case DEFAULT:
                return defaultBot;
            case BORDERLESS:
                return borderlessBot;
            case CLIPPING:
                return clippingBot;
            default:
                return defaultBot; //just to satisfy switch statement
        }
    }

    public boolean[][] getDebugTrace() {
        return debug;
    }
}
