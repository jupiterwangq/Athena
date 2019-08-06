package edgecom.tech.athenasdk.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import edgecom.tech.athena.repo.UserRepo
import edgecom.tech.athenasdk.R
import edgecom.tech.athenasdk.ui.activities.LoginActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.UI

class UserPage: Fragment() {

    var root: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = ui()
        }
        return root
    }

    private fun ui(): View {

        return UI {

            verticalLayout {

                backgroundResource = R.color.common_gray

                frameLayout {

                    backgroundResource = android.R.color.white

                    textView {
                        textColor = Color.BLACK
                        textResource = R.string.logout
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_second_level))
                        gravity = Gravity.CENTER_VERTICAL
                        setPadding(dip(16), 0, 0, 0)
                    }.lparams {
                        width  = MATCH_PARENT
                        height = MATCH_PARENT
                    }

                    onClick {
                        UserRepo.logout()
                        jumpToLogin()
                    }
                }.lparams {
                    topMargin = dip(20)
                    width  = MATCH_PARENT
                    height = dip(42)
                }

            }

        }.view
    }

    private fun jumpToLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context?.startActivity(intent)
    }
}