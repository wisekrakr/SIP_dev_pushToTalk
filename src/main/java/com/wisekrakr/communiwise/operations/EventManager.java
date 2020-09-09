package com.wisekrakr.communiwise.operations;


import com.wisekrakr.communiwise.gui.FrameManagerListener;
import com.wisekrakr.communiwise.gui.fx.AppFrame;
import com.wisekrakr.communiwise.operations.apis.PhoneAPI;
import com.wisekrakr.communiwise.operations.apis.SoundAPI;
import com.wisekrakr.communiwise.phone.connections.RTPConnectionManager;
import com.wisekrakr.communiwise.phone.sip.SipManager;


import javax.swing.*;
import java.net.InetSocketAddress;
import java.util.Map;


public class EventManager implements FrameManagerListener {

    private AppFrame appFrame;
    private final SipManager sipManager;
    private final RTPConnectionManager rtpConnectionManager;

    /**
     *
     * @param sipManager {@link SipManager} for a phone device. The basic functions of a phone will be handled here.
     */
    public EventManager(SipManager sipManager, RTPConnectionManager rtpConnectionManager) {

        this.sipManager = sipManager;
        this.rtpConnectionManager = rtpConnectionManager;
    }

    /**
     * When the user hangs up or on remote cancel or bye.
     * The GUI gets destroyed
     */
    @Override
    public void onHangUp() {
        getPhoneApi().hangup();

        appFrame.hideWindow();

        System.exit(1);
    }

    @Override
    public void onCall(Map<String, String> userInfo, String proxyName, InetSocketAddress proxyAddress) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable e) {
                System.out.println("WARNING: unable to set look and feel, will continue");
            }

            appFrame = new AppFrame(this, getSoundApi(), userInfo, proxyName, proxyAddress );
            appFrame.showWindow();
        });
    }

    public SoundAPI getSoundApi(){

        return new SoundAPI() {

            @Override
            public void mute() {
                //todo interrupt the transmit thread? or the targetdataline?
                rtpConnectionManager.mute();
            }

            @Override
            public void unmute() {
                rtpConnectionManager.unmute();
            }

        };
    }


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
            }

            @Override
            public void register(String realm, String domain, String username, String password, String fromAddress) {
                System.out.println("LOGGING IN....");
                sipManager.login(realm, username, password, domain, fromAddress);
            }
        };
    }
}
