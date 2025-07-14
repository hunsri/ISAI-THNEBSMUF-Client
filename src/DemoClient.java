import lenz.htw.thnebsmuf.net.NetworkClient;
import lenz.htw.thnebsmuf.net.Update;

// used as reference only
public class DemoClient {

    public static void main(String[] args) {
        NetworkClient client = new NetworkClient(null, "ICH", "YEAH!");

        client.getMyPlayerNumber();
        // client.getStartX(player, bot);
        // client.getStartY(0, 0);
        boolean a = false;


        client.getAreaId(0, 0);  //Gebiete und Mauern (0) erkennen
        while (client.isAlive()) {
            Update u;
            while ((u = client.pullNextUpdate()) != null) {
                //Updates in eigenen Datenstruktur einarbeiten
            }

            if(a){
                client.changeDirection(0, 255);
                client.changeDirection(1, 255);
                client.changeDirection(2, 255);

                client.changeDirection(0, 220);
                client.changeDirection(1, 220);
                client.changeDirection(2, 220);
                a = false;
            }
        }
    }
}