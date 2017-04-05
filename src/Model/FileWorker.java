package Model;

import Model.Field;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by sukhanovma on 24.02.2017.
 */
public class FileWorker
{
    Field field;

    public void updateFile(File file) {
        if (file.exists()) {
            try {
                FileWriter newFile = new FileWriter(file);
                PrintWriter out = new PrintWriter(newFile, true);
                out.println((field.getMaxLengthOfLine()) + " " + (field.getNumberOfLines()) + " //field length and width");
                out.println(field.getLineWidth() + " //line width");
                out.println(field.getCellSize() + " //size of the cell");
                out.println(field.getCountCellIsAlive() + " //amount of cells");
                for (int i = 0; i <= field.getNumberOfLines(); ++i) {
                    if (i%2 == 0) {
                        for (int j = 0; j < field.getMaxLengthOfLine(); ++j) {
                            if (field.isAlive(i, j)) {
                                out.println(i + " " + j);
                            }
                        }
                    }
                    else {
                        for (int j = 0; j < field.getMaxLengthOfLine() - 1; ++j) {
                            if (field.isAlive(i, j)) {
                                out.println(i + " " + j);
                            }
                        }
                    }
                }
                out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
