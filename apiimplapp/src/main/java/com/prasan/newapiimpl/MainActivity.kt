package com.prasan.newapiimpl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.prasan.newsapi_lib.NewsAPIHandler
import com.prasan.newsapi_lib.showToast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch { //Bad idea to use GlobalScope, only shown for demonstration purpose
            NewsAPIHandler.with(BuildConfig.API_KEY).getNewsSources(
                {
                    showToast(it.hashCode().toString())
                },
                {
                    showToast(it.message!!)
                }
            )
        }
    }
}