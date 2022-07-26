package projects.mayurmhm.mynotes.api

import okhttp3.Interceptor
import okhttp3.Response
import projects.mayurmhm.mynotes.utils.Constants.HEADER_NAME
import projects.mayurmhm.mynotes.utils.TokenManager
import javax.inject.Inject

// Interceptor which adds a header to make the endpoint authorised
class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager

    // It will observe the request & allows us to modify the request
    override fun intercept(chain: Interceptor.Chain): Response {

        // access request object
        val request = chain.request().newBuilder()

        // get token
        val token = tokenManager.getToken()

        // add header
        request.addHeader(HEADER_NAME, "Bearer $token")

        // return updated request
        return chain.proceed(request.build())
    }
}