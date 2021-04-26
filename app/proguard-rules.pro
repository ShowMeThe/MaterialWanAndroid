# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5
# 指定混淆是采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
#指定外部模糊字典
-obfuscationdictionary proguard.txt
#指定class模糊字典
-classobfuscationdictionary proguard.txt
#指定package模糊字典
-packageobfuscationdictionary proguard.txt
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification
#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
#保留行号
-keepattributes SourceFile,LineNumberTable
#保持泛型
-keepattributes Signature

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose


# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 未混淆的类和成员
-printseeds seeds.txt
# 列出从 apk 中删除的代码
-printusage unused.txt
# 混淆前后的映射
-printmapping mapping.txt


-keep public class * extends android.view.View


-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#关闭 Log日志
#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** v(...);
#    public static *** i(...);
#    public static *** e(...);
#    public static *** w(...);
#}

#保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}



#-----------处理实体类---------------
# 在开发的时候我们可以将所有的实体类放在一个包内，这样我们写一次混淆就行了。
-keep class com.show.dmzj.data.** { *; }
-keep class com.show.dmzj.db.dto.** { *; }

################ gson ###############
-keep class sun.misc.Unsafe.** { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson


################ glide ###############
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

################ annotation ###############
-keep class android.support.annotation.** { *; }
-keep interface android.support.annotation.** { *; }


################ RxJava and RxAndroid ###############
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

-keep class io.reactivex.**{ *; }
-keep interface io.reactivex.**{ *; }

-keep class com.squareup.okhttp.**{ *; }
-dontwarn okio.**
-keep interface com.squareup.okhttp.**{ *; }
-dontwarn com.squareup.okhttp.**

-dontwarn io.reactivex.**
-dontwarn retrofit.**
-keep class retrofit.**{ *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}


-dontwarn java.lang.invoke.*
-keepclassmembers class io.reactivex.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}



-dontwarn io.reactivex.internal.util.unsafe.**


-printconfiguration
-keep,allowobfuscation @interface androidx.annotation.Keep
-keepattributes *Annotation*

-keep @androidx.annotation.Keep class *
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}



-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}


-keepclasseswithmembers class * {
    @a.location.trace.master.util.InjectPermission <methods>;
}

-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

################ okhttp ###############
-keep class com.squareup.okhttp.**{ *; }
-keep interface com.squareup.okhttp.**{ *; }
-keep class okhttp3.**{ *; }
-keep interface okhttp3.**{ *; }
-dontwarn com.squareup.okhttp.**


################ retrofit ###############
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}


# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keep class kotlin.reflect.jvm.internal.**{ *; }
# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}


#moshi
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}

-keep @com.squareup.moshi.JsonQualifier interface *

# Enum field names are used by the integrated EnumJsonAdapter.
# values() is synthesized by the Kotlin compiler and is used by EnumJsonAdapter indirectly
# Annotate enums with @JsonClass(generateAdapter = false) to use them with Moshi.
-keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
    <fields>;
    **[] values();
}

-keep class com.show.launch.** { *; }
-keep class * implements com.show.launch.Initializer
-keepclassmembers class * implements androidx.viewbinding.ViewBinding{
  public static <methods>;
}