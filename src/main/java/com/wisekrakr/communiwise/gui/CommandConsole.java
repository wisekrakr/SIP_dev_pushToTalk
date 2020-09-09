package com.wisekrakr.communiwise.gui;

import com.wisekrakr.communiwise.operations.apis.PhoneAPI;
import org.apache.commons.cli.*;

import java.io.PrintWriter;

import static com.wisekrakr.communiwise.gui.SipAddressMaker.make;

public class CommandConsole  {

    private final PhoneAPI phone;
    private final String[] args;

    private CommandLine cmd;
    private final HelpFormatter formatter;


    //Input fields
    private String username, domain, extension;

    public CommandConsole(PhoneAPI phone, String[] args) {
        this.phone = phone;
        this.args = args;

        formatter = new HelpFormatter();
    }

    public void startConsole(){
        System.out.println("Console started");
        Options options = new Options();

        try {
            cmd = commandLineParser(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        automaticLogin(cmd);

        final PrintWriter writer = new PrintWriter(System.out);
        formatter.printUsage(writer,80,"CommUniWise Push-to-talk SIP Phone", options);
        writer.flush();
    }

    private CommandLine commandLineParser(Options options, String[] strings) throws ParseException {
        options.addOption("u", "username", true, "Username");
        options.addOption("p", "password", true, "Password");
        options.addOption("d", "domain", true, "Domain");
        options.addOption("e", "extension", true, "Extension");

        CommandLineParser parser = new DefaultParser();

        return parser.parse(options, strings);
    }

    private void automaticLogin(CommandLine cmd) {
        username = cmd.getOptionValue("username");
        String password = cmd.getOptionValue("password");
        domain = cmd.getOptionValue("domain");

        String realm = null;
        int dot = domain.indexOf(".");

        if(dot != -1){
            realm = domain.substring(0, dot);
        }

        phone.register(realm, domain, username, password, make(username, domain));
    }


    public void connectWithExtension() {
        extension = cmd.getOptionValue("extension");

        phone.initiateCall(make(extension, domain));
    }

    public void onError(){

    }
}
