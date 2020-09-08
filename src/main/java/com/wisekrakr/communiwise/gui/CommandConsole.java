package com.wisekrakr.communiwise.gui;

import com.wisekrakr.communiwise.operations.apis.PhoneAPI;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

public class CommandConsole  {

    private PhoneAPI phone;

    //Command line
    private final TextIO textIO;

    //Input fields
    private String username, domain, extension;

    public CommandConsole(PhoneAPI phone) {
        this.phone = phone;

        textIO = TextIoFactory.getTextIO();
    }

    public void start(){

        username = textIO.newStringInputReader().withDefaultValue("damian2").read("Username");
        String password = textIO.newStringInputReader().withInputMasking(true).withDefaultValue("45jf83f").read("Password");
        domain = textIO.newStringInputReader().withDefaultValue("asterisk.interzone").read("Domain");

        String realm = null;
        int dot = domain.indexOf(".");

        if(dot != -1){
            realm = domain.substring(0, dot);
        }

        phone.register(realm, domain, username, password, "sip:" + username + "@" + domain);
    }

    public void stop(){
        textIO.dispose();
    }


    private String sipAddressMaker(){
        return "sip:" + (extension + "@" + domain);
    }

    public void connectWithExtension() {
        extension = textIO.newStringInputReader().read("What extension would you like to call?");

        phone.initiateCall(sipAddressMaker());
    }

    public void onError(){
        textIO.getTextTerminal().println("Authentication failed");
    }
}
