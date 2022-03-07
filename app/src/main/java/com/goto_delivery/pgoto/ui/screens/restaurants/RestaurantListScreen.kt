package com.goto_delivery.pgoto.ui.screens.restaurants

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.data.core.test_tags.TestTags
import com.goto_delivery.pgoto.domain.model.Restaurant
import com.goto_delivery.pgoto.ui.utils.Screen
import com.goto_delivery.pgoto.ui.utils.UiEvent
import com.goto_delivery.pgoto.ui.utils.components.SearchBar
import com.goto_delivery.pgoto.ui.utils.rememberWindowInfo
import com.goto_delivery.pgoto.ui.utils.transformations.twoDecimals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun RestaurantListScreen(
    viewModel: RestaurantListViewModel = hiltViewModel(),
    onNavigate: (UiEvent.Navigate) -> Unit
) {
    val state = viewModel.state.value

    val windowInfo = rememberWindowInfo()

    var searchBarValue by remember {
        mutableStateOf("")
    }

    var selectedFoodCategory by remember {
        mutableStateOf<String?>(null)
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                else -> Unit
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(id = R.string.all_food_categories),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBackIosNew,
                                    contentDescription = stringResource(id = R.string.close_categories),
                                    modifier = Modifier.rotate(-90f)
                                )
                            }
                        }
                    )
                }
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(state.foodCategories) { category ->

                        val isSelected = selectedFoodCategory == category

                        ListItem(
                            text = {
                                Text(
                                    text = category.toUpperCase(locale = Locale.current),
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                                )
                            },
                            modifier = Modifier
                                .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                                .clickable {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }

                                    /* Update the current food category */
                                    if (!isSelected) {
                                        selectedFoodCategory = category
                                        viewModel.onEvent(
                                            RestaurantListEvents.OnFilterByCategory(
                                                category = category
                                            )
                                        )
                                    } else {
                                        selectedFoodCategory = null
                                        viewModel.onEvent(
                                            RestaurantListEvents.OnClearRestaurantFilter
                                        )
                                    }
                                }
                        )
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        SearchBar(
                            value = searchBarValue,
                            onValueChange = {
                                if (it.isEmpty())
                                    viewModel.onEvent(RestaurantListEvents.OnClearRestaurantFilter)
                                searchBarValue = it
                            },
                            placeholder = stringResource(
                                id = R.string.search_restaurants
                            ),
                            onSearch = {
                                selectedFoodCategory = null
                                viewModel.onEvent(
                                    RestaurantListEvents.OnFilterRestaurantBySearch(
                                        text = searchBarValue
                                    )
                                )
                            },
                            onClear = {
                                searchBarValue = ""
                                viewModel.onEvent(RestaurantListEvents.OnClearRestaurantFilter)
                            }
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
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(if (!state.isLoading && state.foodCategories.isNotEmpty()) state.foodCategories else emptyList()) { category ->
                        val isSelected =
                            selectedFoodCategory?.equals(category, ignoreCase = true) ?: false

                        FoodFilterChip(
                            text = category,
                            isSelected = isSelected,
                            modifier = Modifier.testTag(TestTags.FILTER_CHIP),
                            onClick = {

                                /* Update the current food category */
                                if (!isSelected) {
                                    selectedFoodCategory = category
                                    viewModel.onEvent(
                                        RestaurantListEvents.OnFilterByCategory(
                                            category = category
                                        )
                                    )
                                } else {
                                    selectedFoodCategory = null
                                    viewModel.onEvent(
                                        RestaurantListEvents.OnClearRestaurantFilter
                                    )
                                }
                            }
                        )
                    }
                    items(if (state.isLoading) 5 else 0) {
                        FoodFilterChip(
                            text = "", isSelected = false, onClick = { /*TODO*/ },
                            modifier = Modifier
                                .width(100.dp)
                                .clip(CircleShape)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                ),
                        )
                    }
                    /* Show all food categories button */
                    item {
                        if (state.foodCategories.isNotEmpty())
                            FoodFilterChip(
                                text = stringResource(id = R.string.show_all),
                                isSelected = true,
                                onClick = {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    }
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
                    items(if (!state.isLoading && state.filteredRestaurants.isEmpty()) state.restaurants else state.filteredRestaurants) { restaurant ->
                        RestaurantCard(
                            windowHeightDp = windowInfo.screenHeightDp,
                            restaurant = restaurant,
                            modifier = Modifier.animateItemPlacement(
                                animationSpec = tween(500)
                            ),
                            onClick = {
                                viewModel.onEvent(
                                    RestaurantListEvents.OnNavigate(
                                        route = Screen.RestaurantMenu.route.replace(
                                            "{restaurantId}",
                                            restaurant.id.toString()
                                        )
                                    )
                                )
                            }
                        )
                    }

                    items(if (state.isLoading) 5 else 0) {
                        RestaurantCard(
                            windowHeightDp = windowInfo.screenHeightDp,
                            restaurant = Restaurant(),
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                ),
                            onClick = { /*TODO*/ }
                        )
                    }
                }
            }

        }
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

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    /* Estimated delivery time */
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.weight(1f)
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(align = Alignment.End)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_courier),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = restaurant.deliveryFee.twoDecimals() + " " + restaurant.currency,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
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

@ExperimentalMaterialApi
@Composable
fun FoodFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.primaryContainer
    )

    FilterChip(
        selected = isSelected,
        onClick = onClick,
        colors = ChipDefaults.filterChipColors(
            backgroundColor = backgroundColor
        ),
        modifier = modifier,
        shape = CircleShape
    ) {
        Text(
            text = text.toUpperCase(locale = Locale.current),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}