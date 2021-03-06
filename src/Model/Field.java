package Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sukhanovma on 24.02.2017.
 */
public class Field {

    public double LIVE_BEGIN = 2.0;
    public double LIVE_END = 3.3;
    public double BIRTH_BEGIN = 2.3;
    public double BIRTH_END = 2.9;
    public double FST_IMPACT = 1.0;
    public double SND_IMPACT = 0.3;

    private Cell[][] field;
    private int max_length_of_line = 15;
    private int number_of_lines = 10;
    private int cellSize = 30;

    private int lineWidth = 5;
    private int countCellIsAlive = 0;
    private boolean unsavedChange = false;
    ArrayList<StateAlive> alive = new ArrayList<>();

    private Set<Observer> observers = new HashSet<Observer>();

    private void notifyObservers() {
        for (final Observer observer : observers)
        {
            observer.onObservableChange(this);
        }
    }

    public void addObserver(Observer observer){
        observers.add(observer);
    }

    public void newField(int _max_length_of_line, int _number_of_lines){
        max_length_of_line = _max_length_of_line;
        number_of_lines = _number_of_lines;

        field = new Cell[number_of_lines][];

        for(int i = 0; i < number_of_lines; ++i)
        {
            if (i%2 == 0)
                field[i] = new Cell[max_length_of_line];
            else
                field[i] = new Cell[max_length_of_line - 1];
        }

        for(int i = 0; i < number_of_lines; ++i)
        {
            if (i%2 == 0) {
                for (int j = 0; j < max_length_of_line; ++j) {
                    field[i][j] = new Cell();
                }
            }
            else {
                for (int j = 0; j < max_length_of_line-1; ++j) {
                    field[i][j] = new Cell();
                }
            }
        }
        notifyObservers();
    }

    public Field(int _max_length_of_line, int _number_of_lines){
        max_length_of_line = _max_length_of_line;
        number_of_lines = _number_of_lines;

        field = new Cell[number_of_lines][];

        for(int i = 0; i < number_of_lines; ++i)
        {
            if (i%2 == 0)
                field[i] = new Cell[max_length_of_line];
            else
                field[i] = new Cell[max_length_of_line - 1];
        }

        for(int i = 0; i < number_of_lines; ++i)
        {
            if (i%2 == 0) {
                for (int j = 0; j < max_length_of_line; ++j) {
                    field[i][j] = new Cell();
                }
            }
            else {
                for (int j = 0; j < max_length_of_line-1; ++j) {
                    field[i][j] = new Cell();
                }
            }
        }
        notifyObservers();
    }

