package com.jetpack.whatsappclone

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.*
import com.jetpack.whatsappclone.ui.theme.WhatsAppCloneTheme
import com.jetpack.whatsappclone.ui.theme.WhatsAppFloatIconColor
import com.jetpack.whatsappclone.ui.theme.WhatsAppThemeColor
import com.jetpack.whatsappclone.utils.Constants._tabCurrentStatus
import com.jetpack.whatsappclone.utils.Constants.tabCurrentStatus
import com.jetpack.whatsappclone.view.WhatsAppCalls
import com.jetpack.whatsappclone.view.WhatsAppChatList
import com.jetpack.whatsappclone.view.WhatsAppChats
import com.jetpack.whatsappclone.view.WhatsAppStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalPagerApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatsAppCloneTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "whats_app_main"
                ) {
                    composable("whats_app_main") { WhatsApp(navController) }
                    composable("whats_app_chat") { WhatsAppChatList() }
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun WhatsApp(navController: NavHostController) {
    val context = LocalContext.current
    val menuExpanded = remember { mutableStateOf(false) }
    val tabStatus = tabCurrentStatus.observeAsState()

    val topBar: @Composable () -> Unit = {
        TopAppBar(
           title = {
               Text(
                   text = "WhatsApp",
                   color = Color.White,
                   fontSize = 20.sp
               )
           } ,

            actions = {
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Clicked Search", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        menuExpanded.value = true
                    }
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    DropdownMenu(
                        modifier = Modifier
                            .width(200.dp)
                            .wrapContentSize(Alignment.TopStart),
                        expanded = menuExpanded.value,
                        onDismissRequest = {
                            menuExpanded.value = false
                        }
                    ) {
                        when(tabStatus.value) {
                            0 -> {
                                DropdownMenuItem(onClick = { /*Handle New group*/ }) {
                                    Text(text = "New group")
                                }
                                DropdownMenuItem(onClick = { /*Handle New broadcast*/ }) {
                                    Text(text = "New broadcast")
                                }
                                DropdownMenuItem(onClick = { /*Handle Linked devices*/ }) {
                                    Text(text = "Linked devices")
                                }
                                DropdownMenuItem(onClick = { /*Handle Starred messages*/ }) {
                                    Text(text = "Starred messages")
                                }
                                DropdownMenuItem(onClick = { /*Handle Payments*/ }) {
                                    Text(text = "Payments")
                                }
                                DropdownMenuItem(onClick = { /*Handle Settings*/ }) {
                                    Text(text = "Settings")
                                }
                            }
                            1 -> {
                                DropdownMenuItem(onClick = { /*Handle Status privacy*/ }) {
                                    Text(text = "Status privacy")
                                }
                                DropdownMenuItem(onClick = { /*Handle Settings*/ }) {
                                    Text(text = "Settings")
                                }
                            }
                            2 -> {
                                DropdownMenuItem(onClick = { /*Handle Clear call log*/ }) {
                                    Text(text = "Clear call log")
                                }
                                DropdownMenuItem(onClick = { /*Handle Settings*/ }) {
                                    Text(text = "Settings")
                                }
                            }
                        }
                    }
                }

            },
            backgroundColor = WhatsAppThemeColor,
            elevation = AppBarDefaults.TopAppBarElevation
        )
    }

    Scaffold(
        topBar = {
            topBar()
        },
        content = {
            WhatsAppTab(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context, "Message Clicked", Toast.LENGTH_SHORT).show()
                },
                backgroundColor = WhatsAppFloatIconColor,
                elevation = FloatingActionButtonDefaults.elevation(),
                modifier = Modifier.padding(10.dp)
            ) {
                when(tabStatus.value) {
                    0 -> {
                        Icon(
                            painterResource(id = R.drawable.ic_chat),
                            contentDescription = "Message",
                            tint = Color.White,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    1 -> {
                        Icon(
                            painterResource(id = R.drawable.ic_camera),
                            contentDescription = "Camera",
                            tint = Color.White,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    2 -> {
                        Icon(
                            painterResource(id = R.drawable.ic_call),
                            contentDescription = "Add Call",
                            tint = Color.White,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    )
}

@ExperimentalPagerApi
@Composable
fun WhatsAppTab(navController: NavHostController) {
    val pagerState = rememberPagerState(pageCount = 3)
    Column {
        Tabs(pagerState)
        TabsContent(pagerState, navController)
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf("CHATS", "STATUS", "CALLS")
    val scope = rememberCoroutineScope()
    _tabCurrentStatus.value = pagerState.currentPage

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = WhatsAppThemeColor,
        contentColor = Color.Gray,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 3.dp,
                color = Color.White
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = Color.White
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, navController: NavHostController) {
    HorizontalPager(state = pagerState) { page ->
        when(page) {
            0 -> WhatsAppChats(navController)
            1 -> WhatsAppStatus()
            2 -> WhatsAppCalls()
        }
    }
}