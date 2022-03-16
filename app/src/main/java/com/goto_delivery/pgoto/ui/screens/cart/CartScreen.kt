package com.goto_delivery.pgoto.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.ui.utils.transformations.twoDecimals

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    if (state.isLoading)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    else
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.cart),
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBackIosNew,
                                contentDescription = stringResource(id = R.string.go_back)
                            )
                        }
                    },
                )
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.order),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = innerPadding.calculateBottomPadding() + 20.dp,
                    top = 15.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                /* Cart items */
                item {
                    Text(
                        text = stringResource(id = R.string.cart_items),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                items(state.cart.items) { cartItem ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = cartItem.name)
                            Text(
                                text = cartItem.quantity.toString(),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        /* Suggestions */
                        if (cartItem.suggestions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))

                            Row {
                                Text(
                                    text = stringResource(id = R.string.with) + ":",
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                var suggestions = ""

                                repeat(cartItem.suggestions.size) { index ->
                                    suggestions += cartItem.suggestions[index].name

                                    if (index != cartItem.suggestions.size - 1)
                                        suggestions += ", "
                                }

                                Text(text = suggestions)
                            }
                        }
                    }
                }
                /* Address */
                item {
                    Text(
                        text = stringResource(id = R.string.address),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ListItem(
                        text = {
                            Text(text = "Aleea Constructorilor 5")
                        },
                        trailing = {
                            TextButton(onClick = { /*TODO*/ }) {
                                Text(text = stringResource(id = R.string.change_address))
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Divider(color = MaterialTheme.colorScheme.primary)
                }

                /* Info */
                item {
                    Text(
                        text = stringResource(id = R.string.info),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    /* Payment method */
                    ListItem(
                        text = {
                            Text(text = stringResource(id = R.string.payment_method))
                        },
                        trailing = {
                            TextButton(onClick = { /*TODO*/ }) {
                                Text(text = stringResource(id = R.string.select))
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Divider(color = MaterialTheme.colorScheme.primary)
                }

                /* Summary */
                item {
                    Text(
                        text = stringResource(id = R.string.summary),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    repeat(state.cart.items.size) { index ->
                        val currentItem = state.cart.items[index]

                        var suggestionsTotal = 0.0

                        currentItem.suggestions.forEach {
                            suggestionsTotal += it.price
                        }

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(text = "${currentItem.quantity}x ${currentItem.name}")
                                Text(text = "${(currentItem.price * currentItem.quantity + suggestionsTotal).twoDecimals()} ${state.restaurant.currency}")

                            }

                            if (currentItem.suggestions.isNotEmpty()) {
                                Row {
                                    Text(
                                        text = stringResource(id = R.string.with) + ":",
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.width(5.dp))

                                    var suggestions = ""

                                    repeat(currentItem.suggestions.size) { index ->
                                        suggestions += currentItem.suggestions[index].name

                                        if (index != currentItem.suggestions.size - 1)
                                            suggestions += ", "
                                    }

                                    Text(text = suggestions)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(id = R.string.delivery_fee))
                        Text(text = state.restaurant.deliveryFee.twoDecimals() + " ${state.restaurant.currency}")
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.total).toUpperCase(Locale.current),
                            style = MaterialTheme.typography.titleSmall
                        )

                        Text(text = "${state.cart.total.twoDecimals()} ${state.restaurant.currency}")
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Divider(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
}