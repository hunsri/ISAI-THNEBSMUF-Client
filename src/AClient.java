import lenz.htw.thnebsmuf.net.NetworkClient;
import lenz.htw.thnebsmuf.net.Update;

public class AClient {
    public static int myNumber; 

    public static void main(String[] args) {
        NetworkClient client = new NetworkClient(null, "AClient", "Victory Text");
        myNumber = client.getMyPlayerNumber();

        Field field = new Field(client);

        Bot botA = new Bot(client, BotType.DEFAULT);
        Bot botB = new Bot(client, BotType.BORDERLESS);
        Bot botC = new Bot(client, BotType.CLIPPING);

        Pathfinder finderA = new Pathfinder(botA, field, new Position(1, 1));
        // Pathfinder finderB = new Pathfinder(botB, field, new Position(1, 1));
        // Pathfinder finderC = new Pathfinder(botC, field, new Position(1, 1));


        FieldViewer fieldViewer = new FieldViewer(field);

        fieldViewer.display();

        // client.getMyPlayerNumber();

        // client.getAreaId(0, 0);  //Gebiete und Mauern (0) erkennen

        while (client.isAlive()) {
            Update u;
            while ((u = client.pullNextUpdate()) != null) {
                //Updates in eigenen Datenstruktur einarbeiten
                field.updateField(u);
                if(u.player == myNumber) {
                    if(u.bot == botA.getBotType().ordinal()) {
                        botA.update(u);
                        System.out.println("===BOT NOW AT===");
                        System.out.println(botA.getPosition() +" Facing: "+botA.getFacingDirection());
                        finderA.refresh(new Position(1, 1));
                        Move m = finderA.nextMove();
                        if(m != null) {
                            client.changeDirection(m.botID, m.moveAt);
                        }
                    // }else if (u.bot == botB.getBotType().ordinal()) {
                    //     botB.update(u);
                    //     finderB.refresh(new Position(1, 1));
                    //     Move m = finderB.nextMove();
                    //     if(m != null) {
                    //         client.changeDirection(m.botID, m.moveAt);
                    //     }
                    // }else if (u.bot == botC.getBotType().ordinal()) {
                    //     botC.update(u);
                    //     finderC.refresh(new Position(1, 1));
                    //     Move m = finderC.nextMove();
                    //     if(m != null) {
                    //         client.changeDirection(m.botID, m.moveAt);
                    //     }
                    }
                }
            }
            fieldViewer.updateImage();

        }
    }
}
