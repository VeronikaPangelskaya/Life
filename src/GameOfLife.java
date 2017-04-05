import Controller.Controller;
import Model.Field;
import View.Frame;

/**
 * Created by sukhanovma on 26.02.2017.
 */
public class GameOfLife {

    public static void main(String[] args) {

        Field field = new Field(10, 15);
        Controller controller = new Controller();
        controller.setModel(field);
        Frame frame = new Frame(controller);
        controller.setView(frame);
        field.addObserver(frame.getHexagon());

    }
}
