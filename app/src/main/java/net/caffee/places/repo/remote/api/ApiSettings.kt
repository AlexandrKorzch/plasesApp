package net.caffee.places.repo.remote.api


object ApiSettings {

    const val SOLT = "removed"

    /*server*/
    private const val SCHEME = "http://"
    private const val HOSTNAME = "removed"
    const val SERVER = SCHEME + HOSTNAME

    /*methods*/
    const val FAQS = "faqs"
    const val ABUSE = "abuse"
    const val PAGES = "pages"
    const val PLACE = "place"
    const val HALLS = "halls"
    const val SORTS = "sorts"
    const val CITIES = "cities"
    const val ACTION = "action"
    const val PRODUCT = "product"
    const val SUPPORT = "support"
    const val ACTIONS = "actions"
    const val ADD_ORDER = "order" // POST ORDER ADD
    const val SIGN_IN = "sign-in"
    const val CURRENT = "current"
    const val BOOKINGS = "bookings"
    const val PRODUCTS = "products"
    const val FAVORITE = "favorite"
    const val KITCHENS = "kitchens"
    const val WALLET = "wallet-info"
    const val BOOKING_ADD = "booking"
    const val GET_PROFILE = "profile"
    const val EDIT_PROFILE = "profile"
    const val ADVANTAGES = "advantages"
    const val PLACES_TYPES = "place-types"
    const val TRANSACTIONS = "transactions"
    const val COMMENTS = "place-get-comments"
    const val PLACES_FILTER = "places-filter"
    const val ADD_COMMENT = "place-add-comment"
    const val ABUSE_CATEGORIES = "abuse-categories"
    const val UPDATE_PROFILE_IMAGE = "profile-image"
    const val SUPPORT_CATEGORIES = "support-categories"
    const val PRODUCT_MAIN_CATEGORIES = "product-main-categories"

    const val BALANCE_RECHARGE = "transactions" // 20) POST TRANSACTIONS GET BALANCE RECHARGE
    const val TRANSACTION_DELETE = "transaction" // 19) DELETE TRANSACTION DELETE
    const val NOTIFICATIONS = "notifications" //12) POST NOTIFICATIONS GET
    const val NOTIFICATION = "notification" //12) POST NOTIFICATION GET
    const val NOTIFICATION_READ = "read" // 13) PATCH NOTIFICATION READ
    const val BOOKING_DELETE = "booking" // 18) DELETE BOOKING DELETE
    const val BOOKING_ANSWER = "answer" //11) PATCH BOOKING ANSWER 11

    const val ADD_PRODUCT = "cart-item" // ADD PRODUCT TO CART
    const val CARTS = "carts" // GET GET CARTS


    /*headers*/
    const val ACCEPT = "Accept"
    const val ACCEPT_SLASH = "*/*"
}