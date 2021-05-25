package com.show.wanandroid;

import com.tencent.mrs.plugin.IDynamicConfig;

public class DynamicConfigImplDemo implements IDynamicConfig {
    public DynamicConfigImplDemo() {}

    public boolean isFPSEnable() { return true;}
    public boolean isTraceEnable() { return true; }
    public boolean isMatrixEnable() { return true; }
    public boolean isDumpHprof() {  return false;}

    @Override
    public String get(String key, String defStr) {

        return defStr;
    }

    @Override
    public int get(String key, int defInt) {

        return defInt;
    }

    @Override
    public long get(String key, long defLong) {
        return defLong;
    }

    @Override
    public boolean get(String key, boolean defBool) {
        return defBool;
    }

    @Override
    public float get(String key, float defFloat) {
        return defFloat;
    }
}
