package edgecom.tech.athenasdk.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

import edgecom.tech.athenasdk.R
import edgecom.tech.athenasdk.ui.fragments.CohortFragments
import edgecom.tech.athenasdk.ui.fragments.EventsFragment
import edgecom.tech.athenasdk.ui.tabLayout
import edgecom.tech.athenasdk.ui.viewPager
import edgecom.tech.uilib.TitlebarActivity

import org.jetbrains.anko.*

/**
 * 应用程序
 */
class ApplicationActivity: TitlebarActivity() {

    private var pager: ViewPager? = null

    private var tab: TabLayout? = null

    private var fragments = listOf(EventsFragment(), CohortFragments())

    private var titles = listOf("事件", "Cohort")

    companion object {

        fun start(context: Context, appid: String, secret: String) {
            val intent = Intent(context, ApplicationActivity::class.java)
            intent.putExtra("appid", appid)
            intent.putExtra("secret", secret)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragments[pager?.currentItem ?: 0].onActivityResult(requestCode, resultCode, data)
    }

    override fun getContentView(): View? {
        return UI {
            val themeColor = ContextCompat.getColor(ctx, R.color.theme_red_light)
            verticalLayout {

                tab = tabLayout {
                    id = R.id.app_activity_tab_id
                    setTabTextColors(Color.BLACK, themeColor)
                    setSelectedTabIndicatorHeight(dip(1.5f))
                    setSelectedTabIndicatorColor(themeColor)
                    backgroundColor = Color.WHITE
                }.lparams {
                    width  = MATCH_PARENT
                    height = dip(42)
                }

                view {
                    backgroundColor = Color.rgb(0xea, 0xea, 0xea)
                }.lparams {
                    width  = MATCH_PARENT
                    height = dip(0.8f)
                }

                pager = viewPager {
                    id = R.id.app_activity_pager_id
                }.lparams {
                    width  = MATCH_PARENT
                    height = MATCH_PARENT
                }
            }
        }.view
    }

    private fun initUI() {
        setTitleText(R.string.app_manager)
        immersive(ContextCompat.getColor(this, R.color.theme_red_light))

        pager?.adapter = object: FragmentStatePagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int) = fragments[position]

            override fun getCount() = fragments.size

            override fun getPageTitle(position: Int) = titles[position]
        }

        tab?.setupWithViewPager(pager)
    }
}