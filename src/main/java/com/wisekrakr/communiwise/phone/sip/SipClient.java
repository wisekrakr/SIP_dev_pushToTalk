package com.wisekrakr.communiwise.phone.sip;

public interface SipClient {
    void login(String fromAddress, String domain, String username, String password, String address);
    void initiateCall(String recipient, int localRtpPort);
    void hangup(String recipient);

}
