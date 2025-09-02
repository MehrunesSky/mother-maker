package com.mehrunessky.mothermaker.clazzs.extend;

public abstract class MyExtendClass implements ExtendClassMotherInterface {

    public ExtendClassMotherInterface customString() {
        return withAString("customString");
    }
}
