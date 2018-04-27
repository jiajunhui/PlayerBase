# 介绍
PlayerBase是一种将播放业务组件化处理的解决方案框架。无论是播放器内的控制视图还是业务视图，均可以做到组件化处理。将播放器的开发变得清晰简单，更利于产品的迭代。框架内包含系统MediaPlayer的解码实现，demo里面有一套完整的IJKPlayer解码方案的实现和接入，请参见源码可以接入其他播放器解码方案。demo自带了播放控制组件、Loading组件，所有UI功能组件可完全自定义。
<br>
# Demo下载
[Demo下载](http://fir.im/lmhz)
<br>
# 设计
PlayerBase是基于事件分发来完成各组件间协作的问题，定义了接收者Receiver以及覆盖层Cover的概念来进行组件的管理。您可以将控制器视图、Loading视图、Error视图以及其他的视图拆分成多个Cover覆盖层进行管理（详见demo中的ControllerCover、LoadingCover、ErrorCover），使用时添加到ReceiverGroup中即可，不用时remove掉即可，方便功能的管理与业务的迭代。详细设计见PPT和代码。

![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/widget_struct.jpeg)

遇到问题，请联系作者。QQ：309812983  Email：junhui_jia@163.com

效果<br>

![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_20180420-170051.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_20180420-170103.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_20180420-170146.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_20180420-170251.png)

# 功能
__-解码方案的组件化、配置化管理__<br>
__-自定义接入各种解码方案__<br>
__-解码方案的切换__<br>
__-提供自定义数据提供者__<br>
__-视图的组件化处理__<br>
__-统一的事件下发机制__<br>
__-扩展事件的添加__<br>
__-支持列表播放中的无缝续播__<br>
__-支持视频切角处理，边缘阴影效果-__<br>
__-等功能……__<br>

# 框架的设计
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure01.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure02.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure03.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure04.png)

详细设计见PPT附件
[PPT附件](https://github.com/jiajunhui/PlayerBase/raw/master/player_base_structure.pptx)

# 使用

需要的权限，如果targetSDK版本在Android M以上的，请注意运行时权限的处理。<br>

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

添加如下依赖<br>

```gradle
dependencies {
  compile 'com.kk.taurus.playerbase:playerbase:3.0.6'
}
```

代码混淆时，请在proguard中添加如下保护<br>

```proguard
-keep public class * extends android.view.View{*;}

-keep public class * implements com.kk.taurus.playerbase.player.IPlayer{*;}

```

使用BaseVideoView对象，可写入xml布局中，也可用代码创建。

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.kk.taurus.playerbase.widget.BaseVideoView
        android:id="@+id/videoView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</RelativeLayout>
```

```java
mVideoView = findViewById(R.id.videoView);
mDataSource = new DataSource("monitor_id");
mVideoView.setOnPlayerEventListener(this);
mVideoView.setOnReceiverEventListener(this);
mVideoView.setReceiverGroup(ReceiverGroupManager.get().getReceiverGroup(this));

//设置数据提供者 MonitorDataProvider
mVideoView.setDataProvider(new MonitorDataProvider());
mVideoView.setDataSource(mDataSource);
mVideoView.start();
```

# 接入其他播放器
具体参见项目代码 IjkPlayer。<br>
使用前做如下配置：<br>

```java
PlayerConfig.addDecoderPlan(new DecoderPlan(1, IjkPlayer.class.getName(), "IjkPlayer"));
PlayerConfig.setDefaultPlanId(1);
```

# 组件视图
demo自带了Loading组件、Controller组件、CompleteCover组件。<br>
这些组件均继承自父类BaseCover（覆盖层基类）

自定义覆盖层组件

```java
public class CustomCover extends BaseCover{
	
	public CustomCover(Context context) {
        super(context);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        //...
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {
		//...
    }

    @Override
    public void onPrivateEvent(int eventCode, Bundle bundle) {
		//...
    }

    @Override
    public View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_custom_cover, null);
    }
    
    //......
	
}
```

自定义组件的使用。

```java
ReceiverGroup receiverGroup = new ReceiverGroup();
receiverGroup.addReceiver("loading_cover", new LoadingCover(context));
receiverGroup.addReceiver("controller_cover", new ControllerCover(context));
mPlayer.setReceiverGroup(receiverGroup);
```

# 数据提供者DataProvider的接入
数据提供者的定义是为了更好的进行播控统一的完整性而设计的。比如Server端给你的是id，你需要用id再去请求某个接口取播放的url，这时我们可以把由id到url这个过程统一的做一个处理，就由DataProvider来完成这个对接过程。

```java
public class MonitorDataProvider extends BaseDataProvider {

    private DataSource mDataSource;

    private int mRequestNum;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    public void handleSourceData(DataSource sourceData) {
        this.mDataSource = sourceData;
        onProviderDataStart();
        mHandler.postDelayed(mLoadDataRunnable, 2000);
    }

    private Runnable mLoadDataRunnable = new Runnable() {
        @Override
        public void run() {
            mRequestNum = mRequestNum%DataUtils.urls.length;
            mDataSource.setData(DataUtils.urls[mRequestNum]);
            mRequestNum++;
            Bundle bundle = BundlePool.obtain();
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA, mDataSource);
            onProviderDataSuccess(IDataProvider.PROVIDER_CODE_SUCCESS_MEDIA_DATA, bundle);
        }
    };

    @Override
    public void cancel() {
        mHandler.removeCallbacks(mLoadDataRunnable);
    }

    @Override
    public void destroy() {
        cancel();
    }
}
```

# 无缝续播的使用
类似于今日头条等应用的效果，在列表中播放时无缝续播进入详情页或者无缝进入全屏页面。<br><br>
原理：解码器动态关联不同的渲染视图（RenderView），比如使用MediaPlayer动态关联SurfaceView，就如同一个电脑主机不断连接不同的显示器。
<br>
详见项目代码。


# 交流
联系方式：junhui_jia@163.com
QQ群：600201778