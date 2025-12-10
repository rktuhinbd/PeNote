package com.rkt.penote.presentation.util

/**
 * Sealed class representing the screens in the application.
 *
 * @param route The navigation route string.
 */
sealed class Screen(val route: String) {
    object NotesScreen : Screen("notes_screen")
    object AddEditNoteScreen : Screen("add_edit_note_screen")
}
