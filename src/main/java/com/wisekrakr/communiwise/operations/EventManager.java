package com.wisekrakr.communiwise.operations;

import com.wisekrakr.communiwise.gui.AppGUI;
import com.wisekrakr.communiwise.phone.connections.RTPConnectionManager;
import com.wisekrakr.communiwise.phone.sip.SipManager;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * This class controls what happens to the GUI
 * It also holds several API's that control basic function of a phone and for audio controls.
 * The GUI(s) that are initialized in this class will use these API's.
 */
public class EventManager implements EventManagerListener, APIHandler {

    private final SipManager sipManager;
    private final RTPConnectionManager rtpConnectionManager;

    /**
     * @param sipManager holds all the information and methods of the current sipsession
     * @param rtpConnectionManager  handles the rtp connection and holds both incoming and outgoing audio threads
     */
    public EventManager(SipManager sipManager, RTPConnectionManager rtpConnectionManager) {

        this.sipManager = sipManager;
        this.rtpConnectionManager = rtpConnectionManager;
    }

    /**
     * Created by the EventManagerListener.
     * Creates a new AppGUI  {@link AppGUI}
     * @param userInfo current logged in user info (username, domain)
     * @param proxyName the name of the callee
     * @param proxyAddress the address of the callee
     */
    @Override
    public void onCall(Map<String, String> userInfo, String proxyName, InetSocketAddress proxyAddress) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable e) {
                System.out.println("WARNING: unable to set look and feel, will continue");
            }

            AppGUI appGUI = new AppGUI(this, userInfo, proxyName, proxyAddress );
            appGUI.showGUI();
        });
    }

    /**
     * Api for easy access for GUI(s) to methods that control basic functions of a phone
     * @return the phone api
     */
    @Override
    public PhoneAPI getPhone() {
        return new PhoneAPI() {

            private String proxyAddress;

            @Override
            public void initiateCall(String recipient) {

                sipManager.initiateCall(recipient, rtpConnectionManager.getSocket().getLocalPort());

                proxyAddress = recipient;
            }

            @Override
            public void hangup() {
                try {
                    sipManager.hangup(proxyAddress);
                } catch (Throwable e) {
                    throw new IllegalStateException("Unable to hang up the device", e);
                }
                System.exit(1);
            }

            @Override
            public void login(String realm, String domain, String username, String password, String fromAddress) {
                System.out.println("LOGGING IN....");
                sipManager.login(realm, username, password, domain, fromAddress);
            }
        };
    }

    /**
     * Api for easy access for GUI(s) to methods that control the audio of the app
     * @return the sound api
     */
    @Override
    public SoundAPI getSound() {
        return new SoundAPI() {

            @Override
            public void mute() {
                rtpConnectionManager.mute();
            }

            @Override
            public void unmute() {
                rtpConnectionManager.unmute();
            }

        };
    }

}
