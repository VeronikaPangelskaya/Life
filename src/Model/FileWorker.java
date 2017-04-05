package Model;

import java.io.*;
import java.util.Scanner;

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
                out.println((field.getMaxLengthOfLine()) + " " + (field.getNumberOfLines()));
                out.println(field.getLineWidth());
                out.println(field.getCellSize());
                out.println(field.getCountCellIsAlive());
                for (int i = 0; i < field.getNumberOfLines(); ++i) {
                    if (i%2 == 0) {
                        for (int j = 0; j < field.getMaxLengthOfLine(); ++j) {
                            if (field.isAlive(i, j)) {
                                out.println(j + " " + i);
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

    public int[] openFile(File file, Field _field) {
        this.field = _field;
        if (file.exists()) {
            try {
                int[] widthHeight= new int[2];
                Scanner read = new Scanner(file);
                int width = Integer.parseInt(read.next());
                int height = Integer.parseInt(read.next());
                field.newField(width, height);
                int aliveCell = Integer.parseInt(read.next());
                for(int i = 0; i < aliveCell; ++i){
                    int y = Integer.parseInt(read.next());
                    int x = Integer.parseInt(read.next());
                    field.setCellState(true, x, y);
                }

                return widthHeight;

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public void setModel(Field field) {
        this.field = field;
    }
}
