package com.shuiguo.service;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.shuiguo.base.BaseAccessibilityService;
import com.shuiguo.presenter.AliPayHouSaiLeiPresenter;
import com.shuiguo.presenter.QQHouSaiLeiPresenter;
import com.shuiguo.presenter.WeChatHouSaiLeiPresenter;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class MonitorService extends BaseAccessibilityService {

    private boolean mMutex , mLuckyMoneyReceived, mNeedUnpack, mNeedBack, mLuckyMoneyPicked;
    private AccessibilityNodeInfo rootNodeInfo, mReceiveNode, mUnpackNode, aliNodeInfo;
    private String lastContentDescription = "";
    private WeChatHouSaiLeiPresenter weChatPresenter = new WeChatHouSaiLeiPresenter();
    private QQHouSaiLeiPresenter qqHouSaiLeiPresenter = new QQHouSaiLeiPresenter();
    private AliPayHouSaiLeiPresenter aliPayHouSaiLeiPresenter = new AliPayHouSaiLeiPresenter();
    @Override
    protected void initWeChatPresenter(AccessibilityEvent event) {
       // if(event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            weChatPresenter.attachIView(this);
            weChatPresenter.accessibilityEvent(event);
            weChatPresenter.checkNodeInfo();
            weChatPresenter.doAction();
        //}
    }
    /**
     * 获得属于桌面的应用的应用包名称
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        //属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo ri : resolveInfo){
            names.add(ri.activityInfo.packageName);
            System.out.println(ri.activityInfo.packageName);
        }
        return names;
    }
    @Override
    protected void initQQPresenter(AccessibilityEvent event) {
        qqHouSaiLeiPresenter.attachIView(this);
        qqHouSaiLeiPresenter.accessibilityEvent(event);
        qqHouSaiLeiPresenter.checkNodeInfo();
        qqHouSaiLeiPresenter.doAction();

    }

    @Override
    protected void initAliPresenter(AccessibilityEvent event) {
        aliPayHouSaiLeiPresenter.attachAliIView(this);
        aliPayHouSaiLeiPresenter.accessibilityEvent(event);
        aliPayHouSaiLeiPresenter.checkNodeInfo();
        aliPayHouSaiLeiPresenter.doAction();

    }


    @Override
    public void setLastContentDescription(String contentDescription) {
        this.lastContentDescription = contentDescription;
    }

    @Override
    public String getLastContentDescription() {
        return lastContentDescription;
    }

    @Override
    public void setRootNodeInfo(AccessibilityNodeInfo rootNodeInfo) {
        this.rootNodeInfo = rootNodeInfo;
    }

    @Override
    public AccessibilityNodeInfo getRootNodeInfo() {
        return rootNodeInfo;
    }

    @Override
    public void setReceiveNode(AccessibilityNodeInfo mReceiveNode) {
        this.mReceiveNode = mReceiveNode;
    }

    @Override
    public AccessibilityNodeInfo getReceiveNode() {
        return mReceiveNode;
    }


    @Override
    public void setUnpackNode(AccessibilityNodeInfo mUnpackNode) {
        this.mUnpackNode = mUnpackNode;
    }

    @Override
    public AccessibilityNodeInfo getUnpackNode() {
        return mUnpackNode;
    }

    @Override
    public void setLuckyMoneyReceived(boolean mLuckyMoneyReceived) {
        this.mLuckyMoneyReceived = mLuckyMoneyReceived;
    }

    @Override
    public boolean isLuckyMoneyReceived() {
        return mLuckyMoneyReceived;
    }

    @Override
    public void setNeedUnpack(boolean mNeedUnpack) {
        this.mNeedUnpack = mNeedUnpack;
    }

    @Override
    public boolean isNeedUnpack() {
        return mNeedUnpack;
    }

    @Override
    public void setNeedBack(boolean mNeedBack) {
        this.mNeedBack = mNeedBack;
    }

    @Override
    public boolean isNeedBack() {
        return mNeedBack;
    }

    @Override
    public void setLuckyMoneyPicked(boolean mLuckyMoneyPicked) {
        this.mLuckyMoneyPicked = mLuckyMoneyPicked;
    }

    @Override
    public boolean isLuckyMoneyPicked() {
        return mLuckyMoneyPicked;
    }

    @Override
    public void setMutex(boolean mMutex) {
        this.mMutex = mMutex;
    }

    @Override
    public boolean isMutex() {
        return mMutex;
    }

    @Override
    public void needBack() {
        performGlobalAction(GLOBAL_ACTION_BACK);
        mMutex = false;
        mNeedBack = false;
    }

    @Override
    public AccessibilityNodeInfo getRootInActiveWindows() {
        return getRootInActiveWindow();
    }

    @Override
    public void setAliNodeInfo(AccessibilityNodeInfo nodeInfo) {
        this.aliNodeInfo = rootNodeInfo;
    }

    @Override
    public AccessibilityNodeInfo getAliNode() {
        return aliNodeInfo;
    }


    @Override
    public AccessibilityNodeInfo getAliRootInActiveWindows() {
        return getRootInActiveWindow();
    }

}
