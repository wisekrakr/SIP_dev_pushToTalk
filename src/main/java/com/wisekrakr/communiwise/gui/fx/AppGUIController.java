package com.wisekrakr.communiwise.gui.fx;

import com.wisekrakr.communiwise.operations.EventManager;
import com.wisekrakr.communiwise.operations.SoundAPI;
import com.wisekrakr.communiwise.user.SipAccountManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.util.Map;

import static com.wisekrakr.communiwise.gui.SipAddressMaker.make;

/**
 * A JavaFX Application controller, but different.
 * Made with a {@link javafx.embed.swing.JFXPanel} and can therefore be initialized, instead of loaded with the FXMLoader,
 * and we can pass parameters to it from the JFrame/GUI class {@link AppGUI}
 */
public class AppGUIController extends ControllerJFXPanel {

    private final AppGUI appGUI;
    private final EventManager eventManager;
    private final SoundAPI sound;
    private final Map<String, String> userInfo;
    private final String proxyName;
    private final InetSocketAddress proxyAddress;

    private boolean isMuted = true;
    private int counter;

    @FXML
    private Label username,domain, proxy;
    @FXML
    private Button closeButton;
    @FXML
    private AnchorPane textPane, container;
    @FXML
    private Button talkButton;

    public AppGUIController(AppGUI appGUI, EventManager eventManager, SoundAPI sound, Map<String, String> userInfo, String proxyName, InetSocketAddress proxyAddress) {
        this.appGUI = appGUI;
        this.eventManager = eventManager;
        this.sound = sound;
        this.userInfo = userInfo;
        this.proxyName = proxyName;
        this.proxyAddress = proxyAddress;

    }

    @FXML
    private void close() {
        eventManager.onHangUp();
    }

    @FXML
    private void pushed(){
        talkButton.setText("Talking!");
        talkButton.setTextFill(Color.GREEN);

        sound.unmute();
    }

    @FXML
    private void released(){
        talkButton.setText("Push to Talk");
        talkButton.setTextFill(Color.RED);

        sound.mute();
    }

    @Override
    public void initComponents() {
        username.setText(userInfo.get(SipAccountManager.UserInfoPart.USERNAME.getInfoPart()));
        domain.setText(userInfo.get(SipAccountManager.UserInfoPart.DOMAIN.getInfoPart()));
        proxy.setText(make(proxyName, proxyAddress.getHostName()));

        closeButton.setGraphic(addIconForButton());

        textPane.setMouseTransparent(true);

        appGUI.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(appGUI,
                        "Are you sure you want to end the call?", "End Call with " + proxy.getText() +  "?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    System.out.println("Closing App");
                    close();
                }
            }
        });
    }

    private static ImageView addIconForButton(){
        ImageView image;

        String path = "/images/exit.png";

        try {
            image = new ImageView(String.valueOf(AppGUI.class.getResource(path)));

            image.setFitWidth(15);
            image.setFitHeight(15);

            return image;


        }catch (Throwable t){
            throw new IllegalArgumentException("Could not find path to image " + path,t);
        }
    }

}
