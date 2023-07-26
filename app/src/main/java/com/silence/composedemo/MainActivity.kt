package com.silence.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.silence.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imgList = listOf(
            "https://picsum.photos/seed/70114/300/600",
            "https://picsum.photos/seed/92616/300/600",
            "https://picsum.photos/seed/69064/300/600",
            "https://picsum.photos/seed/58003/300/600",
            "https://picsum.photos/seed/44183/300/600",
            "https://picsum.photos/seed/2229/300/600",
//            randomSampleImageUrl(width = 300, height = 600),
        )
        setContent {
            ComposeDemoTheme { MainPage(imgList) }
        }

    }

    private var dis = 1

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun MainPage(imgList: List<String>) {
        val pageState = rememberPagerState()
        val nowPageIndex = pageState.currentPage
        val scope = rememberCoroutineScope()
        LaunchedEffect(pageState.settledPage) {
            delay(1000)
            if (pageState.currentPage == 0) {
                dis = 1
            } else if (pageState.currentPage == imgList.size - 1) {
                dis = -1
            }
            pageState.animateScrollToPage(pageState.currentPage + dis)
        }
        ComposeDemoTheme {
            Box {
                HorizontalPager(
                    pageCount = imgList.size,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp, bottom = 100.dp),
                    contentPadding = PaddingValues(horizontal = 50.dp),
                    state = pageState
                ) { index ->
                    val imgScale by animateFloatAsState(targetValue = if (nowPageIndex == index) 1f else 0.8f)
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(imgList[index])
                            .scale(Scale.FILL)
                            .crossfade(true)
                            .build(),
                        contentDescription = "img${index}",
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(imgScale)
                            .clip(RoundedCornerShape(15.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    imgList.indices.forEach { radioIndex ->
                        RadioButton(selected = nowPageIndex == radioIndex, onClick = {
                            scope.launch {
                                pageState.animateScrollToPage(radioIndex)
                            }
                        })
                    }
                }
            }

        }
    }

}
