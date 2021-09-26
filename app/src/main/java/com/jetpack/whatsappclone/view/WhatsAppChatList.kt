package com.jetpack.whatsappclone.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.whatsappclone.R
import com.jetpack.whatsappclone.model.SampleData
import com.jetpack.whatsappclone.repository.WhatsAppRepository
import com.jetpack.whatsappclone.ui.theme.WhatsAppChatBg
import com.jetpack.whatsappclone.ui.theme.WhatsAppOutgoingMsg
import com.jetpack.whatsappclone.ui.theme.WhatsAppThemeColor
import com.jetpack.whatsappclone.utils.OnError
import com.jetpack.whatsappclone.utils.OnSuccess
import com.jetpack.whatsappclone.viewmodel.WhatsAppChatViewModel
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WhatsAppChatList(
    viewModel: WhatsAppChatViewModel = viewModel(
        factory = WhatsAppViewModelFactory(WhatsAppRepository())
    )
) {
    val menuExpanded = remember { mutableStateOf(false) }
    //val viewModel: WhatsAppChatViewModel = viewModel()
    val getAllChat = viewModel.getSampleData.observeAsState(mutableListOf())
    val flag = viewModel.flag.observeAsState()

    val whatsAppChatList = viewModel.whatsAppStateFlow.asStateFlow().collectAsState().value

    val topBar: @Composable () -> Unit = {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back Arrow",
                        tint = Color.White
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_run_circle),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .height(60.dp)
                            .width(60.dp)
                            .padding(0.dp, 5.dp, 5.dp, 5.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "0123456789",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "online",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = {  }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_videocam_24),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {

                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_call_24),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {
                    menuExpanded.value = true
                }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "",
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier.wrapContentSize(Alignment.TopStart)
                ) {
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = {
                            menuExpanded.value = false
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .wrapContentSize(Alignment.TopStart)
                    ) {
                        DropdownMenuItem(onClick = {  }) {
                            Text(text = "Add to contacts")
                        }
                        DropdownMenuItem(onClick = {  }) {
                            Text(text = "Report")
                        }
                        DropdownMenuItem(onClick = {  }) {
                            Text(text = "Block")
                        }
                        DropdownMenuItem(onClick = {  }) {
                            Text(text = "Search")
                        }
                        DropdownMenuItem(onClick = {  }) {
                            Text(text = "Mute notifications")
                        }
                        DropdownMenuItem(onClick = {  }) {
                            Text(text = "Wallpaper")
                        }
                        DropdownMenuItem(onClick = {  }) {
                            Text(text = "More")
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhatsAppChatBg)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Today, ${flag.value}", textAlign = TextAlign.Center, fontSize = 12.sp)
                Spacer(modifier = Modifier.padding(5.dp))
                //CallChatList(getAllChat.value) it's static val

                when(whatsAppChatList) {
                    is OnError -> {
                        Text(text = "Fetching Data failure")
                    }

                    is OnSuccess -> {
                        val listOfChat = whatsAppChatList.querySnapshot?.toObjects(SampleData::class.java)
                        listOfChat?.let {
                            CallChatList(listOfChat)
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomDesign(viewModel)
        }
    )
}

@Composable
fun CallChatList(value: List<SampleData>) {
    val scaffoldState = rememberScaffoldState()
    Surface {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhatsAppChatBg)
            ) {
                CallChatItem(value)
            }
        }
    }
}

@Composable
fun CallChatItem(value: List<SampleData>) {
    LazyColumn {
        items(
            value.size
        ) { index ->
            ChatListItem(data = value[index], index = index)
        }
    }
}

@Composable
fun BottomDesign(viewModel: WhatsAppChatViewModel) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val scope = rememberCoroutineScope()
    val date = SimpleDateFormat("hh:mm a")
    val strDate: String = date.format(Date())

    Row(
        modifier = Modifier
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(0.85f)
                .wrapContentSize()
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(10.dp, 0.dp, 10.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_emoji),
                contentDescription = "Emoji",
                tint = Color.Gray,
                modifier = Modifier.weight(0.1f)
            )
            TextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                placeholder = {
                    Text(
                        text = "Message",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier
                    .weight(0.66f)
                    .wrapContentHeight(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Text,
                    autoCorrect = true,
                    imeAction = ImeAction.Done
                ),
                textStyle = TextStyle(color = Color.Black,
                    fontSize = 15.sp),
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_attach),
                contentDescription = "Attach",
                tint = Color.Gray,
                modifier = Modifier.weight(0.14f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Attach",
                tint = Color.Gray,
                modifier = Modifier.weight(0.1f)
            )
        }
        Row(
            modifier = Modifier.weight(0.15f),
            horizontalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(
                onClick = {
                          if (textState.value.text.isNotEmpty()) {
                              val sampleData = SampleData(
                                  "Name",
                                  textState.value.text,
                                  "Sample Url",
                                  strDate
                              )
                              viewModel.addChat(sampleData)
                          }
                },
                backgroundColor = WhatsAppThemeColor
            ) {
                Icon(
                    painter = painterResource(
                        if (textState.value.text.isEmpty()) {
                            R.drawable.ic_voice_record
                        } else {
                            R.drawable.ic_baseline_send_24
                        }
                    ),
                    contentDescription = "Text Icon",
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}


@Composable
fun ChatListItem(data: SampleData, index: Int) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (index % 2 == 0) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(WhatsAppOutgoingMsg)
                    .padding(5.dp)
            ) {
                Text(
                    text = data.desc,
                    color = Color.Black,
                    fontSize = 15.sp
                )
                Text(
                    text = data.createdDate,
                    color = Color.LightGray,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(5.dp)
                    .align(Alignment.End)
            ) {
                Text(
                    text = data.desc,
                    color = Color.Black,
                    fontSize = 15.sp
                )
                Text(
                    text = data.createdDate,
                    color = Color.LightGray,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

class WhatsAppViewModelFactory(private val whatsAppRepository: WhatsAppRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WhatsAppChatViewModel::class.java)) {
            return WhatsAppChatViewModel(whatsAppRepository) as T
        }
        throw IllegalStateException()
    }
}
























