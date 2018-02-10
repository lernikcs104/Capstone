package com.example.lero.asiecapstone401;

/**
 * Created by lero on 1/31/18.
 */

public class Resources {


    private String desc;

    public Resources(String desc) {


        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {

        return  desc;
    }
}
