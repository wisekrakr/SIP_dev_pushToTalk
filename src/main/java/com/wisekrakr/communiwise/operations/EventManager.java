package com.wisekrakr.communiwise.operations;

import com.wisekrakr.communiwise.gui.FrameManagerListener;
import com.wisekrakr.communiwise.gui.fx.AppGUI;
import com.wisekrakr.communiwise.phone.connections.RTPConnectionManager;
import com.wisekrakr.communiwise.phone.sip.SipManager;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * This class controls what happens to the GUI
 * It also holds several api's that control basic function of a phone and for audio controls
 */
public class EventManager implements FrameManagerListener {

    private AppGUI appGUI;
    private final SipManager sipManager;
    private final RTPConnectionManager rtpConnectionManager;

    /**
     *
     * @param sipManager holds all the information and methods of the current sipsession
     * @param rtpConnectionManager  handles the rtp connection and holds both incoming and outgoing audio threads
     */
    public EventManager(SipManager sipManager, RTPConnectionManager rtpConnectionManager) {

        this.sipManager = sipManager;
        this.rtpConnectionManager = rtpConnectionManager;
    }

    /**
     * The App GUI gets destroyed and we send a bye to the proxy
     */
    @Override
    public void onHangUp() {
        getPhoneApi().hangup();

        appGUI.hideGUI();
    }

    /**
     * Creates a new AppGUI  {@link AppGUI}
     * @param userInfo current logged in user info (username, domain)
     * @param proxyName the name of the callee
     * @param proxyAddress the address of the callee
     */
    @Override
    public void onCall(Map<String, String> userInfo, String proxyName, InetSocketAddress proxyAddress) {
        SwingUtilities.invokeLater(() -> {
            appGUI = new AppGUI(this, getSoundApi(), userInfo, proxyName, proxyAddress );
            appGUI.showGUI();
        });
    }

    /**
     * Api for easy access to methods that control the audio of the app
     * @return the sound api
     */
    public SoundAPI getSoundApi(){

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

    /**
     * Api for easy access to methods that control basic functions of a phone
     * @return the phone api
     */
    public PhoneAPI getPhoneApi(){
        return new PhoneAPI() {

            private String proxyAddress;

            @Override
            public void initiateCall(String recipient) {

                sipManager.initiateCall(recipient, rtpConnectionManager.getSocket().getLocalPort());

                proxyAddress = recipient;
            }

            @Override
            public void accept(String sipAddress) {
                sipManager.acceptCall(rtpConnectionManager.getSocket().getLocalPort());

                proxyAddress = sipAddress;
            }

            @Override
            public void reject() {
                sipManager.reject();
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
            public void register(String realm, String domain, String username, String password, String fromAddress) {
                System.out.println("LOGGING IN....");
                sipManager.login(realm, username, password, domain, fromAddress);
            }
        };
    }
}
