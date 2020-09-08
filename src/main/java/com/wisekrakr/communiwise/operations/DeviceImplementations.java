package com.wisekrakr.communiwise.operations;

import com.wisekrakr.communiwise.operations.apis.PhoneAPI;
import com.wisekrakr.communiwise.operations.apis.SoundAPI;
import com.wisekrakr.communiwise.phone.audio.AudioManager;
import com.wisekrakr.communiwise.phone.connections.RTPConnectionManager;
import com.wisekrakr.communiwise.phone.sip.SipManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class DeviceImplementations {

    private final SipManager sipManager;
    private final RTPConnectionManager rtpConnectionManager;
    private final AudioManager audioManager;

    public DeviceImplementations(SipManager sipManager, RTPConnectionManager rtpConnectionManager,  AudioManager audioManager) {
        this.sipManager = sipManager;
        this.rtpConnectionManager = rtpConnectionManager;
        this.audioManager = audioManager;

    }

    public SoundAPI getSoundApi(){

        return new SoundAPI() {
            public final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, true);

            @Override
            public void startRecording() {

                audioManager.startRecordingWavFile();
            }

            @Override
            public void playRemoteSound(String resource) {
                try {

                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(resource));
                    AudioInputStream lowResAudioStream = AudioSystem.getAudioInputStream(FORMAT, audioStream);

                    audioManager.startSendingAudio(lowResAudioStream);
                } catch (IOException | UnsupportedAudioFileException e) {
                    System.out.println(" error while sending audio file " + e);
                }
            }

            @Override
            public void stopRecording() {
                audioManager.stopRecording();
            }

            @Override
            public void stopRemoteSound() {
                audioManager.stopSendingAudio();
            }

            @Override
            public void ringing(boolean isRinging) {
                audioManager.ringing(isRinging);
            }

            @Override
            public void mute() {
                //todo interrupt the transmit thread? or the targetdataline?
//                rtpConnectionManager.mute();

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
                sipManager.login(realm, username, password, domain, fromAddress);
            }
        };
    }
}
