@file:OptIn(ExperimentalFoundationApi::class)

package com.ahuaman.authsupabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahuaman.authsupabase.ui.supabase
import com.ahuaman.authsupabase.ui.theme.AuthSupabaseTheme
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthSupabaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NoteList(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Serializable
data class Note(
    val id:Int,
    val body:String
)

@Composable
fun NoteList(modifier: Modifier = Modifier) {

    val notes = remember { mutableStateListOf<Note>() }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            val results = supabase.from("notes").select().decodeList<Note>()
            notes.addAll(results)
        }
    }

    Column(modifier) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(notes){ note ->
                ListItem(
                    headlineContent = {
                        Text(text = note.body)
                    }, modifier = Modifier.animateItemPlacement()
                )
            }
        }

        var newNote by remember {mutableStateOf("")}
        val composableScope = rememberCoroutineScope()
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            OutlinedTextField(value = newNote, onValueChange = {
                newNote = it
            }, modifier = Modifier.weight(1f))

            Button(onClick = {
                composableScope.launch(Dispatchers.IO) {
                    val note = supabase.from("notes").insert(mapOf("body" to newNote)){
                        select()
                        single()
                    }.decodeAs<Note>()
                    notes.add(note)
                    newNote = ""
                }
            }) {
                Text(text = "Add Note")
            }
        }
    }


}