package Controller;

import Model.Field;
import Model.FileWorker;
import View.Frame;
import java.io.File;

/**
 * Created by sukhanovma on 06.03.2017.
 */
public class Controller {
    private Frame frame;
    private Field field;
    private FileWorker fileWorker = new FileWorker();

    public void newGame() {
        if (field.hasUnsavedChange()) {
            frame.createDialogForSaveInNewGame();
        }
        else {
            frame.fieldSize();
        }
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

    public void changeField(int newWidth, int newHeight){
        field.change(newWidth, newHeight);
    }

    public Field getField(){
        return field;
    }

    public void saveChangeInNewGame() {
        File file = frame.saveFile();
        fileWorker.setModel(field);
        fileWorker.updateFile(file);
        frame.fieldSize();
    }

    public void saveChangeInOpenGame() {
        File file = frame.saveFile();
        fileWorker.setModel(field);
        fileWorker.updateFile(file);
        frame.openFile();
    }

    public void openGame() {
        if (field.hasUnsavedChange()) {
            frame.createDialogForSaveInOpenGame();
        }
        else {
            openNewGame(frame.openFile());
        }
    }

    public void openNewGame(File file) {
        int[] wH = fileWorker.openFile(file, this.field);
    }

    public void nextStep() {
        this.field.update();
    }

    public double getLIVE_BEGIN() {
        return field.getLIVE_BEGIN();
    }

    public void setLIVE_BEGIN(double LIVE_BEGIN) {
        this.field.setLIVE_BEGIN(LIVE_BEGIN);
    }

    public double getLIVE_END() {
        return field.getLIVE_END();
    }

    public void setLIVE_END(double LIVE_END) {
        this.field.setLIVE_END(LIVE_END);
    }

    public double getBIRTH_BEGIN() {
        return field.getBIRTH_BEGIN();
    }

    public void setBIRTH_BEGIN(double BIRTH_BEGIN) {
        this.field.setBIRTH_BEGIN(BIRTH_BEGIN);
    }

    public double getBIRTH_END() {
        return field.getBIRTH_END();
    }

    public void setBIRTH_END(double BIRTH_END) {
        this.field.setBIRTH_END(BIRTH_END);
    }

    public double getFST_IMPACT() {
        return field.getFST_IMPACT();
    }

    public void setFST_IMPACT(double FST_IMPACT) {
        this.field.setFST_IMPACT(FST_IMPACT);
    }

    public double getSND_IMPACT() {
        return field.getSND_IMPACT();
    }

    public void setSND_IMPACT(double SND_IMPACT) {
        this.field.setSND_IMPACT(SND_IMPACT);
    }

    public int getCellSize() {
        return field.getCellSize();
    }

    public void setCellSize(int i) {
        field.setCellSize(i);
    }

    public int getLineWidth() {
        return field.getLineWidth();
    }

    public void setLineWidth(int i) {
        this.field.setLineWidth(i);
    }
}
