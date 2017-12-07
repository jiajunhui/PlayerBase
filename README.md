# 介绍
PlayerBase是一种将播放业务组件化处理的解决方案框架。无论是播放器内的控制视图还是业务视图，均可以做到组件化处理。将播放器的开发变得清晰简单，更利于产品的迭代。框架内包含系统MediaPlayer的解码实现，demo里面有一套完整的IJKPlayer解码方案的实现和接入，请参见源码可以接入其他播放器解码方案。框架默认自带一套控制组件，包含播放控制组件、Loading组件、Error组件、手势处理组件等，如不满足需求，可添加自定义组件。
<br><br>
遇到问题，请联系作者。QQ：309812983  Email：junhui_jia@163.com

无缝续播效果<br>
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/play_go_on.gif)


# Demo下载
[Demo下载](https://fir.im/ya4e)

# 功能
__-解码方案的组件化、配置化管理__<br>
__-自定义接入各种解码方案__<br>
__-多种解码方案的切换__<br>
__-提供自定义数据提供者__<br>
__-视图的组件化处理__<br>
__-可根据需求自定义视图组件层__<br>
__-统一的事件下发机制__<br>
__-扩展事件的添加__<br>
__-默认支持历史点定点播放__<br>
__-支持列表播放中的无缝续播__<br>
__-支持边播边缓存功能-__<br>
__-等功能……__<br>

![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_20171203-124242.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_20171203-124309.png)

# 框架的设计
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure01.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure02.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure03.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure04.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure05.png)
<br><br><br>
详细的设计思路见附件PPT<br><br>
[PPT](https://github.com/jiajunhui/PlayerBase/raw/master/player_base_structure.pptx)


# 使用

需要的权限，如果targetSDK版本在Android M以上的，请注意运行时权限的处理。<br>

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

添加如下依赖<br>

```gradle
dependencies {
  compile 'com.kk.taurus.playerbase:PlayerBase:2.1.7'
}
```

如果要使用边播边缓存功能，你还需要添加以下依赖：<br>

```gradle
dependencies {
  compile 'com.danikula:videocache:2.7.0'
}
```

并在application初始化时下添加您的配置代码：

```java
VideoCacheProxy.get().initHttpProxyCacheServer(
                new VideoCacheProxy.Builder(this)
                        .setCacheDirectory(Environment.getExternalStorageDirectory())
                        .setFileNameGenerator(new TestCacheFileNameGenerator()));
VideoCacheProxy.get().setVideoCacheState(true);
```

代码混淆时，请在proguard中添加如下保护<br>

```proguard
-keep public class * extends android.view.View{*;}

-keep public class * implements com.kk.taurus.playerbase.inter.IDecoder{*;}

-keep public class * implements com.kk.taurus.playerbase.inter.IRenderWidget{*;}

# 如果添加了缓存依赖，请将如下保护也加入到proguard中
-keep class com.danikula.videocache.HttpProxyCacheServer{*;}

-keep class com.danikula.videocache.file.FileNameGenerator{*;}

-keep class com.danikula.videocache.HttpProxyCacheServer$Builder{*;}
```

使用DefaultPlayer对象，可写入xml布局中，也可用代码创建。

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.kk.taurus.playerbase.DefaultPlayer
        android:id="@+id/player"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</RelativeLayout>
```

```java
DefaultPlayer mPlayer = (DefaultPlayer) findViewById(R.id.player);
DefaultReceiverCollections receiverCollections = new DefaultReceiverCollections(this);
        receiverCollections.buildDefault();
mPlayer.bindReceiverCollections(receiverCollections);
VideoData videoData = new VideoData("http://url...");
mPlayer.setDataSource(videoData);
mPlayer.start();
```

# 说明
系统的播放模式有两种。<br>
一种是Decoder+RenderView方案，对应API设置中的__IPlayer.WIDGET_MODE_DECODER__<br>
另一种是VideoView方案。对应API设置中的__IPlayer.WIDGET_MODE_VIDEO_VIEW__。<br>
框架默认自带系统的MediaPlayer方案和VideoView方案。如果不做任何配置即为默认的MediaPlayer方案。当需要使用无缝切播功能时，必须设置为__IPlayer.WIDGET_MODE_DECODER__模式。

# 接入其他播放器
一下示例为接入IJKPlayer<br>
__Decoder方案，接入IjkMediaPlayer__<br>

```java
public class IJkDecoderPlayer extends BaseDecoder {

    private IjkMediaPlayer mMediaPlayer;

    public IJkDecoderPlayer(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mMediaPlayer = new IjkMediaPlayer();
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    @Override
    public void setDataSource(VideoData data) {
        //......
    }

    @Override
    public void start() {
        //......
    }

    @Override
    public void pause() {
        //......
    }

    @Override
    public void resume() {
        //......
    }

    @Override
    public void seekTo(int msc) {
        //......
    }

    //......
}
```
具体参见项目代码。<br>
使用前做如下配置：<br>

```java
DecoderType.getInstance().addDecoderType(1,new DecoderTypeEntity("ijkplayer","com.kk.taurus.ijkplayer.IJkDecoderPlayer"));
DecoderType.getInstance().setDefaultDecoderType(1);
ConfigLoader.setDefaultWidgetMode(this, IPlayer.WIDGET_MODE_DECODER);
```

__VideoView方案，接入IjkVideoView__<br>
详见项目代码IJKVideoViewPlayer

# 组件视图
框架自带一套默认组件，包含Loading组件、Error组件、Controller组件、手势处理组件等。<br>
这些组件均继承自父类BaseCover（覆盖层基类）

自定义覆盖层组件

```java
public class CustomCover extends BaseCover{
	
	public CustomCover(Context context) {
        super(context);
    }

    public CustomCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }
    
    //......
	
}
```

自定义组件的使用。

```java
mPlayer = (DefaultPlayer) findViewById(R.id.player);
DefaultReceiverCollections receiverCollections = new DefaultReceiverCollections(this);
receiverCollections
	.addCover("custom_cover",new CustomCover(context)).build();
mPlayer.bindReceiverCollections(receiverCollections);
```

# 无缝续播的使用
类似于今日头条等应用的效果，在列表中播放时无缝续播进入详情页或者无缝进入全屏页面。<br><br>
原理：使用Decoder+RenderView方案，解码器动态关联不同的渲染视图（RenderView），比如使用MediaPlayer动态关联SurfaceView，就如同一个电脑主机不断连接不同的显示器。
<br>
主要方法：

```java
public void setRenderViewForDecoder(IRender render)
```
详见项目代码ListVideoAdapter和SecondActivity。


# 交流
联系方式：junhui_jia@163.com
QQ群：600201778