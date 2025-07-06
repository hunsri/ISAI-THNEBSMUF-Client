import lenz.htw.thnebsmuf.net.NetworkClient;

public class Field {
    public final static int SIZE = 256;

    int[][] areaID = new int[SIZE][SIZE];

    NetworkClient client;

    public Field (NetworkClient client) {
        this.client = client;
        updateField();
    }

    private void updateField() {
        for(int i = 0; i < SIZE; i++){

            for(int j = 0; j < SIZE; j++){
                areaID[i][j] = client.getAreaId(i, j);
            }
        }
    }

    public int getCachedAreaId(int x, int y) {
        return areaID[x][y];
    }
}
