# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-printmapping mapping.txt
-verbose
-dontoptimize
-dontpreverify
-dontshrink
-dontskipnonpubliclibraryclassmembers
-dontusemixedcaseclassnames
-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes *Annotation*
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*A

-keep public class duc.googlebook.activity.**{*;}
-keep public class duc.googlebook.view.**{*;}
-keep public class duc.googlebook.database.**{*;}
-keep public class duc.googlebook.json.**{*;}
-keep public class duc.googlebook.listview.**{*;}
-keep public class duc.googlebook.model.**{*;}
-keep public class duc.googlebook.viewpager.**{*;}
-keep class * extends android.support.v7.app.AppCompatActivity

-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn org.gradle.**
-dontwarn com.google.**
-dontwarn android.support.**
-dontwarn com.facebook.**

-dontwarn org.gradle.api.tasks.TaskExecutionException
-dontwarn org.gradle.tooling.BuildException

-keepattributes Signature
# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
