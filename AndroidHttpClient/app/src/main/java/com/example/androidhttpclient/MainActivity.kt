package com.example.androidhttpclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.androidhttpclient.ui.theme.AndroidHttpClientTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidHttpClientTheme {

                val viewModel : MainViewModel by viewModels()

                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Posts")
                            },
                            actions = {
                                IconButton(
                                    onClick = {}
                                )
                                {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Arrow Up"
                                    )
                                }
                                IconButton(
                                    onClick = {}
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Arrow Down"
                                    )
                                }

                                IconButton(
                                    onClick = {
                                    viewModel.fetchPosts()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh"
                                    )
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                viewModel.isDialogVisibleChange(true)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add"
                            )
                        }
                    }
                    ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        if(viewModel.isLoading.value){
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                        if(viewModel.errorMessage.value != null){
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp), contentAlignment = Alignment.Center
                            ){
                                Text("Erreur: ${ viewModel.errorMessage.value!! }")
                            }
                        }else{
                            LazyColumn {
                                items(viewModel.posts){ post ->
                                    ListItem(
                                        headlineContent = { Text(post.title ?: "Titre indisponible") },
                                        supportingContent = { Text(post.body ?: "Aucun Contenu") },
                                        trailingContent = {
                                            Row {
                                                IconButton(
                                                    onClick = {
                                                        viewModel.isDialogVisibleChange(true)
                                                        viewModel.onPostActiveChange(post)
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Modifier"
                                                    )
                                                }
                                                IconButton(
                                                    onClick = {
                                                        viewModel.deletePost(post.id!!)
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Supprimer"
                                                    )
                                                }
                                            }
                                        }
                                    )

                                }
                            }
                        }
                    }

                    if(viewModel.isDialogVisible.value){
                        Dialog(
                            onDismissRequest = {
                                viewModel.isDialogVisibleChange(false)
                                viewModel.onPostActiveChange(Post())
                        }) {
                            Card( modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()) {
                                    Text(if(viewModel.activePost.value.id == null) "Créer post" else "Modifier post")
                                    Spacer(modifier = Modifier.height(12.dp))
                                    TextField(
                                        value = viewModel.activePost.value.title ?: "",
                                        onValueChange = {
                                            viewModel.onTitleValueChange(it)
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        placeholder = {
                                            Text("Titre du post")
                                        }
                                    )
                                    TextField(
                                        value = viewModel.activePost.value.body ?: "",
                                        onValueChange = {
                                            viewModel.onBodyValueChange(it)
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        placeholder = {
                                            Text("Corps du post")
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                        TextButton(
                                            onClick = {
                                                viewModel.isDialogVisibleChange(false)
                                                viewModel.onPostActiveChange(Post())
                                            }
                                        ) {
                                            Text("Annuler")
                                        }
                                        TextButton(
                                            onClick = {
                                                viewModel.createOrUpdatePost()
                                                viewModel.isDialogVisibleChange(false)
                                            }
                                        ) {
                                            Text(if(viewModel.activePost.value.id == null) "Créer" else "Modifier")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

