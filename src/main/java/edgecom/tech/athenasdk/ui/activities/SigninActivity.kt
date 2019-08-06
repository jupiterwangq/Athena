package edgecom.tech.athenasdk.ui.activities

import android.app.Activity
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
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import edgecom.tech.athena.repo.UserRepo
import edgecom.tech.athenasdk.R
import edgecom.tech.uilib.BaseActivity
import edgecom.tech.uilib.fullScreen
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * 新用户注册
 */
class SigninActivity: BaseActivity() {

    private lateinit var evEmail: EditText
    private lateinit var evPassword: EditText
    private lateinit var evCheckPassword: EditText

    companion object {

        fun start(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, SigninActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(createView())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
    }

    fun createView(): View {

        val dp48 = dip(48)
        val dp42 = dip(42)
        val dp28 = dip(28)

        return verticalLayout {
            backgroundColor = Color.WHITE

            imageView {
                imageResource = R.drawable.icon_statistics
            }.lparams {
                width   = ViewGroup.LayoutParams.WRAP_CONTENT
                height  = ViewGroup.LayoutParams.WRAP_CONTENT
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
                width       = ViewGroup.LayoutParams.MATCH_PARENT
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
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_second_level))
            }.lparams {
                width       = ViewGroup.LayoutParams.MATCH_PARENT
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

            //密码确认
            evCheckPassword = editText {
                backgroundColorResource = R.color.common_gray
                hintResource = R.string.check_password
                isCursorVisible = false
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                setPadding(dip(8), 0, 0, 0)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_second_level))
            }.lparams {
                width       = ViewGroup.LayoutParams.MATCH_PARENT
                height      = dp42
                topMargin   = dp28
                leftMargin  = dp48
                rightMargin = dp48
            }
            evCheckPassword.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) = Unit

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    evCheckPassword.backgroundColorResource = R.color.common_gray
                }
            })

            //注册按钮
            textView {
                textColor = Color.WHITE
                gravity = Gravity.CENTER
                textResource = R.string.new_user
                backgroundResource = R.drawable.common_btn_bg
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.uilib_app_text_size_first_level))

                onClick {
                    sign()
                }
            }.lparams {
                width       = ViewGroup.LayoutParams.MATCH_PARENT
                height      = dp42
                topMargin   = dp48
                leftMargin  = dp48
                rightMargin = dp48
            }

        }
    }

    private fun sign() {
        if (checkParams()) {
            UserRepo.signin(evEmail.text.toString(), evPassword.text.toString()) {
                if (it?.data?.hasError == false) {
                    MainActivity.start(this)    //注册成功直接跳转首页，不用去登录了
                } else {
                    Toast.makeText(this, R.string.signin_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkParams(): Boolean {
        val email    = evEmail.text.toString()
        val password = evPassword.text.toString()
        val checkPassword = evCheckPassword.text.toString()

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

        if (!TextUtils.equals(password, checkPassword)) {
            evPassword.backgroundColorResource = R.color.error_color
            evCheckPassword.backgroundColorResource = R.color.error_color
            return false
        }

        return true
    }
}