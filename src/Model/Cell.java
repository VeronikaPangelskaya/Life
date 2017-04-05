package Model;

/**
 * Created by sukhanovma on 24.02.2017.
 */
public class Cell {
    private boolean state = false;
    private boolean newState;

    public Cell() {
        state = false;
    }

    public void setNewState(boolean newState1){
        newState = newState1;
    }

    public void updateState(){
        state = newState;
    }

    public boolean getState(){
        return state;
    }
}
