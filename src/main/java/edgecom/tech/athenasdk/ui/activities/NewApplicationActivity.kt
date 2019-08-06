package edgecom.tech.athenasdk.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText

import edgecom.tech.athena.repo.AdminRepo
import edgecom.tech.athenasdk.R
import edgecom.tech.uilib.TitlebarActivity

import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * 新建app
 */
class NewApplicationActivity: TitlebarActivity() {

    private var evName: EditText? = null
    private var evDesc: EditText? = null

    companion object {

        const val AppName = "an"
        const val Secret  = "as"
        const val AppId   = "id"
        const val Desc    = "dc"

        fun start(activity: Activity?, request: Int) {
            val intent = Intent(activity, NewApplicationActivity::class.java)
            activity?.startActivityForResult(intent, request)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    override fun getContentView(): View? {
        return createView()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    private fun createView(): View {
        val dp8  = dip(8)
        val dp48 = dip(48)

        return UI {
            verticalLayout {

                evName = editText {
                    textColor = Color.BLACK
                    hintResource = R.string.hint_input_app_name
                    backgroundResource = R.color.common_gray
                    setPadding(dp8, dp8, dp8, dp8)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_second_level))
                }.lparams {
                    width = MATCH_PARENT
                    height = WRAP_CONTENT
                    gravity = Gravity.CENTER_HORIZONTAL
                    topMargin = dip(95)
                    leftMargin = dp48
                    rightMargin = dp48
                }

                evDesc = editText {
                    textColor = Color.BLACK
                    hintResource = R.string.hint_app_desc
                    backgroundResource = R.color.common_gray
                    setPadding(dp8, dp8, dp8, dp8)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_second_level))
                }.lparams {
                    width = MATCH_PARENT
                    height = WRAP_CONTENT
                    gravity = Gravity.CENTER_HORIZONTAL
                    topMargin = dip(18)
                    leftMargin = dp48
                    rightMargin = dp48
                }

                textView {
                    textResource = R.string.create_app
                    textColor = Color.WHITE
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.common_btn_bg
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_first_level))

                    onClick {
                        createApp()
                    }
                }.lparams {
                    width = MATCH_PARENT
                    height = dp48
                    topMargin = dip(28)
                    leftMargin = dp48
                    rightMargin = dp48
                }
            }
        }.view
    }

    private fun initUI() {
        immersive(ContextCompat.getColor(this, R.color.theme_red_light))
        setTitleText(R.string.new_application)
    }

    private fun createApp() {
        if (!checkParams()) return

        val name = evName?.text.toString()
        val desc = evDesc?.text.toString()
        AdminRepo.createApplication(name, "ANDROID", desc) {
            if (it?.data?.hasError == false) {  //创建成功
                val intent = Intent()
                intent.putExtra(AppName, name)
                intent.putExtra(Secret,  it.data?.app?.secret)
                intent.putExtra(AppId,   it.data?.app?.appid)
                intent.putExtra(Desc,    desc)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                toast(R.string.create_app_failed)
            }
        }
    }

    private fun checkParams(): Boolean {
        val name = evName?.text.toString()
        return if (TextUtils.isEmpty(name)) {
            toast(R.string.app_name_empty)
            false
        } else true
    }
}