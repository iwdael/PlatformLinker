# LoginSharePay 
[![](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://github.com/hacknife/LoginSharePay)  [![](https://img.shields.io/badge/version-v1.1.4-orange.svg)](https://github.com/hacknife/LoginSharePay)<br/>
loginshare集成QQ，微博，微信的登录和分享。它配置简单，使用方便，且能够快速运用到应用中，为开发者节省了大量时间。
## 使用说明
QQ，微博，微信的登录和分享必须通过LoginShare类来实现，如果你只想实现登录功能，那么这两个方法就可以不用实现(onNewIntent(Intent intent),onActivityResult(int requestCode, int resultCode, Intent data))。[English](https://github.com/hacknife/LoginSharePay/blob/master/README_ENGLISH.md)
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
## 如何配置
将本仓库引入你的项目:
### Step 1. 添加JitPack仓库到Build文件
合并以下代码到项目根目录下的build.gradle文件的repositories尾。[点击查看详情](https://github.com/hacknife/CarouselBanner/blob/master/root_build.gradle.png)
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
			maven { url "https://dl.bintray.com/thelasterstar/maven/" }
		}
	}
```
### Step 2. 添加依赖   
合并以下代码到需要使用的application Module的dependencies尾。[点击查看详情](https://github.com/hacknife/CarouselBanner/blob/master/application_build.gradle.png)
```Java
	dependencies {
                ...
	       compile 'com.github.hacknife:loginshare:v1.0.1'
	       compile 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'
	       compile 'com.sina.weibo.sdk:core:4.1.0:openDefaultRelease@aar'
	}
```
### Step 3. 设置JniLibs目录
合并以下代码到你的application module的build.gradle。[点击查看详情](https://github.com/hacknife/gifengine/blob/master/jnilibs.png)
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
### Step 4. 复制动态库文件
点击这里下载 [动态文件](https://raw.githubusercontent.com/hacknife/loginsharepay/master/libs.7z)，解压并复制文件到libs目录。
![Image text](https://github.com/hacknife/LoginSharepay/blob/master/libs.png)
### Step 5. 复制Java文件
点击这里下载 [Java文件](https://raw.githubusercontent.com/hacknife/loginsharepay/master/wxapi.7z),解压并复制文件到应用包。
![Image text](https://github.com/hacknife/LoginSharepay/blob/master/wxapi.png)
### Step 6. 修改AndroidManifest文件
复制代码且合并到Application标签
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
## step 7. 修改签名
申请的第三方账户的签名必须与应用的签名一致。 否则，登录和分享会出现问题。
<br><br><br>
## 感谢浏览
如果你有任何疑问，请加入QQ群，我将竭诚为你解答。欢迎Star和Fork本仓库，当然也欢迎你关注我。
<br>
![Image Text](https://github.com/hacknife/CarouselBanner/blob/master/qq_group.png)
