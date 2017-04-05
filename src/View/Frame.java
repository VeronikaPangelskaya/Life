package View;

import Controller.Controller;
import Model.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    private boolean exitVariable;
    private int  newWidth = 0;
    private int newHeight = 0;
    private JScrollPane scrollPane;

    public File saveFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.TXT", "*.*");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
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
        file.add(open);

        JMenuItem save = new JMenuItem("Сохранить");
        save.setFont(new Font("Tahoma", Font.PLAIN, 16));
        save.setAccelerator(KeyStroke.getKeyStroke("S".charAt(0), KeyEvent.CTRL_MASK));
        save.setIcon(new ImageIcon("res/save.png"));
        file.add(save);

        JMenuItem save_as = new JMenuItem("Сохранить как...");
        save_as.setFont(new Font("Tahoma", Font.PLAIN, 16));
        save_as.setIcon(new ImageIcon("res/save_as.png"));
        file.add(save_as);

        file.addSeparator();

        JMenuItem exit = new JMenuItem("Выход");
        exit.setFont(new Font("Tahoma", Font.PLAIN, 16));
        exit.setIcon(new ImageIcon("res/close.png"));
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });

        file.add(exit);

        return file;
    }

    public boolean createDialogForExit(){

        JDialog dialog = new JDialog(this);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(720, 480);
        dialog.setLocation(400,300);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextArea textWidth = new JTextArea("Сохранить изменения:");
        textWidth.setSize(300,30);
        textWidth.setEditable(false);
        textWidth.setOpaque(false);
        textWidth.setLineWrap(true);

        JButton yes = new JButton("Да");
        yes.setPreferredSize(new Dimension(100,100));
        yes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitVariable = true;
            }
        });
        panel.add(yes);
        JButton no = new JButton("Нет");
        no.setPreferredSize(new Dimension(100,100));
        no.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitVariable = false;
            }
        });
        panel.add(no);

        return exitVariable;
    }

    private JMenu createMenuEdit() {
        JMenu edit = new JMenu("Редактировать");
        edit.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // меню-переключатели
        JRadioButtonMenuItem XOR = new JRadioButtonMenuItem("XOR");
        XOR.setFont(new Font("Tahoma", Font.PLAIN, 16));
        XOR.setPreferredSize(new Dimension(200, 30));
        XOR.setIcon(new ImageIcon("res/xor.png"));
        JRadioButtonMenuItem replace = new JRadioButtonMenuItem("Replace");
        replace.setFont(new Font("Tahoma", Font.PLAIN, 16));
        replace.setIcon(new ImageIcon("res/xor.png"));
        // организуем переключатели в логическую группу
        ButtonGroup bg = new ButtonGroup();
        bg.add(XOR);
        bg.add(replace);
        edit.add(XOR);
        edit.add(replace);

        edit.addSeparator();

        JMenuItem param = new JMenuItem("Параметры");
        param.setFont(new Font("Tahoma", Font.PLAIN, 16));
        param.setIcon(new ImageIcon("res/param.png"));
        param.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        JDialog dialog = createDialogForParam("Параметры", true);
                                        dialog.setVisible(true);
                                    }
                                });
        edit.add(param);

        edit.addSeparator();

        JCheckBoxMenuItem impact  = new JCheckBoxMenuItem("Impact");
        impact.setIcon(new ImageIcon("res/1.png"));
        impact.setFont(new Font("Tahoma", Font.PLAIN, 16));
        edit.add(impact);

        JCheckBoxMenuItem color  = new JCheckBoxMenuItem("Цвет");
        color.setFont(new Font("Tahoma", Font.PLAIN, 16));
        color.setIcon(new ImageIcon("res/colors.png"));
        edit.add(color);

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
        JTextField fieldSizeWidth = new JTextField(2);
        JTextField fieldSizeHeight = new JTextField(2);
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
        JTextField fieldLineWidth = new JTextField(2);
        JTextField fieldCellSize = new JTextField(2);
        JSlider sliderLineWidth = new JSlider(1, 10, 1);
        sliderLineWidth.setPaintLabels(true);
        sliderLineWidth.setMajorTickSpacing(3);
        JSlider sliderCellSize = new JSlider(10, 100, 20);
        sliderCellSize.setPaintLabels(true);
        sliderCellSize.setMajorTickSpacing(30);
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
        JTextField fieldFST_IMPACT = new JTextField(2);
        JTextField fieldSND_IMPACT = new JTextField(2);
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
        JTextField fieldLIVE_START = new JTextField(2);
        JTextField fieldLIVE_END = new JTextField(2);
        JTextField fieldBIRTH_START = new JTextField(2);
        JTextField fieldBIRTH_END = new JTextField(2);
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

        JMenuItem clear = new JMenuItem("Очистить поле");
        clear.setPreferredSize(new Dimension(200, 30));
        clear.setFont(new Font("Tahoma", Font.PLAIN, 16));
        clear.setIcon(new ImageIcon("res/clear.png"));
        action.add(clear);

        JMenuItem next = new JMenuItem("Следующий шаг");
        next.setFont(new Font("Tahoma", Font.PLAIN, 16));
        next.setIcon(new ImageIcon("res/next.png"));
        action.add(next);

        JMenuItem run = new JMenuItem("Играть");
        run.setFont(new Font("Tahoma", Font.PLAIN, 16));
        run.setIcon(new ImageIcon("res/run.png"));
        action.add(run);

        return action;

    }

    private JMenu createMenuView(){

        JMenu view = new JMenu("Просмотр");
        view.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JCheckBoxMenuItem toolbar  = new JCheckBoxMenuItem("Панель инструметов");
        toolbar.setPreferredSize(new Dimension(200, 30));
        toolbar.setIcon(new ImageIcon("res/statusbar.png"));
        toolbar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        view.add(toolbar);

        JCheckBoxMenuItem statusBar = new JCheckBoxMenuItem("Строка состояния");
        statusBar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        statusBar.setIcon(new ImageIcon("res/toolbar.png"));
        view.add(statusBar);

        return view;
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

        JToolBar toolbar = new JToolBar();
        JButton newGame = new JButton();
        newGame.setToolTipText("Новая игра");
        newGame.setIcon(new ImageIcon("res/new_game.png"));
        newGame.addActionListener(e -> controller.newGame());
        toolbar.add(newGame);

        JButton open = new JButton();
        open.setToolTipText("Открыть...");
        open.setIcon(new ImageIcon("res/open.png"));
        toolbar.add(open);

        JButton save = new JButton();
        save.setToolTipText("Сохранить");
        save.setIcon(new ImageIcon("res/save.png"));
        toolbar.add(save);

        toolbar.add(new JToolBar.Separator());

        JRadioButton XOR = new JRadioButton("XOR");
        XOR.setToolTipText("XOR");
        JRadioButton replace = new JRadioButton("RPL");
        replace.setToolTipText("Replace");
        ButtonGroup bg = new ButtonGroup();
        bg.add(XOR);
        bg.add(replace);
        toolbar.add(XOR);
        toolbar.add(replace);

        toolbar.add(new JToolBar.Separator());

        JButton param = new JButton();
        param.setToolTipText("Параметры");
        param.setIcon(new ImageIcon("res/param.png"));
        param.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = createDialogForParam("Параметры", true);
                dialog.setVisible(true);
            }
        });
        toolbar.add(param);

        JButton impact = new JButton();
        impact.setToolTipText("Impact");
        impact.setIcon(new ImageIcon("res/1.png"));
        toolbar.add(impact);

        JButton color = new JButton();
        color.setToolTipText("Цвет");
        color.setIcon(new ImageIcon("res/colors.png"));
        toolbar.add(color);

        toolbar.add(new JToolBar.Separator());

        JButton clear = new JButton();
        clear.setToolTipText("Очистить поле");
        clear.setIcon(new ImageIcon("res/clear.png"));
        toolbar.add(clear);

        JButton next = new JButton();
        next.setToolTipText("Следующий шаг");
        next.setIcon(new ImageIcon("res/next.png"));
        toolbar.add(next);

        JButton run = new JButton();
        run.setToolTipText("Запустить симулятор");
        run.setIcon(new ImageIcon("res/run.png"));
        toolbar.add(run);

        toolbar.add(new JToolBar.Separator());

        JButton exit = new JButton();
        exit.setToolTipText("Выход");
        exit.setIcon(new ImageIcon("res/close.png"));
        toolbar.add(exit);

        toolbar.setVisible(true);


        return toolbar;
    }

    public Frame(Controller controller) {
        super("Жизнь");
        this.controller = controller;
        setIconImage(new ImageIcon("res/icon.png").getImage());
        setLocation(200,200);
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        // создаем строку главного меню
        JMenuBar menuBar = new JMenuBar();
        // Создание меню "Файл"
        menuBar.add(createMenuFile());
        menuBar.add(createMenuEdit());
        menuBar.add(createMenuAction());
        menuBar.add(createMenuView());
        menuBar.add(createMenuHelp());


        // JMenuBar использует блочное расположение (заполнитель вполне уместен)
        menuBar.add(Box.createHorizontalGlue());
        // Разместим в строке меню не выпадающее меню, а надпись со значком
        JLabel exit = new JLabel(new ImageIcon("images/exit.png"));
        exit.setBorder(BorderFactory.createEtchedBorder());
        menuBar.add(exit);


        hexagon = new Hexagon();

        add(createToolBar(), BorderLayout.PAGE_START);
        scrollPane = new JScrollPane(hexagon);
        add(scrollPane, BorderLayout.CENTER);
//        add(hexagon, BorderLayout.CENTER);
        // поместим меню в наше окно
        setJMenuBar(menuBar);
        // выводим окно на экран
        setSize(1000, 620);
        setVisible(true);
    }

    public Observer getHexagon() {
        return hexagon;
    }

    public JPanel getPanelHexagon(){
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
        JTextField fieldSizeHeight = new JTextField(2);

        fieldSizePanel.add(textWidth);
        fieldSizePanel.add(fieldSizeWidth);
        fieldSizePanel.add(textHeight);
        fieldSizePanel.add(fieldSizeHeight);

        JPanel okCancelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        okCancelPanel.setPreferredSize(new Dimension(110, 100));
        okCancelPanel.setVisible(true);

        JButton ok = new JButton("ОК");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newWidth = Integer.parseInt(fieldSizeWidth.getText());
                newHeight = Integer.parseInt(fieldSizeHeight.getText());
                controller.updateField(newWidth, newHeight);
                dialog.dispose();
            }
        });
        okCancelPanel.add(ok);

        JButton cancel = new JButton("Отмена");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        okCancelPanel.add(cancel);


        panel.add(fieldSizePanel);
        panel.add(okCancelPanel);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    public void setHexagon(Hexagon _hexagon){
        hexagon = _hexagon;
        scrollPane.setViewportView(hexagon);
    }

//    public void span(){
//       getPanelHexagon().addMouseMotionListener(new MouseMotionAdapter(){
//            public void mouseMoved(MouseEvent me){
//                BufferedImage bi = getScreenShot(hexagon);
//                hexagon.spanFilling(bi, hexagon.getHeight(), hexagon.getWidth(),me.getX(), me.getY(), Color.red, Color.blue);
//            }
//        });
//    }
//    private BufferedImage getScreenShot(JPanel panel){
//        BufferedImage bi = new BufferedImage(
//                panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
//        panel.paint(bi.getGraphics());
//        return bi;
//    }
}