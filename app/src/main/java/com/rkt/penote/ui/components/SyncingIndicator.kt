package com.rkt.penote.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SyncingIndicator() {
    var isVisible by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            isVisible = !isVisible
        }
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Sync,
            contentDescription = "Syncing",
            modifier = Modifier
                .size(16.dp)
                .let { if (isVisible) it else it.alpha(0.3f) }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Syncing...",
            style = MaterialTheme.typography.bodySmall
        )
    }
}