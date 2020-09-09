package com.wisekrakr.communiwise.gui.fx;

import com.wisekrakr.communiwise.gui.EventManager;
import com.wisekrakr.communiwise.operations.apis.SoundAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class AppFrame extends JFrame {

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int DESIRED_HEIGHT = 85;
    private static final int DESIRED_WIDTH = 390;

    private final EventManager eventManager;
    private final Map<String, String> userInfo;
    private final SoundAPI sound;

    public AppFrame(EventManager eventManager, SoundAPI sound, Map<String, String> userInfo) {
        this.eventManager = eventManager;
        this.userInfo = userInfo;
        this.sound = sound;

        prepareGUI();

    }


    public void prepareGUI() {
        setUndecorated(false);
        setAlwaysOnTop(true);

        setBounds(screenSize.width + DESIRED_WIDTH, screenSize.height, DESIRED_WIDTH, DESIRED_HEIGHT);
        setLocationRelativeTo(null);

        AppController controller = (AppController) new AppController( eventManager, sound, userInfo).initialize("/app.fxml");
        controller.initComponents();

        add(controller,BorderLayout.CENTER);
    }

    public void showWindow() {
        setVisible(true);

//        addFrameDragAbility();
    }

    public void addFrameDragAbility(){
        FrameDragListener frameDragListener = new FrameDragListener(this);
        this.addMouseListener(frameDragListener);
        this.addMouseMotionListener(frameDragListener);
    }


    public void hideWindow(){
        setVisible(false);
    }

    class FrameDragListener extends MouseAdapter {

        private final JFrame frame;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }
}
