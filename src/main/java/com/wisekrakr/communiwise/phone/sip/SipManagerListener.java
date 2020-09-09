package com.wisekrakr.communiwise.phone.sip;

public interface SipManagerListener {

    void onTextMessage(String message, String from);

    void onRemoteBye();

    void onRemoteCancel();

    void onRemoteDeclined();

    void callConfirmed(String name, String rtpHost, int rtpPort);

    void onUnavailable();

    void onRinging();

    void onBusy();

    void onRemoteAccepted();

    void onRegistered();

    void onBye();

    void onTrying();

    void authenticationFailed();

    void onAccepted(String rtpProxy, int remoteRtpPort);

    void onDeclined();


}
