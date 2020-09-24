package com.wisekrakr.communiwise.gui.utilities;

public class SipAddressMaker {
    public static String make(String name, String domain){
        return "sip:" + (name + "@" + domain);
    }
}
