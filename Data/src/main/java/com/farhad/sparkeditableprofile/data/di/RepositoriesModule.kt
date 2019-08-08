package com.farhad.sparkeditableprofile.data.di

import com.farhad.sparkeditableprofile.data.mapper.*
import com.farhad.sparkeditableprofile.data.repository.ProfileRepositoryImpl
import com.farhad.sparkeditableprofile.data.remote.Remote
import com.farhad.sparkeditableprofile.data.remote.ServiceGenerator
import com.farhad.sparkeditableprofile.data.repository.QuestionRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Named

@Module
class RepositoriesModule {

    @Provides
    fun questionRepositoryImpl(
        remote: Remote,
        locationEntityMapper: LocationEntityMapper,
        singleChoiceAnswerEntityMapper: SingleChoiceAnswerEntityMapper
    ): QuestionRepositoryImpl{
        return QuestionRepositoryImpl(remote, locationEntityMapper, singleChoiceAnswerEntityMapper)
    }

    @Provides
    fun userRepositoryImpl(
        remote: Remote,
        profileEntityMapper: ProfileEntityMapper,
        requestStatusEntityMapper: RequestStatusEntityMapper,
        profilePictureMapper: ProfilePictureEntityMapper
    ): ProfileRepositoryImpl {
        return ProfileRepositoryImpl(remote,requestStatusEntityMapper, profileEntityMapper, profilePictureMapper)
    }

    @Provides
    fun profilePictureMapper(): ProfilePictureEntityMapper{
        return ProfilePictureEntityMapper()
    }

    @Provides
    fun profileEntityMapper(
        locationEntityMapper: LocationEntityMapper,
        singleChoiceAnswerEntityMapper: SingleChoiceAnswerEntityMapper,
        profilePictureMapper: ProfilePictureEntityMapper
    ): ProfileEntityMapper{
        return ProfileEntityMapper(locationEntityMapper, singleChoiceAnswerEntityMapper, profilePictureMapper)
    }

    @Provides
    fun locationEntityMapper(): LocationEntityMapper {
        return LocationEntityMapper()
    }

    @Provides
    fun singleChoiceAnswerEntityMapper(): SingleChoiceAnswerEntityMapper{
        return SingleChoiceAnswerEntityMapper()
    }

    @Provides
    fun requestStatusEntityMapper(): RequestStatusEntityMapper {
        return RequestStatusEntityMapper()
    }

    @Provides
    fun remote(serviceGenerator: ServiceGenerator): Remote {
        return Remote(serviceGenerator)
    }

    @Provides
    fun serviceGenerator(retrofit: Retrofit): ServiceGenerator {
        return ServiceGenerator(retrofit)
    }

    @Provides
    fun retrofit(moshiConverterFactory: MoshiConverterFactory,
                 rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
                 httpClient: OkHttpClient,
                 @Named("BaseUrl")baseUrl: String
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(httpClient)
            .build()
    }

    @Provides
    @Named("BaseUrl")
    fun baseUrl() = "***REMOVED***"

    @Provides
    fun moshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    fun moshi(kotlinJsonAdapterFactory: KotlinJsonAdapterFactory): Moshi {
        return Moshi.Builder()
            .add(kotlinJsonAdapterFactory)
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
    }

    @Provides
    fun kotlinJsonAdapterFactory(): KotlinJsonAdapterFactory {
        return KotlinJsonAdapterFactory()
    }

    @Provides
    fun rxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    fun httpClient(interceptor: Interceptor, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun interceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "EditableProfile")
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return logInterceptor
    }

}