    public double getImpact(int _line, int _pos){

        double IMPACT;

        if (_line%2 == 0) //[l,p]
        {

            int FST_COUNT = 0;
            int SND_COUNT = 0;

            if (((_line - 2) >= 0) && isAlive(_line - 2, _pos))
            {
                SND_COUNT++; //[l-2,p]
            }
            if ((_line - 1) >= 0)
            {
                if ((_pos - 2) >= 0 && isAlive(_line - 1, _pos - 2))
                {
                    SND_COUNT++; //[l-1,p-2]
                }
                if ((_pos - 1) >= 0  && isAlive(_line - 1, _pos - 1))
                {
                    FST_COUNT++; //[l-1,p-1]
                }
                if ((_pos) < (max_length_of_line - 1) && isAlive(_line - 1, _pos))
                {
                    FST_COUNT++; //[l-1,p]
                }
                if ((_pos + 1) < (max_length_of_line - 1) && isAlive(_line - 1, _pos + 1))
                {
                    SND_COUNT++; //[l-1,p+1]
                }
            }
            if(_pos - 1 >= 0 && isAlive(_line, _pos - 1))
            {
                FST_COUNT++; //[l,p-1]
            }
            if(_pos + 1 < max_length_of_line && isAlive(_line, _pos + 1))
            {
                FST_COUNT++; //[l,p+1]
            }
            if ((_line + 1) < number_of_lines)
            {
                if ((_pos - 2) >= 0  && isAlive(_line + 1, _pos - 2))
                {
                    SND_COUNT++; //[l+1,p-2]
                }
                if ((_pos - 1) >= 0  && isAlive(_line + 1, _pos - 1))
                {
                    FST_COUNT++; //[l+1,p-1]
                }
                if ((_pos) < (max_length_of_line - 1) && isAlive(_line + 1, _pos))
                {
                    FST_COUNT++; //[l+1,p]
                }
                if ((_pos + 1) < (max_length_of_line - 1) && isAlive(_line + 1, _pos + 1))
                {
                    SND_COUNT++; //[l+1,p+1]
                }
            }
            if ((_line + 2) < number_of_lines  && isAlive(_line + 2, _pos))
            {
                SND_COUNT++; //[l+2,p]
            }
            IMPACT = FST_IMPACT * FST_COUNT + SND_IMPACT * SND_COUNT;
            return IMPACT;
        }

        //строки длины (m-1)
        if (_line%2 != 0) //[l,p]
        {
            int FST_COUNT = 0;
            int SND_COUNT = 0;

            if ((_line - 2) >= 0  && isAlive(_line - 2, _pos))
            {
                SND_COUNT++; //[l-2,p]
            }
            if ((_line - 1) >= 0)
            {
                if ((_pos - 1) >= 0 && isAlive(_line - 1, _pos - 1))
                {
                    SND_COUNT++; //[l-1,p-1]
                }
                if ((_pos) >= 0 && isAlive(_line - 1, _pos))
                {
                    FST_COUNT++; //[l-1,p]
                }
                if ((_pos + 1) < max_length_of_line && isAlive(_line - 1, _pos + 1))
                {
                    FST_COUNT++; //[l-1,p+1]
                }
                if ((_pos + 2) < max_length_of_line && isAlive(_line - 1, _pos + 2))
                {
                    SND_COUNT++; //[l-1,p+2]
                }
            }
            if(_pos - 1 >= 0 && isAlive(_line, _pos - 1))
            {
                FST_COUNT++; //[l,p-1]
            }
            if(_pos + 1 < (max_length_of_line - 1) && isAlive(_line, _pos + 1))
            {
                FST_COUNT++; //[l,p+1]
            }
            if ((_line + 1) < number_of_lines)
            {
                if ((_pos - 1) >= 0 && isAlive(_line + 1, _pos - 1))
                {
                    SND_COUNT++; //[l+1,p-1]
                }
                if ((_pos) >= 0 && isAlive(_line + 1, _pos))
                {
                    FST_COUNT++; //[l+1,p]
                }
                if ((_pos + 1) < max_length_of_line && isAlive(_line + 1, _pos + 1))
                {
                    FST_COUNT++; //[l+1,p+1]
                }
                if ((_pos + 2) < max_length_of_line && isAlive(_line + 1, _pos + 2))
                {
                    SND_COUNT++; //[l+1,p+2]
                }
            }
            if ((_line + 2) < number_of_lines && isAlive(_line + 2, _pos))
            {
                SND_COUNT++; //[l+2,p]
            }
            IMPACT = FST_IMPACT * FST_COUNT + SND_IMPACT * SND_COUNT;
            return IMPACT;
        }
        return 0;
    }

    public boolean isAlive(int _line, int _pos) {
        return field[_line][_pos].getState();
    }

