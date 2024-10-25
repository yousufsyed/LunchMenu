package com.gusto.lunchmenu.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gusto.lunchmenu.LunchMenuViewModel
import com.gusto.lunchmenu.LunchMenuViewModelFactory
import com.gusto.lunchmenu.R
import com.gusto.lunchmenu.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: LunchMenuViewModelFactory

    private val viewModel: LunchMenuViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * You are welcome make use of Fragments or Compose for your UI
         * The decision made will not affect the assessment
         * Please utilize whatever you are most comfortable with
         * Below are two functions that can be used to get started
         * Uncomment one and remove the other
         */
        setupComposeUI()
    }

    private fun setupComposeUI() {
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    topBar = { Appbar() },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                ) { innerPadding ->
                    MainActivityPrompt(
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Appbar() {
        TopAppBar(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary),
            title = {
                Text(stringResource(id = R.string.app_name))
            },
            navigationIcon = {
                IconButton(
                    onClick = { /* todo: navigate back */ },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home"
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { /* todo: navigate prev */ },
                    enabled = false
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous"
                    )
                }
                IconButton(
                    onClick = {
                        /* todo: navigate next */
                        Toast.makeText(
                            applicationContext,
                            "Not Implemented yet",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    enabled = false
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next"
                    )
                }
            },
        )
    }
}