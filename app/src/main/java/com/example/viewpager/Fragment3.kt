package com.example.viewpager

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Math.round

class Fragment3 : Fragment() {

    lateinit var runnable: Runnable
    private var handler = Handler()
    private var mediaplayer: MediaPlayer? = null

    /*/7.5 GPS
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    val PERMISSIONS_REQUEST_CODE = 100

    val locatioNManager: LocationManager? =
        requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager


    private fun getLocation(){
        var userLocation: Location = getLatLng()

        if(userLocation != null){
            var latitude = userLocation.latitude
            var longitude = userLocation.longitude
            Log.d("CheckCurrentLocation", "현재 내 위치 값: ${latitude}, ${longitude}")

            var mGeoCoder =  Geocoder(getActivity()?.getApplicationContext(), Locale.KOREAN)
            var mResultList: List<Address>? = null
            try{
                mResultList = mGeoCoder.getFromLocation(
                    latitude!!, longitude!!, 1
                )
            }catch(e: IOException){
                e.printStackTrace()
            }
            if(mResultList != null){
                Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))
            }
        }
    }

    private fun getLatLng(): Location {
        var currentLatLng: Location? = null
        var hasFineLocationPermission = ContextCompat.checkSelfPermission(context as Activity,
            Manifest.permission.ACCESS_FINE_LOCATION)
        var hasCoarseLocationPermission = ContextCompat.checkSelfPermission(context as Activity,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            val locatioNProvider = LocationManager.GPS_PROVIDER
            currentLatLng = locatioNManager?.getLastKnownLocation(locatioNProvider)
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, REQUIRED_PERMISSIONS[0])){
                ActivityCompat.requestPermissions(context as Activity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }else{
                ActivityCompat.requestPermissions(context as Activity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }
            currentLatLng = getLatLng()
        }
        return currentLatLng!!
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size){
            var check_result = true
            for(result in grantResults){
                if(result != PackageManager.PERMISSION_GRANTED){
                    check_result = false;
                    break;
                }
            }
            if(check_result){
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, REQUIRED_PERMISSIONS[1])){
                    activity?.finish()
                }
            }
        }
    }*/
    ////7.4 날씨 API
    companion object{
        var BaseUrl = "https://api.openweathermap.org/"
        var AppId = "7e16a9a491e717202ac972d8e4cccd3b"
        var lat = "35.1438"
        var lon = "127.33"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //MediaPlayer object

        val playbutton = view.findViewById<ImageButton>(R.id.playBtn)
        val seekbar = view.findViewById<SeekBar>(R.id.seekbar)

        //7.4 날씨 API
        val tem = view.findViewById<TextView>(R.id.tem)
        val musicCover = view.findViewById<ImageView>(R.id.MusicCover)
        val weatherIcon = view.findViewById<ImageView>(R.id.weatherIcon)
        val musicTitle = view.findViewById<TextView>(R.id.MusicTitle)

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        var mediaplayer = MediaPlayer.create(requireContext(), R.raw.peaches)
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("MainActivity", "result :" + t.message)
            }
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if(response.code() == 200){
                    val weatherResponse = response.body()
                    Log.d("MainActivity", "result: " + weatherResponse.toString())
                    var cTemp =  round(weatherResponse!!.main!!.temp - 273.15)  //켈빈을 섭씨로 변환
                    var minTemp = weatherResponse!!.main!!.temp_min - 273.15
                    var maxTemp = weatherResponse!!.main!!.temp_max - 273.15
                    var weatherResult = weatherResponse!!.weather!!.get(0).main
                    val stringBuilder =
                        "날씨: " + weatherResult + "\n"
                    tem.text = "${cTemp}°C"


                    if(weatherResult == "Clouds"){
                        mediaplayer = MediaPlayer.create(requireContext(), R.raw.peaches)
                        weatherIcon.setImageResource(R.drawable.cloud)
                        musicCover.setImageResource(R.drawable.cover)
                        musicTitle.text = "Peaches - Justin Bieber"

                    }
                    else if(weatherResult == "Rain"){
                        mediaplayer = MediaPlayer.create(requireContext(), R.raw.umbrella)
                        weatherIcon.setImageResource(R.drawable.rainy)
                        musicCover.setImageResource(R.drawable.umbrella_cover)
                        musicTitle.text = "Paris In the Rain - Lauv"
                    }
                    else if(weatherResult == "Snow"){
                        mediaplayer = MediaPlayer.create(requireContext(), R.raw.snowman)
                        weatherIcon.setImageResource(R.drawable.snow)
                        musicCover.setImageResource(R.drawable.snowman_cover)
                        musicTitle.text = "Snowman - Sia"
                    }
                    else if(weatherResult == "Wind"){
                        mediaplayer = MediaPlayer.create(requireContext(), R.raw.thunder)
                        weatherIcon.setImageResource(R.drawable.wind)
                        musicCover.setImageResource(R.drawable.thunder_cover)
                        musicTitle.text = "Thunder - Jessie J"
                    }
                    else{
                        weatherIcon.setImageResource(R.drawable.ic_launcher_foreground)
                    }

                }
            }

        })

        seekbar.progress = 0
        seekbar.max = mediaplayer!!.duration

        playbutton.setOnClickListener {
            if (!mediaplayer!!.isPlaying) {
                mediaplayer!!.start()
                playbutton.setImageResource(R.drawable.ic_baseline_pause_24)
            }else {
                mediaplayer!!.pause()
                playbutton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }


        //seekbar - change the position
        seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, pos: Int, changed: Boolean) {
                if (changed) {
                    mediaplayer!!.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        runnable = Runnable {
            seekbar.progress = mediaplayer!!.currentPosition
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
        mediaplayer!!.setOnCompletionListener {
            playbutton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            seekbar.progress = 0
        }

        fun onResponse(
            call: Call<WeatherResponse>,
            response: Response<WeatherResponse>
        ) {
            TODO("Not yet implemented")
        }

    }

    override fun onStop() {
        super.onStop()
        mediaplayer?.release()
        mediaplayer = null

    }
}

//7.4 날씨 API
interface WeatherService{

    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String) :
            Call<WeatherResponse>
}

class WeatherResponse(){
    @SerializedName("weather") var weather = ArrayList<Weather>()
    @SerializedName("main") var main: Main? = null
    @SerializedName("wind") var wind : Wind? = null
    @SerializedName("sys") var sys: Sys? = null
}

class Weather {
    @SerializedName("id") var id: Int = 0
    @SerializedName("main") var main : String? = null
    @SerializedName("description") var description: String? = null
    @SerializedName("icon") var icon : String? = null
}

class Main {
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
    @SerializedName("humidity")
    var humidity: Float = 0.toFloat()
    @SerializedName("pressure")
    var pressure: Float = 0.toFloat()
    @SerializedName("temp_min")
    var temp_min: Float = 0.toFloat()
    @SerializedName("temp_max")
    var temp_max: Float = 0.toFloat()

}

class Wind {
    @SerializedName("speed")
    var speed: Float = 0.toFloat()
    @SerializedName("deg")
    var deg: Float = 0.toFloat()
}

class Sys {
    @SerializedName("country")
    var country: String? = null
    @SerializedName("sunrise")
    var sunrise: Long = 0
    @SerializedName("sunset")
    var sunset: Long = 0
}