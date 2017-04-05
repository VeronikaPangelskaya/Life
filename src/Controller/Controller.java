package Controller;

import Model.Field;
import Model.FileWorker;
import View.Frame;
import View.Hexagon;

import java.io.File;

/**
 * Created by sukhanovma on 06.03.2017.
 */
public class Controller {
    private Frame frame;
    private Field field;
    private FileWorker fileWorker;
    Hexagon hexagon;

    public void newGame() {
        if (field.hasUnsavedChange()) {
            boolean result = frame.createDialogForExit();
            if(result) {
                fileWorker.updateFile(frame.saveFile());
                frame.fieldSize();
            }
            else {
                frame.fieldSize();
            }
        }
        frame.fieldSize();
    }

    public void setView(Frame frame) {
        this.frame = frame;
    }

    public void setModel(Field field) {
        this.field = field;
    }

    public void updateField(int newWidth, int newHeight) {
        field.newField(newWidth, newHeight);
    }
}
