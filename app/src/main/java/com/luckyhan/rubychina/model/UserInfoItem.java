package com.luckyhan.rubychina.model;

public class UserInfoItem {

    /**
     * 0: login,name
     * 1: location
     * 2: company
     * 3: twitter
     * 4: web
     * 5: github
     * 6: email
     */
    public int type;
    public String desc;
    public String detail;

    public UserInfoItem(int type, String desc, String detail) {
        this.type = type;
        this.desc = desc;
        this.detail = detail;
    }

}
