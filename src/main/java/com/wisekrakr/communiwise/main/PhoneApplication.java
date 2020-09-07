package com.wisekrakr.communiwise.main;


import com.wisekrakr.communiwise.operations.DeviceImplementations;
import com.wisekrakr.communiwise.phone.audio.AudioManager;
import com.wisekrakr.communiwise.phone.connections.RTPConnectionManager;
import com.wisekrakr.communiwise.phone.sip.SipManager;
import com.wisekrakr.communiwise.phone.sip.SipManagerListener;
import com.wisekrakr.communiwise.user.SipAccountManager;

import javax.sound.sampled.*;
import java.io.Serializable;
import java.net.InetSocketAddress;

public class PhoneApplication implements Serializable {

    private static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, true);

    private RTPConnectionManager rtpConnectionManager;

    private static void printHelp(String message) {
        System.out.println(message);
        System.out.println("Arguments: <local address> <audio input> <audio output>");

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        System.out.println("Available: ");
        for (int i = 0; i < mixers.length; i++) {
            System.out.println(String.format("%-50s %50s %30s %30s", mixers[i].getName(), mixers[i].getDescription(), mixers[i].getVersion(), mixers[i].getVendor()));
        }
    }

    public static void main(String[] args) {
        if (args.length == 1 && "help".equalsIgnoreCase(args[0]) || args.length != 3) {
            printHelp((args.length == 1 && "help".equalsIgnoreCase(args[0])) ? "Help" : "Invalid arguments");

            System.exit(1);
        }


        PhoneApplication application = new PhoneApplication();

        try {
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();

            TargetDataLine inputLine = null;
            SourceDataLine outputLine = null;


            for (int i = 0; i < mixers.length; i++) {
                if (args[1].equals(mixers[i].getName())) {
                    inputLine = (TargetDataLine) AudioSystem.getMixer(mixers[i]).getLine(new DataLine.Info(TargetDataLine.class, FORMAT));
                }
                if (args[2].equals(mixers[i].getName())) {
                    outputLine = (SourceDataLine) AudioSystem.getMixer(mixers[i]).getLine(new DataLine.Info(SourceDataLine.class, FORMAT));
                }
            }

            if (inputLine == null) {
                printHelp("Input line not found " + args[1]);
                System.exit(1);
            }
            if (outputLine == null) {
                printHelp("Output line not found " + args[2]);
                System.exit(1);
            }

            inputLine.open(FORMAT);
            outputLine.open(FORMAT);

            String localAddress = args[0];

            application.initialize(
                    inputLine,
                    outputLine,
                    localAddress,
                    5080,
                    "udp",
                    "asterisk.interzone",
                    5060
            );


        } catch (Exception e) {
            System.out.println("Unable to initialize: " + e);
            e.printStackTrace();

            System.exit(1);

            return;
        }

        application.run();

    }

    private void run() {
    }

    private void initialize(TargetDataLine inputLine, SourceDataLine outputLine, String localAddress, int localPort, String transport, String proxyHost, int proxyPort) throws Exception {

        SipManager sipManager = new SipManager(proxyHost, proxyPort, localAddress, localPort, transport).
                logging("server.log", "debug.log", 16).
                listener(new SipManagerListener() {


                    @Override
                    public void onTextMessage(String message, String from) {
                        System.out.println("Received message from " + from + " :" + message);
                    }

                    @Override
                    public void onRemoteBye() {

                        rtpConnectionManager.stopStreamingAudio();
                    }

                    @Override
                    public void onRemoteCancel() {

                    }

                    @Override
                    public void onRemoteDeclined() {

                    }

                    @Override
                    public void callConfirmed(String rtpHost, int rtpPort) {
                        InetSocketAddress proxyAddress = new InetSocketAddress(rtpHost, rtpPort);
                        try {
                            rtpConnectionManager.connectRTPAudio(proxyAddress);

                        } catch (Throwable e) {
                            throw new IllegalStateException("Unable to connect call", e);
                        }

                    }

                    @Override
                    public void onUnavailable() {

                    }

                    @Override
                    public void onRinging() {

                    }

                    @Override
                    public void onAccepted(String rtpProxy, int remoteRtpPort) {
                        String proxy = rtpProxy.substring(rtpProxy.lastIndexOf("@") + 1);

                        InetSocketAddress proxyAddress = new InetSocketAddress(proxy, remoteRtpPort);

                        try {
                            rtpConnectionManager.connectRTPAudio(proxyAddress);
                        } catch (Throwable e) {
                            System.out.println("Unable to connect: " + e);

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDeclined() {

                    }

                    @Override
                    public void onBusy() {

                    }

                    @Override
                    public void onRemoteAccepted() {
                    }

                    @Override
                    public void onRegistered() {

                    }

                    @Override
                    public void onBye() {

                    }

                    @Override
                    public void onTrying() {
                    }

                    @Override
                    public void authenticationFailed() {
                        System.out.println("Authentication failed :-(");

                    }
                });

        rtpConnectionManager = new RTPConnectionManager(inputLine, outputLine);
        rtpConnectionManager.init();

        AudioManager audioManager = new AudioManager(rtpConnectionManager.getSocket(), inputLine, outputLine);

        sipManager.initialize();

        DeviceImplementations impl = new DeviceImplementations(sipManager, rtpConnectionManager, audioManager);


    }

}
