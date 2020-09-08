package com.wisekrakr.communiwise.gui.fx;

import com.wisekrakr.communiwise.operations.apis.PhoneAPI;
import com.wisekrakr.communiwise.operations.apis.SoundAPI;
import com.wisekrakr.communiwise.user.SipAccountManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.Map;

public class AppController extends ControllerJFXPanel {

    private final AppFrame gui;
    private final PhoneAPI phone;
    private final SoundAPI sound;
    private final Map<String, String> userInfo;

    private boolean isMuted;

    @FXML
    private Label username,domain;
    @FXML
    private Button muteButton, closeButton;

    public AppController(AppFrame gui, PhoneAPI phone, SoundAPI sound, Map<String, String> userInfo) {
        this.gui = gui;
        this.phone = phone;
        this.sound = sound;
        this.userInfo = userInfo;
    }

    @FXML
    private void close() {
        gui.hideWindow();

        phone.hangup();
    }

    @FXML
    private void mute(){
        sound.mute();

        if(isMuted){
            if(muteButton.getText().equals("Unmute")){
                muteButton.setText("Mute");
            }
        }else{
            if(muteButton.getText().equals("Mute")){
                muteButton.setText("Unmute");
            }
        }

        isMuted = !isMuted;
    }

    @Override
    public void initComponents() {
        username.setText(userInfo.get(SipAccountManager.UserInfoPart.USERNAME.getInfoPart()));
        domain.setText(userInfo.get(SipAccountManager.UserInfoPart.DOMAIN.getInfoPart()));

        closeButton.setGraphic(addIconForButton());
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
