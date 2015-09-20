package com.plusconnect.Beans;

import java.util.ArrayList;

/**
 * Created by ambesh on 31-08-2015.
 */
public class ChatUserBean extends BaseRequestBean {
    public static String SENT_TO="sentTo";
    public static String MESSAGE="message";
    public static String MESSAGE_DATA="messageData";
    public static String FILENAME="fileName";
    public static String MESSAGETYPE="messageType";
    public static String SENTBY="sentBy";
    public static String MESSAGEID="messageId";
    public static String EXTENTION="extension";
    public static String MESSAGESENTOTYPE="messageSenToType";

    public static String GROUPNAMEIFAPPLICABLE="groupNameIfApplicable";

    public static String ISMESSAGESENTTOSERVERFROMUSERACKNL="isMsgSentToServerFromUserAcknl";

    public static String ISMEGGAGESENTTOUSERFROMSERVERACKNL="isMsgSentToUserFromServerAcknl";
    public static String MSGREADACKNLBYCLIENT="msgReadAcknlByClient";
    public static String MSGREADACKNLBYSERVERTOCLIENT="msgReadAcknlByServerToClient";
    public static String ISERROR="isError";
    public static String TIMEINMILLISECS="timeInMilliSecs";

    private String sentTo;
    private String message;
    private String messageData;
    private String fileName;
    private String messageType;
    private String sentBy;
    private String messageId;
    private String extension;
    private String messageSenToType;
    private String groupNameIfApplicable;
    private String isMsgSentToServerFromUserAcknl;
    private String isMsgSentToUserFromServerAcknl;
    private String msgReadAcknlByClient;
    private String msgReadAcknlByServerToClient;
    private String isError;
    private String timeInMilliSecs;


    public String getSentTo() {

        return sentTo;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageData() {
        return messageData;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getSentBy() {
        return sentBy;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getExtension() {
        return extension;
    }

    public String getMessageSenToType() {
        return messageSenToType;
    }

    public String getGroupNameIfApplicable() {
        return groupNameIfApplicable;
    }

    public String isMsgSentToServerFromUserAcknl() {
        return isMsgSentToServerFromUserAcknl;
    }

    public String isMsgSentToUserFromServerAcknl() {
        return isMsgSentToUserFromServerAcknl;
    }

    public String isMsgReadAcknlByClient() {
        return msgReadAcknlByClient;
    }

    public String isMsgReadAcknlByServerToClient() {
        return msgReadAcknlByServerToClient;
    }

    public String isError() {
        return isError;
    }

    public String getTimeInMilliSecs() {
        return timeInMilliSecs;
    }

}
