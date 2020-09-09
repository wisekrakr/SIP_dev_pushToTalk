package com.wisekrakr.communiwise.gui;

import java.net.InetSocketAddress;
import java.util.Map;

public interface FrameManagerListener {

    void onHangUp();
    void onCall(Map<String, String> userInfo, String name, InetSocketAddress proxyAddress);
}
