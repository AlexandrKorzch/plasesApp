package net.caffee.places.push

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import net.caffee.places.repo.sp.SharedPrefRepo


class TokenRefreshService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        SharedPrefRepo.setPushToken(refreshedToken)
    }
}