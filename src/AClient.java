import lenz.htw.thnebsmuf.net.NetworkClient;
import lenz.htw.thnebsmuf.net.Update;

public class AClient {
    public static int myNumber; 

    private static boolean resetA = false;
    private static boolean resetB = false;
    private static boolean resetC = false;

    public static void main(String[] args) {
        NetworkClient client = new NetworkClient(null, "AClient", "Victory Text");
        myNumber = client.getMyPlayerNumber();

        Field field = new Field(client);

        Bot botA = new Bot(client, BotType.DEFAULT);
        Bot botB = new Bot(client, BotType.BORDERLESS);
        Bot botC = new Bot(client, BotType.CLIPPING);

        Pathfinder finderA = new Pathfinder(botA, field);
        Pathfinder finderB = new Pathfinder(botB, field);
        Pathfinder finderC = new Pathfinder(botC, field);

        FieldViewer fieldViewer = new FieldViewer(field);

        fieldViewer.display();


        while (client.isAlive()) {
            Update u;
            while ((u = client.pullNextUpdate()) != null) {
                //Updates in eigenen Datenstruktur einarbeiten
                field.updateField(u);

                if(u.player == myNumber) {
                    moveBot(client, u, botB, finderB, field);
                    moveBot(client, u, botC, finderC, field);
                    moveBot(client, u, botA, finderA, field);
                    fieldViewer.updateImage();
                }
            }

            fieldViewer.cleanDebug();

        }
    }

    private static void moveBot(NetworkClient client, Update u, Bot bot, Pathfinder finder, Field field) {

        if(resetA) {
            finder.refresh(true);
            resetA = false;
        }
        if(resetB) {
            finder.refresh(true);
            resetB = false;
        }
        if(resetC) {
            finder.refresh(true);
            resetC = false;
        }

        int destinationRefreshRate = 50;

        if(u.bot == bot.getBotType().ordinal()) {
            boolean botOrientationValid = bot.update(u);

            if(!botOrientationValid) {
                resetBotPathfinding(bot);
            }

            // System.out.println("===BOT NOW AT===");
            // System.out.println(bot.getPosition() +" Facing: "+bot.getFacingDirection());
            
            if(finder.getConsumptionCounter() >= destinationRefreshRate) {
                resetBotPathfinding(bot);
            }

            Move m;
            if(MoveChecker.isAheadInvalid(bot, field) || MoveChecker.isStepsAheadInvalid(bot, field, 2)) {
                System.out.println("Invalid move ahead, issuing panic move");
                m = MoveChecker.panicMove(bot, field);
                resetBotPathfinding(bot);;
            } else {
                m = finder.getNextMove();
            }

            if(m != null) {
                client.changeDirection(m.botID, m.moveAt);
            } else {
                resetBotPathfinding(bot);
            }
        }
    }

    private static void resetBotPathfinding(Bot b) {
        switch (b.getBotType()) {
            case DEFAULT:
                resetA = true;
                break;
            case BORDERLESS:
                resetB = true;
                break;
            case CLIPPING:
                resetC = true;
                break;
            default:
                break;
        }
    }
        
}
