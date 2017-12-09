package com.ky.logic.utils.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by yl on 2017/8/25.
 */
public class MyAuthenticator extends Authenticator {
    String userName = null;
    String password = null;

    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
