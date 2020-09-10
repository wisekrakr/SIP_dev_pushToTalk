package com.wisekrakr.communiwise.rtp;

/**
 * The Real-time Transport Protocol (RTP) is a network protocol for delivering audio and video over IP networks.
 * This app's RTP runs over User Datagram Protocol (UDP).
 *
 * The RTP header contains a number of fields.
 */
public class RTPPacket {
    /**
     * RTP payload formats often need to include metadata relating to the payload data being transported. Such metadata is sent as a payload header,
     * at the start of the payload section of the RTP packet. The RTP packet also includes space for a header extension [RFC5285]; this can be used
     * to transport payload format independent metadata, for example a SMPTE time code for the packet [RFC5484]. The RTP header extensions are not
     * intended to carry headers that relate to a particular payload format., and must not contain information needed in order to decode the payload.
     */
    private boolean extension;
    /**
     * A single Marker bit normally used to provide important indications. In audio it is normally used to indicate the start of a talk burst.
     * This enables jitter buffer adaptation prior to the beginning of the burst with minimal audio quality impact. In video the marker
     * bit is normally used to indicate the last packet part of a frame. This enables a decoder to finish decoding the picture, where it
     * otherwise may need to wait for the next packet to explicitly know that the frame is finished.
     */
    private boolean marker;
    /**
     * The payload type is used to indicate on a per packet basis which format is used. The binding between a payload type number and a payload
     * format and its configuration are dynamically bound and RTP session specific. The configuration information can be bound to a payload type
     * value by out-of-band signalling [signal]. An example of this would be video decoder configuration information. Commonly the same payload
     * type is used for a media stream for the whole duration of a session. However, in some cases it may be necessary to change the payload
     * format or its configuration during the session.
     */
    private int payloadType;
    /**
     * The sequence number is monotonically increasing and is set as the packet is sent. This property is used in many payload formats
     * to recover the order of everything from the whole stream down to fragments of application data units (ADUs) and the order they need to be decoded.
     * Discontinuous transmissions do not result in gaps in the sequence number, as it is monotonically increasing for each sent RTP packet.
     */
    private int sequenceNumber;
    /**
     * The RTP timestamp indicates the time instant the media main belongs to. For discrete media like video, it normally indicates
     * when the media (frame) was sampled. For continuous media it normally indicates the first time instance the media present in the payload represents.
     * For audio this is the sampling time of the first main. All RTP payload formats must specify the meaning of the timestamp value
     * and the clock rates allowed. Selecting timestamp rate is an active design choice and is further discussed in Section 5.2.
     * Discontinuous transmissions (DTX) that is common among speech codecs, typically results in gaps or jumps in the timestamp values due
     * to that there is no media payload to transmit and the next used timestamp value represent the actual sampling time of the data transmitted.
     */
    private long timestamp;
    /**
     * The Synchronisation Source Identifier (SSRC) is normally not used by a payload format other than to identify the RTP timestamp and sequence
     * number space a packet belongs to, allowing simultaneously reception of multiple media sources. However, some of the RTP mechanisms for improving
     * resilience to packet loss uses multiple SSRCs to separate original data and repair or redundant data.
     */
    private long ssrc;

    /**
     * If the padding bit is set, the packet contains one or more additional padding octets at the end which are not part of the payload
     */
    private boolean padding;

    /**
     * This field identifies the version of RTP. The version defined by this specification is two (2).
     */
    private int version;
    /**
     * The CSRC count contains the number of CSRC identifiers that follow the fixed header.
     */
    private int csrcCount;
    /**
     * The CSRC list identifies the contributing sources for the payload contained in this packet. The number of identifiers is given by the CC field.
     * If there are more than 15 contributing sources, only 15 may be identified. CSRC identifiers are inserted by mixers, using the SSRC identifiers
     * of contributing sources.
     */
    private long[] csrcList;

    private byte[] data;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isPadding() {
        return padding;
    }

    public void setPadding(boolean padding) {
        this.padding = padding;
    }

    public boolean isExtension() {
        return extension;
    }

    public void setExtension(boolean extension) {
        this.extension = extension;
    }

    public int getCsrcCount() {
        return csrcCount;
    }

    public void setCsrcCount(int csrcCount) {
        this.csrcCount = csrcCount;
    }

    public boolean isMarker() {
        return marker;
    }

    public void setMarker(boolean marker) {
        this.marker = marker;
    }

    public int getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(int payloadType) {
        this.payloadType = payloadType;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getSsrc() {
        return ssrc;
    }

    public void setSsrc(long ssrc) {
        this.ssrc = ssrc;
    }

    public long[] getCsrcList() {
        return csrcList;
    }

    public void setCsrcList(long[] csrcList) {
        this.csrcList = csrcList;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
