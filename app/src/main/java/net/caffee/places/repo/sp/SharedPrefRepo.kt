package net.caffee.places.repo.sp

import android.content.Context
import net.caffee.places.App
import java.util.*


object SharedPrefRepo : SpDataSource {

    private const val NAME = "sharedPrefs"
    private const val DEVICE_ID = "deviceId"
    private const val PUSH_TOKEN = "pushToken"
    private const val AUTH_KEY = "authKey"
    private const val TERMS = "termsAndConditions"
    private const val SIGN_IN = "sign_in"
    private const val FIRST_ENTER = "firstEnter"

    private val cachedValues = HashSet<CachedValue<*>>()

    private var deviceId: CachedValue<String?>
    private var pushToken: CachedValue<String?>
    private var authKey: CachedValue<String?>
    private var terms: CachedValue<Boolean?>
    private var signIn: CachedValue<Boolean?>
    private var firstEnter: CachedValue<Boolean?>

    init {
        CachedValue.sp = App.applicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE)

        deviceId = CachedValue(DEVICE_ID, "no deviceId", String::class.java)
        pushToken = CachedValue(PUSH_TOKEN, "no pushToken", String::class.java)
        authKey = CachedValue(AUTH_KEY, "no authKey", String::class.java)
        terms = CachedValue(TERMS, false, Boolean::class.java)
        signIn = CachedValue(SIGN_IN, false, Boolean::class.java)
        firstEnter = CachedValue(FIRST_ENTER, true, Boolean::class.java)

        cachedValues.add(deviceId)
        cachedValues.add(pushToken)
        cachedValues.add(authKey)
        cachedValues.add(terms)
        cachedValues.add(signIn)
        cachedValues.add(firstEnter)
    }

    override fun setDeviceId(deviceId: String?) = SharedPrefRepo.deviceId.setValue(deviceId)
    override fun getDeviceId(): String? = SharedPrefRepo.deviceId.getValue()

    override fun setPushToken(pushToken: String?) = SharedPrefRepo.pushToken.setValue(pushToken)
    override fun getPushToken(): String? = SharedPrefRepo.pushToken.getValue()

    override fun setAuthKey(authKey: String) = SharedPrefRepo.authKey.setValue(authKey)
    override fun getAuthKey(): String? = SharedPrefRepo.authKey.getValue()

    override fun setTermsChecked(checked: Boolean) = SharedPrefRepo.terms.setValue(checked)
    override fun getTermsChecked(): Boolean = SharedPrefRepo.terms.getValue()!!

    override fun setSignIn(signIn: Boolean) = SharedPrefRepo.signIn.setValue(signIn)
    override fun isSignIn(): Boolean = SharedPrefRepo.signIn.getValue()!!

    override fun setFirstEnter(signIn: Boolean) = SharedPrefRepo.firstEnter.setValue(signIn)
    override fun isSFirstEnter(): Boolean = SharedPrefRepo.firstEnter.getValue()!!


    override fun clear() = cachedValues.forEach { it.delete() }

}