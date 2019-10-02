package com.eng.elfarsisy.blog.data;

import com.google.firebase.database.ServerValue;

public class Comment {
    private String comment;
    private String userId;
    private String userImage;
    private String userName;

    Object timeStamp;

    public Comment() {
    }

    public Comment(String comment, String userId, String userName, String userImage) {
        this.comment = comment;
        this.userId = userId;
        this.userImage = userImage;
        this.userName=userName;
        this.timeStamp = ServerValue.TIMESTAMP;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Comment(String comment, String userId, String userName, String userImage, Object timeStamp) {
        this.comment = comment;
        this.userId = userId;
        this.userImage = userImage;
        this.userName=userName;
        this.timeStamp = timeStamp;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
