package edgecom.tech.athenasdk.ui

import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.ViewManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import org.jetbrains.anko.custom.ankoView

fun ViewManager.floatingActionButton(init: FloatingActionButton.() -> Unit): FloatingActionButton {
    return ankoView( {FloatingActionButton(it)}, theme = 0, init = init)
}

fun ViewManager.recyclerView(init: RecyclerView.() -> Unit): RecyclerView {
    return ankoView( {RecyclerView(it)}, theme = 0, init = init)
}

fun ViewManager.tabLayout(init: TabLayout.() -> Unit): TabLayout {
    return ankoView( {TabLayout(it)}, theme = 0, init = init)
}

fun ViewManager.viewPager(init: ViewPager.() -> Unit): ViewPager {
    return ankoView( {ViewPager(it)}, theme = 0, init = init)
}

fun ViewManager.smartRefershLayout(init: SmartRefreshLayout.() -> Unit): SmartRefreshLayout {
    return ankoView( {SmartRefreshLayout(it)}, theme = 0, init = init)
}

fun ViewManager.classicHeader(init: ClassicsHeader.() -> Unit): ClassicsHeader {
    return ankoView( {ClassicsHeader(it)}, theme = 0, init = init)
}