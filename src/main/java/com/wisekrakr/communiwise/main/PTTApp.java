package com.wisekrakr.communiwise.main;


import com.wisekrakr.communiwise.operations.EventManager;
import com.wisekrakr.communiwise.phone.connections.RTPConnectionManager;
import com.wisekrakr.communiwise.phone.sip.SipManager;
import com.wisekrakr.communiwise.phone.sip.SipManagerListener;
import com.wisekrakr.communiwise.user.SipAccountManager;
import org.apache.commons.cli.*;

import javax.sound.sampled.*;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetSocketAddress;

import static com.wisekrakr.communiwise.gui.SipAddressMaker.make;

public class PTTApp  implements Serializable {

    private static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, true);
    private static final int DESIRED_HEIGHT = 100;
    private static final int DESIRED_WIDTH = 400;

    private RTPConnectionManager rtpConnectionManager;
    private EventManager eventManager;
    private SipAccountManager accountManager;

    public static void main(String[] args) {

        System.out.println(" =======>< CommUniWise activated ><======= ");

        Options options = new Options();

        CommandLine cmd = null;
        HelpFormatter formatter = new HelpFormatter();

        try {
            cmd = commandLineParser(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final PrintWriter writer = new PrintWriter(System.out);
        formatter.printUsage(writer,80,"CommUniWise Push-to-talk SIP Phone", options);
        writer.flush();

        optionNotThere(cmd, "i", "You have to assign an audio input device", options, formatter);
        optionNotThere(cmd, "o", "You have to assign an audio output device", options, formatter);

        PTTApp application = new PTTApp();

        try {
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();

            TargetDataLine inputLine = null;
            SourceDataLine outputLine = null;

            for (int i = 0; i < mixers.length; i++) {
                if (cmd.getOptionValue("i").equals(mixers[i].getName())) {
                    inputLine = (TargetDataLine) AudioSystem.getMixer(mixers[i]).getLine(new DataLine.Info(TargetDataLine.class, FORMAT));
                }
                if (cmd.getOptionValue("o").equals(mixers[i].getName())) {
                    outputLine = (SourceDataLine) AudioSystem.getMixer(mixers[i]).getLine(new DataLine.Info(SourceDataLine.class, FORMAT));
                }
            }

            if (inputLine == null) {
                formatter.printHelp("Input line not found " , options);
                System.exit(1);
            }
            if (outputLine == null) {
                formatter.printHelp("Output line not found " ,options);
                System.exit(1);
            }

            inputLine.open(FORMAT);
            outputLine.open(FORMAT);

            optionNotThere(cmd, "ip", " Please fill in your current IP Address", options, formatter);
            optionNotThere(cmd, "u", "Please fill in the username used on the domain", options, formatter);
            optionNotThere(cmd, "p", "Please fill in the password you used to register", options, formatter);
            optionNotThere(cmd, "d", "Please fill in the domain you registered on", options, formatter);
            optionNotThere(cmd, "e", "Please fill in the extension you want to call", options, formatter);

            String localAddress = cmd.getOptionValue("ip");
            String username = cmd.getOptionValue("username");
            String password = cmd.getOptionValue("password");
            String domain = cmd.getOptionValue("domain");
            String extension = cmd.getOptionValue("extension");

            application.initialize(
                    inputLine,
                    outputLine,
                    username,
                    localAddress,
                    extension,
                    domain,
                    password);

        } catch (Exception e) {
            System.out.println("Unable to initialize: " + e);
            e.printStackTrace();

            System.exit(1);

            return;
        }

        application.run();
    }

    private static void optionNotThere(CommandLine cmd, String option, String message, Options options, HelpFormatter formatter){
        if(!cmd.hasOption(option)){
            formatter.printHelp("The -" + option + " is missing from the arguments", message, options,"Thank you for using a Wisekrakr product");
            System.exit(1);
        }
    }

    private static CommandLine commandLineParser(Options options, String[] strings) throws ParseException {
        options
                .addOption("u", "username", true, "Username used on the domain")
                .addOption("p", "password", true, "Password used to register on the domain")
                .addOption("d", "domain", true, "Domain you are registered on")
                .addOption("e", "extension", true, "Extension you want to call")
                .addOption("ip", "ipAddress", true, "Your IP Address")
                .addRequiredOption("i", "inputLine", true, "Audio input device for the target data line")
                .addRequiredOption("o", "outputLine", true, "Audio output device for the source data line");

        CommandLineParser parser = new DefaultParser();

        return parser.parse(options, strings);
    }



    private void run() {


    }

    private void initialize(TargetDataLine inputLine, SourceDataLine outputLine, String username, String localAddress, String proxyExtension, String proxyHost, String password) throws Exception {

        //todo ports?
        SipManager sipManager = new SipManager(proxyHost, 5060, localAddress, 5080, "udp").
                logging("server.log", "debug.log", 16).
                listener(new SipManagerListener() {


                    @Override
                    public void onRemoteBye() {
                        eventManager.onHangUp();

                        rtpConnectionManager.stopStreamingAudio();
                    }

                    @Override
                    public void callConfirmed(String name, String rtpHost, int rtpPort) {
                        InetSocketAddress proxyAddress = new InetSocketAddress(rtpHost, rtpPort);

                        eventManager.onCall(accountManager.getUserInfo(),name, proxyAddress);

                        try {
                            rtpConnectionManager.connectRTPAudio(proxyAddress);

                        } catch (Throwable e) {
                            throw new IllegalStateException("Unable to connect call", e);
                        }
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
                    public void onRegistered() {
                        eventManager.getPhoneApi().initiateCall(make(proxyExtension,proxyHost));
                    }

                    @Override
                    public void onCancel() {
                        eventManager.onHangUp();
                    }

                    @Override
                    public void authenticationFailed() {
                        System.out.println("Authentication failed :-(");

                    }
                });

        rtpConnectionManager = new RTPConnectionManager(inputLine, outputLine);
        rtpConnectionManager.init();

        accountManager = new SipAccountManager();

        sipManager.initialize(accountManager);

        eventManager = new EventManager(sipManager, rtpConnectionManager);

        String realm = null;
        int dot = proxyHost.indexOf(".");

        if(dot != -1){
            realm = proxyHost.substring(0, dot);
        }

        eventManager.getPhoneApi().register(realm, proxyHost, username, password, make(username, proxyHost));

    }
}
