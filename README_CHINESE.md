# LoginShare  [![](https://jitpack.io/v/aliletter/loginshare.svg)](https://jitpack.io/#aliletter/loginshare)
loginshare集成QQ，微博，微信的登录和分享。它配置简单，使用方便，且能够快速运用到应用中，为开发者节省了大量时间。
## 使用说明
QQ，微博，微信的登录和分享必须通过LoginShare类来实现，如果你只想实现登录功能，那么这两个方法就可以不用实现(onNewIntent(Intent intent),onActivityResult(int requestCode, int resultCode, Intent data))。
### 代码示例
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
     * @param type 类型 QQ ,Wechat 或 Weibo
     * @param info json格式字符串的用户信息
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
Add it in your root build.gradle at the end of repositories.   [click here for details](https://github.com/aliletter/CarouselBanner/blob/master/root_build.gradle.png)
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
			maven { url "https://dl.bintray.com/thelasterstar/maven/" }
		}
	}
```
## Step 2. Add the dependency

	dependencies {
	       compile 'com.github.aliletter:loginshare:v1.0.0'
	       compile 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'
	       compile 'com.sina.weibo.sdk:core:4.1.0:openDefaultRelease@aar'
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
## step 7. Modify the signature
The signature of the third-party account you apply to is consistent with the signature of your application. Otherwise, there is a problem logging in and sharing.
