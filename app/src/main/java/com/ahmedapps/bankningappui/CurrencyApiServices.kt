import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("latest.json")
    suspend fun getLatestRates(
        @Query("app_id") appId: String
    ): CurrencyResponse
}

data class CurrencyResponse(
    val rates: Map<String, Float>
)

suspend fun fetchCurrencyRates(apiKey: String): Map<String, Float> {
    val httpClient = OkHttpClient.Builder().build()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://openexchangerates.org/api/")
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service = retrofit.create(CurrencyApiService::class.java)

    val response = service.getLatestRates(apiKey)
    return response.rates
}

suspend fun main() {
    val apiKey = "eb08453b2fdd44c5a4fbc1781ee0afae"
    val rates = fetchCurrencyRates(apiKey)
    println(rates)
}
