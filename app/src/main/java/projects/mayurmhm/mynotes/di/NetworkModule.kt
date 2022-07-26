package projects.mayurmhm.mynotes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import projects.mayurmhm.mynotes.api.AuthInterceptor
import projects.mayurmhm.mynotes.api.NotesAPI
import projects.mayurmhm.mynotes.api.UserAPI
import projects.mayurmhm.mynotes.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Network module requires application context hence used singleton component
@InstallIn(SingletonComponent::class)
@Module // Module will help to create objects for the app
class NetworkModule {

    // returns singleton retrofit builder object
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    // returns UserAPI retrofit object
    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): UserAPI {
        return retrofitBuilder
            .build()
            .create(UserAPI::class.java)
    }

    // add interceptor
    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    // returns NotesAPI retrofit object
    @Singleton
    @Provides
    fun providesNotesAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NotesAPI {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(NotesAPI::class.java)
    }
}