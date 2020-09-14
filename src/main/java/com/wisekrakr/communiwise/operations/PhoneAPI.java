package com.wisekrakr.communiwise.operations;


public interface PhoneAPI {
    void login(String realm, String domain, String username, String password, String fromAddress);

    void initiateCall(String recipient);

    void hangup();

}
