![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/playerbase_top_slogen.jpg)
<br><br><br>
**博文地址** ：[Android播放器基础封装库PlayerBase](https://juejin.im/post/5b0d4e6bf265da090f7376d2)

**项目wiki持续更新中……**
# 介绍
**请注意！** **请注意！** **请注意！** **PlayerBase**区别于大部分播放器封装库。

**PlayerBase**是一种将解码器和播放视图组件化处理的解决方案框架。您需要什么解码器实现抽象引入即可，对于视图，无论是播放器内的控制视图还是业务视图，均可以做到组件化处理。将播放器的开发变得清晰简单，更利于产品的迭代。

**PlayerBase**不会为您做任何多余的功能业务组件，有别于大部分播放器封装库的通过配置或者继承然后重写然后定制你需要的功能组件和屏蔽你不需要的功能组件（low!!!）。正确的方向应该是需要什么组件就拓展添加什么组件，而不是已经提供了该组件去选择用不用。

框架内包含系统**MediaPlayer**的解码实现，demo里面包含**IJKPlayer**和**ExoPlayer**的解码方案的实现和接入，请参见源码可以接入其他播放器解码方案。

demo示例集成了播放控制组件**ControllerCover**、加载中组件**LoadingCover**、手势处理组件**GestureCover**、播放完成提示组件**CompleteCover**、错误提示组件**ErrorCover**等。

如果不满足您的需求。没问题，所有UI功能组件您可完全自定义接入并无缝对接播放事件。
<br>
# 功能特色
* **视图的组件化处理**<br>
* **视图组件的高复用、低耦合**<br>
* **解码方案的组件化、配置化管理**<br>
* **视图组件的完全定制**<br>
* **视图组件的热插拔，用时添加不用时移除**<br>
* **自定义接入各种解码方案**<br>
* **解码方案的切换**<br>
* **支持倍速播放**<br>
* **支持Window模式播放**<br>
* **支持Window模式的无缝续播**<br>
* **支持列表模式的无缝续播**<br>
* **支持跨页面无缝续播**<br>
* **支持调整画面显示比例**<br>
* **支持动态调整渲染视图类型**<br>
* **支持VideoView切角处理，边缘阴影效果**<br>
* **提供自定义数据提供者**<br>
* **统一的事件下发机制**<br>
* **扩展事件的添加**<br>
* **等功能……**<br>

# Demo下载
[Demo下载](http://fir.im/lmhz)
<br>
<img src="https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/qrcode.png" width="180" height="180">

# QQ交流群
<img src="https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/qrcode_qq_group.jpg" width="270" height="370">


# 特色
完全将解码器与播放视图组件化处理。不染指任何具体的业务，可随意接入其他播放器，组件完全由用户自定义，组件即插即用。让使用变的更加灵活。如下代码示例，需要什么视图就添加什么视图，不需要时可随时移除。

框架自带MediaPlayer解码，其他解码器的接入只需要实现框架定义的接口并做配置引入即可。

解码器的配置化管理，存在多种解码方案时，可随时切换解码器。

效果<br>

<div align="center">
<img src="https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_001.jpeg" width = "250" height = "444" />
<img src="https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_002.jpeg" width = "250" height = "444" />
<img src="https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_003.jpeg" width = "250" height = "444" />
</div>

<div align="center">
<img src="https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_004.jpeg" width = "250" height = "444" />
<img src="https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_005.jpeg" width = "250" height = "444" />
<img src="https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/Screenshot_006.jpeg" width = "250" height = "444" />
</div>

# 使用

需要的权限，如果targetSDK版本在Android M以上的，请注意运行时权限的处理。<br>

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

添加如下依赖<br>

```gradle
dependencies {
  compile 'com.kk.taurus.playerbase:playerbase:3.2.7.5'
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
    }
    
}
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

**添加组件设置数据**

```java
mVideoView = findViewById(R.id.videoView);
mDataSource = new DataSource("monitor_id");
mVideoView.setOnPlayerEventListener(this);
mVideoView.setOnReceiverEventListener(this);

ReceiverGroup receiverGroup = new ReceiverGroup();
receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
receiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(context));
receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
receiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(context));
mVideoView.setReceiverGroup(receiverGroup);

