package mazes.generators.maze;

import java.util.Random;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import misc.graphs.Graph;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        Random rand = new Random();
        ISet<Wall> wallsToRemove = new ChainedHashSet<Wall>();
        
        for (Wall wall : maze.getWalls()) {
            wall.setDistance(rand.nextDouble());
        }
        
        Graph<Room, Wall> graph = new Graph<Room, Wall>(maze.getRooms(), maze.getWalls());
        wallsToRemove = graph.findMinimumSpanningTree();
        
        for (Wall wall : wallsToRemove) {
            wall.resetDistanceToOriginal();
        }
        
        return wallsToRemove;
    }
}
