package com.septagon.states;

/*
Class that is used to manage all of the states within the game and manage the changes between
the different states
 */

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StateManager 
{
    //List that keeps track of all the states that have been seen in the game so far
    private ArrayList<State> states;
    private int currentIndex = 0;

    public StateManager()
    {
        states = new ArrayList<State>();
    }

    /**
     * Intialises the current state of the game
     */
    public void initialise()
    {
    	states.get(currentIndex).initialise();
    }

    /**
     * Updates the current state of the game
     */
    public void update()
    {
    	states.get(currentIndex).update();
    }

    /**
     * Renders the current state of the game
     * @param batch The batch that is used for rendering all entities in the game
     */
    public void render(SpriteBatch batch)
    {
    	states.get(currentIndex).render(batch);
    }

    /**
     * Dispose of all elements from all states when the game is closed
     */
    public void dispose(){
        for(State s: states){
            s.dispose();
        }
    }

    /**
     * Changes the current state of the game
     * @param newState the state that is going to become the current state
     */
    public void changeState(State newState)
    {
        newState.initialise();
    	states.add(newState);
    	currentIndex = states.indexOf(newState);
    }

    public void changeToExistingState(State.StateID id){
        if (stateExists(id)) {
            int currentIndex = 0;
            for (int i = 0; i < this.states.size(); i++) {
                if (this.states.get(i).getID() == id) {
                    currentIndex = i;
                }
            }
            this.currentIndex = currentIndex;
        } else{
            System.out.println("State: " + id + " doesn't exist!");
        }

    }

    private boolean stateExists(State.StateID id){
        boolean result = false;
        for (State s: this.states){
            if (s.getID() == id){
                result = true;
                break;
            }
        }
        return result;
    }
    //Getters
    public int getCurrentIndex() { return currentIndex; }
    public State getCurrentState() { return states.get(currentIndex); }
    public ArrayList<State> getStates() { return states; }
}