//设置一个事件处理器
mVideoView.setEventHandler(new OnVideoViewEventHandler());

//设置数据提供者 MonitorDataProvider
mVideoView.setDataProvider(new MonitorDataProvider());
mVideoView.setDataSource(mDataSource);
mVideoView.start();
```

## AVPlayer的使用
如果您想直接使用AVPlayer自己进行处理播放，那么大致步骤如下：<br>
1.初始化一个AVPlayer对象。<br>
2.初始化一个SuperContainer对象，将ReceiverGroup设置到SuperContainer中。<br>
3.使用SuperContainer设置一个渲染视图Render，然后自己处理RenderCallBack并关联解码器。

代码如下：

```java
SuperContainer mSuperContainer = new SuperContainer(context);
ReceiverGroup receiverGroup = new ReceiverGroup();
receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
receiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(context));
receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
receiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(context));
mSuperContainer.setReceiverGroup(receiverGroup);

final RenderTextureView render = new RenderTextureView(mAppContext);
render.setTakeOverSurfaceTexture(true);
//....

mPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE){
            mVideoWidth = bundle.getInt(EventKey.INT_ARG1);
            mVideoHeight = bundle.getInt(EventKey.INT_ARG2);
            mVideoSarNum = bundle.getInt(EventKey.INT_ARG3);
            mVideoSarDen = bundle.getInt(EventKey.INT_ARG4);
            render.updateVideoSize(mVideoWidth, mVideoHeight);
            render.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
        }else if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED){
            mVideoRotation = bundle.getInt(EventKey.INT_DATA);
            render.setVideoRotation(mVideoRotation);
        }else if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED){
            bindRenderHolder(mRenderHolder);
        }
        //将事件分发给子视图
        mSuperContainer.dispatchPlayEvent(eventCode, bundle);
    }
});
mPlayer.setOnErrorEventListener(new OnErrorEventListener() {
    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {
        //将事件分发给子视图
        mSuperContainer.dispatchErrorEvent(eventCode, bundle);
    }
});
mSuperContainer.setOnReceiverEventListener(mInternalReceiverEventListener);
render.setRenderCallback(new IRender.IRenderCallback() {
    @Override
    public void onSurfaceCreated(IRender.IRenderHolder renderHolder, int width, int height) {
        mRenderHolder = renderHolder;
        bindRenderHolder(mRenderHolder);
    }
    @Override
    public void onSurfaceChanged(IRender.IRenderHolder renderHolder, int format, int width, int height) {

    }
    @Override
    public void onSurfaceDestroy(IRender.IRenderHolder renderHolder) {
        mRenderHolder = null;
    }
});
mSuperContainer.setRenderView(render.getRenderView());
mPlayer.setDataSource(dataSource);
mPlayer.start();
```

# 接入其他播放器
具体参见项目代码 IjkPlayer。<br>
使用前做如下配置：<br>

```java
PlayerConfig.addDecoderPlan(new DecoderPlan(1, IjkPlayer.class.getName(), "IjkPlayer"));
PlayerConfig.setDefaultPlanId(1);
```

**解码器的切换**

```java
int PLAN_ID_IJK = 1;
mVideoView.switchDecoder(PLAN_ID_IJK);
mVideoView.setDataSource(dataSource);
mVideoView.start();
```

# 组件视图
demo示例代码演示接入了Loading组件、Controller组件、CompleteCover组件。<br>
这些组件均继承自父类BaseCover（覆盖层基类）

自定义覆盖层cover组件

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
    public void onErrorEvent(int eventCode, Bundle bundle) {
        //...
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {
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
receiverGroup.addReceiver("loading_cover", new CustomCover(context));
mPlayer.setReceiverGroup(receiverGroup);
```
# 无缝续播的使用
类似于今日头条等应用的列表播放效果，在列表中播放时无缝续播进入详情页或者无缝进入全屏页面。<br><br>
原理：解码器动态关联不同的渲染视图（RenderView），比如使用MediaPlayer动态关联SurfaceView，就如同一个电脑主机不断连接不同的显示器。

版本3.2.0之后增加了关联助手，让无缝续播的使用更加简单化。使用关联播放时，您需要提供一个播放视图的容器。比如您要把正在view1容器中播放的画面切换到view2容器中，那么您只需要把view2的容器关联到助手即可。如下示例：

