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
import android.view.ViewGroup
import android.widget.EditText
import edgecom.tech.athena.repo.AdminRepo
import edgecom.tech.athenasdk.R
import edgecom.tech.uilib.TitlebarActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * 新建事件
 */
class NewEventActivity: TitlebarActivity() {

    private var evName: EditText? = null

    companion object {

        fun start(activity: Activity, appid: String, request: Int) {
            val intent = Intent(activity, NewEventActivity::class.java)
            intent.putExtra("appid", appid)
            activity.startActivityForResult(intent, request)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    override fun getContentView() = createView()

    private fun initUI() {
        immersive(ContextCompat.getColor(this, R.color.theme_red_light))
        setTitleText(R.string.new_event)
    }

    private fun createView(): View {
        val dp8  = dip(8)
        val dp48 = dip(48)

        return UI {
            verticalLayout {

                evName = editText {
                    textColor = Color.BLACK
                    hintResource = R.string.hint_input_event_name
                    backgroundResource = R.color.common_gray
                    setPadding(dp8, dp8, dp8, dp8)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_second_level))
                }.lparams {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    gravity = Gravity.CENTER_HORIZONTAL
                    topMargin = dip(95)
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
                        createEvent()
                    }
                }.lparams {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = dp48
                    topMargin = dip(28)
                    leftMargin = dp48
                    rightMargin = dp48
                }
            }
        }.view
    }

    private fun createEvent() {
        val name = evName?.text.toString()
        if (TextUtils.isEmpty(name)) {
            toast(R.string.empty_event_name)
            return
        }
        AdminRepo.createEvent(intent.getStringExtra("appid"), name) {
            if (it?.data?.hasError == false) {
                val data = Intent()
                data.putExtra("name", it.data?.r?.name)
                data.putExtra("eid",  it.data?.r?.eventId)
                setResult(Activity.RESULT_OK, data)
                finish()
            } else {
                toast(R.string.common_error)
            }
        }
    }

}