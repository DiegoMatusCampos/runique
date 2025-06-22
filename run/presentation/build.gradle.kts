plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.dm.run.presentation"

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.timber)

    implementation(projects.core.domain)
    implementation(projects.run.domain)
}