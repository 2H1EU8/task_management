# Tasker
Tasker  -  is a personal task management app designed to help you stay organized and focused. With Tasker, you can easily manage your daily tasks and prioritize your to-do list to achieve your goals.
## System Requirements
* minSdk 28 or higer
* targetSdk 33 or higher
* Gradle version 7.0.2 or higher
## Installation Guide
1. Clone this repo
```git
git clone https://github.com/2H1EU8/task_management
```
2. Open Android Studio and open the cloned project.
3. Log in to your Firebase account and create a new project.
4. Add the app to your Firebase project and download the google-services.json file.
5. Copy the google-services.json file to the app folder of the project.
6. Set your Firebase configuration values in the build.gradle file:
```
 buildscript {
    dependencies {
        // Add Firebase plugin
       classpath 'com.google.gms:google-services:4.3.8'
    }
}

// Add Firebase plugin
apply plugin: 'com.google.gms.google-services'
```

## Preview 
<img width="565" alt="pr2" src="https://github.com/2H1EU8/task_management/assets/94243225/f3ed23be-ff3f-4d78-8cd4-da9a023cc44d">
<img width="565" alt="pr3" src="https://github.com/2H1EU8/task_management/assets/94243225/2d254dda-957d-4ef8-a06f-cd46617d3dc1">




