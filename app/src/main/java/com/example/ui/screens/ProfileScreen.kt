package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PhoneInTalk
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.components.GlassCard
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.PremiumRose
import com.example.ui.theme.PureWhite
import com.example.viewmodel.TekeAppViewModel

@Composable
fun ProfileScreen(
    viewModel: TekeAppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(bottom = 90.dp) // buffer for bottom navigation
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header Title
        Text(
            text = viewModel.translate("profile").uppercase(),
            color = LuxuryGold,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Concierge & Customization",
            color = PureWhite,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 1. VIP Member Profile Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            backgroundAlpha = 0.12f,
            glowRadius = 6.dp,
            glowColor = LuxuryGold.copy(alpha = 0.15f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Crown Avatar
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            Brush.linearGradient(colors = listOf(Color(0xFF3F3F46), Color(0xFF18181B))),
                            shape = CircleShape
                        )
                        .border(1.5.dp, LuxuryGold, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = "VIP Badge",
                        tint = LuxuryGold,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Elite Guest Account",
                        color = PureWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "PLATINUM LEVEL MEMBER",
                        color = LuxuryGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Personalization options block
        Text(
            text = "PERSONALIZATION",
            color = LuxuryGold,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Language Selection Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.Language, contentDescription = "lang", tint = LuxuryGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = viewModel.translate("languages"), color = PureWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    // Direct toggle buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(AppLanguage.ENGLISH to "EN", AppLanguage.AMHARIC to "አማ").forEach { (lang, label) ->
                            val isSelected = viewModel.currentLanguage == lang
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) LuxuryGold else Color.Black.copy(alpha = 0.25f))
                                    .border(0.5.dp, if (isSelected) LuxuryGold else PureWhite.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setLanguage(lang) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color.Black else PureWhite.copy(alpha = 0.8f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(LuxuryGold.copy(alpha = 0.1f)).padding(vertical = 6.dp))

                // Theme Selection Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.Palette, contentDescription = "theme", tint = LuxuryGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = viewModel.translate("themes"), color = PureWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    // Three theme state selector chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("light" to "LIGHT", "dark" to "DARK", "auto" to "AUTO").forEach { (setting, label) ->
                            val isSelected = viewModel.currentThemeSetting == setting
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) LuxuryGold else Color.Black.copy(alpha = 0.25f))
                                    .border(0.5.dp, if (isSelected) LuxuryGold else PureWhite.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setThemeSetting(setting) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color.Black else PureWhite.copy(alpha = 0.8f),
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Official Boutique Concierge Contacts
        Text(
            text = viewModel.translate("contact_us").uppercase(),
            color = LuxuryGold,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = viewModel.translate("contacts_desc"),
            color = PureWhite.copy(alpha = 0.55f),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Contact Items
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // TikTok Account Card
            ContactGlassCard(
                title = "TikTok",
                subtitle = "@teke_man_promotion",
                icon = Icons.Default.Audiotrack,
                onCopy = { viewModel.copyToClipboard(context, "@teke_man_promotion") },
                onAction = { viewModel.openSocialIntent(context, "https://www.tiktok.com/@teke_man_promotion") },
                actionLabel = "OPEN"
            )

            // Telegram Boutique Card
            ContactGlassCard(
                title = "Telegram Boutique",
                subtitle = "t.me/Teke_Man_Promotion",
                icon = Icons.AutoMirrored.Filled.Send,
                onCopy = { viewModel.copyToClipboard(context, "https://t.me/Teke_Man_Promotion") },
                onAction = { viewModel.openSocialIntent(context, "https://t.me/Teke_Man_Promotion") },
                actionLabel = "OPEN"
            )

            // Phone Card 1 (Sales Office)
            ContactGlassCard(
                title = "Concierge Office",
                subtitle = "+251921935862",
                icon = Icons.Outlined.PhoneInTalk,
                onCopy = { viewModel.copyToClipboard(context, "+251921935862") },
                onAction = { viewModel.initiateOrderCall(context) },
                actionLabel = "CALL"
            )

            // Phone Card 2 (Customization Division)
            ContactGlassCard(
                title = "Customizations Division",
                subtitle = "+251911518012",
                icon = Icons.Outlined.PhoneInTalk,
                onCopy = { viewModel.copyToClipboard(context, "+251911518012") },
                onAction = {
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:+251911518012"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                },
                actionLabel = "CALL"
            )

            // Phone Card 3 (Bespoke Services)
            ContactGlassCard(
                title = "Bespoke Celebrations",
                subtitle = "+251983838309",
                icon = Icons.Outlined.PhoneInTalk,
                onCopy = { viewModel.copyToClipboard(context, "+251983838309") },
                onAction = {
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:+251983838309"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                },
                actionLabel = "CALL"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. About Teke Promotion Description Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            backgroundAlpha = 0.05f
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = "about", tint = LuxuryGold, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = viewModel.translate("about_teke").uppercase(),
                        color = PureWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = viewModel.translate("about_desc"),
                    color = PureWhite.copy(alpha = 0.65f),
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp
                )
            }
        }

        // 5. Admin Console Entrance
        Spacer(modifier = Modifier.height(20.dp))
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            backgroundAlpha = 0.12f,
            glowRadius = 6.dp,
            glowColor = LuxuryGold.copy(alpha = 0.15f),
            onClick = {
                viewModel.isAdminConsoleOpen = true
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Admin Console",
                        tint = LuxuryGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Teke Man Promotion Admin",
                            color = PureWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage products & video streams",
                            color = PureWhite.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open",
                    tint = LuxuryGold,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ContactGlassCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onCopy: () -> Unit,
    onAction: () -> Unit,
    actionLabel: String
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundAlpha = 0.08f,
        borderAlpha = 0.16f
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Circle icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(LuxuryGold.copy(alpha = 0.12f), shape = CircleShape)
                        .border(1.dp, LuxuryGold.copy(alpha = 0.25f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = title, tint = LuxuryGold, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(text = title, color = PureWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = PureWhite.copy(alpha = 0.55f),
                        fontSize = 11.5.sp,
                        maxLines = 1
                    )
                }
            }

            // Quick actions
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Copy Action icon
                IconButton(onClick = onCopy, modifier = Modifier.size(34.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = "Copy text",
                        tint = PureWhite.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Primary trigger button
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(text = actionLabel, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
