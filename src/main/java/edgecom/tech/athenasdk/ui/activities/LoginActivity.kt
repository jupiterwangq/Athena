package edgecom.tech.athenasdk.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.Toast
import edgecom.tech.athena.repo.UserRepo
import edgecom.tech.athenasdk.R
import edgecom.tech.uilib.BaseActivity
import edgecom.tech.uilib.fullScreen
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * 登录页
 */
class LoginActivity: BaseActivity() {

    private lateinit var evEmail: EditText
    private lateinit var evPassword: EditText

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(createView())
    }

    override fun onBackPressed() = Unit

    fun createView(): View {

        val dp48 = dip(48)
        val dp42 = dip(42)
        val dp28 = dip(28)

        return verticalLayout {
            backgroundColor = Color.WHITE

            imageView {
                imageResource = R.drawable.icon_statistics
            }.lparams {
                width   = WRAP_CONTENT
                height  = WRAP_CONTENT
                topMargin = dip(85)
                gravity = Gravity.CENTER_HORIZONTAL
            }

            //邮箱地址输入
            evEmail = editText {
                backgroundColorResource = R.color.common_gray
                hintResource = R.string.input_email
                isCursorVisible = false
                setPadding(dip(8), 0, 0, 0)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_second_level))
            }.lparams {
                width       = MATCH_PARENT
                height      = dp42
                topMargin   = dp48
                leftMargin  = dp48
                rightMargin = dp48
            }
            evEmail.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) = Unit

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    evEmail.backgroundColorResource = R.color.common_gray
                }

            })

            //密码输入
            evPassword = editText {
                backgroundColorResource = R.color.common_gray
                hintResource = R.string.input_password
                isCursorVisible = false
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                setPadding(dip(8), 0, 0, 0)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(edgecom.tech.uilib.R.dimen.uilib_app_text_size_second_level))
            }.lparams {
                width       = MATCH_PARENT
                height      = dp42
                topMargin   = dp28
                leftMargin  = dp48
                rightMargin = dp48
            }
            evPassword.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) = Unit

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    evPassword.backgroundColorResource = R.color.common_gray
                }

            })

            //登录按钮
            textView {
                textColor = Color.WHITE
                gravity = Gravity.CENTER
                textResource = R.string.login
                backgroundResource = R.drawable.common_btn_bg
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(edgecom.tech.uilib.R.dimen.uilib_app_text_size_first_level))

                onClick {
                    login()
                }
            }.lparams {
                width       = MATCH_PARENT
                height      = dp42
                topMargin   = dp48
                leftMargin  = dp48
                rightMargin = dp48
            }

            textView {
                textResource = R.string.new_user
                textColorResource = R.color.theme_red_light
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(edgecom.tech.uilib.R.dimen.uilib_app_text_size_small))
                onClick {
                    SigninActivity.start(this@LoginActivity, 100)
                }
            }.lparams {
                topMargin  = dip(15)
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
    }

    private fun login() {
        if (checkParams()) {
            UserRepo.login(evEmail.text.toString(), evPassword.text.toString()) {
                if (it?.data?.hasError == false && it?.data?.r != null) {  //登录成功
                    MainActivity.start(this)
                } else {
                    Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkParams(): Boolean {
        val email    = evEmail.text.toString()
        val password = evPassword.text.toString()
        if (TextUtils.isEmpty(email)) {
            evEmail.backgroundColorResource = R.color.error_color
            return false
        }
        val reg = Regex("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$")
        val match = reg.find(email)
        if (match == null) {
            evEmail.backgroundColorResource = R.color.error_color
            return false
        }

        if (TextUtils.isEmpty(password) || (password.length < 8)) {
            evPassword.backgroundColorResource = R.color.error_color
            return false
        }

        return true
    }
}