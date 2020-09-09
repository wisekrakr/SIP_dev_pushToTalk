package com.wisekrakr.communiwise.gui.fx;

import com.wisekrakr.communiwise.operations.EventManager;
import com.wisekrakr.communiwise.operations.apis.SoundAPI;
import com.wisekrakr.communiwise.user.SipAccountManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.InetSocketAddress;
import java.util.Map;

import static com.wisekrakr.communiwise.gui.SipAddressMaker.make;

public class AppController extends ControllerJFXPanel {

    private final EventManager eventManager;
    private final SoundAPI sound;
    private final Map<String, String> userInfo;
    private final String proxyName;
    private final InetSocketAddress proxyAddress;

    private boolean isMuted;

    @FXML
    private Label username,domain, proxy;
    @FXML
    private Button closeButton;
    @FXML
    private AnchorPane textPane;
    @FXML
    private ToggleButton toggleButton;



    public AppController(EventManager eventManager, SoundAPI sound, Map<String, String> userInfo, String proxyName, InetSocketAddress proxyAddress) {
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
    private void talk(){
        if(isMuted){
            if(toggleButton.getText().equals("Muted")){

                toggleButton.setText("Talking!");
                toggleButton.setTextFill(Color.GREEN);

                sound.unmute();
            }
        }else{
            if(toggleButton.getText().equals("Talking!")){

                toggleButton.setText("Muted");
                toggleButton.setTextFill(Color.RED);

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
    }

    private static ImageView addIconForButton(){
        ImageView image;

        String path = "/images/exit.png";

        try {
            image = new ImageView(String.valueOf(AppFrame.class.getResource(path)));

            image.setFitWidth(15);
            image.setFitHeight(15);

            return image;


        }catch (Throwable t){
            throw new IllegalArgumentException("Could not find path to image " + path,t);
        }
    }

}
