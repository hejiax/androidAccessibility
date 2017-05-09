package com.shuiguo.iview;

import android.view.accessibility.AccessibilityNodeInfo;

import com.shuiguo.base.BaseIView;


/**
 * Created by xy on 16/1/30.
 */
public interface IAliPayView extends BaseIView {
    void setAliNodeInfo(AccessibilityNodeInfo nodeInfo);
    AccessibilityNodeInfo getAliNode();
    AccessibilityNodeInfo getAliRootInActiveWindows();

}
