package com.example.weather.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.weather.R
import com.example.weather.ui.theme.ubuntuBold

@Composable
fun DialogSearch(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.browni)
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // header in the same style as your weather cards
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp),
                ) {
                    Text(
                        text = "SEARCH CITY",
                        fontSize = 12.sp,
                        fontFamily = ubuntuBold,
                        color = Color.Gray,
                    )
                }

                TextField(
                    value = text,
                    onValueChange = { newText -> text = newText },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = ubuntuBold,
                        fontSize = 16.sp,
                        color = Color.White,
                    ),
//                    placeholder = {
//                        Text(
//                            text = "Zolochiv, Lviv, Kyiv...",
//                            fontFamily = ubuntuBold,
//                            color = Color.Gray,
//                        )
//                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { onConfirm(text) }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        selectionColors = TextSelectionColors(
                            handleColor = Color.White,
                            backgroundColor = Color(0x44FFFFFF),
                        ),
                        focusedContainerColor = Color(0x33FFFFFF),
                        unfocusedContainerColor = Color(0x1AFFFFFF),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                    ),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel", fontFamily = ubuntuBold, color = Color.LightGray)
                    }
                    TextButton(onClick = { onConfirm(text) }) {
                        Text(text = "Search", fontFamily = ubuntuBold, color = Color.White)
                    }
                }
            }
        }
    }
}