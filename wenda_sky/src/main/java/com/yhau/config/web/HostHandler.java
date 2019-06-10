package com.yhau.config.web;

import com.yhau.model.User;
import org.springframework.stereotype.Component;

@Component
public class HostHandler {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}