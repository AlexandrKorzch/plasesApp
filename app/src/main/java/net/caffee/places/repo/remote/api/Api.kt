package net.caffee.places.repo.remote.api


import io.reactivex.Flowable
import net.caffee.places.repo.remote.api.ApiSettings.ABUSE
import net.caffee.places.repo.remote.api.ApiSettings.ABUSE_CATEGORIES
import net.caffee.places.repo.remote.api.ApiSettings.ACCEPT
import net.caffee.places.repo.remote.api.ApiSettings.ACTION
import net.caffee.places.repo.remote.api.ApiSettings.ACTIONS
import net.caffee.places.repo.remote.api.ApiSettings.ADD_COMMENT
import net.caffee.places.repo.remote.api.ApiSettings.ADD_ORDER
import net.caffee.places.repo.remote.api.ApiSettings.ADD_PRODUCT
import net.caffee.places.repo.remote.api.ApiSettings.ADVANTAGES
import net.caffee.places.repo.remote.api.ApiSettings.BALANCE_RECHARGE
import net.caffee.places.repo.remote.api.ApiSettings.BOOKINGS
import net.caffee.places.repo.remote.api.ApiSettings.BOOKING_ADD
import net.caffee.places.repo.remote.api.ApiSettings.BOOKING_ANSWER
import net.caffee.places.repo.remote.api.ApiSettings.BOOKING_DELETE
import net.caffee.places.repo.remote.api.ApiSettings.CARTS
import net.caffee.places.repo.remote.api.ApiSettings.CITIES
import net.caffee.places.repo.remote.api.ApiSettings.COMMENTS
import net.caffee.places.repo.remote.api.ApiSettings.CURRENT
import net.caffee.places.repo.remote.api.ApiSettings.EDIT_PROFILE
import net.caffee.places.repo.remote.api.ApiSettings.FAQS
import net.caffee.places.repo.remote.api.ApiSettings.FAVORITE
import net.caffee.places.repo.remote.api.ApiSettings.GET_PROFILE
import net.caffee.places.repo.remote.api.ApiSettings.HALLS
import net.caffee.places.repo.remote.api.ApiSettings.KITCHENS
import net.caffee.places.repo.remote.api.ApiSettings.NOTIFICATION
import net.caffee.places.repo.remote.api.ApiSettings.NOTIFICATIONS
import net.caffee.places.repo.remote.api.ApiSettings.NOTIFICATION_READ
import net.caffee.places.repo.remote.api.ApiSettings.PAGES
import net.caffee.places.repo.remote.api.ApiSettings.PLACE
import net.caffee.places.repo.remote.api.ApiSettings.PLACES_FILTER
import net.caffee.places.repo.remote.api.ApiSettings.PLACES_TYPES
import net.caffee.places.repo.remote.api.ApiSettings.PRODUCT
import net.caffee.places.repo.remote.api.ApiSettings.PRODUCTS
import net.caffee.places.repo.remote.api.ApiSettings.PRODUCT_MAIN_CATEGORIES
import net.caffee.places.repo.remote.api.ApiSettings.SIGN_IN
import net.caffee.places.repo.remote.api.ApiSettings.SORTS
import net.caffee.places.repo.remote.api.ApiSettings.SUPPORT
import net.caffee.places.repo.remote.api.ApiSettings.SUPPORT_CATEGORIES
import net.caffee.places.repo.remote.api.ApiSettings.TRANSACTIONS
import net.caffee.places.repo.remote.api.ApiSettings.TRANSACTION_DELETE
import net.caffee.places.repo.remote.api.ApiSettings.UPDATE_PROFILE_IMAGE
import net.caffee.places.repo.remote.api.ApiSettings.WALLET
import net.caffee.places.repo.remote.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*


interface Api {

    @GET(ADVANTAGES)
    fun getAdvantages(): Flowable<BaseDto<List<Advantage>>>

    @GET
    fun loadPhoto(@Url url: String?): Flowable<ResponseBody>

    @POST(SIGN_IN)
    fun signIn(@Header("token") token: String,
               @Body body: SignInRequestBody): Flowable<BaseDto<List<SignIn>>>

    @POST(PLACES_FILTER)
    fun getFilteredPlaces(@Body body: GetFilteredPlacesBody): Flowable<BaseDto<List<Place>>>

    @POST(FAVORITE)
    fun setFavorite(@Body placeId: FavoriteBody): Flowable<BaseDto<Any>>

    @POST(PLACE)
    fun getPlace(@Body placeId: GetPlaceBody): Flowable<BaseDto<List<PlaceExtended>>>

    @GET(FAQS)
    fun getFags(): Flowable<BaseDto<Fags>>

    @GET(CITIES)
    fun getCities(): Flowable<BaseDto<List<City>>>

