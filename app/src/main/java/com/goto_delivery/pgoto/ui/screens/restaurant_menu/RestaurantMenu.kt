package com.goto_delivery.pgoto.ui.screens.restaurant_menu

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.data.core.test_tags.TestTags
import com.goto_delivery.pgoto.domain.model.CartItem
import com.goto_delivery.pgoto.domain.model.Food
import com.goto_delivery.pgoto.ui.utils.UiEvent
import com.goto_delivery.pgoto.ui.utils.components.SearchBar
import com.goto_delivery.pgoto.ui.utils.mappers.toCartItem
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

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val coroutineScope = rememberCoroutineScope()

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


    var latestClickedFood by remember {
        mutableStateOf<Food?>(null)
    }

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

                    val currentCartItem: CartItem? = viewModel.alreadyInCart(name = food.name)

                    InspectFoodBottomSheet(
                        food = food,
                        currency = state.restaurant.currency,
                        onAddToCartClick = {
                            viewModel.onEvent(RestaurantMenuEvents.OnAddToCartClick(it))

                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }

                            latestClickedFood = null
                        },
                        onClose = {
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        },
                        cartItem = currentCartItem
                    )
                }
            },
            sheetPeekHeight = 0.dp,
            sheetElevation = 0.dp
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
                                    if (searchBarValue.isNotEmpty())
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
                                    contentDescription = stringResource(id = R.string.go_back)
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    contentPadding = PaddingValues(
                        bottom = innerPadding.calculateBottomPadding() + 10.dp,
                        top = 10.dp
                    )
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
                                    else "${state.restaurant.deliveryFee.twoDecimals()} ${state.restaurant.currency}"
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

                        Spacer(modifier = Modifier.height(15.dp))

                        /* Divider */
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
                        val currentCartItem = viewModel.alreadyInCart(name = food.name)

                        FoodCard(
                            food = food,
                            currency = state.restaurant.currency,
                            onClick = {
                                latestClickedFood = food
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            },
                            cartItem = currentCartItem,
                            onEvent = viewModel::onEvent,
                        )
                    }

                    /* Menu */
                    items(state.restaurant.menu) { menuCategory ->

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

                            val currentCartItem = viewModel.alreadyInCart(name = food.name)

                            FoodCard(
                                food = food,
                                currency = state.restaurant.currency,
                                onClick = {
                                    latestClickedFood = food

                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    }
                                },
                                cartItem = currentCartItem,
                                onEvent = viewModel::onEvent
                            )

                            if (index != menuCategory.food.size)
                                Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
}

@Composable
private fun FoodCard(
    food: Food,
    currency: String,
    onClick: (Food) -> Unit,
    cartItem: CartItem? = null,
    onEvent: (RestaurantMenuEvents) -> Unit
) {
    Column(
        modifier = Modifier
            .animateContentSize(
                tween(500)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(tag = TestTags.FOOD)
                .clickable { onClick(food) }
                .padding(vertical = 10.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                /* Food name */
                Text(text = food.name, style = MaterialTheme.typography.titleSmall)

                /* Food ingredients */
                if (food.ingredients.isNotEmpty())
                    Text(
                        text = food.ingredients,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
            }

            /* Price and plus button */
            Row(
                modifier = Modifier.weight(0.9f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                /* Price */
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(5.dp)
                ) {
                    Text(text = "${food.price.twoDecimals()} $currency")
                }

                Spacer(modifier = Modifier.width(5.dp))

                /* Add button */
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

        /* Increase and decrease quantity if the food is in cart */
        AnimatedVisibility(visible = cartItem != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                cartItem?.let {
                    FoodAddedToCart(
                        quantity = cartItem.quantity,
                        food = food,
                        onDecreaseQuantityClick = {
                            onEvent(RestaurantMenuEvents.OnDecreaseQuantity(food = food))
                        },
                        onIncreaseQuantityClick = {
                            onEvent(RestaurantMenuEvents.OnIncreaseQuantity(food = food))
                        },
                        currency = currency
                    )

                    /* Enumerates the suggestions */
                    if (cartItem.suggestions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))

                        var suggestions = ""

                        cartItem.suggestions.forEachIndexed { index, item ->
                            suggestions += item.name

                            if (index != cartItem.suggestions.size - 1)
                                suggestions += ", "
                        }

                        Text(text = suggestions)
                    }
                }
            }
        }
    }
}

