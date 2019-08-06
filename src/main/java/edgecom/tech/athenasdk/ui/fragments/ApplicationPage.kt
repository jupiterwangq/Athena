package edgecom.tech.athenasdk.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import edgecom.tech.athena.model.App

import edgecom.tech.athena.repo.AdminRepo
import edgecom.tech.athenasdk.R
import edgecom.tech.athenasdk.ui.activities.NewApplicationActivity
import edgecom.tech.athenasdk.ui.adapter.AppListAdapter
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
import org.jetbrains.anko.toast

/**
 * 应用程序列表
 */
class ApplicationPage: Fragment() {

    private val RequestNewApp = 10000

    private var root: View? = null

    private var list: RecyclerView? = null

    private var listAdapter: AppListAdapter = AppListAdapter()

    private var srl: SmartRefreshLayout? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = createView()
            init()
        }
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestNewApp && resultCode == Activity.RESULT_OK) {
            val app = App()
            app.appid  = data?.getStringExtra(NewApplicationActivity.AppId)   ?: ""
            app.name   = data?.getStringExtra(NewApplicationActivity.AppName) ?: ""
            app.secret = data?.getStringExtra(NewApplicationActivity.Secret)  ?: ""
            app.desc   = data?.getStringExtra(NewApplicationActivity.Desc)    ?: ""
            listAdapter.appendApp(app)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun createView(): View {
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

                    list = recyclerView {}

                    setOnRefreshListener {
                        refreshData()
                    }

                }.lparams() {
                    width = MATCH_PARENT
                    height = MATCH_PARENT
                }

                floatingActionButton {
                    scaleType = ImageView.ScaleType.FIT_XY
                    elevation = dip(5).toFloat()
                    imageResource = R.drawable.ic_action_add
                    rippleColor = ContextCompat.getColor(context, R.color.common_gray)
                    backgroundResource = R.drawable.common_btn_bg
                    onClick {
                        NewApplicationActivity.start(activity, RequestNewApp)
                    }
                }.lparams {
                    gravity = Gravity.BOTTOM or Gravity.END
                    bottomMargin = dp16
                    rightMargin = dp16
                }
            }
        }.view
    }

    private fun init() {
        initUI()
        initData()
    }

    private fun initUI() {
        list?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        list?.adapter = listAdapter
    }

    private fun initData() {
        AdminRepo.getApplicationList(0, 10) {
            if (it?.data?.hasError == false) {
                listAdapter.setApps(it.data?.list?.apps)
            } else {
                activity?.toast(R.string.common_error)
            }
        }
    }

    private fun refreshData() {
        AdminRepo.getApplicationList(0, 10) {
            if (it?.data?.hasError == false) {
                srl?.finishRefresh(true)
                listAdapter.setApps(it.data?.list?.apps)
            } else {
                srl?.finishRefresh(false)
                activity?.toast(R.string.common_error)
            }
        }
    }
}