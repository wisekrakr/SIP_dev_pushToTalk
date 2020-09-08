package com.wisekrakr.communiwise.gui.fx;

import com.wisekrakr.communiwise.operations.apis.PhoneAPI;
import com.wisekrakr.communiwise.operations.apis.SoundAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class AppFrame extends JFrame {

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int DESIRED_HEIGHT = 120;
    private static final int DESIRED_WIDTH = 390;

    private final PhoneAPI phone;
    private final Map<String, String> userInfo;
    private final SoundAPI sound;

    public AppFrame(PhoneAPI phone, SoundAPI sound, Map<String, String> userInfo) {
        this.phone = phone;
        this.userInfo = userInfo;
        this.sound = sound;

        prepareGUI();
    }


    public void prepareGUI() {
        setUndecorated(true);

        setBounds(screenSize.width, screenSize.height + DESIRED_HEIGHT, DESIRED_WIDTH, DESIRED_HEIGHT);
        setLocationRelativeTo(null);

        AppController controller = (AppController) new AppController( this, phone, sound, userInfo).initialize("/app.fxml");
        controller.initComponents();

        add(controller,BorderLayout.CENTER);
    }

    public void showWindow() {
        setVisible(true);

//        new Timer(1, new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                setLocation(screenSize.width - DESIRED_WIDTH, getY() - 1);
//                if (getY() == screenSize.height - DESIRED_HEIGHT) {
//                    ((Timer) e.getSource()).stop();
//                }
//            }
//        }).start();
    }

    public void hideWindow(){
        setVisible(false);
    }
}