```java
public class TestActivity extends AppcompatActivity{

	RelationAssist mAssist;
	ViewGroup view2;

	public void onCreate(Bundle saveInstance){
		super.onCreate(saveInstance);
		 
		//...
		 
		mAssist = new RelationAssist(this);
		mAssist.setEventAssistHandler(eventHandler);
		mReceiverGroup = ReceiverGroupManager.get().getLiteReceiverGroup(this);
		mAssist.setReceiverGroup(mReceiverGroup);
		DataSource dataSource = new DataSource();
		dataSource.setData("http://...");
		dataSource.setTitle("xxx");
		mAssist.setDataSource(dataSource);
		mAssist.attachContainer(mVideoContainer);
		mAssist.play();
		    
		//...
		switchPlay(view2);
	}
	
	private void switchPlay(ViewGroup container){
		 mAssist.attachContainer(container);
	}

}
```
更加详细的操作请参见项目demo。

# Window模式播放
框架提供了一个WindowVideoView，如果您并不需要对Window进行无缝切播的话，请使用这个WindowVideoView，使用很简单，如下代码示例：

```java
public class WindowVideoViewActivity extends AppCompatActivity {

    WindowVideoView mWindowVideoView;

    DataSource mDataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_video_view);
        int width = 640;
        int height = 360;
        mWindowVideoView = new WindowVideoView(this,
                new FloatWindowParams()
                        .setX(100)
                        .setY(100)
                        .setWidth(width)
                        .setHeight(height)
                        .setGravity(Gravity.TOP | Gravity.LEFT));
        mWindowVideoView.setBackgroundColor(Color.BLACK);
		 //...
        mWindowVideoView.setReceiverGroup(receiverGroup);

        mDataSource = new DataSource();
        mDataSource.setData("http://...");
        mDataSource.setTitle("xxx");
    }

    public void activeWindowVideoView(View view){
        if(mWindowVideoView.isWindowShow()){
            mWindowVideoView.close();
        }else{
            mWindowVideoView.show();
            mWindowVideoView.setDataSource(mDataSource);
            mWindowVideoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWindowVideoView.close();
        mWindowVideoView.stopPlayback();
    }
}
```

如果您需要在Window模式下使用无缝续播，那么请将FloatWindow和RelationAssist二者结合使用。
此处不做代码展示，如需要可进入项目参见WindowSwitchPlayActivity中的代码示例。

# 数据提供者DataProvider的接入
数据提供者的定义是为了更好的进行播控统一的完整性而设计的。比如Server端给你的是id，你需要用id再去请求某个接口取播放的url，这时我们可以把由id到url这个过程统一的做一个处理，就由DataProvider来完成这个对接过程。

```java
public class MonitorDataProvider extends BaseDataProvider {
    
    @Override
    public void handleSourceData(DataSource sourceData) {
        //callback start
        onProviderDataStart(sourceData);
        loadData();
    }
    
    private void loadData(DataSource dataSource){
        //...
        if(sucess){
            Bundle bundle = BundlePool.obtain();
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA, mDataSource);
            //callback success
            onProviderMediaDataSuccess(bundle);
        }else{
            //callback error
            onProviderError(IDataProvider.PROVIDER_CODE_DATA_PROVIDER_ERROR, null);
        }
    }

    @Override
    public void cancel() {
        //...
    }

    @Override
    public void destroy() {
        cancel();
    }
}
```

# 框架的设计
PlayerBase是基于事件分发来完成各组件间协作的问题，定义了接收者Receiver以及覆盖层Cover的概念来进行组件的管理。您可以将控制器视图、Loading视图、Error视图以及其他的视图拆分成多个Cover覆盖层进行管理（详见demo中的ControllerCover、LoadingCover、ErrorCover），使用时添加到ReceiverGroup中即可，不用时remove掉即可，方便功能的管理与业务的迭代。详细设计见PPT和代码。

![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/widget_struct.jpg)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure01.jpg)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure02.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure03.png)
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/frame_structure04.png)


# 交流
联系方式：junhui_jia@163.com
QQ群：600201778

# License
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
