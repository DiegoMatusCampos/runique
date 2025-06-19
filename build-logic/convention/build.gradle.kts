import org.gradle.kotlin.dsl.gradlePlugin

plugins {
    `kotlin-dsl`
}

group = "com.dm.runique.buildlogic"

dependencies{
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins{
        register("androidApplication"){
            id = "runique.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
    }
}
