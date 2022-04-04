package com.example.asasfans.util

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import com.example.asasfans.AsApplication

/**
 * 动态设置View的Margin。
 *
 * @param v view
 * @param left 左px
 * @param top 上px
 * @param right 右px
 * @param bottom 下px
 */
fun setMargin(v: View, left: Int, top: Int, right: Int, bottom: Int) {
    if (v.layoutParams is MarginLayoutParams) {
        val p = v.layoutParams as MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        v.requestLayout()
    }
}

/**
 * dp to px
 *
 * @param dpValue dp value
 * @return px值
 */
fun dip2px(dpValue: Float): Int {
    val scale = AsApplication.context.resources.displayMetrics.density
    return (dpValue * scale + 0.5F).toInt()
}