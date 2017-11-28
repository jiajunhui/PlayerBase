# PlayerBase
a base player widget library,The library layered the player business. Between the decoder and the layer, the layer and layer communicate with each other through the event. You can use the default written layer, and if you are not satisfied with this, you can customize your layer. Each layer supports plug and play. At the same time, you can always replace the decoder without any sense, so as to minimize the impact.
# Dependency
```gradle
dependencies {
  compile 'com.kk.taurus.playerbase:PlayerBase:2.0.8'
}
```
# Use
```java
mPlayer = new DefaultPlayer(this);
mContainer.addView(mPlayer,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

mCoverCollections = new DefaultReceiverCollections(this);
mCoverCollections.buildDefault();

mPlayer.bindCoverCollections(mCoverCollections);

VideoData videoData = new VideoData("http://...some url");
mPlayer.setDataSource(videoData);
mPlayer.start();

//some handle
mPlayer.pause();
mPlayer.resume();
mPlayer.destroy();

```
#### for setting data include ad videos
```java
PlayData playData = new PlayData(videoData);
List<BaseAdVideo> adVideos = new ArrayList<>();
adVideos.add(new BaseAdVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
playData.setAdVideos(adVideos);

mPlayer.playData(playData,new OnAdCallBack(){
    @Override
    public void onAdPlay(BaseAdPlayer adPlayer, BaseAdVideo adVideo) {
        super.onAdPlay(adPlayer, adVideo);
    }

    @Override
    public void onAdPlayComplete(BaseAdVideo adVideo, boolean isAllComplete) {
        Toast.makeText(PlayerActivity.this, adVideo.getData(), Toast.LENGTH_SHORT).show();
        super.onAdPlayComplete(adVideo, isAllComplete);
    }

    @Override
    public void onVideoStart(BaseAdPlayer adPlayer,VideoData data) {
        super.onVideoStart(adPlayer,data);
    }
});
```
## register listeners
```java
mPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {

    }
});
mPlayer.setOnCoverEventListener(new OnCoverEventListener() {
    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {

    }
});
```
### custom cover
```java
public class PlayCompleteCover extends BaseCover{

    public static final String KEY = "complete_cover";
    private OnCompleteListener onCompleteListener;
    private TextView mTvReplay;

    public PlayCompleteCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {

    }

    @Override
    public View initCoverLayout(Context context) {
        return View.inflate(context, R.layout.layout_play_complete_cover,null);
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE:

                break;
        }
    }

}
```
# Structure
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/screenshot01.png)
### cover
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/screenshot02.png)
### EventReceiver
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/screenshot03.png)
### Event delivery
![image](https://github.com/jiajunhui/PlayerBase/raw/master/screenshot/screenshot04.png)
## License
```
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