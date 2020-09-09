package com.wisekrakr.communiwise.phone.sip;

public interface SipManagerListener {

    void onRemoteBye();

    void callConfirmed(String name, String rtpHost, int rtpPort);

    void authenticationFailed();

    void onAccepted(String rtpProxy, int remoteRtpPort);

    void onRegistered();
}
