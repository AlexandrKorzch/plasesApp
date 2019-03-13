package net.caffee.places

import android.content.Context
import android.support.multidex.MultiDexApplication
import io.realm.Realm


class App : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

//        fun getRefWatcher(context: Context): RefWatcher? {
//            val application = context.getApplicationContext() as (App);
//            return application.refWatcher;
//        }
    }

//    private var refWatcher: RefWatcher? = null

    override fun onCreate() {
        super.onCreate()

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            Toast.makeText(this, " This process is dedicated to LeakCanary for heap analysis. ", LENGTH_LONG).show()
//            return;
//        }
//        refWatcher = LeakCanary.install(this);

        Realm.init(this)
    }
}