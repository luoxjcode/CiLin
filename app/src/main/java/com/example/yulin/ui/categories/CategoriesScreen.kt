package com.example.yulin.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yulin.ui.theme.*

data class Category(val title: String, val count: String, val icon: ImageVector, val bgColor: Color, val iconColor: Color)

@Composable
fun CategoriesScreen(modifier: Modifier = Modifier) {
    val categories = listOf(
        Category("写景词", "120/450", Icons.Filled.Terrain, Blue50, Color(0xFF3B82F6)),
        Category("品德词", "85/200", Icons.Filled.Favorite, Orange50, OrangePrimary),
        Category("心理词", "42/150", Icons.Filled.Psychology, Green50, Green600),
        Category("文学典故", "12/300", Icons.Filled.Book, Purple50, Color(0xFFA855F7))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBg)
            .padding(24.dp)
    ) {
        Text(
            text = "词语分类",
            style = MaterialTheme.typography.headlineMedium,
            color = SlatePrimary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    colors = CardDefaults.cardColors(containerColor = category.bgColor),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, category.iconColor.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 2.dp
                        ) {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = null,
                                tint = category.iconColor,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        
                        Spacer(Modifier.height(12.dp))
                        
                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = SlatePrimary
                        )
                        
                        Text(
                            text = category.count,
                            style = MaterialTheme.typography.labelSmall,
                            color = Gray400
                        )
                    }
                }
            }
        }
    }
}
