package com.wisekrakr.communiwise.gui;

import java.util.Map;

public interface FrameManagerListener {

    void close();
    void open(String[] args);
    void onHangUp();
    void onOutgoingCall(Map<String, String> userInfo);
    void onIncomingCall();
    void onAcceptingCall();
    void onDecliningCall();
    void onAuthenticationFailed();
    void onRegistered();
}
