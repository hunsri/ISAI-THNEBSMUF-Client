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
        Pathfinder finderB = new Pathfinder(botB, field, new Position(1, 1));
        Pathfinder finderC = new Pathfinder(botC, field, new Position(1, 1));


        FieldViewer fieldViewer = new FieldViewer(field);

        fieldViewer.display();

        while (client.isAlive()) {
            Update u;
            while ((u = client.pullNextUpdate()) != null) {
                //Updates in eigenen Datenstruktur einarbeiten
                field.updateField(u);
                if(u.player == myNumber) {
                    moveBot(client, u, botA, finderA, field);
                    // moveBot(client, u, botB, finderB, field);
                    // moveBot(client, u, botC, finderC, field);
                    fieldViewer.updateImage();
                }
            }
            fieldViewer.cleanDebug();

        }
    }

    private static void moveBot(NetworkClient client, Update u, Bot bot, Pathfinder finder, Field field) {

        if(u.bot == bot.getBotType().ordinal()) {
            bot.update(u);
            System.out.println("===BOT NOW AT===");
            System.out.println(bot.getPosition() +" Facing: "+bot.getFacingDirection());
            finder.refresh(new Position(128, 128));

            Move m;
            if(MoveChecker.isAheadInvalid(bot, field)) {
                System.out.println("Invalid move ahead");
                m = MoveChecker.panicMove(bot, field);
            } else {
                m = finder.getNextMove();
            }
            if(m != null) {
                client.changeDirection(m.botID, m.moveAt);
            }
        }
    }
        
}
