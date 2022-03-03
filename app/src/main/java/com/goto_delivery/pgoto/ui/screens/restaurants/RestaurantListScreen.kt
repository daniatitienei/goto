package com.goto_delivery.pgoto.ui.screens.restaurants

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipColors
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.data.repository.RestaurantRepositoryImpl
import com.goto_delivery.pgoto.domain.model.Restaurant
import com.goto_delivery.pgoto.domain.repository.RestaurantRepository
import com.goto_delivery.pgoto.domain.use_case.restaurant.GetRestaurantById
import com.goto_delivery.pgoto.domain.use_case.restaurant.GetRestaurants
import com.goto_delivery.pgoto.domain.use_case.restaurant.RestaurantUseCases
import com.goto_delivery.pgoto.ui.theme.GotoTheme
import com.goto_delivery.pgoto.ui.theme.Lime80
import com.goto_delivery.pgoto.ui.utils.components.SearchBar
import com.goto_delivery.pgoto.ui.utils.rememberWindowInfo

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun RestaurantListScreen(
    viewModel: RestaurantListViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    val windowInfo = rememberWindowInfo()

    var searchBarValue by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    SearchBar(
                        value = searchBarValue,
                        onValueChange = { searchBarValue = it },
                        placeholder = stringResource(
                            id = R.string.search_restaurants
                        ),
                        onSearch = { /*TODO*/ }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_account_circle),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 5.dp)
        ) {
            LazyRow(
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                ),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(7) {
                    var isSelected by remember {
                        mutableStateOf(false)
                    }
                    FilterChip(
                        text = "PIZZA",
                        isSelected = isSelected,
                        onClick = {
                            isSelected = !isSelected
                        }
                    )
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 15.dp
                ),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(if (!state.isLoading) state.restaurants else emptyList()) { restaurant ->
                    RestaurantCard(
                        windowHeightDp = windowInfo.screenHeightDp,
                        restaurant = restaurant,
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(500)
                        ),
                        onClick = { /*TODO*/ }
                    )
                }
            }
        }

    }
}

@ExperimentalMaterialApi
@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.primaryContainer
    )

    Chip(
        onClick = onClick,
        colors = ChipDefaults.chipColors(
            backgroundColor = backgroundColor
        )
    ) {
        Text(
            text = text.toUpperCase(locale = Locale.current),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun FilterChipPreview() {
    GotoTheme {
        FilterChip(onClick = {}, isSelected = true, text = "PIZZA")
    }
}

@ExperimentalMaterial3Api
@Composable
private fun RestaurantCard(
    windowHeightDp: Dp,
    restaurant: Restaurant,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height((windowHeightDp.value / 3.5).dp)
            .clickable { onClick() }
            .then(modifier),
        shape = RoundedCornerShape(15.dp),
        shadowElevation = 5.dp,
        tonalElevation = 5.dp
    ) {
        /* Restaurant image */
        Image(
            painter = rememberImagePainter(
                data = restaurant.imageUrl,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.Bottom)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.4f)
                            )
                        )
                    )
                    .padding(15.dp)
            ) {
                /* Restaurant name */
                Text(
                    text = restaurant.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(5.dp))

                /* Estimated delivery time */
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = restaurant.estimatedDeliveryTime,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        /* Restaurant rating */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.TopEnd)
        ) {
            ElevatedCard(
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(15.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 5.dp
                )
            ) {
                Text(
                    text = restaurant.rating.toString(),
                    modifier = Modifier.padding(
                        vertical = 5.dp,
                        horizontal = 10.dp
                    )
                )
            }
        }
    }
}