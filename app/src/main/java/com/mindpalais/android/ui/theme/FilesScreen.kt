package com.mindpalais.android.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.mindpalais.android.model.Message
import com.mindpalais.android.model.MindMapViewModel
import com.mindpalais.android.model.UiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FilesScreen(mindMapViewModel: MindMapViewModel, uiViewModel: UiViewModel) {
    val scaffoldState = rememberScaffoldState()

    val messages = mutableListOf<Message>()
    val assetManager = LocalContext.current.resources.assets
    val expandBottomSheet by uiViewModel.expandBottomSheet.collectAsState()

    LaunchedEffect(expandBottomSheet) {
        if (expandBottomSheet) {
        } else {
            mindMapViewModel.clearChatMessages()
        }
    }


    val onSendMessage: (message: String) -> Unit = { message ->
        messages.add(Message(role = "user", content = message))
        mindMapViewModel.setChatMessages(Message(role = "user", content = message))
        val updatedMessages = mutableListOf<Message>()
        updatedMessages.add(messages.get(0))
        updatedMessages.addAll(if (messages.size > 3) messages.takeLast(3) else messages)
        mindMapViewModel.askAssistant(updatedMessages)
    }

    val onItemClick: (String) -> Unit = {

        messages.clear()
        messages.add(
            Message(
                role = "system",
                content = "You are a helpful assistant. Here is a file : "+ it
            )
        )

        mindMapViewModel.askAssistant(messages)

        uiViewModel.setBottomSheetContent {
            getNoteBottomSheetContent( mindMapViewModel, onSendMessage)
            uiViewModel.setExpandBottomSheet(true)
        }

    }

//    ReadFilesFromExternalStorage(context = LocalContext.current)

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        topBar = {
            MindpalaisTopAppBar({ getFilesTopBar() })
        },
        content = {
            textExtractor(mindMapViewModel, onItemClick)
        }
    )
}

@Composable
fun getFilesTopBar() {
    Text(
        text = "Files", fontFamily = interFontFamily, color = Color.White,
        fontWeight = FontWeight.Bold, fontSize = 18.sp
    )
}

@Composable
fun textExtractor(mindMapViewModel: MindMapViewModel, OnItemClick: (message: String) -> Unit) {

    val extractedText = remember {
        mutableStateOf("")
    }

    Button(
        modifier = Modifier
            .fillMaxWidth()

            .padding(20.dp),

        onClick = {

            extractData(extractedText)
            OnItemClick( Html.fromHtml(extractedText.value).toString() )

        }) {

        Text(modifier = Modifier.padding(6.dp), text = "Sample pdf")

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .fillMaxSize()
            .padding(6.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        Text(text = extractedText.value, color = Color.Black, fontSize = 12.sp)
//
//        Spacer(modifier = Modifier.height(10.dp))
//

    }

}



private fun extractData(extractedString: MutableState<String>) {

    try {
        var extractedText = ""

        val pdfReader: PdfReader = PdfReader("res/raw/sample.pdf")

        val n = pdfReader.numberOfPages

        for (i in 0 until n) {
            extractedText =
                """
                 $extractedText${
                    PdfTextExtractor.getTextFromPage(pdfReader, i + 1).trim { it <= ' ' }
                }
                 
                 """.trimIndent()
        }

        extractedString.value = extractedText
        pdfReader.close()

    }
    catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun ReadFilesFromExternalStorage(context: Context) {
    val files = remember { mutableStateListOf<File>() }

    LaunchedEffect(Unit) {
        val externalStorageDirectory = context.getExternalFilesDir(null)
        if (externalStorageDirectory != null) {
            withContext(Dispatchers.IO) {
                readFilesRecursively(externalStorageDirectory, files)
            }
        }
    }

    // Display the list of files
    Column {
        for (file in files) {
            Text(text = file.name)
        }
    }
}

private fun readFilesRecursively(directory: File, fileList: MutableList<File>) {
    val files = directory.listFiles()
    if (files != null) {
        for (file in files) {
            if (file.isDirectory) {
                readFilesRecursively(file, fileList)
            } else {
                fileList.add(file)
            }
        }
    }
}



