package com.krylovichVI.sweater.domain.util;


import com.krylovichVI.sweater.domain.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author){
        return author != null ? author.getUsername() : "<none>";
    }
}
