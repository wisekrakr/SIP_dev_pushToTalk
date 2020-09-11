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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.InetSocketAddress;
import java.util.Map;

import static com.wisekrakr.communiwise.gui.SipAddressMaker.make;

/**
 * A JavaFX Application controller, but different.
 * Made with a {@link javafx.embed.swing.JFXPanel} and can therefore be initialized, instead of loaded with the FXMLoader,
 * and we can pass parameters to it from the JFrame/GUI class {@link AppGUI}
 */
public class AppGUIController extends ControllerJFXPanel {

    private final EventManager eventManager;
    private final SoundAPI sound;
    private final Map<String, String> userInfo;
    private final String proxyName;
    private final InetSocketAddress proxyAddress;

    private boolean isMuted;
    private int counter;

    @FXML
    private Label username,domain, proxy;
    @FXML
    private Button closeButton;
    @FXML
    private AnchorPane textPane;
    @FXML
    private Button talkButton;

    public AppGUIController(EventManager eventManager, SoundAPI sound, Map<String, String> userInfo, String proxyName, InetSocketAddress proxyAddress) {
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


    private void talk(){
        if(isMuted){
            if(talkButton.getText().equals("Muted")){

                talkButton.setText("Talking!");
                talkButton.setTextFill(Color.GREEN);

                sound.unmute();
            }
        }else{
            if(talkButton.getText().equals("Talking!")){

                talkButton.setText("Muted");
                talkButton.setTextFill(Color.RED);

                sound.mute();
            }
        }

        isMuted = !isMuted;
    }

    @Override
    public void initComponents() {
        username.setText(userInfo.get(SipAccountManager.UserInfoPart.USERNAME.getInfoPart()));
        domain.setText(userInfo.get(SipAccountManager.UserInfoPart.DOMAIN.getInfoPart()));
        proxy.setText(make(proxyName, proxyAddress.getHostName()));

        closeButton.setGraphic(addIconForButton());

        textPane.setMouseTransparent(true);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), actionEvent -> { talk(); }));
        timeline.setCycleCount(Animation.INDEFINITE);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                talkButton.armedProperty().addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if( newValue) {
                            timeline.play();
                        } else {
                            timeline.stop();
                        }
                    }
                });
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
