package edgecom.tech.athenasdk

import android.app.Application
import edgecom.tech.athena.Athena

class AthenaApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Athena.initAdmin(this)
    }
}