/* It shows the food name, price, quantity and increase and decrease button */
@Composable
private fun FoodAddedToCart(
    quantity: Int,
    food: Food,
    onDecreaseQuantityClick: () -> Unit,
    onIncreaseQuantityClick: () -> Unit,
    currency: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestTags.FOOD_ADDED_TO_CART)
    ) {
        /* Name, quantity, price */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${quantity}x ${food.name}")
            Text(text = (quantity * food.price).twoDecimals() + " $currency")
        }

        Spacer(modifier = Modifier.height(10.dp))

        /* Increase and decrease buttons */
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            /* Decrease quantity button */
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(0.5f))
                    .clickable {
                        onDecreaseQuantityClick()
                    }
                    .padding(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = stringResource(id = R.string.decrease_quantity)
                )
            }

            /* Increase quantity button */
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(0.5f))
                    .clickable {
                        onIncreaseQuantityClick()
                    }
                    .padding(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.increase_quantity)
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun InspectFoodBottomSheet(
    food: Food,
    currency: String,
    onAddToCartClick: (CartItem) -> Unit,
    onClose: () -> Unit,
    cartItem: CartItem? = null
) {
    val windowInfo = rememberWindowInfo()

    /* To store locally food from bottom sheet */
    val suggestionsCart = remember {
        mutableStateListOf<Food>()
    }

    var cartTotal by remember {
        mutableStateOf(food.price)
    }

    /* It adds selected suggestions in locally cart */
    LaunchedEffect(key1 = true) {
        cartItem?.let {
            suggestionsCart.addAll(cartItem.suggestions)
        }
    }

    /* Total after suggestion cart size changes */
    LaunchedEffect(key1 = suggestionsCart.size) {
        cartTotal = food.price

        suggestionsCart.forEach { item ->
            cartTotal += item.price
        }
    }

    Scaffold(
        /* Add to cart button */
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp),
            ) {
                Button(
                    onClick = {
                        onAddToCartClick(
                            food.toCartItem().copy(suggestions = suggestionsCart)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .testTag(TestTags.ADD_TO_CART)
                ) {
                    Text(
                        text = "${stringResource(id = R.string.add_for)} ${cartTotal.twoDecimals()} $currency",
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
                ImageOverflowedByTopBar(
                    food = food,
                    imageHeightDp = windowInfo.screenHeightDp / 3,
                    onClose = onClose
                )
            }

            /* Food info */
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /* Food name */
                    Text(
                        text = food.name,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    /* Food ingredients */
                    Text(
                        text = food.ingredients,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.7f),
                    )
                }
            }

            /* Suggestions */
            item {
                if (food.suggestions.isNotEmpty())
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.suggestions),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        repeat(food.suggestions.size) { index ->

                            val currentSuggestion = food.suggestions[index]

                            val isInCart = suggestionsCart.contains(currentSuggestion)

                            SelectFoodCard(
                                name = currentSuggestion.name,
                                price = currentSuggestion.price,
                                onClick = {
                                    if (isInCart)
                                        suggestionsCart.remove(currentSuggestion)
                                    else
                                        suggestionsCart.add(currentSuggestion)
                                },
                                selected = isInCart,
                                currency = currency,
                            )

                            if (index != food.suggestions.size)
                                Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
            }
        }
    }
}

@Composable
private fun SelectFoodCard(
    name: String,
    price: Double,
    onClick: (Boolean) -> Unit,
    selected: Boolean,
    currency: String,
    modifier: Modifier = Modifier
) {
    val icon = if (selected) Icons.Rounded.Remove else Icons.Rounded.Add

    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
    )

    val iconColor by animateColorAsState(
        targetValue = if (!selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(!selected) }
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            /* Name */
            Text(text = name)

            Spacer(modifier = Modifier.width(8.dp))

            /* Price and currency */
            Text(
                text = "+ ${price.twoDecimals()} $currency",
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        /* Selected and unselected icon */
        IconToggleButton(
            checked = selected,
            onCheckedChange = onClick,
            modifier = Modifier.testTag(TestTags.FOOD_SUGGESTION)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .padding(5.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(id = if (selected) R.string.selected else R.string.unselected),
                    tint = iconColor
                )
            }
        }
    }
}

@Composable
private fun ImageOverflowedByTopBar(
    food: Food,
    imageHeightDp: Dp,
    onClose: () -> Unit
) {
    Box {
        food.imageUrl?.let { imageUrl ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeightDp)
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
