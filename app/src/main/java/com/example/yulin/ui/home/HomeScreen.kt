package com.example.yulin.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yulin.ui.theme.*

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var quizStatus by remember { mutableStateOf("idle") }
    var ansIdx by remember { mutableStateOf<Int?>(null) }
    
    val options = listOf("比喻受到很大启发", "形容天气严寒", "形容人很愚钝")
    val correctIdx = 0

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBg)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = "语林",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SlatePrimary
                )
                Text(
                    text = "DAILY WISDOM",
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray500,
                    letterSpacing = 2.sp
                )
            }

            // Quiz Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "今日练习 03/10",
                        style = MaterialTheme.typography.labelSmall,
                        color = OrangePrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Text(
                        text = "醍醐灌顶",
                        style = MaterialTheme.typography.displayLarge,
                        color = SlatePrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "tí hú guàn dǐng",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = Gray400,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    options.forEachIndexed { index, opt ->
                        val isSelected = ansIdx == index
                        val isCorrect = index == correctIdx
                        val borderColor = when {
                            quizStatus == "correct" && isSelected -> Green600
                            quizStatus == "wrong" && isSelected -> Red500
                            else -> Color(0xFFF1F5F9)
                        }
                        val bgColor = when {
                            quizStatus == "correct" && isSelected -> Green50
                            quizStatus == "wrong" && isSelected -> Color(0xFFFEF2F2)
                            else -> Color.White
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                                .background(bgColor, RoundedCornerShape(16.dp))
                                .clickable {
                                    ansIdx = index
                                    quizStatus = if (isCorrect) "correct" else "wrong"
                                }
                                .padding(16.dp)
                        ) {
                            Text(
                                text = opt,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = SlatePrimary
                            )
                        }
                    }

                    // Feedback
                    AnimatedVisibility(
                        visible = quizStatus != "idle",
                        enter = fadeIn() + slideInVertically()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (quizStatus == "correct") {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Green600, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("太棒了！理解正确", color = Green600, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            } else {
                                Icon(Icons.Filled.Cancel, contentDescription = null, tint = Red500, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("再想想？", color = Red500, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
