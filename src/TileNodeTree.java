import java.util.ArrayList;
import java.util.LinkedList;

public class TileNodeTree {

    Position target;

    private ArrayList<TileNode> children = new ArrayList<TileNode>(4);
    private TileNode rootNode;

    private boolean[][] visited = new boolean[Field.SIZE][Field.SIZE];
    private Pathfinder finder;

    private LinkedList<TileNode> queue = new LinkedList<TileNode>();

    private LinkedList<TileNode> pathToTarget = new LinkedList<TileNode>();

    public TileNodeTree(Pathfinder finder, Position start, Position end) {
        this.finder = finder;

        this.target = end;

        rootNode = new TileNode(null, start.x, start.y, Direction.EAST);
        queue.add(rootNode);
    }

    public void buildTree() {
        TileNode lastNode = new TileNode(null, 0, 0, Direction.EAST); 

        while(queue.size() > 0) {
            lastNode = queue.pop();
            addChildrenToNode(lastNode);
        }

        createTrailToTarget(lastNode);
    }

    private void createTrailToTarget(TileNode lastNode) {
        
        TileNode currentNode = lastNode;
        //as long as the start node hasn't been reached, fill the trail
        while(currentNode.getParentNode() != null) {
            pathToTarget.add(currentNode);
            finder.markDebug(currentNode.getPosition().x, currentNode.getPosition().y);
            finder.addToPlannedRoute(currentNode);
            currentNode = currentNode.getParentNode();
        }
    }

    private void addChildrenToNode(TileNode node) {
        for(Direction d: Direction.values()) {
            Position p = node.getPosition().getNeighbor(d);
            if(canCreateAt(p)) {
                TileNode n = new TileNode(node, p, d);
                children.add(n);
                visited[p.x][p.y] = true;
                queue.add(n);

                if(n.getPosition().x == target.x && n.getPosition().y == target.y) {
                    queue.clear();
                }
            }
        }
    }

    private boolean canCreateAt(Position p) {
        return canCreateAt(p.x, p.y);
    }

    private boolean canCreateAt(int posX, int posY) {

        //boundary guard check
        if(posX < 0 || posX > Field.SIZE-1 || posY < 0 || posY > Field.SIZE-1)
            return false;

        if(finder.canStepAt(posX, posY) && visited[posX][posY] == false) {
            return true;
        }
        return false;
    }


}
