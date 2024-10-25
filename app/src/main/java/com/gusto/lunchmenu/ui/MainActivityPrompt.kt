package com.gusto.lunchmenu.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gusto.lunchmenu.LunchMenuState
import com.gusto.lunchmenu.LunchMenuViewModel
import com.gusto.lunchmenu.R
import com.gusto.lunchmenu.data.ErrorMessage
import com.gusto.lunchmenu.data.LunchItem
import com.gusto.lunchmenu.data.TitleMessage
import com.gusto.lunchmenu.ui.theme.MyApplicationTheme

@Composable
fun MainActivityPrompt(
    modifier: Modifier,
    viewModel: LunchMenuViewModel
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    LaunchedEffect(Unit) {
        viewModel.getLunchMenu()
    }

    val lunchMenuModel = viewModel.lunchMenu.collectAsStateWithLifecycle()

    val savableState = listSaver<LunchMenuState, Any>(
        save = {
            when (it) {
                LunchMenuState.Loading -> listOf("Loading")
                is LunchMenuState.Error -> listOf("Error", it.error.resId)
                is LunchMenuState.Success -> listOf("Success", it.items)
            }
        },
        restore = {
            when (it[0]) {
                "Error" -> LunchMenuState.Error(ErrorMessage(it[1] as Int))
                "Success" -> LunchMenuState.Success(it[1] as? List<LunchItem> ?: emptyList())
                else -> LunchMenuState.Loading
            }
        }
    )
    val state by rememberSaveable(
        stateSaver = savableState,
        init = { lunchMenuModel as MutableState<LunchMenuState> })

    when (val result = state) {
        LunchMenuState.Loading -> LunchMenuLoading()
        is LunchMenuState.Success -> ShowLunchMenu(result.items)
        is LunchMenuState.Error -> LunchMenuError(result.error, viewModel)
    }
}

@Composable
fun LunchMenuLoading() {
    Text(
        text = stringResource(id = R.string.lunch_menu_loading),
        fontSize = 16.sp,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun LunchMenuError(
    message: ErrorMessage,
    viewModel: LunchMenuViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = message.resId),
            fontSize = 14.sp,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.error
            )
        )
        Button(
            modifier = Modifier.padding(top = 16.dp),
            enabled = remember { viewModel.canRetry.value },
            onClick = {
                viewModel.getLunchMenu()
            }
        ) {
            Text(text = stringResource(id = R.string.lunch_menu_retry))
        }
    }
}

@Composable
fun ShowLunchMenu(list: List<LunchItem>) {
    LazyColumn {
        items(
            items = list,
            itemContent = { lunchItem ->
                when (lunchItem) {
                    is LunchItem.Header -> LunchHeader(lunchItem)
                    is LunchItem.Menu -> LunchMenu(lunchItem)
                }
            }
        )
    }
}

@Composable
fun LunchHeader(item: LunchItem.Header) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(16.dp)
            ,
        text = stringResource(id = R.string.lunch_week, item.weekNumber),
        color = MaterialTheme.colorScheme.onTertiary,
        fontSize = 18.sp,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
fun LunchMenu(item: LunchItem.Menu) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp,  top = 8.dp, bottom = 16.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable { expanded = !expanded }
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 8.dp)

    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = item.titleMessage.toTitle(),
            fontSize = 16.sp
        )

        AnimatedVisibility(expanded) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                    .padding(top = 4.dp, start = 8.dp, bottom = 4.dp),
                text = item.description,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun TitleMessage.toTitle() = stringResource(id = resId, name)


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        LunchMenu(
            item = LunchItem.Menu(
                TitleMessage(R.string.lunch_weekday_monday, "Title"), "Description"
            )
        )
    }
}