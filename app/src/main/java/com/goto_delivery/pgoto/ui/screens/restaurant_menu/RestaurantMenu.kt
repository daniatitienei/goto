package com.goto_delivery.pgoto.ui.screens.restaurant_menu

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.domain.model.Food
import com.goto_delivery.pgoto.domain.model.MenuCategory
import com.goto_delivery.pgoto.ui.theme.GotoTheme
import com.goto_delivery.pgoto.ui.utils.Constants
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
fun RestaurantMenu(
    viewModel: RestaurantMenuViewModel = hiltViewModel(),
    onPopBackStack: (UiEvent.PopBackStack) -> Unit
) {
    val state = viewModel.state.value

    var searchBarValue by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                else -> Unit
            }
        }
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    var latestClickedFood by remember {
        mutableStateOf<Food?>(null)
    }

    val coroutineScope = rememberCoroutineScope()

    if (state.isLoading)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    else
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
                    sheetContent = {
                latestClickedFood?.let { food ->
                    InspectFoodBottomSheet(
                        food = food,
                        currency = state.restaurant.currency,
                        onClose = {
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        },
                        onAddToCartClick = {
                            viewModel.onEvent(RestaurantMenuEvents.OnAddToCartClick(it))

                            coroutineScope.launch {
                                if (bottomSheetScaffoldState.bottomSheetState.isExpanded)
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                            }

                            latestClickedFood = null

                        }
                    )
                }
            },
            sheetPeekHeight = 0.dp,
        ) {
            Scaffold(
                bottomBar = {
                    if (state.cart.items.isNotEmpty())
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(align = Alignment.CenterHorizontally)
                                .padding(bottom = 10.dp),
                        ) {
                            Button(
                                onClick = { /*TODO*/ },
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ) {
                                Text(
                                    text = "${stringResource(id = R.string.buy_for)} ${state.cart.total.twoDecimals()} ${state.restaurant.currency}",
                                )
                            }
                        }
                },
                topBar = {
                    SmallTopAppBar(
                        title = {
                            SearchBar(
                                value = searchBarValue,
                                onValueChange = { searchBarValue = it },
                                placeholder = stringResource(id = R.string.search_food),
                                onSearch = {
                                    viewModel.onEvent(RestaurantMenuEvents.OnSearchFood(value = searchBarValue))
                                },
                                onClear = { searchBarValue = "" }
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(RestaurantMenuEvents.OnPopBackStack)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBackIosNew,
                                    contentDescription = stringResource(
                                        id = R.string.go_back
                                    )
                                )
                            }
                        }
                    )
                }
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    /* Restaurant title and info */
                    item {
                        /* Restaurant name */
                        Text(
                            text = state.restaurant.name,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        /* Restaurant info */
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(align = Alignment.CenterHorizontally)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                /* Rating */
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_star_circle),
                                        contentDescription = stringResource(id = R.string.rating),
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(text = state.restaurant.rating.twoDecimals())
                                }

                                /* Delivery fee */
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_courier_circle),
                                        contentDescription = stringResource(id = R.string.delivery_fee),
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.primary

                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = if (state.restaurant.deliveryFee == 0.0)
                                            stringResource(id = R.string.free)
                                        else state.restaurant.deliveryFee.twoDecimals()
                                    )
                                }

                                /* Estimated delivery time */
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_time),
                                        contentDescription = stringResource(id = R.string.estimated_delivery_time),
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(text = state.restaurant.estimatedDeliveryTime)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(align = Alignment.CenterHorizontally)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(1.dp)
                                    .background(color = MaterialTheme.colorScheme.primary)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    /* Search results */
                    item {
                        AnimatedVisibility(
                            visible = state.filteredFoodList.isNotEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = stringResource(id = R.string.search_results),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    items(state.filteredFoodList) { food ->
                        FoodCard(
                            food = food,
                            currency = state.restaurant.currency,
                            onClick = {
                                latestClickedFood = food

                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            },
                        )
                    }

                    /* Menu */
                    items(state.restaurant.menu) { menuCategory ->
                        FoodCategory(
                            menuCategory = menuCategory,
                            currency = state.restaurant.currency,
                            onFoodCardClick = { food ->
                                latestClickedFood = food

                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            },
                        )
                    }
                }
            }
        }
}

@Composable
private fun FoodCategory(
    menuCategory: MenuCategory,
    currency: String,
    onFoodCardClick: (Food) -> Unit
) {
    /* Category name */
    Text(
        text = menuCategory.name,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(15.dp))

    /* Food from that category */
    repeat(menuCategory.food.size) { index ->

        val food = menuCategory.food[index]

        FoodCard(
            food = food,
            currency = currency,
            onClick = onFoodCardClick,
        )

        if (index != menuCategory.food.size)
            Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun FoodCard(
    food: Food,
    currency: String,
    onClick: (Food) -> Unit,
    isInCart: Boolean = false,
    quantity: Int? = null
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(food) }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = food.name, style = MaterialTheme.typography.titleSmall)
                if (food.ingredients.isNotEmpty())
                    Text(
                        text = food.ingredients,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
            }

            Row(
                modifier = Modifier.weight(0.9f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(5.dp)
                ) {
                    Text(text = "${food.price.twoDecimals()} $currency")
                }

                Spacer(modifier = Modifier.width(5.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(id = R.string.inspect_food)
                    )
                }
            }
        }

        AnimatedVisibility(visible = isInCart) {

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FoodCardPreview() {
    GotoTheme {
        FoodCard(
            food = Constants.food,
            currency = "RON",
            onClick = {},
            isInCart = true,
            quantity = 2
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun InspectFoodBottomSheet(
    food: Food,
    onClose: () -> Unit,
    currency: String,
    onAddToCartClick: (List<Food>) -> Unit
) {
    val windowInfo = rememberWindowInfo()

    val cart = remember {
        mutableStateListOf<Food>()
    }

    cart.add(food)

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp),
            ) {
                Button(
                    onClick = { onAddToCartClick(cart) },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text = "${stringResource(id = R.string.add_for)} ${food.price.twoDecimals()} $currency",
                    )
                }
            }
        },
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
        ) {
            item {
                Box {
                    food.imageUrl?.let { imageUrl ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(windowInfo.screenHeightDp / 3)
                                .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                        ) {
                            Image(
                                painter = rememberImagePainter(data = imageUrl),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                        }
                    }
                    SmallTopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = onClose) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = stringResource(
                                        id = R.string.close
                                    )
                                )
                            }
                        },
                        colors = smallTopAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = food.name,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = food.ingredients,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.7f),
                    )
                }
            }

            item {
                if (food.suggestions.isNotEmpty())
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.suggestions),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        repeat(food.suggestions.size) { index ->
                            val currentFoodSuggestion = food.suggestions[index]

                            FoodCard(
                                food = currentFoodSuggestion,
                                currency = currency,
                                onClick = {
                                    cart.add(food)
                                }
                            )

                            if (index != food.suggestions.size)
                                Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun InspectFoodBottomSheetLight() {
    GotoTheme {
        InspectFoodBottomSheet(
            currency = "RON",
            food = Constants.food,
            onClose = {},
            onAddToCartClick = {}
        )
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun InspectFoodBottomSheetDark() {
    GotoTheme {
        InspectFoodBottomSheet(
            currency = "RON",
            food = Constants.food,
            onClose = {},
            onAddToCartClick = {}
        )
    }
}