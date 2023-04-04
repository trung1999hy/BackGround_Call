package com.suntech.colorcall.view.snaphelper

import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.leochuan.CenterSnapHelper
import kotlin.math.abs
/**
 * for DownloadActivity|RecyclerView
 * */
class SnapHelperCustom : CenterSnapHelper() {

    private var mVerticalHelper: OrientationHelper? = null
    private var mHorizontalHelper: OrientationHelper? = null

    fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager))
        } else if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager))
        }
        return null
    }

    private fun findCenterView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper?
    ): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }
        var closestChild: View? = null
        helper?.run {
            val center = startAfterPadding + totalSpace / 2
            var absClosest = Int.MAX_VALUE
            for (i in 0 until childCount) {
                val child = layoutManager.getChildAt(i)
                val childCenter = (getDecoratedStart(child) + getDecoratedMeasurement(child) / 2)
                val absDistance = abs(childCenter - center)
                if (absDistance < absClosest) {
                    absClosest = absDistance
                    closestChild = child
                }
            }
        }
        return closestChild
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper? {
        if (mVerticalHelper == null || mVerticalHelper?.layoutManager !== layoutManager) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return mVerticalHelper
    }

    private fun getHorizontalHelper(
        layoutManager: RecyclerView.LayoutManager
    ): OrientationHelper? {
        if (mHorizontalHelper == null || mHorizontalHelper?.layoutManager !== layoutManager) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper
    }
}