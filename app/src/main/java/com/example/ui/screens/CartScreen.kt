package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocalMall
import androidx.compose.material.icons.outlined.PhoneInTalk
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import com.example.model.ProductRepository
import com.example.ui.components.GlassCard
import com.example.ui.components.ProductVisualThumbnail
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PremiumRose
import com.example.ui.theme.PureWhite
import com.example.viewmodel.TekeAppViewModel

@Composable
fun CartScreen(
    viewModel: TekeAppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var promoInput by remember { mutableStateOf("") }
    var isConfirmDialogOpen by remember { mutableStateOf(false) }

    // Resolve cart items
    val cartProducts = viewModel.cartItems.mapNotNull { (productId, qty) ->
        val prod = viewModel.productsList.find { it.id == productId }
        if (prod != null) Pair(prod, qty) else null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Screen Header
        Text(
            text = viewModel.translate("cart").uppercase(),
            color = LuxuryGold,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Your Luxury Selections",
            color = PureWhite,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (cartProducts.isEmpty()) {
            // Empty state view
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    backgroundAlpha = 0.08f
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = "Empty basket",
                            tint = LuxuryGold.copy(alpha = 0.45f),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = viewModel.translate("empty_cart"),
                            color = PureWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = viewModel.translate("empty_desc"),
                            color = PureWhite.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.activeTab = "home" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LuxuryGold,
                                contentColor = ObsidianBlack
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "EXPLORE GIFTS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        } else {
            // Cart lists and summary checkout block
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 80.dp) // buffer for navigation
            ) {
                // List of Products in Cart
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartProducts) { (product, qty) ->
                        CartItemRow(
                            product = product,
                            quantity = qty,
                            viewModel = viewModel
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Promo code entry
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    backgroundAlpha = 0.1f
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ConfirmationNumber,
                            contentDescription = "Promo icon",
                            tint = LuxuryGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (promoInput.isEmpty()) {
                                Text(
                                    text = "Enter Code (TEKEGOLD)...",
                                    color = PureWhite.copy(alpha = 0.35f),
                                    fontSize = 12.sp
                                )
                            }
                            BasicTextField(
                                value = promoInput,
                                onValueChange = { promoInput = it },
                                textStyle = LocalTextStyle.current.copy(color = PureWhite, fontSize = 12.sp),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Apply action text
                        Text(
                            text = "APPLY",
                            color = LuxuryGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                if (promoInput.isNotEmpty()) {
                                    val success = viewModel.applyPromo(promoInput)
                                    if (success) {
                                        Toast.makeText(context, "10% Promo Code Applied!", Toast.LENGTH_SHORT).show()
                                        promoInput = ""
                                    } else {
                                        Toast.makeText(context, "Invalid Promo Code", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Pricing Summary Box
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    backgroundAlpha = 0.08f
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Subtotal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = viewModel.translate("subtotal"), color = PureWhite.copy(alpha = 0.6f), fontSize = 12.sp)
                            Text(text = "${viewModel.getCartSubtotal().toInt()} ${viewModel.translate("currency")}", color = PureWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Promo discount if active
                        if (viewModel.appliedPromoCode != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Promo Discount (10%)", color = LuxuryGold, fontSize = 12.sp)
                                Text(
                                    text = "-${(viewModel.getCartSubtotal() * 0.1).toInt()} ${viewModel.translate("currency")}",
                                    color = LuxuryGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Luxury delivery (Free)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = viewModel.translate("shipping"), color = PureWhite.copy(alpha = 0.6f), fontSize = 12.sp)
                            Text(text = viewModel.translate("free"), color = LuxuryGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(LuxuryGold.copy(alpha = 0.15f)))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Grand Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = viewModel.translate("total"), color = PureWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                            Text(
                                text = "${viewModel.getCartTotal().toInt()} ${viewModel.translate("currency")}",
                                color = LuxuryGold,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Checkout button
                Button(
                    onClick = { isConfirmDialogOpen = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LuxuryGold,
                        contentColor = ObsidianBlack
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Outlined.LocalMall, contentDescription = "checkout", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = viewModel.translate("checkout").uppercase(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        // Checkout Confirmation Dialog
        if (isConfirmDialogOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
                    .blur(10.dp)
                    .pointerInput(Unit) {}
            )

            androidx.compose.ui.window.Dialog(
                onDismissRequest = { isConfirmDialogOpen = false },
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
            ) {
                GlassCard(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    backgroundAlpha = 0.4f,
                    borderAlpha = 0.3f,
                    glowRadius = 16.dp,
                    glowColor = LuxuryGold
                ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(LuxuryGold.copy(alpha = 0.15f), shape = CircleShape)
                                    .border(1.dp, LuxuryGold.copy(alpha = 0.3f), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneInTalk,
                                    contentDescription = "Checkout verification call",
                                    tint = LuxuryGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = viewModel.translate("order_confirm_title"),
                                color = PureWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = viewModel.translate("order_confirm_desc"),
                                color = PureWhite.copy(alpha = 0.75f),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Custom summary spec block
                            GlassCard(
                                shape = RoundedCornerShape(12.dp),
                                backgroundAlpha = 0.12f,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${cartProducts.size} ${viewModel.translate("items")}",
                                        color = PureWhite,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${viewModel.getCartTotal().toInt()} ${viewModel.translate("currency")}",
                                        color = LuxuryGold,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { isConfirmDialogOpen = false },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PureWhite),
                                    border = BorderStroke(1.dp, PureWhite.copy(alpha = 0.3f)),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text(
                                        text = viewModel.translate("cancel").uppercase(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Button(
                                    onClick = {
                                        isConfirmDialogOpen = false
                                        viewModel.initiateOrderCall(context)
                                        viewModel.clearCart() // empty cart on call placement
                                    },
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .height(48.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LuxuryGold,
                                        contentColor = ObsidianBlack
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                                ) {
                                    Text(
                                        text = viewModel.translate("order_confirm_btn").uppercase(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

@Composable
fun CartItemRow(
    product: Product,
    quantity: Int,
    viewModel: TekeAppViewModel
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundAlpha = 0.05f,
        borderAlpha = 0.15f
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Thumbnail
            ProductVisualThumbnail(
                product = product,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .clickable { viewModel.selectedProductForDetail = product }
            )

            // Content details
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = product.getTitle(viewModel.currentLanguage),
                        color = PureWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    val discountedPrice = product.price * (1.0 - product.discountPercentage / 100.0)
                    Text(
                        text = "${discountedPrice.toInt()} ${viewModel.translate("currency")}",
                        color = LuxuryGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Incrementer counter
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
                ) {
                    IconButton(
                        onClick = { viewModel.updateCartQuantity(product.id, quantity - 1) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "dec", tint = PureWhite, modifier = Modifier.size(14.dp))
                    }
                    Text(
                        text = "$quantity",
                        color = PureWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    IconButton(
                        onClick = { viewModel.updateCartQuantity(product.id, quantity + 1) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "inc", tint = PureWhite, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}
