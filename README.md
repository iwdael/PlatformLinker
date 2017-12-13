# LoginShare  [![](https://jitpack.io/v/aliletter/loginshare.svg)](https://jitpack.io/#aliletter/loginshare)
loginshare integrates login and share of QQ, Weibo and WeChat . It is simple to configure, easy to use, and can be quickly applied to applications, saving a lot of time for developers. (loginshare集成QQ，微博，微信的登录和分享。它配置简单，使用方便，且能够快速运用到应用中，为开发者节省了大量时间。)
# How to
To get a Git project into your build:
## Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
## Step 2. Add the dependency

	dependencies {
	       compile 'com.github.aliletter:loginshare:v1.0.0'
	}
## Step 3. Set JniLibs directory
```Java
android {
    ...
    sourceSets {
        main() {
            jniLibs.srcDirs = ['libs']
        }
    }
}

```
## Step 4. Copy dynamic library file
Click here [dynamic library file](https://raw.githubusercontent.com/aliletter/loginshare/master/libs.7z) ,copy the files to your application.
![Image text](https://github.com/aliletter/LoginShare/blob/master/libs.png)
## Step 5. Copy java file
Click here [java file](https://raw.githubusercontent.com/aliletter/loginshare/master/wxapi.7z),copy and unzip the files to your package.
![Image text](https://github.com/aliletter/LoginShare/blob/master/wxapi.png)
## Step 6. Modify AndroidManifest file
Copy the following code to your Application tag
```Java



```

