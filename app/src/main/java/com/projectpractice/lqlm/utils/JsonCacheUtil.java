package com.projectpractice.lqlm.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.projectpractice.lqlm.base.BaseApplication;
import com.projectpractice.lqlm.model.entity.CacheWithDuration;

public class JsonCacheUtil {

    public static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    private JsonCacheUtil() {
        sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveCache(String key, Object value) {
        this.saveCache(key, value, -1L);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String valueStr = gson.toJson(value);
        if (duration != -1L) {
            //当前的时间
            duration += System.currentTimeMillis();
        }
        //保存一个有数据有时间的内容
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, valueStr);
        String cacheWithTime = gson.toJson(cacheWithDuration);
        edit.putString(key, cacheWithTime);
        edit.apply();
    }

    public void delCache(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String valueWithDuration = sharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = gson.fromJson(valueWithDuration, CacheWithDuration.class);
        //对时间进行判断
        long duration = cacheWithDuration.getDuration();
        if (duration != -1 && duration - System.currentTimeMillis() <= 0) {
            //判断是否过期了
            //过期了
            return null;
        } else {
            //没过期
            String cache = cacheWithDuration.getCache();
            return gson.fromJson(cache, clazz);
        }
    }

    private static JsonCacheUtil sInstance = null;

    public static JsonCacheUtil getsInstance() {
        if (sInstance == null) {
            sInstance = new JsonCacheUtil();
        }
        return sInstance;
    }
}
