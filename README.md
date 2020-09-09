![Master](https://github.com/ipphone/core/workflows/Master/badge.svg)
<img src="https://img.shields.io/badge/Java-build%20with%20Java-blue"/>
![version](https://img.shields.io/badge/version-0.0.3-blue)

    
                                  CommUniWise: java push-to-talk sip softphone
                                 http://github.com/wisekrakr/Sip_dev_pushToTalk



<a href="https://twitter.com/intent/follow?screen_name=shields_io">
        <img src="https://img.shields.io/twitter/follow/wisekrakr?style=social&logo=twitter"
            alt="follow on Twitter"></a>
            
            

An open source sip phone with push to talk capabilities.
CommUniWise provides an object-oriented JavaScript API for embedding
two-way audio. This is a pure client-side solution and requires zero 
server-side logic on your part.

####LICENSE



####SPECIFICATION

CommUniWise is a software phone (softphone) compatible with the
following specifications:
 - RFC 3261 (SIP),
 - RFC 4566 (SDP),
 - RFC 3550 (RTP),
 - RFC 3551 (RTP Audio/Video profile),
 - RFC 2617 (Digest Authentication),
 - ITU-T G.722 (PCMU, PCMA)

####PREREQUISITES

This software has been developed using Oracle Java Development Kit
version 7.

####MAVEN DEPENDENCIES

These are the dependencies used in the project:
 - Commons-cli 
 - Jain-sip-api 
 - Jain-sip-ri 
 - Jain-sdp 
 - Jain-sip-sdp 
 - Jain-sip-tck 
 - Log4j 
 - Commons-lang3 


####USAGE
SIP account configuration settings:
- **Username:** _name used to register on the domain_
- **Domain:** _domain name_ (like: asterisk.<whatever>)
- **Password:** _sip account password_
- **Realm:** _*_ (done automatically)
- **Proxy Address:** _*_ (done automatically)
- **SIP Registrar:** asterisk server address (server IP or DNS name)


> **For example**, if you have SIP account `666@asterisk.local` with password `1101101`, configuration settings you would use:
> - **Display Name:** `666@asterisk.local`
> - **Username:** `myusername`
> - **Password:** `1101101`
> - **Realm:** `asterisk`
> - **SIP Registrar:** `asterisk.local`
>

In program arguments use the following:
- **-ip <ip address>:** _Your IP address_
- **-i <audio input device>:** _Your audio input device to be used_
- **-o <audio output device>:** _Your audio output device to be used_
- **-u <username>:** _Your username_
- **-d <domain>:** _The domain/server registered on_
- **-p <password>:** _Your password_
- **-e <extension>:** _The extension that will be called immediately after registering/logging in._

It will register/log you in automatically, with the help of commons cli. When registration is successful, the program will call the extension.






##AUTHOR

David Buendia Cosano davidiscodinghere@gmail.com