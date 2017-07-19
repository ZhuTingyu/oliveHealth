package com.warehourse.app.push;

import com.biz.application.BaseApplication;
import com.biz.push.PushManager;
import com.biz.util.LogUtil;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.main.MainActivity;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
//http://dev.xiaomi.com/doc/?p=544

/**
 * 1、PushMessageReceiver是个抽象类，该类继承了BroadcastReceiver。
 * 2、需要将自定义的DemoMessageReceiver注册在AndroidManifest.xml文件中 <receiver
 * android:exported="true"
 * android:name="com.xiaomi.mipushdemo.DemoMessageReceiver"> <intent-filter>
 * <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" /> </intent-filter>
 * <intent-filter> <action android:name="com.xiaomi.mipush.ERROR" />
 * </intent-filter> <intent-filter>
 * <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" /></intent-filter>
 * </receiver>
 * 3、DemoMessageReceiver的onReceivePassThroughMessage方法用来接收服务器向客户端发送的透传消息
 * 4、DemoMessageReceiver的onNotificationMessageClicked方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发
 * 5、DemoMessageReceiver的onNotificationMessageArrived方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数
 * 6、DemoMessageReceiver的onCommandResult方法用来接收客户端向服务器发送命令后的响应结果
 * 7、DemoMessageReceiver的onReceiveRegisterResult方法用来接收客户端向服务器发送注册命令后的响应结果
 * 8、以上这些方法运行在非UI线程中
 *
 * @author mayixiang
 */
public class XiaoMiMessageReceiver extends PushMessageReceiver {
    public static final String TAG = "TAG_XIAOMI";

    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mAccount;
    private String mStartTime;
    private String mEndTime;
    //MiPushMessage
//    1.messageType表示消息的类型，分为三种:MESSAGE_TYPE_REG、MESSAGE_TYPE_ALIAS、MESSAGE_TYPE_TOPIC，这三个是MiPushMessage的静态变量。
//    2.如果服务器是给alias推送的消息，则alias不为null。
//    3.如果服务器是给topic推送的消息，则topic内容不为null。
//    4.passThrough指示服务器端推送的消息类型。
//    5.如果passThrough值为1,则是透传消息；如果passThrough值为0,则是通知栏消息。
//    6.isNotified表示消息是否通过通知栏传给app的。如果为true，表示消息在通知栏出过通知；如果为false，表示消息是直接传给app的，没有弹出过通知。
//    7.messageId是消息的id。
//    8.content是消息的内容。
//    9.otifyType是消息的提醒方式，如震动、响铃和闪光灯。
//    10.description是消息描述。
//    11.title是消息的标题。
//    12.extra是一个map类型，包含一些附加信息，如自定义通知栏铃声的URI、通知栏的点击行为等等。

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        Log.v(TAG,
                "onReceivePassThroughMessage is called. " + message.toString());
        String log = "string.recv_passthrough_message" + message.getContent();
        LogUtil.print(getSimpleDate() + " " + log);

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        Log.v(TAG,
                "onNotificationMessageClicked is called. " + message.toString());
        String log = "string.click_notification_message" + message.getContent();
        LogUtil.print(getSimpleDate() + " " + log);

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }
        String url=null;
        try {
            url=message.getExtra().get("url");
            if (!PushManager.getInstance().shouldInit()){
                Intent intent1 = context.getPackageManager().
                        getLaunchIntentForPackage("com.warehourse.b2b");
                intent1.setData(Uri.parse(url));
                intent1.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intent1);
            }else {
                Intent intent1 = new Intent(context, MainActivity.class);
                intent1.setData(Uri.parse(url));
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(intent1);
            }
        }catch (Exception e){}
        //TODO
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        UserModel.getInstance().updateUserStatus();
    }

//    当客户端向服务器发送注册push、设置alias、取消注册alias、订阅topic、取消订阅topic等等命令后，从服务器返回结果。
//message	服务器返回的命令封装在 MiPushCommandMessage的对象中，可以从该对象中获取command、commandArguments、
//    resultCode、 reason等信息。
//    command表示命令的类型。
//            调用MiPushClient.registerPush(),返回MiPushClient.COMMAND_REGISTER
//    调用MiPushClient.setAlias()，返回MiPushClient.COMMAND_SET_ALIAS
//    调用MiPushClient.unsetAlias()，返回MiPushClient.COMMAND_UNSET_ALIAS
//    调用MiPushClient.subscribe()，返回MiPushClient.COMMAND_SUBSCRIBE_TOPIC
//    调用MiPushClient.unsubscribe()，返回MiPushClient.COMMAND_UNSUBSCIRBE_TOPIC
//    调用MiPushClient.setAcceptTime()，返回MiPushClient.COMMAND_SET_ACCEPT_TIME
//    调用MiPushClient.pausePush()，返回MiPushClient.COMMAND_SET_TARGETPT_TIME
//    调用MiPushClient.resumePush()，返回MiPushClient.COMMAND_SET_ACCEPT_TIME
//    commandArguments 表示命令的参数。例如:
//    注册app就会返回app本次初始化所对应MiPush推送服务的唯一标识regId，alias就会返回alias的内容，订阅和取消订阅主题就会返回topic，setAcceptTime就会返回时间段。
//    resultCode 表示调用命令的结果。如果成功，返回ErrorCode.Sussess即0；否则返回错误类型值。
//    reason表示调用命令失败的原因。如果失败，则返回失败原因，否则返回为null。
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        Log.v(TAG,
                "onCommandResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log = "";
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = "string.register_success";
            } else {
                log = "string.register_fail";
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                log = "set_alias_success" + mAlias;
            } else {
                PushManager.getInstance().register(BaseApplication.getAppContext());
                log = "set_alias_fail" + message.getReason();
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                log = "unset_alias_success" + mAlias;
            } else {
                log = "unset_alias_fail" + message.getReason();
            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAccount = cmdArg1;
                log = "set_account_success" + mAccount;
            } else {
                log = "set_account_fail" + message.getReason();
            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAccount = cmdArg1;
                log = "unset_account_success" + mAccount;
            } else {
                log = "unset_account_fail" + message.getReason();
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
                log = "subscribe_topic_success" + mTopic;
            } else {
                log = "subscribe_topic_fail" + message.getReason();
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = "unsubscribe_topic_success" + mTopic;
            } else {
                log = "unsubscribe_topic_fail" + message.getReason();
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
                log = "string.set_accept_time_success" + mStartTime + "  " + mEndTime;
            } else {
                log = "string.set_accept_time_fail" + message.getReason();
            }
        } else {
            log = message.getReason();
        }
        LogUtil.print(getSimpleDate() + "    " + log);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        Log.v(TAG,
                "onReceiveRegisterResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = "register_success";
            } else {
                log = "string.register_fail";
            }
        } else {
            log = message.getReason();
        }
        LogUtil.print(log);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getSimpleDate() {
        return new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date());
    }
}