    @GET(PLACES_TYPES)
    fun getPlacesTypes(): Flowable<BaseDto<List<BaseCategory>>>

    @GET(KITCHENS)
    fun getKitchens(): Flowable<BaseDto<List<BaseCategory>>>

    @GET(SORTS)
    fun getSorts(): Flowable<BaseDto<List<BaseCategory>>>

    @GET(SUPPORT_CATEGORIES)
    fun getSupportCategories(): Flowable<BaseDto<Support>>

    @POST(SUPPORT)
    fun sendSupportMessage(@Body message: SendToSupportBody): Flowable<BaseDto<Any>>

    @GET(ABUSE_CATEGORIES)
    fun getAbuseCategories(): Flowable<BaseDto<List<BaseCategory>>>

    @POST(ABUSE)
    fun sendAbuse(@Body abuse: AbuseBody): Flowable<BaseDto<Any>>

    @GET(WALLET)
    fun getWalletInfo(): Flowable<BaseDto<List<WalletInfo>>>

    @POST(BOOKINGS)
    fun getBookings(@Body getBooking: GetBookingsBody): Flowable<BaseDto<List<Booking>>>

    @POST(PRODUCT_MAIN_CATEGORIES)
    fun getProductCategories(@Body getCategories: GetProductsCategoriesBody): Flowable<BaseDto<List<ProdCategory>>>

    @POST(PRODUCTS)
    fun getProducts(@Body getProducts: GetProductsBody): Flowable<BaseDto<List<Product>>>

    @POST(PRODUCT)
    fun getProduct(@Body getProduct: GetProductBody): Flowable<BaseDto<List<Product>>>

    @POST(COMMENTS)
    fun getComments(@Body getComments: GetCommentsBody): Flowable<BaseDto<List<Comment>>>

    @POST(ADD_COMMENT)
    fun addComment(@Body addCommentBody: AddCommentBody): Flowable<BaseDto<Any>>

    @POST(TRANSACTIONS)
    fun getTransactions(@Body getTransactionsBody: GetTransactionsBody): Flowable<BaseDto<Any>>

    @GET(GET_PROFILE)
    fun getProfile(): Flowable<BaseDto<List<Profile>>>

    @PATCH(EDIT_PROFILE)
    fun editProfile(@Body editProfile: EditProfileBody): Flowable<BaseDto<Any>>

    @Multipart
    @POST(UPDATE_PROFILE_IMAGE)
    fun updateProfileImage(
            @Header(ACCEPT) accept: String,
            @Part image : MultipartBody.Part): Flowable<BaseDto<Any>>

    @POST(ACTIONS)
    fun getActions(@Body getActions: GetActionsBody): Flowable<BaseDto<List<ActionPojo>>>

    @POST(ACTION)
    fun getAction(@Body getAction: GetActionBody): Flowable<BaseDto<List<ActionPojo>>>

    @GET(PAGES)
    fun getPages(): Flowable<BaseDto<List<Page>>>

    @POST(HALLS)
    fun getHalls(@Body getHallsBody: GetHallsBody): Flowable<BaseDto<List<Hall>>>

    @PUT(BOOKING_ADD)
    fun addBooking(@Body addBooking: AddBookingBody): Flowable<BaseDto<Any>>

    @GET(CURRENT)
    fun getCurrent(): Flowable<BaseDto<CurrentBookingsAndOrders>>

    @POST(ADD_ORDER)
    fun addOrder(@Body body: AddOrderBody): Flowable<BaseDto<Any>>

    @GET(CARTS)
    fun getCarts(): Flowable<BaseDto<List<Cart>>>

    @POST(ADD_PRODUCT)
    fun addProductToCarts(@Body body: AddProductBody): Flowable<BaseDto<Any>>

    @POST(BALANCE_RECHARGE)
    fun getBalanceRecharge(@Body body: BalanceRechargeBody): Flowable<BaseDto<Any>>

    @POST(NOTIFICATION)
    fun getNotification(@Body body: NotificationBody): Flowable<BaseDto<Any>>

    @POST(NOTIFICATIONS)
    fun getNotifications(@Body body: NotificationsBody): Flowable<BaseDto<Any>>

    @PATCH(BOOKING_ANSWER)
    fun bookingAnswer(@Body body: BookingAnswerBody): Flowable<BaseDto<Any>>

    @PATCH(NOTIFICATION_READ)
    fun editProfile(@Body body: NotificationReadBody): Flowable<BaseDto<Any>>

    @DELETE(TRANSACTION_DELETE)
    fun deleteTransaction(@Body body: DeleteTransactionBody): Flowable<BaseDto<Any>>

    @DELETE(BOOKING_DELETE)
    fun deleteBooking(@Body body: DeleteBookingBody): Flowable<BaseDto<Any>>

}
