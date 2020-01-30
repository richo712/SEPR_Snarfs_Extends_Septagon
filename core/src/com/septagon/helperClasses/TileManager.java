package com.septagon.helperClasses;

import com.septagon.entites.Engine;
import com.septagon.entites.Tile;
import com.septagon.entites.TiledGameMap;

import java.util.ArrayList;

/**
 * Used to handle all of the tile interaction methods and methods for updating values of tiles
 */

public class TileManager {

    private ArrayList<Engine> engines;
    private ArrayList<Tile> tiles;

    public TileManager(ArrayList<Engine> engines, ArrayList<Tile> tiles){
        this.engines = engines;
        this.tiles = tiles;
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

        //Set the all the tiles within the fire station fortress bounds as occupied
        for (int x = 4; x < 12; x++)
        {
            for (int y = 10; y < 15; y++)
            {
                Tile t = getTileAtLocation(x, y);
                if (t != null)
                    t.setOccupied(true);
            }
        }

        //Sets all the tiles within the minister fortress as occupied
        for (int x = 11; x < 19; x++)
        {
            for (int y = 41; y < 48; y++)
            {
                Tile t = getTileAtLocation(x, y);
                if (t != null)
                    t.setOccupied(true);
            }
        }

        //Sets all the tiles within the station fortress as occupied
        for (int x = 31; x < 39; x++)
        {
            for (int y = 30; y < 34; y++)
            {
                Tile t = getTileAtLocation(x, y);
                if (t != null)
                    t.setOccupied(true);
            }
        }

        //Sets all the tiles in the fire station as occupied
        for (int x = 42; x < 50; x++)
        {
            for (int y = 6; y < 10; y++)
            {
                Tile t = getTileAtLocation(x, y);
                if (t != null)
                    t.setOccupied(true);
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

        //Makes so that the bridge tiles are not occupied
        for (int i = 34; i < 38; i++)
        {
            this.getTileAtLocation(i, 7).setOccupied(false);
        }

        for (int i = 25; i < 29; i++)
        {
            this.getTileAtLocation(i, 2).setOccupied(false);
        }

        for (int i = 17; i < 23; i++){
            this.getTileAtLocation(i, 18).setOccupied(false);
        }

        for(int i = 30; i < 34; i++){
            this.getTileAtLocation(8, i).setOccupied(false);
            this.getTileAtLocation(9, i).setOccupied(false);
        }

        for(int i = 39; i < 43; i++){
            this.getTileAtLocation(41, i).setOccupied(false);
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
