package edgecom.tech.athenasdk.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import edgecom.tech.athenasdk.R
import edgecom.tech.athenasdk.ui.fragments.AdminPage
import edgecom.tech.athenasdk.ui.fragments.ApplicationPage
import edgecom.tech.athenasdk.ui.fragments.UserPage
import edgecom.tech.uilib.BottomNavigationActivity
import edgecom.tech.uilib.widget.TextTab

class MainActivity : BottomNavigationActivity() {

    //应用列表页
    private var appPage   = ApplicationPage()

    //管理页
    private var adminPage = AdminPage()

    //用户页
    private var userPage  = UserPage()

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersive(ContextCompat.getColor(this, R.color.theme_red_light))
    }

    override fun getTabs(): List<Tab> {
        return listOf(
            newTab(appPage, R.string.app_list_title, TextTab(this)
                .setup(R.string.app_list_tab, R.color.tab_text_color, R.drawable.selector_app_list)),
            newTab(adminPage, R.string.app_admin_tab, TextTab(this)
                .setup(R.string.app_admin_tab, R.color.tab_text_color, R.drawable.selector_admin)),
            newTab(userPage, R.string.app_user_tab, TextTab(this)
                .setup(R.string.app_user_tab, R.color.tab_text_color, R.drawable.selector_user))
        )
    }
}
