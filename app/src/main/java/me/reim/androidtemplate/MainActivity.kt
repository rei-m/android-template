package me.reim.androidtemplate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import dagger.hilt.android.AndroidEntryPoint
import me.reim.androidtemplate.domain.QiitaArticleRepository
import me.reim.androidtemplate.ui.main.MainFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var qiitaArticleRepository: QiitaArticleRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        qiitaArticleRepository.articles.asLiveData().observe(this) {

        }
    }
}
