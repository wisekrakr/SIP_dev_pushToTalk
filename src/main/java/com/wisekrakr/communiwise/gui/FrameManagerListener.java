package com.wisekrakr.communiwise.gui;

import java.net.InetSocketAddress;
import java.util.Map;

public interface FrameManagerListener {

    void close();
    void open(String[] args);
    void onHangUp();
    void onOutgoingCall(Map<String, String> userInfo, String name, InetSocketAddress proxyAddress);
    void onIncomingCall();
    void onAcceptingCall();
    void onDecliningCall();
    void onAuthenticationFailed();
    void onRegistered();
}
