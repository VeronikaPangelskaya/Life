package View;

import Model.Field;
import Model.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by sukhanovma on 28.02.2017.
 */
public class Hexagon extends JPanel implements Observer{
    Field field;

    int length = 30;
    int number_of_lines = 10;
    int max_length_of_line = 15;
    int cos30 = (int) (Math.sqrt(3)/2 * length);
    int sin30 = (length /2);


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(2*cos30*max_length_of_line, (sin30 + length)*number_of_lines);
        //return new Dimension(1000, 1000);
    }

    private int sign (int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
        //возвращает 0, если аргумент (x) равен нулю; -1, если x < 0 и 1, если x > 0.
    }

    public void drawBresenhamLine (int xstart, int ystart, int xend, int yend, Graphics g) {
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

        dx = xend - xstart;//проекция на ось икс
        dy = yend - ystart;//проекция на ось игрек

        incx = sign(dx);
        //System.out.println(incx);
	/*
	 * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
	 * справа налево по иксу, то incx будет равен -1.
	 * Это будет использоваться в цикле постороения.
	 */
        incy = sign(dy);
        //System.out.println(incy);
	/*
	 * Аналогично. Если рисуем отрезок снизу вверх -
	 * это будет отрицательный сдвиг для y (иначе - положительный).
	 */

        if (dx < 0) dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
        if (dy < 0) dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|
        //эти две строчки можно записать и так: dx = Math.abs(dx); dy = Math.abs(dy);

        if (dx > dy)
        //определяем наклон отрезка:
        {
	 /*
	  * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
	  * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
	  * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
	  * по y сдвиг такой отсутствует.
	  */
            pdx = incx;	pdy = 0;
            es = dy;	el = dx;
        }
        else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
        {
            pdx = 0;	pdy = incy;
            es = dx;	el = dy;//тогда в цикле будем двигаться по y
        }

        x = xstart;
        y = ystart;
        err = el/2;
        g.drawLine (x, y, x, y);//ставим первую точку
        //все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла

        for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
        {
            err -= es;
            if (err < 0)
            {
                err += el;
                x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += incy;//или сместить влево-вправо, если цикл проходит по y
            }
            else
            {
                x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
            }

            g.drawLine (x, y, x, y);
        }
    }

    public void drawHexagon(int x, int y, int length, Graphics g) {

        drawBresenhamLine(x, y, x + cos30, y - sin30, g);
        drawBresenhamLine(x + cos30, y - sin30, x + 2*cos30, y, g);
        drawBresenhamLine(x + 2*cos30, y, x+2*cos30, y+length, g);
        drawBresenhamLine(x+2*cos30, y+length, x + cos30, y + length+sin30, g);
        drawBresenhamLine(x + cos30, y + length+sin30, x, y + length, g);
        drawBresenhamLine(x, y + length, x, y, g);

    }

    @Override
    public void paintComponent(Graphics g) {
        revalidate();
        super.paintComponent(g);
        g.setColor(Color.black);
        for(int i = 0; i < number_of_lines; ++i){
                if (i%2 == 0)
                    for(int j = 0; j < max_length_of_line; ++j)
                        drawHexagon(j*2*cos30, sin30 + i*(length+sin30), length, g);
                else
                    for(int j = 0; j < max_length_of_line-1; ++j)
                        drawHexagon(j*2*cos30+cos30, sin30+i*(length+sin30), length, g);
        }
    }

    @Override
    public void onObservableChange(Field field) {
        this.field = field;
        max_length_of_line = field.getMaxLengthOfLine();
        number_of_lines = field.getNumberOfLines();
        repaint();
    }

    public void setNumberOfLine(int height){
        number_of_lines = height;
    }

    public void setMaxLengthOfLine(int width){
        max_length_of_line = width;
    }

//    public int getWidth(){
//        return  2*cos30*max_length_of_line;
//    }
//
//    public int getHeight(){
//        return (sin30 + length)*number_of_lines;
//    }


//    public static void spanFilling(BufferedImage img, int height, int width, int px, int py,
//                                   Color targetColor, Color replacementColor) {
//        int target = targetColor.getRGB();
//        int replacement = replacementColor.getRGB();
//
//        if (target != replacement) {
//            Deque<Point> queue = new LinkedList<>();
//            queue.add(new Point(px, py));
//            Point p;
//            while ((p = queue.pollFirst()) != null) {
//                int x = p.x;
//                int y = p.y;
//                while (x > 0 && img.getRGB(x - 1, y) == target) {
//                    x--;
//                }
//                boolean spanAbove = false;
//                boolean spanBelow = false;
//
//                while (x < width && img.getRGB(x, y) == target) {
//                    img.setRGB(x, y, replacement);
//                    if (!spanAbove && y > 0 && img.getRGB(x, y - 1) == target) {
//                        queue.add(new Point(x, y - 1));
//                        spanAbove = true;
//                    } else if (spanAbove && y > 0 && img.getRGB(x, y - 1) != target) {
//                        spanAbove = false;
//                    }
//                    if (!spanBelow && y < height - 1 && img.getRGB(x, y + 1) == target) {
//                        queue.add(new Point(x, y + 1));
//                        spanBelow = true;
//                    } else if (spanBelow && y < height - 1 && img.getRGB(x, y + 1) != target) {
//                        spanBelow = false;
//                    }
//                    x++;
//                }
//            }
//        }
//    }


}
