package me.reim.androidtemplate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.reim.androidtemplate.domain.QiitaArticleRepository
import me.reim.androidtemplate.domain.QiitaUserId
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CoroutineScope {

    @Inject
    lateinit var qiitaArticleRepository: QiitaArticleRepository

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var searchJob: Job? = null

    private fun search(qiitaUserId: QiitaUserId) {
        println("search !!")
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            qiitaArticleRepository.getArticleStream(qiitaUserId).collectLatest {
//                (supportFragmentManager.fragments.first() as MainFragment).submitData(it)
                //                adapter.submitData(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, MainFragment.newInstance())
//                .commitNow()
        }

        search(QiitaUserId("rei-m"))

//        launch {
//
//        }

//        launch {
//            qiitaArticleRepository.tryUpdateRecentArticlesCache()
//        }
//
//        qiitaArticleRepository.articles.asLiveData().observe(this) {
//            println(it.size)
//        }


    }
}
