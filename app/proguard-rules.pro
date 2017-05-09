# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\soft\android-sdk_r24.4.1-windows\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
 #ָ�������ѹ������
    -optimizationpasses 5

    #��������ϴ�Сд
    -dontusemixedcaseclassnames

    #��ȥ���Էǹ����Ŀ���
    -dontskipnonpubliclibraryclasses

     #�Ż�  ���Ż���������ļ�
    -dontoptimize

     #ԤУ��
    -dontpreverify

     #����ʱ�Ƿ��¼��־
    -verbose

     # ����ʱ�����õ��㷨
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
    #  compile fileTree(include: ['*.jar'], dir: 'libs')
    #    compile 'com.android.support:appcompat-v7:23.1.1'
    #    compile files('libs/libs/ormlite-android-4.49-SNAPSHOT.jar')
    #    compile files('libs/libs/ormlite-core-4.49-SNAPSHOT.jar')
    #     compile files('libs/libs/Volley.jar')
    #    compile files('libs/libs/android-async-http-1.4.5.jar')
    #     compile files('libs/libs/gson-2.3.jar')
    #     compile files('libs/libs/http-download-manager-1.2.1.jar')
    #     compile files('libs/android-async-http-1.4.5.jar')
    #     compile files('libs/gson-2.3.jar')
    #     compile files('libs/libs/httpclient-4.2.5.jar')
    #    compile files('libs/libs/commons-codec-1.6.jar')
    #    compile files('libs/libs/commons-logging-1.1.1.jar')
    #    compile files('libs/libs/httpcore-4.2.4.jar')
    #����ע��
      -keepattributes *Annotation*
#    -libraryjars libs/libs/ormlite-android-4.49-SNAPSHOT.jar
#    -libraryjars libs/libs/ormlite-core-4.49-SNAPSHOT.jar
#    -libraryjars libs/libs/Volley.jar
#    -libraryjars libs/libs/android-async-http-1.4.5.jar
#    -libraryjars libs/libs/gson-2.3.jar
#    -libraryjars libs/libs/http-download-manager-1.2.1.jar
#    -libraryjars libs/libs/android-async-http-1.4.5.jar
#    -libraryjars libs/libs/httpclient-4.2.5.jar
#    -libraryjars libs/libs/commons-codec-1.6.jar
#    -libraryjars libs/libs/commons-logging-1.1.1.jar
#    -libraryjars libs/libs/httpcore-4.2.4.jar
    # ������Щ�಻������
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService


    #���������v4�����������������
    -keep public class * extends android.support.v4.app.Fragment


    #���Ծ���
    -ignorewarning

    ##��¼���ɵ���־����,gradle buildʱ�ڱ���Ŀ��Ŀ¼���##

    #apk �������� class ���ڲ��ṹ
    -dump class_files.txt
    #δ��������ͳ�Ա
    -printseeds seeds.txt
    #�г��� apk ��ɾ���Ĵ���
    -printusage unused.txt
    #����ǰ���ӳ��
    -printmapping mapping.txt

    ########��¼���ɵ���־���ݣ�gradle buildʱ �ڱ���Ŀ��Ŀ¼���-end######+

    #####���������Լ���Ŀ�Ĳ��ִ����Լ����õĵ�����jar��library#######

    #-libraryjars libs/umeng-analytics-v5.2.4.jar

    #����Ӧ���г���Ҫ���:sdk-v1.0.0.jar,look-v1.0.1.jar
    #-libraryjars libs/sdk-v1.0.0.jar
    #-libraryjars libs/look-v1.0.1.jar



    #���Ծ���
    -dontwarn com.lippi.recorder.utils**
    #����һ�������İ�
    -keep class com.lippi.recorder.utils.** {
        *;
     }

    -keep class  com.lippi.recorder.utils.AudioRecorder{*;}


    #���������v4����v7��
    -dontwarn android.support.**

    ####���������Լ���Ŀ�Ĳ��ִ����Լ����õĵ�����jar��library-end####

    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    #���� native ������������
    -keepclasseswithmembernames class * {
        native <methods>;
    }

    #�����Զ���ؼ��಻������
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    #�����Զ���ؼ��಻������
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    #���� Parcelable ��������
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #���� Serializable ��������
    -keepnames class * implements java.io.Serializable

    #���� Serializable ������������enum ��Ҳ��������
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

    #����ö�� enum �಻������ ���������������ֱ��ʹ������� -keepclassmembers class * implements java.io.Serializable����
    #-keepclassmembers enum * {
    #  public static **[] values();
    #  public static ** valueOf(java.lang.String);
    #}

    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #��������Դ��
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    #����������� �������������ص�
    #�Ckeepattributes Signature

    #�Ƴ�log ��������û���û��ǽ����Լ�����һ�����ؿ����Ƿ������־
    #-assumenosideeffects class android.util.Log {
    #    public static boolean isLoggable(java.lang.String, int);
    #    public static int v(...);
    #    public static int i(...);
    #    public static int w(...);
    #    public static int d(...);
    #    public static int e(...);
    #}
    #����
    -keep class com.eidf.werrr.ec.**{*;}
