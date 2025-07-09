import lenz.htw.thnebsmuf.net.NetworkClient;
import lenz.htw.thnebsmuf.net.Update;

public class AClient {
    public static int myNumber; 

    public static void main(String[] args) {
        NetworkClient client = new NetworkClient(null, "AClient", "Victory Text");
        myNumber = client.getMyPlayerNumber();

        Field field = new Field(client);

        Position posA = new Position(client.getStartX(myNumber, 0), client.getStartY(myNumber, 0));
        Position posB = new Position(client.getStartX(myNumber, 1), client.getStartY(myNumber, 1));
        Position posC = new Position(client.getStartX(myNumber, 2), client.getStartY(myNumber, 2));

        Pathfinder finderA = new Pathfinder(BotType.DEFAULT, field, posA, new Position(0, 0));
        Pathfinder finderB = new Pathfinder(BotType.BORDERLESS ,field, posB, new Position(0, 0));
        Pathfinder finderC = new Pathfinder(BotType.CLIPPING ,field, posC, new Position(0, 0));

        FieldViewer fieldViewer = new FieldViewer(field);

        fieldViewer.display();

        client.getMyPlayerNumber();

        client.getAreaId(0, 0);  //Gebiete und Mauern (0) erkennen

        while (client.isAlive()) {
            Update u;
            while ((u = client.pullNextUpdate()) != null) {
                //Updates in eigenen Datenstruktur einarbeiten
                field.updateField(u);
            }
            fieldViewer.updateImage();

        }
    }
}
