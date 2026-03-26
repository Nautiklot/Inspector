# Keep the validation annotations for runtime retention
-keep @interface com.inspector.annotations.** { *; }

# Preserve fields that are marked with any of the inspector annotations
-keepclassmembers class * {
    @com.inspector.annotations.* <fields>;
}

# Keep Kotlin metadata for reflection support
-keep class kotlin.Metadata { *; }
