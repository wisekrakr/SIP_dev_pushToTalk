package com.wisekrakr.communiwise.gui.fx;

import com.wisekrakr.communiwise.operations.EventManager;
import com.wisekrakr.communiwise.operations.SoundAPI;

import javax.swing.*;
import java.awt.*;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * The GUI of this app.
 */
public class AppGUI extends JFrame {

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int DESIRED_HEIGHT = 100;
    private static final int DESIRED_WIDTH = 400;

    private final EventManager eventManager;
    private final Map<String, String> userInfo;
    private final SoundAPI sound;
    private final String proxyName;
    private final InetSocketAddress proxyAddress;

    public AppGUI(EventManager eventManager, SoundAPI sound, Map<String, String> userInfo, String proxyName, InetSocketAddress proxyAddress) throws HeadlessException  {
        this.eventManager = eventManager;
        this.userInfo = userInfo;
        this.sound = sound;
        this.proxyName = proxyName;
        this.proxyAddress = proxyAddress;

        prepareGUI();
    }

    /**
     * A controller is created for the fxml file. This is a {@link ControllerJFXPanel} and therefore is initialized differently than a normal JavaFx app controller.
     * Initializes the controller and adds it to the JFrame.
     */
    public void prepareGUI() {
        setUndecorated(false);
        setAlwaysOnTop(true);

        setBounds(screenSize.width + DESIRED_WIDTH, screenSize.height, DESIRED_WIDTH, DESIRED_HEIGHT);
        setLocationRelativeTo(null);

        AppGUIController controller = (AppGUIController) new AppGUIController( eventManager, sound, userInfo, proxyName, proxyAddress).initialize("/app.fxml");
        controller.initComponents();

        add(controller,BorderLayout.CENTER);
    }

    public void showGUI() {
        setVisible(true);
    }
    public void hideGUI(){
        setVisible(false);
    }
}
