# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Developer/SDKs/adt-bundle-mac-x86_64/sdk/tools/proguard/proguard-android.txt
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
# Vault
-keepattributes Signature
-dontwarn javax.lang.model.type.TypeMirror
-keep class com.contentful.vault.** { *; }
-keep class **$$SpaceHelper { *; }
-keep class **$$ModelHelper { *; }
-keep class **$Fields extends com.contentful.vault.BaseFields { *; }
-keep @com.contentful.vault.ContentType class * { *; }
-keep @com.contentful.vault.Space class * { *; }

# contentful.java
-keep class com.contentful.java.cda.** { *; }

# RxJava
-dontwarn rx.**

# OkHttp
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

# Gson
-keepattributes EnclosingMethod
-keep class com.google.gson.stream.** { *; }