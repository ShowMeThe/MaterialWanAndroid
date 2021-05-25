package com.show.wanandroid

import android.content.Context
import com.tencent.matrix.plugin.DefaultPluginListener
import com.tencent.matrix.report.Issue
import com.tencent.matrix.util.MatrixLog

class TestPluginListener(context: Context?) : DefaultPluginListener(context) {

    private companion object{
        const val TAG = "TestPluginListener"
    }

    override fun onReportIssue(issue: Issue?) {
        super.onReportIssue(issue)
        MatrixLog.e(TAG, issue.toString())
    }
}