package View;

import Model.Field;
import Model.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.util.*;

/**
 * Created by sukhanovma on 28.02.2017.
 */
public class Hexagon extends JPanel implements Observer, MouseListener, MouseMotionListener{
    Field field;

    int length;
    int number_of_lines;
    int max_length_of_line;
    int cos30;
    int sin30;
    int lineWidth;
    boolean impact = false;
    boolean repaint = true;

    boolean gameStarted = false;

    int x = 0;
    int y = 0;

    BufferedImage imgField;
    Graphics graf;

    boolean replaceXOR = true; //true - replace

    Map<Point, Point> coordinatesToPoint = new HashMap();
    Map<Point, Point> pointToCoordinates = new HashMap();

    Hexagon(Field _field) {
        this.field = _field;
        length = field.getCellSize();
        lineWidth = field.getLineWidth();
        cos30 = (int) (Math.sqrt(3)/2 * length);
        sin30 = (length /2);
        number_of_lines = field.getNumberOfLines();
        max_length_of_line = field.getMaxLengthOfLine();

        imgField = new BufferedImage(getWidth_(), getHeight_(), BufferedImage.TYPE_INT_ARGB);
        graf = imgField.createGraphics();

        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth_(), getHeight_());
    }

    public int getWidth_(){
        return 2*max_length_of_line*cos30+max_length_of_line;
    }

    public int getHeight_(){
        return (length + sin30 + 1)*(number_of_lines-1) +2*length+2;
    }

    private int sign (int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
    }

    public void drawBresenhamLine (int xstart, int ystart, int xend, int yend) {
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

        dx = xend - xstart;
        dy = yend - ystart;

        incx = sign(dx);
        incy = sign(dy);

        if (dx < 0) dx = -dx;
        if (dy < 0) dy = -dy;

        if (dx > dy)
        {
            pdx = incx;
            pdy = 0;
            es = dy;
            el = dx;
        }
        else
        {
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy;
        }

        x = xstart;
        y = ystart;
        err = el/2;

        graf.setColor(Color.BLACK);
        graf.drawLine(x,y,x,y);

        for (int t = 0; t < el; t++)
        {
            err -= es;
            if (err < 0)
            {
                err += el;
                x += incx;
                y += incy;
            }
            else
            {
                x += pdx;
                y += pdy;
            }
            graf.setColor(Color.BLACK);
            graf.drawLine(x,y,x,y);
        }
    }

    public void drawHexagon(int x, int y, int length) {

        if(lineWidth == 1) {
            ((Graphics2D) graf).setStroke(new BasicStroke(lineWidth));
            drawBresenhamLine(x, y, x + cos30, y - sin30);
            drawBresenhamLine(x + cos30, y - sin30, x + 2 * cos30, y);
            drawBresenhamLine(x + 2 * cos30, y, x + 2 * cos30, y + length);
            drawBresenhamLine(x + 2 * cos30, y + length, x + cos30, y + length + sin30);
            drawBresenhamLine(x + cos30, y + length + sin30, x, y + length);
            drawBresenhamLine(x, y + length, x, y);
        }
        else{
            graf.setColor(Color.BLACK);
            ((Graphics2D) graf).setStroke(new BasicStroke(lineWidth));
            graf.drawLine(x, y, x + cos30, y - sin30);
            graf.drawLine(x + cos30, y - sin30, x + 2 * cos30, y);
            graf.drawLine(x + 2 * cos30, y, x + 2 * cos30, y + length);
            graf.drawLine(x + 2 * cos30, y + length, x + cos30, y + length + sin30);
            graf.drawLine(x + cos30, y + length + sin30, x, y + length);
            graf.drawLine(x, y + length, x, y);
        }
        if(!gameStarted)
        {
            startFilling(imgField, getHeight_(), getWidth_(), x+10, y, Color.WHITE, Color.LIGHT_GRAY);
        }
    }

    public void drawHexagonField(){
        for(int i = 0; i < number_of_lines; ++i){
            if (i%2 == 0)
                for(int j = 0; j < max_length_of_line; ++j) {
                    coordinatesToPoint.put(new Point(j * 2 * cos30+cos30, 2*sin30 + i * 3*sin30),
                            new Point(j,i));
                    pointToCoordinates.put(new Point(j,i),
                            new Point(j * 2 * cos30+cos30, 2*sin30 + i * 3*sin30));
                    drawHexagon(j * 2 * cos30, sin30 + i * (length + sin30), length);
                }
            else {
                for (int j = 0; j < max_length_of_line - 1; ++j) {
                    coordinatesToPoint.put(new Point(j * 2 * cos30 + cos30+cos30, sin30 + i * (length + sin30)+sin30),
                            new Point(j,i));
                    pointToCoordinates.put(new Point(j,i),
                            new Point(j * 2 * cos30 + cos30+cos30, sin30 + i * (length + sin30)+sin30));
                    drawHexagon(j * 2 * cos30 + cos30, sin30 + i * (length + sin30), length);
                }
            }
        }
    }

    public BufferedImage drawImpact(){
        BufferedImage imgImpact = new BufferedImage(getWidth_(), getHeight_(), BufferedImage.TYPE_INT_ARGB);
        Graphics grafImpact = imgImpact.createGraphics();
        grafImpact.setColor(Color.DARK_GRAY);
        for(int i =0; i<number_of_lines; ++i){
            if(i%2 == 0) {
                for(int j = 0; j< max_length_of_line; ++j) {
                    grafImpact.drawString(Double.toString(field.getImpact(i,j)).substring(0,3),
                            (int) pointToCoordinates.get(new Point(j,i)).getX()-cos30/4,
                            (int) pointToCoordinates.get(new Point(j,i)).getY()+sin30/3);
                }
            }
            else{
                for(int j =0; j<max_length_of_line-1; ++j) {
                    grafImpact.drawString(Double.toString(field.getImpact(i,j)).substring(0,3),
                            (int) pointToCoordinates.get(new Point(j,i)).getX()-cos30/4,
                            (int) pointToCoordinates.get(new Point(j,i)).getY()+sin30/3);
                }
            }
        }
        grafImpact.drawImage(imgImpact, 0, 0, this);
        return imgImpact;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        addMouseListener(this);
        addMouseMotionListener(this);
        if (gameStarted) {
            spanFilling(imgField, getHeight_(), getWidth_(), x, y, Color.LIGHT_GRAY, Color.GRAY);
            field.setUnsavedChange(true);
        }

        drawHexagonField();
        for(int i =0; i<number_of_lines; ++i){
            if(i%2 == 0) {
                for(int j = 0; j< max_length_of_line; ++j) {
                    if (this.field.isAlive(i,j))
                    {
                        spanFilling(imgField, getHeight_(), getWidth_(),
                                (int)pointToCoordinates.get(new Point(j,i)).getX(),
                                (int)pointToCoordinates.get(new Point(j,i)).getY(),
                                Color.LIGHT_GRAY, Color.GRAY);
                    }
                    else{
                        spanFilling(imgField, getHeight_(), getWidth_(),
                                (int)pointToCoordinates.get(new Point(j,i)).getX(),
                                (int)pointToCoordinates.get(new Point(j,i)).getY(),
                                Color.GRAY, Color.LIGHT_GRAY);
                    }
                }
            }
            else{
                for(int j =0; j<max_length_of_line-1; ++j) {
                    if (this.field.isAlive(i,j)){
                        spanFilling(imgField, getHeight_(), getWidth_(),
                                (int)pointToCoordinates.get(new Point(j,i)).getX(),
                                (int)pointToCoordinates.get(new Point(j,i)).getY(),
                                Color.LIGHT_GRAY, Color.GRAY);
                    }
                    else{
                        spanFilling(imgField, getHeight_(), getWidth_(),
                                (int)pointToCoordinates.get(new Point(j,i)).getX(),
                                (int)pointToCoordinates.get(new Point(j,i)).getY(),
                                Color.GRAY, Color.LIGHT_GRAY);
                    }
                }
            }
        }
        g.drawImage(imgField, 0, 0, this);
        if(impact) {
            BufferedImage impac = drawImpact();
            g.drawImage(impac, 0, 0, this);
        }
        gameStarted = true;
    }

    @Override
    public void onObservableChange(Field field) {

        this.field = field;
        max_length_of_line = field.getMaxLengthOfLine();
        number_of_lines = field.getNumberOfLines();
        gameStarted = false;
        setPreferredSize(new Dimension(getWidth_(), getHeight_()));
        imgField = new BufferedImage(getWidth_(), getHeight_(), BufferedImage.TYPE_INT_ARGB);
        graf = imgField.getGraphics();
        revalidate();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(repaint) {
            x = e.getX();
            y = e.getY();
            if (x <= getWidth_() && y <= getHeight_()) {
                Raster raster = imgField.getRaster();
                ColorModel model = imgField.getColorModel();
                Object data = raster.getDataElements(x, y, null);
                int argb = model.getRGB(data);
                Color color = new Color(argb, true);
                if (color.equals(Color.LIGHT_GRAY)) {
                    Point cell = centerOfTheSelectedCell();
                    this.field.setCellState(true, (int) cell.getX(), (int) cell.getY());
                    revalidate();
                    repaint();
                }
                if (color.equals(Color.GRAY) && !replaceXOR) {
                    Point cell = centerOfTheSelectedCell();
                    this.field.setCellState(false, (int) cell.getX(), (int) cell.getY());
                    revalidate();
                    repaint();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(repaint) {
            x = e.getX();
            y = e.getY();
            if (x <= getWidth_() && y <= getHeight_()) {
                Raster raster = imgField.getRaster();
                ColorModel model = imgField.getColorModel();
                Object data = raster.getDataElements(x, y, null);
                int argb = model.getRGB(data);
                Color color = new Color(argb, true);
                if (color.equals(Color.LIGHT_GRAY)) {
                    Point cell = centerOfTheSelectedCell();
                    this.field.setCellState(true, (int) cell.getX(), (int) cell.getY());
                    revalidate();
                    repaint();
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void spanFilling(BufferedImage img, int height, int width, int px, int py,
                                   Color targetColor, Color replacementColor) {
        int target = targetColor.getRGB();
        int replacement = replacementColor.getRGB();

        if (target != replacement) {
            Deque<Point> queue = new LinkedList<>();
            queue.add(new Point(px, py));
            Point p;
            while ((p = queue.pollFirst()) != null) {
                int x = p.x;
                int y = p.y;
                Raster raster = imgField.getRaster();
                ColorModel model = imgField.getColorModel();
                while (x > 0 && (model.getRGB(raster.getDataElements(x-1, y, null)) == target)) {
                    x--;
                }
                boolean spanAbove = false;
                boolean spanBelow = false;

                while (x < width && (model.getRGB(raster.getDataElements(x, y, null)) == target)) {
                    img.setRGB(x, y, replacement);
                    if (!spanAbove && y > 0 && (model.getRGB(raster.getDataElements(x, y-1, null)) == target)) {
                        queue.add(new Point(x, y - 1));
                        spanAbove = true;
                    } else if (spanAbove && y > 0 && (model.getRGB(raster.getDataElements(x, y-1, null)) != target)) {
                        spanAbove = false;
                    }
                    if (!spanBelow && y < height - 1 && (model.getRGB(raster.getDataElements(x, y+1, null)) == target)) {
                        queue.add(new Point(x, y + 1));
                        spanBelow = true;
                    } else if (spanBelow && y < height - 1 && (model.getRGB(raster.getDataElements(x, y+1, null)) != target)) {
                        spanBelow = false;
                    }
                    x++;
                }
            }
        }
    }

    public static void startFilling(BufferedImage img, int height, int width, int px, int py,
                                   Color targetColor, Color replacementColor) {
        int target = targetColor.getRGB();
        int replacement = replacementColor.getRGB();

        if (target != replacement) {
            Deque<Point> queue = new LinkedList<>();
            queue.add(new Point(px, py));
            Point p;
            while ((p = queue.pollFirst()) != null) {
                int x = p.x;
                int y = p.y;

                while (x > 0 && (img.getRGB(x - 1, y) == target || img.getRGB(x - 1, y) == 0)) {
                    x--;
                }
                boolean spanAbove = false;
                boolean spanBelow = false;

                while (x < width && (img.getRGB(x , y) == target || img.getRGB(x, y) == 0)) {
                    img.setRGB(x, y, replacement);
                    if (!spanAbove && y > 0 && (img.getRGB(x , y-1) == target || img.getRGB(x, y-1) == 0)) {
                        queue.add(new Point(x, y - 1));
                        spanAbove = true;
                    } else if (spanAbove && y > 0 && (img.getRGB(x , y-1) != target && img.getRGB(x, y-1) != 0)) {
                        spanAbove = false;
                    }
                    if (!spanBelow && y < height - 1 && (img.getRGB(x , y+1) == target || img.getRGB(x, y+1) == 0)) {
                        queue.add(new Point(x, y + 1));
                        spanBelow = true;
                    } else if (spanBelow && y < height - 1 && (img.getRGB(x , y+1) != target && img.getRGB(x, y+1) != 0)) {
                        spanBelow = false;
                    }
                    x++;
                }
            }
        }
    }

    private double distanceToCenter(int x, int y, int centerX, int centerY){
        double dist = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        return dist;
    }


    private Point centerOfTheSelectedCell(){
        int _x = x;
        int _y = y;
        double d1 = 0;
        double d2 = 0;
        double d3 = 0;
        double d4 = 0;

        if(_y >= 2*sin30 && _y <= (number_of_lines-1)*(sin30+length) + length
                && _x >= cos30 && _x <= max_length_of_line*2*cos30-cos30)
        {
            for(int i = 2*sin30; i < this.getHeight_(); i+=3*sin30){
                for(int j = 0; j < this.getWidth_(); j+=cos30) {
                    if ((_y > i) && (_y <= i + 3 * sin30) && (_x > j) && (_x <= (j+cos30))) {
                        if(coordinatesToPoint.containsKey(new Point(j,i))){
                            d1 = distanceToCenter(_x,_y,j,i);
                        }
                        if(coordinatesToPoint.containsKey(new Point(j,i+3*sin30))){
                            d2 = distanceToCenter(_x,_y,j,i+3*sin30);
                        }
                        if(coordinatesToPoint.containsKey(new Point(j+cos30,i+3*sin30))){
                            d3 = distanceToCenter(_x,_y,j+cos30,i+3*sin30);
                            if(d1<d3) {
                                return coordinatesToPoint.get(new Point(j,i));
                            }
                            else{
                                return coordinatesToPoint.get(new Point(j+cos30,i+3*sin30));
                            }
                        }
                        if(coordinatesToPoint.containsKey(new Point(j+cos30,i))){
                            d4 = distanceToCenter(_x,_y,j+cos30,i);
                            if(d2<d4) {
                                return coordinatesToPoint.get(new Point(j,i+3*sin30));
                            }
                            else{
                                return coordinatesToPoint.get(new Point(j+cos30,i));
                            }
                        }

                    }
                }
            }
        }
        else if(_y < 2*sin30){
            for(int i = 0; i < getWidth_(); i+=2*cos30){
                if(_x > i && _x < i+2*cos30){
                    return coordinatesToPoint.get(new Point(i+cos30, length));
                }
            }
        }
        else if(_y > (number_of_lines-1)*(sin30+length) + length){
            if(max_length_of_line%2 == 0)
            {
                for(int i = 0; i < getWidth_(); i+=2*cos30){
                    if(_x > i && _x < i+2*cos30){
                        return coordinatesToPoint.get(new Point(i+cos30, (number_of_lines-1)*(sin30+length) + length));
                    }
                }
            }
            else
            {
                for(int i = cos30; i < getWidth_()-cos30; i+=2*cos30){
                    if(_x > i && _x < i+2*cos30){
                        return coordinatesToPoint.get(new Point(i+cos30, (number_of_lines-1)*(sin30+length) + length));
                    }
                }
            }
        }
        else if(_x <= cos30 && _y >= 2*sin30 && _y <= (number_of_lines-1)*(sin30+length) + length)
        {
            for(int i = 2*sin30; i < this.getHeight_(); i+=3*sin30){
                if(_y >= i && _y < i+3*sin30) {
                    if (coordinatesToPoint.containsKey(new Point(cos30, i))) {
                        return coordinatesToPoint.get(new Point(cos30, i));
                    } else if (coordinatesToPoint.containsKey(new Point(cos30, i + 3 * sin30))) {
                        return coordinatesToPoint.get(new Point(cos30, i + 3 * sin30));
                    }
                }
            }
        }
        else if(_x <= max_length_of_line*2*cos30 && _y >= 2*sin30 && _y <= (number_of_lines-1)*(sin30+length) + length)
        {
            for(int i = 2*sin30; i < this.getHeight_(); i+=3*sin30){
                if(_y >= i && _y < i+3*sin30) {
                    if (coordinatesToPoint.containsKey(new Point(max_length_of_line*2*cos30-cos30, i))) {
                        return coordinatesToPoint.get(new Point(max_length_of_line*2*cos30-cos30, i));
                    } else if (coordinatesToPoint.containsKey(new Point(max_length_of_line*2*cos30-cos30, i + 3 * sin30))) {
                        return coordinatesToPoint.get(new Point(max_length_of_line*2*cos30-cos30, i + 3 * sin30));
                    }
                }
            }
        }
        repaint();
        return new Point(cos30,sin30+length/2);
    }


    public void setXOR() {
        replaceXOR = false;
    }

    public void setReplace() {
        replaceXOR = true;
    }

    public void clear() {
        for(int i =0; i<number_of_lines; ++i){
            if(i%2 == 0) {
                for(int j = 0; j< max_length_of_line; ++j) {
                    if (this.field.isAlive(i,j)) {
                        this.field.setCellState(false, j, i);
                    }
                }
            }
            else{
                for(int j =0; j<max_length_of_line-1; ++j) {
                    if (this.field.isAlive(i,j)){
                        this.field.setCellState(false,j, i);
                    }
                }
            }
        }
        repaint();
    }

    public void setRepaint(boolean b) {
        repaint = b;
    }

    public void setImpact(boolean b) {
        impact = b;
    }

    public int getNumberOfLines() {
        return number_of_lines;
    }

    public int getMaxLengthOfLine() {
        return max_length_of_line;
    }

    public int getLength() {
        return length;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLength(int i) {
        if(i%2 == 0)
            length = i;
        else
            length = i-1;
        cos30 = (int) (Math.sqrt(3)/2 * length);
        sin30 = (length /2);
        pointToCoordinates.clear();
        coordinatesToPoint.clear();
        imgField = new BufferedImage(getWidth_(), getHeight_(), BufferedImage.TYPE_INT_ARGB);
        graf = imgField.createGraphics();
        revalidate();
        gameStarted = false;
    }

    public void setLineWidth(int i) {
        lineWidth = i;
        imgField = new BufferedImage(getWidth_(), getHeight_(), BufferedImage.TYPE_INT_ARGB);
        graf = imgField.createGraphics();
        gameStarted = false;
    }
}
