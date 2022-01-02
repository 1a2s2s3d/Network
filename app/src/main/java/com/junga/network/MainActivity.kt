package com.junga.network

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.junga.network.databinding.ActivityMainBinding
import com.junga.network.domain.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //화면
        //리사이클러뷰 adapter
        // 레트로핏 인터페이스 : 주소를 호출하기 위한
        with(binding){
            // 1. 어댑터 생성 및 리사이클러뷰 연결
            val customAdapter = CustomAdapter()
            recyclerView.adapter = customAdapter
            recyclerView.layoutManager = LinearLayoutManager(baseContext)

            // 2. 레트로핏 생성
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // 3. 아래에 선언해둔 인터페이스를 실제 사용 가능한 코드로 변환
            val githubService = retrofit.create(GithubService::class.java)

            // 4. 버튼 클릭시 데이터를 가져와서 어댑터에 담아준다
            buttonRequest.setOnClickListener {
                githubService.users().enqueue(object: Callback<Repository>{
                    override fun onResponse(call: Call<Repository>, response: Response<Repository> ) {
                        response.body()?.let{ result ->
                            customAdapter.userList = result
                            customAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<Repository>, t: Throwable) {
                        Log.e("메인액티비티", "${t.localizedMessage}")
                    }
                })
            }


        }
    }
}

interface GithubService{

    @GET("users/Kotlin/repos")
    fun users() : Call<Repository>

}