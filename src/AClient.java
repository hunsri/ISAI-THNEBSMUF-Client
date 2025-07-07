import lenz.htw.thnebsmuf.net.NetworkClient;
import lenz.htw.thnebsmuf.net.Update;

public class AClient {
    public static void main(String[] args) {
        NetworkClient client = new NetworkClient(null, "AClient", "Victory Text");

        Field field = new Field(client);

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
