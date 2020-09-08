package com.wisekrakr.communiwise.gui;


import com.wisekrakr.communiwise.gui.fx.AppFrame;
import com.wisekrakr.communiwise.operations.DeviceImplementations;
import com.wisekrakr.communiwise.operations.apis.PhoneAPI;
import com.wisekrakr.communiwise.operations.apis.SoundAPI;


import javax.swing.*;
import java.util.Map;


public class EventManager implements FrameManagerListener {

    private AppFrame appFrame;
    private CommandConsole commandConsole;

    private final PhoneAPI phone;
    private final SoundAPI sound;

    /**
     *
     * @param impl {@link DeviceImplementations} for a phone device. The basic functions of a phone will be handled here.
     */
    public EventManager(DeviceImplementations impl) {
        phone = impl.getPhoneApi();
        sound = impl.getSoundApi();
    }

    /**
     *
     *
     */
    @Override
    public void onIncomingCall() {

        sound.ringing(true);
    }

    /**
     * When the user hangs up or on remote cancel or bye.
     * The GUI gets destroyed
     */
    @Override
    public void onHangUp() {
        appFrame.hideWindow();
    }

    @Override
    public void onOutgoingCall(Map<String, String> userInfo) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable e) {
                System.out.println("WARNING: unable to set look and feel, will continue");
            }

            appFrame = new AppFrame(phone, sound, userInfo );
            appFrame.showWindow();

            commandConsole.stop();
        });
    }


    /**
     * When the user accepts a call. The GUI gets created
     * We also stop the phone ringing sound.
     */
    @Override
    public void onAcceptingCall() {

        sound.ringing(false);
    }



    /**
     * When the user declines a call
     */
    @Override
    public void onDecliningCall() {

        sound.ringing(false);
    }

    /**
     * When the user closes the main phone GUI
     */
    @Override
    public void close() {
        phone.hangup();

        appFrame.hideWindow();

        System.exit(1);
    }

    /**
     * When the user starts the app. A new Console gets created.
     */
    @Override
    public void open() {
        commandConsole = new CommandConsole(phone);
        commandConsole.start();
    }


    /**
     * When the user clicked the register button, the Console gets hidden and a new GUI starts up
     */
    @Override
    public void onRegistered() {
        commandConsole.connectWithExtension();
    }

    /**
     * When registering/logging in has failed, a error message will pop up.
     */
    @Override
    public void onAuthenticationFailed() {
        //textIO show error/alert and start over
        commandConsole.onError();

        commandConsole.start();
    }



}
