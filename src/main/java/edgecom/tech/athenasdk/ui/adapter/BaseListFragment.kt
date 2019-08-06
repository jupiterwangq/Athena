package edgecom.tech.athenasdk.ui.adapter

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import edgecom.tech.athenasdk.R
import edgecom.tech.athenasdk.ui.classicHeader
import edgecom.tech.athenasdk.ui.floatingActionButton
import edgecom.tech.athenasdk.ui.recyclerView
import edgecom.tech.athenasdk.ui.smartRefershLayout
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip

/**
 * 列表基类
 */
open class BaseListFragment: Fragment() {

    private var root: View? = null

    private var listView: RecyclerView? = null

    private var srl: SmartRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = createView()
            initData()
        }
        return root
    }

    open fun getAdapter(): IAdapterProxy? {
        return null
    }

    open fun initData() = Unit

    open fun add() = Unit

    open fun onRefresh() = Unit

    fun finishRefresh(success: Boolean) {
        srl?.finishRefresh(success)
    }

    protected fun notifyDatasetChanged() {
        listView?.adapter?.notifyDataSetChanged()
    }

    private fun createView(): View {

        val dp16 = dip(16)

        return UI {
            frameLayout {

                srl = smartRefershLayout {

                    classicHeader {
                        setTextSizeTitle(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_third_level))
                        setTextSizeTime(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_small))
                        setPrimaryColor(Color.WHITE)
                        setAccentColorId(R.color.colorAccent)
                    }

                    listView = recyclerView {
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        adapter = BaseListAdapter(this@BaseListFragment.getAdapter())
                    }

                    setOnRefreshListener {
                        onRefresh()
                    }
                }

                floatingActionButton {
                    scaleType   = ImageView.ScaleType.FIT_XY
                    elevation   = dip(5).toFloat()
                    imageResource = R.drawable.ic_action_add
                    rippleColor = ContextCompat.getColor(context, R.color.common_gray)
                    backgroundResource = R.drawable.common_btn_bg
                    onClick {
                        add()
                    }
                }.lparams {
                    gravity = Gravity.END or Gravity.BOTTOM
                    rightMargin  = dp16
                    bottomMargin = dp16
                }

            }
        }.view
    }
}