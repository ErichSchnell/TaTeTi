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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep the User data class
-keep class com.example.tateti_20.data.network.model.GameModelData { *; }

# Keep the default constructor for classes involved in deserialization
-keepclassmembers class com.example.tateti_20.data.network.model.GameModelData {
    <init>();
}
# Keep the User data class
-keep class com.example.tateti_20.data.network.model.PlayerModelData { *; }

# Keep the default constructor for classes involved in deserialization
-keepclassmembers class com.example.tateti_20.data.network.model.PlayerModelData {
    <init>();
}
# Keep the User data class
-keep class com.example.tateti_20.data.network.model.UserModelData { *; }

# Keep the default constructor for classes involved in deserialization
-keepclassmembers class com.example.tateti_20.data.network.model.UserModelData {
    <init>();
}

# Keep the names of classes/members we are using for Firebase serialization/deserialization
-keepnames class com.google.firebase.** {*;}
-keepnames class com.google.android.gms.** {*;}

# Keep the Firestore annotations
-keepattributes *Annotation*,EnclosingMethod

# Keep the required methods for Firestore to work properly
-keep class com.google.firebase.firestore.** { *; }
-keepclassmembers class com.google.firebase.firestore.** { *; }