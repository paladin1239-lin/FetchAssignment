package com.homework.fetchassignment.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.homework.fetchassignment.R
import com.homework.fetchassignment.presentation.ui.theme.FetchAssignmentTheme
import com.homework.fetchassignment.presentation.utils.ItemsPresentationScreenUtils

@Composable
fun ItemsPresentationScreen(modifier: Modifier = Modifier){
    val viewModel: MainActivityViewModel = hiltViewModel() // DI
    val items by viewModel.itemsData.collectAsState()
    items?.let { items ->
        LazyColumn(modifier = modifier) {
            item {
                Row(Modifier.background(Color.Gray)){
                    TableCell(text = stringResource(R.string.list_id), weight = ItemsPresentationScreenUtils.COLUMN_WEIGHT_1)
                    TableCell(text = stringResource(R.string.name), weight = ItemsPresentationScreenUtils.COLUMN_WEIGHT_2)
                    TableCell(text = stringResource(R.string.id), weight = ItemsPresentationScreenUtils.COLUMN_WEIGHT_3)
                }
            }
            items(items) { item ->
                Row(Modifier.background(Color.Cyan)) {
                    TableCell(text = item.listId.toString(), weight = ItemsPresentationScreenUtils.COLUMN_WEIGHT_1)
                    TableCell(text = item.name, weight = ItemsPresentationScreenUtils.COLUMN_WEIGHT_2)
                    TableCell(text = item.id.toString(), weight = ItemsPresentationScreenUtils.COLUMN_WEIGHT_3)
                }
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
){
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp),
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.ExtraBold
    )
}


@Preview(showBackground = true)
@Composable
fun ItemsPresentationScreen() {
    FetchAssignmentTheme {
        ItemsPresentationScreen()
    }
}