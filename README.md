# LoginSharePay  [![](https://jitpack.io/v/blackchopper/loginsharepay.svg)](https://jitpack.io/#blackchopper/loginsharepay)
loginshare integrates login and share of QQ, Weibo and WeChat . It is simple to configure, easy to use, and can be quickly applied to applications, saving a lot of time for developers.[中文文档](https://github.com/blackchopper/LoginSharePay/blob/master/README_CHINESE.md)
# Instruction
Login and Share of WeChat, QQ and Weibo can be achieved through loginshare. If you only want to achieve login function, you can not achieve these two methods(onNewIntent(Intent intent),onActivityResult(int requestCode, int resultCode, Intent data)).
## Code Sample
```Java
public class MainActivity extends AppCompatActivity implements OnLoginshareListener {
    LoginShare loginShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginShare = new LoginShare(this);
	
        //loginShare.launchQQLogin();
        //loginShare.launchWechatLogin();
        //loginShare.launchWeiboLogin();
	    //loginShare.launchQQShare(MessageBody.QQMessageBodyBuilder());
        //loginShare.launchWechatShare(MessageBody.WechatMessageBodyBuilder());
        //loginShare.launchWeiboShare(MessageBody.WeiboMessageBodyBuilder());
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginShare.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginShare.unRegister();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        loginShare.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loginShare.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * @param type QQ ,Wechat or Weibo
     * @param info Json String of userinfo
     */
    @Override
    public void onLoginSuccess(Type type, String info) {
    }

    @Override
    public void onLoginCancel(Type type) {
    }

    @Override
    public void onLoginError(Type type, int errorCode) {
    }

    @Override
    public void onShareSuccess(Type type) {
    }

    @Override
    public void onShareCancel(Type type) {
    }

    @Override
    public void onShareError(Type type, int code) {
    }
```
## How to
To get a Git project into your build:
### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories.   [click here for details](https://github.com/blackchopper/CarouselBanner/blob/master/root_build.gradle.png)
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
			maven { url "https://dl.bintray.com/thelasterstar/maven/" }
		}
	}
```
### Step 2. Add the dependency
Add it in your application module build.gradle at the end of dependencies where you want to use.[click here for details](https://github.com/blackchopper/CarouselBanner/blob/master/application_build.gradle.png)
```Java
	dependencies {
	       compile 'com.github.blackchopper:loginshare:v1.0.1'
	       compile 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'
	       compile 'com.sina.weibo.sdk:core:4.1.0:openDefaultRelease@aar'
	}
```
### Step 3. Set JniLibs directory
Add it in your application module build.gradle.[click here for details](https://github.com/blackchopper/gifengine/blob/master/jnilibs.png)
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
### Step 4. Copy dynamic library file
Click here [dynamic library file](https://raw.githubusercontent.com/blackchopper/loginshare/master/libs.7z) ,copy the files to your application.
![Image text](https://github.com/blackchopper/LoginShare/blob/master/libs.png)
### Step 5. Copy java file
Click here [java file](https://raw.githubusercontent.com/blackchopper/loginshare/master/wxapi.7z),copy and unzip the files to your package.
![Image text](https://github.com/blackchopper/LoginShare/blob/master/wxapi.png)
### Step 6. Modify AndroidManifest file
Copy the following code to your Application tag
```Java
    <application 
        android:icon="@mipmap/app_logo"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/app_logo_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
	......
        <meta-data
            android:name="qq"
            android:value="qq_key" />
        <meta-data
            android:name="wechat"
            android:value="wechat_key" />
        <meta-data
            android:name="wechatSecret"
            android:value="wechat_secret" />
        <meta-data
            android:name="weibo"
            android:value="weibo_key" />
        <meta-data
            android:name="weiboRedirectUrl"
            android:value="weibo_redirectUrl" />
        <meta-data
            android:name="weiboScope"
            android:value="weibo_scope" />
        <activity
            android:name="com.tencent.tauth.AuthActivity"
            android:launchMode="singleTask"
            android:noHistory="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data android:scheme="tencent1234567" />
		<!-- tencent+qq_key -->
            </intent-filter>
        </activity>
        <activity
            android:name="com.tencent.connect.common.AssistActivity"
            android:configChanges="orientation|keyboardHidden"
            android:screenOrientation="behind"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

        <!-- java file corresponds to it  -->
        <activity
            android:name=".wxapi.WXEntryActivity"
            android:exported="true"
            android:label="@string/app_name" />	    
	......

```
### step 7. Modify the signature
The signature of the third-party account you apply to is consistent with the signature of your application. Otherwise, there is a problem logging in and sharing.
</br></br></br>
## Thank you for your browsing
If you have any questions, please join the QQ group. I will do my best to answer it for you. Welcome to star and fork this repository, alse follow me.
<br>
![Image Text](https://github.com/blackchopper/CarouselBanner/blob/master/qq_group.png)
