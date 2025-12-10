package com.rkt.penote.presentation.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rkt.penote.domain.entity.NoteType
import com.rkt.penote.presentation.add_edit_note.components.TransparentHintTextField
import com.rkt.penote.presentation.ui.theme.NoteColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Screen for adding a new note or editing an existing one.
 *
 * @param navController Controller for navigation.
 * @param noteColor The initial color of the note (passed via navigation arguments).
 * @param viewModel ViewModel for managing UI state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.state.value.title
    val contentState = viewModel.state.value.content
    val state = viewModel.state.value

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.state.value.noteColor)
        )
    }
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = noteBackgroundAnimatable.value
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NoteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.state.value.noteColor == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = color,
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { viewModel.onEvent(AddEditNoteEvent.ChangeNoteType(NoteType.NORMAL)) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.noteType == NoteType.NORMAL) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surface,
                        contentColor = if (state.noteType == NoteType.NORMAL) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface 
                    )
                ) {
                    Text("Normal")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                     onClick = { viewModel.onEvent(AddEditNoteEvent.ChangeNoteType(NoteType.CHECKBOX)) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.noteType == NoteType.CHECKBOX) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surface,
                        contentColor = if (state.noteType == NoteType.CHECKBOX) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Checkbox")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState,
                hint = state.titleHint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = state.isTitleHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            if(state.noteType == NoteType.NORMAL) {
                TransparentHintTextField(
                    text = contentState,
                    hint = state.contentHint,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                    },
                    isHintVisible = state.isContentHintVisible,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxHeight()
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(state.checkList.size) { index ->
                        Row(
                             modifier = Modifier.fillMaxWidth(),
                             verticalAlignment = Alignment.CenterVertically
                        ) {
                             Checkbox(
                                 checked = state.checkList[index].isChecked,
                                 onCheckedChange = {
                                     viewModel.onEvent(AddEditNoteEvent.ToggleCheckListItem(index))
                                 }
                             )
                             TransparentHintTextField(
                                 text = state.checkList[index].text,
                                 hint = "Item...",
                                 isHintVisible = state.checkList[index].text.isEmpty(),
                                 onValueChange = {
                                     viewModel.onEvent(AddEditNoteEvent.EnteredCheckListItem(index, it))
                                 },
                                 onFocusChange = {},
                                 textStyle = MaterialTheme.typography.bodyLarge,
                                 modifier = Modifier.weight(1f)
                             )
                            IconButton(onClick = { viewModel.onEvent(AddEditNoteEvent.RemoveCheckListItem(index)) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete item")
                            }
                        }
                    }
                    item {
                         Button(onClick = { viewModel.onEvent(AddEditNoteEvent.AddCheckListItem("")) }) {
                             Icon(imageVector = Icons.Default.Add, contentDescription = null)
                             Text("Add Item")
                         }
                    }
                }
            }
        }
    }
}
