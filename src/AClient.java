import lenz.htw.thnebsmuf.net.NetworkClient;
import lenz.htw.thnebsmuf.net.Update;

public class AClient {
    public static int myNumber; 

    private static boolean resetDestinationA = false;
    private static boolean resetDestinationB = false;
    private static boolean resetDestinationC = false;

    private static boolean debugMessage = false;

    //Showing the debug screen
    //Deactivate for saving processing resources
    private static boolean showField = true;

    public static void main(String[] args) {

        //Connecting to server
        String serverAddress = null;
        NetworkClient client = new NetworkClient(serverAddress, "AClient", "Victory Text");
        
        myNumber = client.getMyPlayerNumber();
        
        //building the field representation
        Field field = new Field(client);

        //initializing the bots
        Bot botA = new Bot(client, BotType.DEFAULT);
        Bot botB = new Bot(client, BotType.BORDERLESS);
        Bot botC = new Bot(client, BotType.CLIPPING);

        //initializing the pathfinders
        Pathfinder finderA = new Pathfinder(botA, field);
        Pathfinder finderB = new Pathfinder(botB, field);
        Pathfinder finderC = new Pathfinder(botC, field);

        //displays field representation in real time
        FieldViewer fieldViewer = new FieldViewer(field);
        if(showField) { 
            fieldViewer.display();
        }

        while (client.isAlive()) {
            Update u;
            while ((u = client.pullNextUpdate()) != null) {
                //Updates in eigenen Datenstruktur einarbeiten
                field.updateField(u);

                //do all in here to just update when we get a new location for us
                if(u.player == myNumber) {

                    if(u.bot == botA.getBotType().ordinal()) {
                        moveBot(client, u, botA, finderA, field);
                    } else if(u.bot == botB.getBotType().ordinal()) {
                        moveBot(client, u, botB, finderB, field);
                    } else if(u.bot == botC.getBotType().ordinal()) {
                        moveBot(client, u, botC, finderC, field);
                    }

                    if(showField) {
                        fieldViewer.updateImage();
                        fieldViewer.cleanDebug();
                    }
                }
            }
        }
    }

    private static void moveBot(NetworkClient client, Update u, Bot bot, Pathfinder finder, Field field) {

        // once the signal for refreshing the destination has been triggered
        if(resetDestinationA) {
            finder.refresh(true);
            resetDestinationA = false;
        }
        if(resetDestinationB) {
            finder.refresh(true);
            resetDestinationB = false;
        }
        if(resetDestinationC) {
            finder.refresh(true);
            resetDestinationC = false;
        }

        int destinationRefreshRate = 1000;

        if(u.bot == bot.getBotType().ordinal()) {
            boolean botOrientationValid = bot.update(u);

            if(!botOrientationValid) {
                resetBotPathfinding(bot);
            }

            if(debugMessage) {
                System.out.println("===BOT NOW AT===");
                System.out.println(bot.getPosition() +" Facing: "+bot.getFacingDirection());
            }
            
            if(finder.getConsumptionCounter() >= destinationRefreshRate) {
                resetBotPathfinding(bot);
            }

            Move m;
            if(MoveChecker.isAheadInvalid(bot, field) || MoveChecker.isStepsAheadInvalid(bot, field, 2)) {
                if(debugMessage) {
                    System.out.println("Invalid move ahead, issuing panic move");
                }
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
                resetDestinationA = true;
                break;
            case BORDERLESS:
                resetDestinationB = true;
                break;
            case CLIPPING:
                resetDestinationC = true;
                break;
            default:
                break;
        }
    }
        
}
