package View;

import Controller.Controller;
import Model.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Frame extends JFrame
{
    private Hexagon hexagon;
    private Controller controller;
    private int  newWidth = 0;
    private int newHeight = 0;
    private JScrollPane scrollPane;
    private JToolBar toolbar;
    private boolean stopGame = true;
    private JButton toolClear;
    private JButton toolNext;
    private JMenuItem menuClear;
    private JMenuItem menuNext;
    private boolean imp = false;


    public File saveFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.LIFE", "*.*");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try{
            File f = fileChooser.getSelectedFile();
            f.createNewFile();
            return f;}
            catch (IOException e)
            {

            }
        }
        return null;
    }

    public File openFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.LIFE", "*.*");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try{
                File f = fileChooser.getSelectedFile();
                f.createNewFile();
                return f;}
            catch (IOException e)
            {

            }
        }
        return null;
    }

    private JMenu createMenuFile() {
        JMenu file = new JMenu("Файл");
        file.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JMenuItem new_game = new JMenuItem("Новая игра");
        new_game.setPreferredSize(new Dimension(200, 30));
        new_game.setFont(new Font("Tahoma", Font.PLAIN, 16));
        new_game.setAccelerator(KeyStroke.getKeyStroke("N".charAt(0), KeyEvent.CTRL_MASK));
        new_game.setIcon(new ImageIcon("res/new_game.png"));
        new_game.addActionListener(e -> controller.newGame());
        file.add(new_game);

        JMenuItem open = new JMenuItem("Открыть...");
        open.setFont(new Font("Tahoma", Font.PLAIN, 16));
        open.setAccelerator(KeyStroke.getKeyStroke("O".charAt(0), KeyEvent.CTRL_MASK));
        open.setIcon(new ImageIcon("res/open.png"));
        open.addActionListener(e -> controller.openGame());
        file.add(open);

        JMenuItem save = new JMenuItem("Сохранить");
        save.setFont(new Font("Tahoma", Font.PLAIN, 16));
        save.setAccelerator(KeyStroke.getKeyStroke("S".charAt(0), KeyEvent.CTRL_MASK));
        save.setIcon(new ImageIcon("res/save.png"));
        save.addActionListener(e -> saveFile());
        file.add(save);

        file.addSeparator();

        JMenuItem exit = new JMenuItem("Выход");
        exit.setFont(new Font("Tahoma", Font.PLAIN, 16));
        exit.setIcon(new ImageIcon("res/close.png"));
        exit.addActionListener(e -> System.exit(1));

        file.add(exit);

        return file;
    }

    public void createDialogForSaveInNewGame(){

        JDialog dialog = new JDialog(this);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(250, 150);
        dialog.setLocation(600, 400);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextArea textWidth = new JTextArea("Сохранить изменения?\n");
        textWidth.setFont(new Font("Tahoma", Font.PLAIN, 16));
        textWidth.setSize(200,30);
        textWidth.setEditable(false);
        textWidth.setOpaque(false);
        textWidth.setLineWrap(true);
        panel.add(textWidth);

        JButton yes = new JButton("Да");
        yes.addActionListener(e -> {
            dialog.dispose();
            controller.saveChangeInNewGame();
        });
        panel.add(yes);

        JButton no = new JButton("Нет");
        no.addActionListener(e -> {
            dialog.dispose();
            fieldSize();
        });
        panel.add(no);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    public void createDialogForSaveInOpenGame(){

        JDialog dialog = new JDialog(this);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(250, 150);
        dialog.setLocation(600, 400);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextArea textWidth = new JTextArea("Сохранить изменения?\n");
        textWidth.setFont(new Font("Tahoma", Font.PLAIN, 16));
        textWidth.setSize(200,30);
        textWidth.setEditable(false);
        textWidth.setOpaque(false);
        textWidth.setLineWrap(true);
        panel.add(textWidth);

        JButton yes = new JButton("Да");
        yes.addActionListener(e -> {
            dialog.dispose();
            controller.saveChangeInOpenGame();
        });
        panel.add(yes);

        JButton no = new JButton("Нет");
        no.addActionListener(e -> {
            dialog.dispose();
            controller.openNewGame(openFile());
        });
        panel.add(no);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JMenu createMenuEdit() {
        JMenu edit = new JMenu("Редактировать");
        edit.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JRadioButtonMenuItem XOR = new JRadioButtonMenuItem("XOR");
        XOR.setFont(new Font("Tahoma", Font.PLAIN, 16));
        XOR.setPreferredSize(new Dimension(200, 30));
        XOR.setIcon(new ImageIcon("res/xor.png"));
        XOR.addActionListener(e -> hexagon.setXOR());
        JRadioButtonMenuItem replace = new JRadioButtonMenuItem("Replace");
        replace.setFont(new Font("Tahoma", Font.PLAIN, 16));
        replace.setIcon(new ImageIcon("res/xor.png"));
        replace.addActionListener(e -> hexagon.setReplace());
        replace.setSelected(true);

        ButtonGroup bg = new ButtonGroup();
        bg.add(XOR);
        bg.add(replace);
        edit.add(XOR);
        edit.add(replace);

        edit.addSeparator();

        JMenuItem param = new JMenuItem("Параметры");
        param.setFont(new Font("Tahoma", Font.PLAIN, 16));
        param.setIcon(new ImageIcon("res/param.png"));
        param.addActionListener(e -> {
            JDialog dialog = createDialogForParam("Параметры", true);
            dialog.setVisible(true);
        });
        edit.add(param);

        edit.addSeparator();

        JCheckBoxMenuItem impact  = new JCheckBoxMenuItem("Impact");
        impact.setIcon(new ImageIcon("res/1.png"));
        impact.setFont(new Font("Tahoma", Font.PLAIN, 16));
        edit.add(impact);

        return edit;
    }

    private JDialog createDialogForParam(String title, boolean modal) {
        JDialog dialog = new JDialog(this, title, modal);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(700, 265);
        dialog.setLocation(400,300);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));


        JPanel fieldSizePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fieldSizePanel.setBorder(BorderFactory.createTitledBorder("Размеры поля"));
        fieldSizePanel.setPreferredSize(new Dimension(110, 100));
        JTextArea textWidth = new JTextArea("Ширина:");
        textWidth.setSize(50,10);
        textWidth.setEditable(false);
        textWidth.setOpaque(false);
        textWidth.setLineWrap(true);
        JTextArea textHeight = new JTextArea("Высота:");
        textHeight.setSize(50,10);
        textHeight.setEditable(false);
        textHeight.setOpaque(false);
        textHeight.setLineWrap(true);
        JTextField fieldSizeWidth = new JTextField(String.valueOf(hexagon.getMaxLengthOfLine()), 2);
        JTextField fieldSizeHeight = new JTextField(String.valueOf(hexagon.getNumberOfLines()),2);
        fieldSizeWidth.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                int a = 10;
                try
                {
                    final int fieldValue = Integer.parseInt(fieldSizeWidth.getText());
                    if ((fieldValue <= 100) && (fieldValue >= 2))
                    {
                        a = fieldValue;
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldSizeWidth.setText(String.valueOf(a));
                }
            }
        });
        fieldSizeHeight.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                int a = 10;
                try
                {
                    final int fieldValue = Integer.parseInt(fieldSizeHeight.getText());
                    if ((fieldValue <= 100) && (fieldValue >= 2))
                    {
                        a = fieldValue;
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldSizeHeight.setText(String.valueOf(a));
                }
            }
        });
        fieldSizePanel.add(textWidth);
        fieldSizePanel.add(fieldSizeWidth);
        fieldSizePanel.add(textHeight);
        fieldSizePanel.add(fieldSizeHeight);


        JPanel drawPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        drawPanel.setBorder(BorderFactory.createTitledBorder("Рисование"));
        drawPanel.setPreferredSize(new Dimension(110, 100));
        JRadioButton XOR = new JRadioButton("XOR");
        JRadioButton replace = new JRadioButton("Replace");
        ButtonGroup bg = new ButtonGroup();
        bg.add(XOR);
        bg.add(replace);
        replace.addActionListener(e -> hexagon.setReplace());
        XOR.addActionListener(e -> hexagon.setXOR());
        replace.setSelected(true);
        drawPanel.add(XOR);
        drawPanel.add(replace);


        JPanel cellPropertiesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cellPropertiesPanel.setBorder(BorderFactory.createTitledBorder("Параметры клетки"));
        cellPropertiesPanel.setPreferredSize(new Dimension(400, 100));
        JTextArea textLineWidth = new JTextArea("Ширина линии:");
        textLineWidth.setEditable(false);
        textLineWidth.setOpaque(false);
        textLineWidth.setLineWrap(true);
        JTextArea textCellSize = new JTextArea("Размер клетки:");
        textCellSize.setEditable(false);
        textCellSize.setOpaque(false);
        textCellSize.setLineWrap(true);
        JTextField fieldLineWidth = new JTextField(String.valueOf(hexagon.getLineWidth()),2);
        JTextField fieldCellSize = new JTextField(String.valueOf(hexagon.getLength()),2);
        JSlider sliderLineWidth = new JSlider(1, 10, hexagon.getLineWidth());
        sliderLineWidth.setPaintLabels(true);
        sliderLineWidth.setMajorTickSpacing(3);
        sliderLineWidth.addChangeListener(e -> fieldLineWidth.setText(String.valueOf(sliderLineWidth.getValue())));
        JSlider sliderCellSize = new JSlider(20, 100, hexagon.getLength());
        sliderCellSize.setPaintLabels(true);
        sliderCellSize.setMajorTickSpacing(20);
        sliderCellSize.addChangeListener(e -> fieldCellSize.setText(String.valueOf(sliderCellSize.getValue())));
        fieldCellSize.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try
                {
                    final int fieldValue = Integer.parseInt(fieldCellSize.getText());
                    if ((fieldValue >= sliderCellSize.getMinimum()) && (fieldValue <= sliderCellSize.getMaximum()))
                    {
                        sliderCellSize.setValue(fieldValue);
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldCellSize.setText(Integer.toString(sliderCellSize.getValue()));
                }
            }
        });
        fieldLineWidth.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try
                {
                    final int fieldValue = Integer.parseInt(fieldLineWidth.getText());
                    if ((fieldValue >= sliderLineWidth.getMinimum()) && (fieldValue <= sliderLineWidth.getMaximum()))
                    {
                        sliderLineWidth.setValue(fieldValue);
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldLineWidth.setText(Integer.toString(sliderLineWidth.getValue()));
                }
            }
        });
        cellPropertiesPanel.add(textLineWidth);
        cellPropertiesPanel.add(fieldLineWidth);
        cellPropertiesPanel.add(sliderLineWidth);
        cellPropertiesPanel.add(textCellSize);
        cellPropertiesPanel.add(fieldCellSize);
        cellPropertiesPanel.add(sliderCellSize);

        JPanel panelGameRules = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelGameRules.setBorder(BorderFactory.createTitledBorder("Impact"));
        panelGameRules.setPreferredSize(new Dimension(150, 80));
        JTextArea textFST_IMPACT = new JTextArea("FST_IMPACT");
        textFST_IMPACT.setEditable(false);
        textFST_IMPACT.setOpaque(false);
        textFST_IMPACT.setLineWrap(true);
        JTextArea textSND_IMPACT = new JTextArea("SND_IMPACT");
        textSND_IMPACT.setEditable(false);
        textSND_IMPACT.setOpaque(false);
        textSND_IMPACT.setLineWrap(true);
        JTextField fieldFST_IMPACT = new JTextField(String.valueOf(controller.getFST_IMPACT()),2);
        JTextField fieldSND_IMPACT = new JTextField(String.valueOf(controller.getSND_IMPACT()),2);
        fieldFST_IMPACT.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                double a = 1.0;
                try
                {
                    double fieldValue = Double.parseDouble(fieldFST_IMPACT.getText());
                    System.out.println(fieldValue);
                    if ((fieldValue <= 9.9) && (fieldValue >= 0.1))
                    {
                        a = fieldValue;
                        System.out.println(a);
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldFST_IMPACT.setText(String.valueOf(a));
                }
            }
        });
        fieldSND_IMPACT.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                double a = 0.3;
                try
                {
                    double fieldValue = Double.parseDouble(fieldSND_IMPACT.getText());
                    System.out.println(fieldValue);
                    if ((fieldValue <= 9.9) && (fieldValue >= 0.1))
                    {
                        a = fieldValue;
                        System.out.println(a);
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldSND_IMPACT.setText(String.valueOf(a));
                }
            }
        });
        panelGameRules.add(textFST_IMPACT);
        panelGameRules.add(fieldFST_IMPACT);
        panelGameRules.add(textSND_IMPACT);
        panelGameRules.add(fieldSND_IMPACT);

        JPanel panelHowLive = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelHowLive.setBorder(BorderFactory.createTitledBorder("Рождение, жизнь, смерть"));
        panelHowLive.setPreferredSize(new Dimension(300, 80));
        JTextArea textLIVE_START = new JTextArea("LIVE_START");
        textLIVE_START.setEditable(false);
        textLIVE_START.setOpaque(false);
        textLIVE_START.setLineWrap(true);
        JTextArea textLIVE_END = new JTextArea("LIVE_END");
        textLIVE_END.setEditable(false);
        textLIVE_END.setOpaque(false);
        textLIVE_END.setLineWrap(true);
        JTextArea textBIRTH_START = new JTextArea("BIRTH_START");
        textBIRTH_START.setEditable(false);
        textBIRTH_START.setOpaque(false);
        textBIRTH_START.setLineWrap(true);
        JTextArea textBIRTH_END = new JTextArea("BIRTH_END");
        textBIRTH_END.setEditable(false);
        textBIRTH_END.setOpaque(false);
        textBIRTH_END.setLineWrap(true);
        JTextField fieldLIVE_START = new JTextField(String.valueOf(controller.getLIVE_BEGIN()),2);
        fieldLIVE_START.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                double a = 2.0;
                try
                {
                    double fieldValue = Double.parseDouble(fieldLIVE_START.getText());
                    System.out.println(fieldValue);
                    if ((fieldValue <= 9.9) && (fieldValue >= 0.1))
                    {
                        a = fieldValue;
                        System.out.println(a);
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldLIVE_START.setText(String.valueOf(a));
                }
            }
        });
        JTextField fieldLIVE_END = new JTextField(String.valueOf(controller.getLIVE_END()),2);
        fieldLIVE_END.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                double a = 3.3;
                try
                {
                    double fieldValue = Double.parseDouble(fieldLIVE_END.getText());
                    System.out.println(fieldValue);
                    if ((fieldValue <= 9.9) && (fieldValue >= 0.1))
                    {
                        a = fieldValue;
                        System.out.println(a);
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldLIVE_END.setText(String.valueOf(a));
                }
            }
        });
        JTextField fieldBIRTH_START = new JTextField(String.valueOf(controller.getBIRTH_BEGIN()),2);
        fieldBIRTH_START.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                double a = 2.3;
                try
                {
                    double fieldValue = Double.parseDouble(fieldBIRTH_START.getText());
                    System.out.println(fieldValue);
                    if ((fieldValue <= 9.9) && (fieldValue >= 0.1))
                    {
                        a = fieldValue;
                        System.out.println(a);
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldBIRTH_START.setText(String.valueOf(a));
                }
            }
        });
        JTextField fieldBIRTH_END = new JTextField(String.valueOf(controller.getBIRTH_END()),2);
        fieldBIRTH_END.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                double a = 2.9;
                try
                {
                    double fieldValue = Double.parseDouble(fieldBIRTH_END.getText());
                    System.out.println(fieldValue);
                    if ((fieldValue <= 9.9) && (fieldValue >= 0.1))
                    {
                        a = fieldValue;
                        System.out.println(a);
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldBIRTH_END.setText(String.valueOf(a));
                }
            }
        });
        panelHowLive.add(textLIVE_START);
        panelHowLive.add(fieldLIVE_START);
        panelHowLive.add(textLIVE_END);
        panelHowLive.add(fieldLIVE_END);
        panelHowLive.add(textBIRTH_START);
        panelHowLive.add(fieldBIRTH_START);
        panelHowLive.add(textBIRTH_END);
        panelHowLive.add(fieldBIRTH_END);

        JPanel panelOkCancel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelOkCancel.setPreferredSize(new Dimension(100, 80));
        JButton ok = new JButton("Готово");
        ok.addActionListener(e -> {
            if(Integer.parseInt(fieldSizeWidth.getText()) != hexagon.getMaxLengthOfLine()
                    || Integer.parseInt(fieldSizeHeight.getText()) != hexagon.getNumberOfLines()){
                controller.changeField(Integer.parseInt(fieldSizeWidth.getText()), Integer.parseInt(fieldSizeHeight.getText()));
            }
            if(Double.parseDouble(fieldFST_IMPACT.getText()) != controller.getFST_IMPACT())
                controller.setFST_IMPACT(Double.parseDouble(fieldFST_IMPACT.getText()));
            if(Double.parseDouble(fieldSND_IMPACT.getText()) != controller.getSND_IMPACT())
                controller.setSND_IMPACT(Double.parseDouble(fieldSND_IMPACT.getText()));
            if(Double.parseDouble(fieldLIVE_START.getText()) != controller.getLIVE_BEGIN())
                controller.setLIVE_BEGIN(Double.parseDouble(fieldLIVE_START.getText()));
            if(Double.parseDouble(fieldLIVE_END.getText()) != controller.getLIVE_END())
                controller.setLIVE_END(Double.parseDouble(fieldLIVE_END.getText()));
            if(Double.parseDouble(fieldBIRTH_START.getText()) != controller.getBIRTH_BEGIN())
                controller.setBIRTH_BEGIN(Double.parseDouble(fieldBIRTH_START.getText()));
            if(Double.parseDouble(fieldBIRTH_END.getText()) != controller.getBIRTH_END())
                controller.setBIRTH_END(Double.parseDouble(fieldBIRTH_END.getText()));

            if(Integer.parseInt(fieldCellSize.getText()) != controller.getCellSize())
            {
                hexagon.setLength(Integer.parseInt(fieldCellSize.getText()));
                controller.setCellSize(Integer.parseInt(fieldCellSize.getText()));
                hexagon.repaint();
            }
            if(Integer.parseInt(fieldLineWidth.getText()) != controller.getLineWidth())
            {
                hexagon.setLineWidth(Integer.parseInt(fieldLineWidth.getText()));
                controller.setLineWidth(Integer.parseInt(fieldLineWidth.getText()));
//                    hexagon = new Hexagon(controller.getField());
                hexagon.repaint();
            }
            dialog.dispose();
        });

        JButton cancel = new JButton("Отмена");

        panelOkCancel.add(ok);
        panelOkCancel.add(cancel);


        panel.add(fieldSizePanel);
        panel.add(drawPanel);
        panel.add(cellPropertiesPanel);
        panel.add(panelGameRules);
        panel.add(panelHowLive);
        panel.add(panelOkCancel);
        dialog.add(panel);
        return dialog;
    }

    private JMenu createMenuAction(){
        JMenu action = new JMenu("Поле");
        action.setFont(new Font("Tahoma", Font.PLAIN, 16));

        menuClear = new JMenuItem("Очистить поле");
        menuClear.setPreferredSize(new Dimension(200, 30));
        menuClear.setFont(new Font("Tahoma", Font.PLAIN, 16));
        menuClear.setIcon(new ImageIcon("res/clear.png"));
        menuClear.addActionListener(e -> hexagon.clear());

        action.add(menuClear);

        menuNext = new JMenuItem("Следующий шаг");
        menuNext.setFont(new Font("Tahoma", Font.PLAIN, 16));
        menuNext.setIcon(new ImageIcon("res/next.png"));
        menuNext.addActionListener(e -> controller.nextStep());
        action.add(menuNext);

        JMenuItem run = new JMenuItem("Играть");
        run.setFont(new Font("Tahoma", Font.PLAIN, 16));
        run.setIcon(new ImageIcon("res/run.png"));
        run.addActionListener(e -> {
            if (stopGame) {
                stopGame = false;
                runGame();
                menuNext.setEnabled(false);
                menuClear.setEnabled(false);
                toolNext.setEnabled(false);
                toolClear.setEnabled(false);
            }
            else{
                stopGame = true;
                menuNext.setEnabled(true);
                menuClear.setEnabled(true);
                hexagon.setRepaint(true);
                toolNext.setEnabled(true);
                toolClear.setEnabled(true);
            }
        });
        action.add(run);

        return action;

    }

    private JMenu createMenuHelp(){
        JMenu help = new JMenu("Помощь");
        help.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JMenuItem about = new JMenuItem("Об авторе...");
        about.setPreferredSize(new Dimension(200, 30));
        about.setFont(new Font("Tahoma", Font.PLAIN, 16));
        about.setIcon(new ImageIcon("res/about.png"));
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = null;
                try {
                    dialog = createDialogForHelp("Об авторе...", true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dialog.setVisible(true);
            }
        });
        help.add(about);
        return help;
    }

    private JDialog createDialogForHelp(String title, boolean modal) throws IOException {
        JDialog dialog = new JDialog(this, title, modal);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(480, 360);
        dialog.setLocation(400,300);
        dialog.setResizable(false);
        dialog.setIconImage(new ImageIcon("res/icon.png").getImage());

        JPanel aboutPanel = new JPanel();

        BufferedImage myPicture = null;
        myPicture = ImageIO.read(new File("res/pangelskaya.jpg"));

        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        picLabel.setPreferredSize(new Dimension(200, 300));

        JTextArea textArea = new JTextArea(5, 20);
        //запрет на редактирование
        textArea.setEditable(false);
        //сделали прозрачным
        textArea.setOpaque(false);
        //перенос по словам
        textArea.setWrapStyleWord(true);
        //перенос на следующую строку
        textArea.setLineWrap(true);
        textArea.append("Игра «Жизнь» (англ. Conway's Game of Life)\n\n");
        textArea.append("Автор: Пангельская Вероника, ФИТ НГУ, 14204\n\n");
        textArea.append("pangelskaya.va@gmail.com\n");
        textArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
        aboutPanel.add(picLabel);
        aboutPanel.add(textArea);
        dialog.add(aboutPanel);
        return dialog;
    }

    private JToolBar createToolBar(){

        toolbar = new JToolBar();
        JButton newGame = new JButton();
        newGame.setToolTipText("Новая игра");
        newGame.setIcon(new ImageIcon("res/new_game.png"));
        newGame.addActionListener(e -> controller.newGame());
        toolbar.add(newGame);

        JButton open = new JButton();
        open.setToolTipText("Открыть...");
        open.setIcon(new ImageIcon("res/open.png"));
        open.addActionListener(e -> controller.openGame());
        toolbar.add(open);

        JButton save = new JButton();
        save.setToolTipText("Сохранить");
        save.setIcon(new ImageIcon("res/save.png"));
        toolbar.add(save);

        toolbar.add(new JToolBar.Separator());

        JRadioButton XOR = new JRadioButton("XOR");
        XOR.setToolTipText("XOR");
        XOR.addActionListener(e -> hexagon.setXOR());
        JRadioButton replace = new JRadioButton("RPL");
        replace.setToolTipText("Replace");
        replace.addActionListener(e -> hexagon.setReplace());
        replace.setSelected(true);
        ButtonGroup bg = new ButtonGroup();
        bg.add(XOR);
        bg.add(replace);
        toolbar.add(XOR);
        toolbar.add(replace);

        toolbar.add(new JToolBar.Separator());

        JButton param = new JButton();
        param.setToolTipText("Параметры");
        param.setIcon(new ImageIcon("res/param.png"));
        param.addActionListener(e -> {
            JDialog dialog = createDialogForParam("Параметры", true);
            dialog.setVisible(true);
        });
        toolbar.add(param);

        JButton impact = new JButton();
        impact.setToolTipText("Impact");
        impact.setIcon(new ImageIcon("res/1.png"));
        impact.addActionListener(e -> {
            if (!imp) {
                imp = true;
                hexagon.setImpact(true);
                hexagon.repaint();
            }
            else{
                imp = false;
                hexagon.setImpact(false);
                hexagon.repaint();
            }
        });
        toolbar.add(impact);
        toolbar.add(new JToolBar.Separator());

        toolClear = new JButton();
        toolClear.setToolTipText("Очистить поле");
        toolClear.setIcon(new ImageIcon("res/clear.png"));
        toolClear.addActionListener(e -> hexagon.clear());
        toolbar.add(toolClear);

        toolNext = new JButton();
        toolNext.setToolTipText("Следующий шаг");
        toolNext.setIcon(new ImageIcon("res/next.png"));
        toolNext.addActionListener(e -> controller.nextStep());
        toolbar.add(toolNext);

        JButton run = new JButton();
        run.setToolTipText("Запустить симулятор");
        run.setIcon(new ImageIcon("res/run.png"));
        run.addActionListener(e -> {
            if (stopGame) {
                stopGame = false;
                runGame();
                toolNext.setEnabled(false);
                toolClear.setEnabled(false);
                menuNext.setEnabled(false);
                menuClear.setEnabled(false);
            }
            else{
                stopGame = true;
                toolNext.setEnabled(true);
                toolClear.setEnabled(true);
                hexagon.setRepaint(true);
                menuNext.setEnabled(true);
                menuClear.setEnabled(true);
            }
        });
        toolbar.add(run);

        toolbar.add(new JToolBar.Separator());

        JButton exit = new JButton();
        exit.setToolTipText("Выход");
        exit.setIcon(new ImageIcon("res/close.png"));
        exit.addActionListener(e -> System.exit(1));
        toolbar.add(exit);

        toolbar.setVisible(true);


        return toolbar;
    }

    private void runGame(){

        Thread myThready = new Thread(() -> {
            int timeUpdate = 1000;
            long lastTimeUpdate = System.currentTimeMillis() - timeUpdate;
            while(!stopGame){
                Thread myThready1 = new Thread(() -> {
                    hexagon.setRepaint(false);
                    controller.nextStep();
                });
                myThready1.start();
                try {
                    Thread.sleep(timeUpdate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        myThready.start();
    }

    public Frame(Controller controller) {
        super("Жизнь");
        this.controller = controller;
        setIconImage(new ImageIcon("res/icon.png").getImage());
        setLocation(200,200);
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenuFile());
        menuBar.add(createMenuEdit());
        menuBar.add(createMenuAction());
        menuBar.add(createMenuHelp());

        menuBar.add(Box.createHorizontalGlue());
        JLabel exit = new JLabel(new ImageIcon("images/exit.png"));
        exit.setBorder(BorderFactory.createEtchedBorder());
        menuBar.add(exit);


        hexagon = new Hexagon(controller.getField());

        add(createToolBar(), BorderLayout.PAGE_START);
        scrollPane = new JScrollPane(hexagon);
        add(scrollPane, BorderLayout.CENTER);
        setJMenuBar(menuBar);
        setSize(1000, 620);
        setVisible(true);
    }

    public Observer getHexagon() {
        return hexagon;
    }

    public void fieldSize() {

        JDialog dialog = new JDialog(this, "Размеры нового поля", true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300, 150);
        dialog.setLocation(500,400);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JPanel fieldSizePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fieldSizePanel.setPreferredSize(new Dimension(110, 100));
        JTextArea textWidth = new JTextArea("Ширина:");
        textWidth.setSize(50,10);
        textWidth.setEditable(false);
        textWidth.setOpaque(false);
        textWidth.setLineWrap(true);

        JTextArea textHeight = new JTextArea("Высота:");
        textHeight.setSize(50,10);
        textHeight.setEditable(false);
        textHeight.setOpaque(false);
        textHeight.setLineWrap(true);
        JTextField fieldSizeWidth = new JTextField(2);
        fieldSizeWidth.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                int a = 10;
                try
                {
                    final int fieldValue = Integer.parseInt(fieldSizeWidth.getText());
                    if ((fieldValue <= 100) && (fieldValue >= 2))
                    {
                        a = fieldValue;
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldSizeWidth.setText(String.valueOf(a));
                }
            }
        });
        JTextField fieldSizeHeight = new JTextField(2);
        fieldSizeHeight.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                int a = 10;
                try
                {
                    final int fieldValue = Integer.parseInt(fieldSizeHeight.getText());
                    if ((fieldValue <= 100) && (fieldValue >= 2))
                    {
                        a = fieldValue;
                    }
                }
                catch (Exception err)
                {
                }
                finally
                {
                    fieldSizeHeight.setText(String.valueOf(a));
                }
            }
        });


        fieldSizePanel.add(textWidth);
        fieldSizePanel.add(fieldSizeWidth);
        fieldSizePanel.add(textHeight);
        fieldSizePanel.add(fieldSizeHeight);

        JPanel okCancelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        okCancelPanel.setPreferredSize(new Dimension(110, 100));
        okCancelPanel.setVisible(true);

        JButton ok = new JButton("ОК");
        ok.addActionListener(e -> {
            newWidth = Integer.parseInt(fieldSizeWidth.getText());
            newHeight = Integer.parseInt(fieldSizeHeight.getText());
            controller.updateField(newWidth, newHeight);
            dialog.dispose();
        });
        okCancelPanel.add(ok);

        JButton cancel = new JButton("Отмена");
        cancel.addActionListener(e -> dialog.dispose());
        okCancelPanel.add(cancel);


        panel.add(fieldSizePanel);
        panel.add(okCancelPanel);
        dialog.add(panel);
        dialog.setVisible(true);
    }
}