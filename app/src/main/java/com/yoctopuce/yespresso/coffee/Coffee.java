package com.yoctopuce.yespresso.coffee;

import java.util.ArrayList;

public class Coffee {
    private final int _intensity;
    private final int _img;
    private final String _description;
    private String _name;

    public Coffee(String name,String description, int intensity, int img) {
        _name = name;
        _description = description;
        _intensity = intensity;
        _img = img;
    }

    public String getName() {
        return _name;
    }


    public int getIntensity() {
        return _intensity;
    }

    public String getDescription() {
        return _description;
    }

    public int getImg() {
        return _img;
    }
}
