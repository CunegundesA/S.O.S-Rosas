package com.example.sosrosas.Api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.RuntimeException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.stream.DoubleStream.builder
import javax.net.ssl.*

class ApiClient {

    private val BASE_URL = "https://newsapi.org/v2/"
    private var retrofit: Retrofit? = null

    fun getApiClient(): Retrofit? {

        if (retrofit == null) {
            retrofit =
                Retrofit.Builder().baseUrl(BASE_URL).client(getUnsafeOkHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create()).build()
        }

        return retrofit
    }

    fun getUnsafeOkHttpClient(): OkHttpClient.Builder{
        try {

                val X509trustManager = object : X509TrustManager {
                    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                    }

                    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        val arrayCertificate = arrayOf<X509Certificate>()
                        return arrayCertificate
                    }

                }
            val trustAllCerts = arrayOf<TrustManager>(X509trustManager)

            val SSLContext = SSLContext.getInstance("SSL")
            SSLContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory = SSLContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(p0: String?, p1: SSLSession?): Boolean {
                    return true
                }
            })
            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}