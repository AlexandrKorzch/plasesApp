package net.caffee.places.repo.sp


interface SpDataSource {

    fun setDeviceId(deviceId: String?)
    fun getDeviceId() : String?

    fun setPushToken(pushToken: String?)
    fun getPushToken(): String?

    fun setAuthKey(authKey: String)
    fun getAuthKey() : String?

    fun setTermsChecked(checked: Boolean)
    fun getTermsChecked(): Boolean?

    fun setSignIn(signIn: Boolean)
    fun isSignIn(): Boolean

    fun setFirstEnter(signIn: Boolean)
    fun isSFirstEnter(): Boolean

    fun clear()

}