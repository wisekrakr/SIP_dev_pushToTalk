============================================================
           CommUniWise: java push-to-talk sip softphone
           http://github.com/wisekrakr/Sip_dev_pushToTalk
============================================================


An open source sip phone with push to talk capabilities.
The user has to setup their own server to register on.

LICENSE



SPECIFICATION


CommUniWise is a software phone (softphone) compatible with the
following specifications:
 - RFC 3261 (SIP),
 - RFC 4566 (SDP),
 - RFC 3550 (RTP),
 - RFC 3551 (RTP Audio/Video profile),
 - RFC 2617 (Digest Authentication),
 - ITU-T G.722 (PCMU, PCMA)


PREREQUISITES

This software has been developed using Oracle Java Development Kit
version 7.


RUNNING

Run the jar file. Register with your username, password and to what domain you are registering on.
You then will be prompted to start a session. Your sessions will always be started muted.


As a Main-Class has been defined in jar manifest, you can also use the following
command line:

  java -jar build/wisephone.jar -Dwise.home=user1 com.wisekrakr.main.PhoneApplication



AUTHOR

David Buendia Cosano davidiscodinghere@gmail.com