package com.zaurh.polskismak.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.zaurh.polskismak.presentation.main_screen.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    text: String,
    onBack: () -> Unit,
    onSearch: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var query by rememberSaveable { mutableStateOf("") }

    BackHandler(enabled = query.isNotEmpty(), onBack = {
        query = ""
        onBack()
        focusManager.clearFocus()
    })

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(22.dp)
            .clip(RoundedCornerShape(40)),
        value = query,
        onValueChange = {
            query = it
            onSearch(it)
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.background,
            textColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.tertiary
            )
        },
        placeholder = { Text(text = text, color = Color.Gray) }
    )

}