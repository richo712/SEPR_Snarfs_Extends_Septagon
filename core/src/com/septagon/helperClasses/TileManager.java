package com.septagon.helperClasses;

import com.septagon.entites.*;

import java.util.ArrayList;

/**
 * Used to handle all of the tile interaction methods and methods for updating values of tiles
 */

public class TileManager {

    private ArrayList<Engine> engines;
    private ArrayList<Tile> tiles;
    private ArrayList<Alien> aliens;
    private ArrayList<Fortress> fortresses;
    private Station station;

    public TileManager(ArrayList<Engine> engines, ArrayList<Tile> tiles, ArrayList<Alien> aliens, ArrayList<Fortress> fortresses, Station station){
        this.engines = engines;
        this.tiles = tiles;
        this.aliens = aliens;
        this.fortresses = fortresses;
        this.station = station;
    }

    /***
     * Clear all the current moveable tiles
     */
    public void resetMovableTiles(){
        for(Tile t: tiles)
        {
            t.setMovable(false);
        }
    }

    /***
     * Sets up the tiles which contain an engine, fortress or the station to be occupied
     * All of this method was changed for Assessment 3, except the Engines loop
     */
    public void setOccupiedTiles(TiledGameMap gameMap)
    {
        //Set the tiles that currently have an engine on to be occupied
        for (Engine e : engines)
        {
            for (Tile t : tiles)
            {
                if (t.getCol() == e.getCol() && t.getRow() == e.getRow())
                {
                    t.setOccupied(true);
                    break;
                }
            }
        }

        //Setting the tiles the aliens are currently on as occupied
        for(Alien a : aliens){
            for (Tile t:tiles){
                if(t.getCol() == a.getCol() && t.getRow() == a.getRow()){
                    t.setOccupied(true);
                    break;
                }
            }
        }

        //Setting fortresses as occupied
        for(Fortress f : fortresses){
            int minX = f.getCol();
            int minY = f.getRow();
            int maxX = minX + f.getxBound();
            int maxY = minY + f.getyBound();

            for (int x = minX; x < maxX; x++){
                for( int y = minY; y < maxY; y++){
                    getTileAtLocation(x, y).setOccupied(true);
                }
            }
        }

        //Setting the fire station as occupied
        int stationMinX = station.getCol();
        int stationMaxX = stationMinX + station.getxBound();
        int stationMinY = station.getRow();
        int stationMaxY = stationMinY + station.getyBound();

        for (int x = stationMinX; x < stationMaxX; x++){
            for(int y = stationMinY; y < stationMaxY; y++){
                getTileAtLocation(x, y).setOccupied(true);
            }
        }

        //Setting the world boundaries
        for(int x = 0; x < 4; x++) {
            for (int y = 0; y < 200; y++) {
                getTileAtLocation(x, y).setOccupied(true);
                getTileAtLocation(y, x).setOccupied(true);
                getTileAtLocation(199-x, 199-y).setOccupied(true);
                getTileAtLocation(199-y, 199-x).setOccupied(true);
            }
        }

        //Loops through all tiles to work out if they are water tiles, and if so makes them occupied
        for (Tile t : tiles)
        {
            if (gameMap.checkIfWaterTile(t.getCol(), t.getRow()))
            {
                t.setOccupied(true);
            }
        }
    }

    /***
     * Method to get the tile at a row and column
     * @param col The column of the tile you want to get
     * @param row The row of the tile you want to get
     * @return The tile at the location asked for
     */
    public Tile getTileAtLocation(int col, int row)
    {
        for(Tile t: tiles)
        {
            if(t.getCol() == col && t.getRow() == row)
                return t;
        }
        return null;
    }

    /***
     * Get the movable tiles for all the engines based on their positions
     */
    public void setMovableTiles(Engine currentEngine){
        //Reset all moveable tiles from previous turn
        resetMovableTiles();
        //Get the movable tiles
        int loopCounter = 0;
        while(loopCounter <= currentEngine.getSpeed())
        {
            for (int i = 0; i <= currentEngine.getSpeed() - loopCounter; i++)
            {
                Tile nextTile = this.getTileAtLocation(currentEngine.getCol() - i, currentEngine.getRow() - loopCounter);
                if (nextTile != null)
                {
                    if (!nextTile.isOccupied())
                    {
                        nextTile.setMovable(true);
                    }

                }

                nextTile = this.getTileAtLocation(currentEngine.getCol() + i, currentEngine.getRow() - loopCounter);
                if (nextTile != null)
                {
                    if (!nextTile.isOccupied())
                    {
                        nextTile.setMovable(true);
                    }

                }

                nextTile = this.getTileAtLocation(currentEngine.getCol() - i, currentEngine.getRow() + loopCounter);
                if (nextTile != null)
                {
                    if (!nextTile.isOccupied())
                    {
                        nextTile.setMovable(true);
                    }

                }

                nextTile = this.getTileAtLocation(currentEngine.getCol() + i, currentEngine.getRow() + loopCounter);
                if (nextTile != null)
                {
                    if (!nextTile.isOccupied())
                    {
                        nextTile.setMovable(true);
                    }

                }
            }
            loopCounter++;
        }
    }
}
