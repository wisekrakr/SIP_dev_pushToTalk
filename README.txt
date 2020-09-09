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

MAVEN DEPENDENCIES

These are the dependencies used in the project:
 - Commons-cli 1.4
 - Jain-sip-api 1.2.1.4
 - Jain-sip-ri 1.3.0-91
 - Jain-sdp 1.0.11111
 - Jain-sip-sdp 1.2.11111
 - Jain-sip-tck 1.2.11111
 - Jline 2.14.6
 - Log4j 1.2.17
 - Commons-lang3 3.11


USAGE

In program arguments use the following:
-u <username> Your username
-p <password> Your password
-d <domain> The domain/server registered on
-e <extension> The extension you want to call

It will register/log you in automatically, with the help of commons cli. When registration is successful, the program will call the extension.






AUTHOR

David Buendia Cosano davidiscodinghere@gmail.com