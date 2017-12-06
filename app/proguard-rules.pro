# 代码的压缩级别
-optimizationpasses 5
#混淆时类名
-dontusemixedcaseclassnames
#指定不去忽略非公共的库类。
-dontskipnonpubliclibraryclasses
#指定不去忽略包可见的库类的成员
-dontskipnonpubliclibraryclassmembers
#不预校验
-dontpreverify
# 混淆时记录日志
-verbose
#不压缩输入的类文件
-dontshrink
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep class tv.danmaku.ijk.media.**{*;}

-keep public class * extends android.view.View{*;}

-keep public class * implements com.kk.taurus.playerbase.inter.IDecoder{*;}

-keep public class * implements com.kk.taurus.playerbase.inter.IRenderWidget{*;}

# 如果添加了缓存依赖，请将如下保护加入到proguard中
-keep class com.danikula.videocache.HttpProxyCacheServer{*;}

-keep class com.danikula.videocache.file.FileNameGenerator{*;}

-keep class com.danikula.videocache.HttpProxyCacheServer$Builder{*;}