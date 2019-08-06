package edgecom.tech.athenasdk.ui.activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View

import edgecom.tech.athena.repo.UserRepo
import edgecom.tech.athenasdk.R
import edgecom.tech.uilib.fullScreen

import org.jetbrains.anko.*

class SplashActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(contentView())
        window.setBackgroundDrawable(null)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        grantResults?.let {
            if (it[0] == PackageManager.PERMISSION_GRANTED) { //读取不了IMEI，上报会有问题
                Log.w("SplashActivity", "location permission denied.")
            }
            auth()
        }
    }

    private fun contentView(): View {

        val dip128 = dip(128)

        return frameLayout {

            imageView {
                imageResource = R.drawable.splash_icon
            }.lparams {
                gravity = Gravity.CENTER
                bottomMargin = dip(125)
                width   = dip128
                height  = dip128
            }

            backgroundColor = Color.WHITE

            verticalLayout {
                textView {
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, dip(24).toFloat())
                    textResource = R.string.slogo
                    textColorResource = R.color.theme_red_light
                }.lparams {
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                textView {
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, dip(10).toFloat())
                    textResource = R.string.copyright
                    textColorResource = R.color.theme_red_light
                }.lparams {
                    gravity = Gravity.CENTER_HORIZONTAL
                    topMargin = dip(3)
                }
            }.lparams {
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                bottomMargin = dip(20)
            }
        }
    }

    private fun auth() {
        Handler().postDelayed( {
            //登录态校验成功，直接跳转首页，否则跳转登录页
            UserRepo.auth {
                if (it?.data?.hasError == false) {
                    MainActivity.start(this@SplashActivity)
                } else {
                    LoginActivity.start(this@SplashActivity)
                }
            }
        }, 3000)
    }
}