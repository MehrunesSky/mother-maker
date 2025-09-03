package com.mehrunessky.mothermaker.lombok.clazzs;

import com.mehrunessky.mothermaker.Mother;

@Mother
public class User {
    private String name;
    private int age;

    public void setAge(int age) {
        this.age = age;
    }
}