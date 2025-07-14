public class Move {

    public int botID;
    public int moveAt;

    public Move(BotType botType, int moveAt) {
        botID = botType.ordinal();
        this.moveAt = moveAt;
    }
}
