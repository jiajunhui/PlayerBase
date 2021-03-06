![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/playerbase_top_slogen.png)

#### PlayerBase-Core 
[ ![Download](https://api.bintray.com/packages/taurus/Tools/PlayerBase/images/download.svg) ](https://bintray.com/taurus/Tools/PlayerBase/_latestVersion)[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

#### ExoPlayerPkg 
[ ![Download](https://api.bintray.com/packages/taurus/Tools/exoplayer/images/download.svg) ](https://bintray.com/taurus/Tools/exoplayer/_latestVersion)[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)

#### IjkPlayerPkg 
[ ![Download](https://api.bintray.com/packages/taurus/Tools/ijkplayer/images/download.svg) ](https://bintray.com/taurus/Tools/ijkplayer/_latestVersion)[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

**博文地址** ：[Android播放器基础封装库PlayerBase](https://juejin.im/post/5b0d4e6bf265da090f7376d2)

### [提issue注意事项](https://github.com/jiajunhui/PlayerBase/wiki/Issue-Attention)

### [有问题先看介绍和wiki文档](https://github.com/jiajunhui/PlayerBase/wiki)

### [项目介绍](https://github.com/jiajunhui/PlayerBase/wiki/Related-introduction)

### [Demo下载](http://d.firim.info/lmhz)

### 使用及依赖-已适配AndroidX

需要的权限，如果targetSDK版本在Android M以上的，请注意运行时权限的处理。<br>

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

#### 只使用MediaPlayer

```gradle
dependencies {
  
  //该依赖仅包含MediaPlayer解码
  implementation 'com.kk.taurus.playerbase:playerbase:3.4.2'
  
}
```

#### 使用ExoPlayer + MediaPlayer

```gradle
dependencies {
 
  //该依赖包含exoplayer解码和MediaPlayer解码
  //注意exoplayer的最小支持SDK版本为16
  implementation 'cn.jiajunhui:exoplayer:342_2132_019'
  
}
```

#### 使用ijkplayer + MediaPlayer

```gradle
dependencies {
  
  //该依赖包含ijkplayer解码和MediaPlayer解码
  implementation 'cn.jiajunhui:ijkplayer:342_088_012'
  //ijk官方的解码库依赖，较少格式版本且不支持HTTPS。
  implementation 'tv.danmaku.ijk.media:ijkplayer-armv7a:0.8.8'
  # Other ABIs: optional
  implementation 'tv.danmaku.ijk.media:ijkplayer-armv5:0.8.8'
  implementation 'tv.danmaku.ijk.media:ijkplayer-arm64:0.8.8'
  implementation 'tv.danmaku.ijk.media:ijkplayer-x86:0.8.8'
  implementation 'tv.danmaku.ijk.media:ijkplayer-x86_64:0.8.8'
  
}
```

#### 使用ijkplayer + ExoPlayer + MediaPlayer

```gradle
dependencies {
  
  //该依赖包含exoplayer解码和MediaPlayer解码
  //注意exoplayer的最小支持SDK版本为16
  implementation 'cn.jiajunhui:exoplayer:342_2132_019'

  //该依赖包含ijkplayer解码和MediaPlayer解码
  implementation 'cn.jiajunhui:ijkplayer:342_088_012'
  //ijk官方的解码库依赖，较少格式版本且不支持HTTPS。
  implementation 'tv.danmaku.ijk.media:ijkplayer-armv7a:0.8.8'
  # Other ABIs: optional
  implementation 'tv.danmaku.ijk.media:ijkplayer-armv5:0.8.8'
  implementation 'tv.danmaku.ijk.media:ijkplayer-arm64:0.8.8'
  implementation 'tv.danmaku.ijk.media:ijkplayer-x86:0.8.8'
  implementation 'tv.danmaku.ijk.media:ijkplayer-x86_64:0.8.8'
  
}
```

如果您添加依赖exoplayer的库，需要在gradle中增加如下配置。
```gradle
buildTypes {

    //...
    
    compileOptions{
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

代码混淆时，请在proguard中添加如下保护<br>

```proguard
-keep public class * extends android.view.View{*;}

-keep public class * implements com.kk.taurus.playerbase.player.IPlayer{*;}

```

**初始化**

```java
public class App extends Application {

    @Override
    public void onCreate() {
        //...
        
        //如果您想使用默认的网络状态事件生产者，请添加此行配置。
        //并需要添加权限 android.permission.ACCESS_NETWORK_STATE
        PlayerConfig.setUseDefaultNetworkEventProducer(true);
        //初始化库
        PlayerLibrary.init(this);
        
        //-------------------------------------------
        
        //如果添加了'cn.jiajunhui:exoplayer:xxxx'该依赖
        ExoMediaPlayer.init(this);
        
        //如果添加了'cn.jiajunhui:ijkplayer:xxxx'该依赖
        IjkPlayer.init(this);
        
        
        //播放记录的配置
        //开启播放记录
        PlayerConfig.playRecord(true);
        PlayRecordManager.setRecordConfig(
                        new PlayRecordManager.RecordConfig.Builder()
                                .setMaxRecordCount(100)
                                //.setRecordKeyProvider()
                                //.setOnRecordCallBack()
                                .build());
        
    }
    
}
```

### 交流
联系方式：junhui_jia@163.com

QQ群：600201778

<img src="https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/qrcode_qq_group.jpg" width="270" height="370">

### License
```license
Copyright 2017 jiajunhui<junhui_jia@163.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
   
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
