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

    //установка нового состояния клетки
    public void setNewState(boolean newState1){
        newState = newState1;
    }

    //обновление состояния клетки
    public void updateState(){
        state = newState;
    }

    //состояние клетки
    public boolean getState(){
        return state;
    }
}
