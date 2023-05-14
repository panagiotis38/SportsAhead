package com.example.sportsahead.ui.home

import com.example.sportsahead.ui.base.BaseActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.sportsahead.R
import com.example.sportsahead.databinding.ActivityHomeBinding
import com.example.sportsahead.ui.dashboard.DashboardFragment
import com.example.sportsahead.ui.theme.SportsAheadTheme

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
   // private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // initViewModel()

        installSplashScreen()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        setContent {
//            SportsAheadTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Column(Modifier.fillMaxSize()) {
//                        Greeting("Android")
//                        Spacer(modifier = Modifier.height(20.dp))
//                        Button(onClick = {  viewModel.testDataSource() }) {
//                            Greeting("Test datasource")
//                        }
//                    }
//
//                }
//            }
//        }
        loadHomeFragment()
    }

//    private fun initViewModel() {
//        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
//    }


    private fun loadHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, DashboardFragment.newInstance())
            .commitAllowingStateLoss()
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SportsAheadTheme {
        Greeting("Android")
    }
}