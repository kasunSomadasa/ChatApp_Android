package com.kasun.chat.chatapp;

//message format of JSON Object
public class MessageFormat {

    private String Username;
    private String Message;


    public MessageFormat(String username, String message) {
        Username = username;
        Message = message;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


}