    public void prepare(){
        for (int l = 0; l < number_of_lines; ++l)
        {
            if (l % 2 == 0)
            {
                for (int p = 0; p < max_length_of_line; ++p)
                {
                    double im = getImpact(l, p);
                    if (!isAlive(l,p) && im >= BIRTH_BEGIN && im<=BIRTH_END)
                    {
                        field[l][p].setNewState(true);
                    }
                    else if (isAlive(l,p) && im >=LIVE_BEGIN && im<=LIVE_END)
                    {
                        field[l][p].setNewState(true);
                    }
                    else if (isAlive(l,p) && (im<LIVE_BEGIN || im>LIVE_END))
                    {
                        field[l][p].setNewState(false);
                    }
                }
            }
            else
            {
                for (int p = 0; p < (max_length_of_line - 1); ++p)
                {
                    double im = getImpact(l, p);
                    if (!isAlive(l,p) && im >= BIRTH_BEGIN && im<=BIRTH_END)
                    {
                        field[l][p].setNewState(true);
                    }
                    else if (isAlive(l,p) && im >=LIVE_BEGIN && im<=LIVE_END)
                    {
                        field[l][p].setNewState(true);
                    }
                    else if (isAlive(l,p) && (im<LIVE_BEGIN || im>LIVE_END))
                    {
                        field[l][p].setNewState(false);
                    }
                }
            }
        }
    }

    public void update() {
        prepare();

        for (int l = 0; l < number_of_lines; ++l)
        {
            if (l % 2 == 0)
            {
                for (int p = 0; p < max_length_of_line; ++p)
                {
                    field[l][p].updateState();
                }
            }
            else
            {
                for (int p = 0; p < (max_length_of_line - 1); ++p)
                {
                    field[l][p].updateState();
                }
            }
        }
        unsavedChange = true;
        notifyObservers();
    }

    public int getMaxLengthOfLine(){
        return max_length_of_line;
    }

    public int getNumberOfLines(){
        return number_of_lines;
    }

    public int getLineWidth(){
        return lineWidth;
    }

    public int getCountCellIsAlive(){
        for(int i = 0; i < number_of_lines; ++i)
        {
            if (i%2 == 0) {
                for (int j = 0; j < max_length_of_line; ++j) {
                    if (isAlive(i, j)) {
                        countCellIsAlive++;
                    }
                }
            }
            else {
                for (int j = 0; j < max_length_of_line - 1; ++j) {
                    if (isAlive(i, j)) {
                        countCellIsAlive++;
                    }
                }
            }
        }
        return countCellIsAlive;
    }

    public boolean hasUnsavedChange() {
        return unsavedChange;
    }

    public void setUnsavedChange(boolean _unsavedChange){
        unsavedChange = _unsavedChange;
    }

    public void setCellState(boolean st, int x, int y){
        field[y][x].setNewState(st);
        field[y][x].updateState();
    }

    public double getLIVE_BEGIN() {
        return LIVE_BEGIN;
    }

    public void setLIVE_BEGIN(double LIVE_BEGIN) {
        this.LIVE_BEGIN = LIVE_BEGIN;
    }

    public double getLIVE_END() {
        return LIVE_END;
    }

    public void setLIVE_END(double LIVE_END) {
        this.LIVE_END = LIVE_END;
    }

    public double getBIRTH_BEGIN() {
        return BIRTH_BEGIN;
    }

    public void setBIRTH_BEGIN(double BIRTH_BEGIN) {
        this.BIRTH_BEGIN = BIRTH_BEGIN;
    }

    public double getBIRTH_END() {
        return BIRTH_END;
    }

    public void setBIRTH_END(double BIRTH_END) {
        this.BIRTH_END = BIRTH_END;
    }

    public double getFST_IMPACT() {
        return FST_IMPACT;
    }

    public void setFST_IMPACT(double FST_IMPACT) {
        this.FST_IMPACT = FST_IMPACT;
    }

    public double getSND_IMPACT() {
        return SND_IMPACT;
    }

    public void setSND_IMPACT(double SND_IMPACT) {
        this.SND_IMPACT = SND_IMPACT;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
        notifyObservers();
    }

    public int getCellSize(){
        return cellSize;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void change(int newWidth, int newHeight) {
        this.max_length_of_line = newWidth;
        this.number_of_lines = newHeight;
        notifyObservers();
    }
}

