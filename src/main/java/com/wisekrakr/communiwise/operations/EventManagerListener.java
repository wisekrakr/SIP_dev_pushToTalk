package com.wisekrakr.communiwise.operations;

import java.net.InetSocketAddress;
import java.util.Map;

public interface EventManagerListener {

    void onCall(Map<String, String> userInfo, String name, InetSocketAddress proxyAddress);
}
