import lenz.htw.thnebsmuf.net.NetworkClient;
import lenz.htw.thnebsmuf.net.Update;

public class Field {
    public final static int SIZE = 256;
    public final static int BOTS = 3;
    public final static int PLAYERS = 3;

    int[][] areaID = new int[SIZE][SIZE];

    NetworkClient client;

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
    }

    public void markDebug(int x, int y) {
        areaID[x][y] = Trails.DEBUG.getValue();
    }

    public int getFieldAreaId(int x, int y) {
        return areaID[x][y];
    }
}
