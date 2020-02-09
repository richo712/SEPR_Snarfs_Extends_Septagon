package com.septagon.helperClasses;

import com.septagon.entites.Entity;
import com.septagon.entites.Tile;
import com.septagon.entites.TiledGameMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Maths {

    private Maths(){ }

    /**
     * Finds the manhattan distance between two .
     * @param x1 first points x coordinate
     * @param y1 first points y coordinate
     * @param x2 second points x coordinate
     * @param y2 second points y coordinate
     * @return manhattan distance between both points
     */
    public static int manDistance(int x1, int y1, int x2, int y2){
        int xDif = Math.abs(x1 - x2);
        int yDif = Math.abs(y1 - y2);
        return xDif + yDif;
    }

    /**
     * Finds the manhattan distance in tiles between two entities.
     * @param e1 An entity
     * @param e2 Another entity
     * @return Manhattan distance in tiles between the two entities
     */
    public static int manDistance(Entity e1, Entity e2){
        return manDistance(e1.getCol(), e1.getRow(), e2.getCol(), e2.getRow());
    }

    /**
     * Implements A* search to find the path between two points.
     * @param startCol starting column on the map
     * @param startRow starting row on the map
     * @param goalCol ending column on the map
     * @param goalRow ending row on the map
     * @param map map we need to find a path through
     * @return An arraylist of tiles leading from the start to the goal, null if it couldn't find a path
     */
    public static ArrayList<Tile> findPathTo(int startCol, int startRow, int goalCol, int goalRow, TiledGameMap map){
        class Node{
            Tile tile;
            ArrayList<Tile> tileHistory; //An array list of tiles needed to get to the goal, including the current tile
            float traveledCost; //Actual code to get to this tile
            float estimatedCost; //Optimistic distance to goal
            public Node(Tile tile, ArrayList<Tile> tileHistory, float traveledCost, float estimatedCost) {
                this.tile = tile;
                this.tileHistory = tileHistory;
                this.traveledCost = traveledCost;
                this.estimatedCost = estimatedCost;
            }
        }
        Comparator costComparator = new Comparator<Node>() {
            //Compares the cost of nodes so priority queue is ordered by it, lowest at the head
            @Override
            public int compare(Node a, Node b) {
                if (a.estimatedCost < b.estimatedCost) {
                    return -1;
                } else {
                    return 1;
                }
            }};
        PriorityQueue<Node> fringe = new PriorityQueue<Node>(1, costComparator); //Set of Nodes still to explore beyond
        HashSet<Tile> exploredTiles = new HashSet<>(); //Set of tiles Explored
        Tile startTile = map.getTileByCoordinate(0, startCol, startRow);
        Tile goalTile = map.getTileByCoordinate(0, goalCol, goalRow);
        fringe.add(new Node(startTile, new ArrayList<Tile>(), 0, manDistance(startCol, startRow, goalCol, goalRow)));
        exploredTiles.add(startTile);

        while (!fringe.isEmpty()){
            Node currentNode = fringe.poll(); //Remove the lowest estimated cost Node from the fringe
            if (currentNode.tile.getCol() == goalCol && currentNode.tile.getRow() == goalRow){ //Check to see if we have found our goal
                return currentNode.tileHistory;
            }
            ArrayList<Tile> adjacentTiles = map.getAdjacentTiles(currentNode.tile);
            for (Tile t : adjacentTiles){
                if (!exploredTiles.contains(t)){ //if we haven't found a path to this tile yet
                    exploredTiles.add(t);
                    float traveledCost = currentNode.traveledCost + t.getTileCost();
                    float estimatedCost = traveledCost + manDistance(t.getCol(), t.getRow(), goalCol, goalRow);
                    ArrayList<Tile> tileHistory = new ArrayList<>(currentNode.tileHistory);
                    tileHistory.add(t);
                    fringe.add(new Node(t, tileHistory, traveledCost, estimatedCost)); //Add it to the fringe
                }
            }

        }
        return null;
    }
}
