/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.kk.taurus.playerbase.config;

import android.content.Context;

import com.kk.taurus.playerbase.inter.CacheFileNameGenerator;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Taurus on 2017/12/5.
 */

public class VideoCacheLibraryLoader {

    private static final String CLASS_PATH_ANDROID_VIDEO_CACHE_BUILDER = "com.danikula.videocache.HttpProxyCacheServer$Builder";
    private static final String CLASS_PATH_ANDROID_VIDEO_CACHE_SERVER = "com.danikula.videocache.HttpProxyCacheServer";
    private static final String CLASS_PATH_ANDROID_VIDEO_CACHE_FILE_NAME_GENERATOR = "com.danikula.videocache.file.FileNameGenerator";

    private static final String METHOD_NAME_CACHE_SERVER_GET_PROXY_URL = "getProxyUrl";
    private static Class[] METHOD_PARAMS_GET_PROXY_URL = {String.class};

    private static final String METHOD_NAME_CACHE_BUILDER_CACHE_DIRECTORY = "cacheDirectory";
    private static Class[] METHOD_PARAMS_CACHE_DIRECTORY = {File.class};

    private static final String METHOD_NAME_CACHE_BUILDER_FILE_NAME_GENERATOR = "fileNameGenerator";

    private static final String METHOD_NAME_CACHE_BUILDER_MAX_CACHE_SIZE = "maxCacheSize";
    private static Class[] METHOD_PARAMS_MAX_CACHE_SIZE = {long.class};

    private static final String METHOD_NAME_CACHE_BUILDER_MAX_CACHE_FILES_COUNT = "maxCacheFilesCount";
    private static Class[] METHOD_PARAMS_MAX_CACHE_FILES_COUNT = {int.class};

    private static final String METHOD_NAME_CACHE_BUILDER_BUILD = "build";

    private static Object mVideoCacheServerObject;

    public static void initHttpProxyCacheServer(VideoCacheProxy.Builder builder){
        Context context = builder.getContext();
        Class builderSdkClass = getSDKClass(CLASS_PATH_ANDROID_VIDEO_CACHE_BUILDER);
        if(builderSdkClass==null)
            return;
        //1.create builder instance
        Constructor builderConstructor = getConstructorContextParam(builderSdkClass);
        if(builderConstructor==null)
            return;
        Object builderInstance = newBuilderInstance(builderConstructor,context);
        if(builderInstance==null)
            return;

        //2.setting cache directory
        File cacheDirectory = builder.getCacheDirectory();
        if(cacheDirectory!=null){
            reflectSettingCacheDirectory(builderSdkClass, builderInstance, cacheDirectory);
        }

        //3.setting max cache size
        long maxCacheSize = builder.getMaxCacheSize();
        if(maxCacheSize>0){
            reflectSettingMaxCacheSize(builderSdkClass, builderInstance, maxCacheSize);
        }

        //4.setting max cache count
        int maxCacheCount = builder.getMaxCacheCount();
        if(maxCacheCount>0){
            reflectSettingMaxCacheCount(builderSdkClass, builderInstance, maxCacheCount);
        }

        //5.setting file name generator
        reflectSettingFileNameGenerator(builderSdkClass,builderInstance,builder.getFileNameGenerator());

        //6.build
        reflectBuildCacheServerInstance(builderSdkClass,builderInstance);
    }

    public static String getProxyUrl(Context context, String sourceUrl){
        if(mVideoCacheServerObject==null)
            reflectGetCacheServerInstance(context);
        return reflectGetProxyUrl(sourceUrl);
    }

    private static String reflectGetProxyUrl(String sourceUrl){
        if(mVideoCacheServerObject==null)
            return sourceUrl;
        Class<?> serverClass = mVideoCacheServerObject.getClass();
        try {
            Method method = serverClass.getMethod(METHOD_NAME_CACHE_SERVER_GET_PROXY_URL, METHOD_PARAMS_GET_PROXY_URL);
            Object result = method.invoke(mVideoCacheServerObject, sourceUrl);
            if(result!=null){
                return (String)result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceUrl;
    }

    private static Object reflectGetCacheServerInstance(Context context){
        Class cacheServerClass = getSDKClass(CLASS_PATH_ANDROID_VIDEO_CACHE_SERVER);
        Constructor cacheServerConstructor = getConstructorContextParam(cacheServerClass);
        if(cacheServerConstructor==null)
            return null;
        try {
            return cacheServerConstructor.newInstance(context);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void reflectBuildCacheServerInstance(Class builderSdkClass, Object builderInstance){
        Method method = getMethod(builderSdkClass, METHOD_NAME_CACHE_BUILDER_BUILD);
        try {
            mVideoCacheServerObject = method.invoke(builderInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //setting max cache count for builder
    private static void reflectSettingMaxCacheCount(Class builderSdkClass, Object builderInstance, long maxCacheCount) {
        Method method = getMethod(builderSdkClass, METHOD_NAME_CACHE_BUILDER_MAX_CACHE_FILES_COUNT, METHOD_PARAMS_MAX_CACHE_FILES_COUNT);
        try {
            method.invoke(builderInstance,maxCacheCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //setting max cache size for builder
    private static void reflectSettingMaxCacheSize(Class builderSdkClass, Object builderInstance, long maxCacheSize) {
        Method method = getMethod(builderSdkClass, METHOD_NAME_CACHE_BUILDER_MAX_CACHE_SIZE, METHOD_PARAMS_MAX_CACHE_SIZE);
        try {
            method.invoke(builderInstance,maxCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //setting cache directory for builder
    private static void reflectSettingCacheDirectory(Class builderSdkClass, Object builderInstance, File cacheDirectory) {
        Method method = getMethod(builderSdkClass, METHOD_NAME_CACHE_BUILDER_CACHE_DIRECTORY, METHOD_PARAMS_CACHE_DIRECTORY);
        try {
            method.invoke(builderInstance,cacheDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object newBuilderInstance(Constructor constructor, Context context){
        try {
            return constructor.newInstance(context);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Constructor getConstructorContextParam(Class clz){
        Constructor result = null;
        try{
            result = clz.getConstructor(Context.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private static Method getMethod(Class clz, String methodName, Class...params){
        try {
            return clz.getMethod(methodName,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class getSDKClass(String classPath){
        Class result = null;
        try {
            result = Class.forName(classPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void reflectSettingFileNameGenerator(Class builderSdkClass, Object builderInstance, CacheFileNameGenerator cacheFileNameGenerator) {
        if(cacheFileNameGenerator==null)
            return;
        Class nameGeneratorClass = getSDKClass(CLASS_PATH_ANDROID_VIDEO_CACHE_FILE_NAME_GENERATOR);
        if(nameGeneratorClass==null)
            return;
        try {
            Object proxyInstance = Proxy.newProxyInstance(nameGeneratorClass.getClassLoader()
                    , new Class[]{nameGeneratorClass}, new FileNameGeneratorImpl(cacheFileNameGenerator));
            Method method = getMethod(builderSdkClass, METHOD_NAME_CACHE_BUILDER_FILE_NAME_GENERATOR, nameGeneratorClass);
            if(method!=null){
                method.invoke(builderInstance,proxyInstance);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class FileNameGeneratorImpl implements InvocationHandler{

        private String method_generator = "generate";
        private CacheFileNameGenerator cacheFileNameGenerator;

        public FileNameGeneratorImpl(CacheFileNameGenerator cacheFileNameGenerator){
            this.cacheFileNameGenerator = cacheFileNameGenerator;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            try {
                if(method_generator.equals(method.getName())){
                    result = cacheFileNameGenerator.generate((String) args[0]);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

    }


}
