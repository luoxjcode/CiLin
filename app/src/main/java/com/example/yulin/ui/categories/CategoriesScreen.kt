package com.example.yulin.ui.categories

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yulin.ui.theme.*

@Composable
fun EmptyHint(message: String = "暂无数据") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Inbox,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Gray400.copy(alpha = 0.3f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Gray400
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val colors = listOf(Blue50, Orange50, Green50, Purple50)
    val iconColors = listOf(Color(0xFF3B82F6), OrangePrimary, Green600, Color(0xFFA855F7))
    val icons = listOf(Icons.Filled.Terrain, Icons.Filled.Favorite, Icons.Filled.Psychology, Icons.Filled.Book)

    var offsetX by remember { mutableStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBg)
            .pointerInput(uiState.catLevel) {
                if (uiState.catLevel > 0) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX > 200f) { // Swipe threshold
                                viewModel.goBack()
                            }
                            offsetX = 0f
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount
                        }
                    )
                }
            }
            .padding(24.dp)
    ) {
        // Breadcrumbs
        if (uiState.catLevel > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "全部分类",
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray400,
                    modifier = Modifier.clickable { viewModel.reset() }
                )
                uiState.catPath.forEachIndexed { index, node ->
                    Icon(
                        Icons.Filled.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp).padding(horizontal = 4.dp),
                        tint = Gray400.copy(alpha = 0.5f)
                    )
                    Text(
                        text = node.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (index == uiState.catPath.size - 1) SlatePrimary else Gray400,
                        fontWeight = if (index == uiState.catPath.size - 1) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.clickable {
                            viewModel.navigateTo(index + 1, uiState.catPath.take(index + 1))
                        }
                    )
                }
            }
        }

        Text(
            text = if (uiState.catLevel == 0) "词语分类" else uiState.catPath.lastOrNull()?.name ?: "",
            style = MaterialTheme.typography.headlineMedium,
            color = SlatePrimary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (uiState.isLoading && uiState.catLevel == 0) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = OrangePrimary)
            }
        } else {
            AnimatedContent(
                targetState = uiState.catLevel,
                transitionSpec = {
                    if (targetState > initialState) {
                        // Forward: Slide in from right, slide out to left
                        (slideInHorizontally(animationSpec = tween(300)) { it } + fadeIn(animationSpec = tween(300)))
                            .togetherWith(slideOutHorizontally(animationSpec = tween(300)) { -it / 3 } + fadeOut(animationSpec = tween(300)))
                    } else {
                        // Backward: Slide in from left, slide out to right
                        (slideInHorizontally(animationSpec = tween(300)) { -it } + fadeIn(animationSpec = tween(300)))
                            .togetherWith(slideOutHorizontally(animationSpec = tween(300)) { it / 3 } + fadeOut(animationSpec = tween(300)))
                    }.using(SizeTransform(clip = false))
                },
                label = "CategoryLevelTransition"
            ) { level ->
                when (level) {
                    0 -> {
                        if (uiState.categoryTree.isEmpty()) {
                            EmptyHint("暂无分类数据")
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                itemsIndexed(uiState.categoryTree) { index, category ->
                                    val colorIndex = index % colors.size
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(140.dp)
                                            .clickable {
                                                viewModel.navigateTo(1, listOf(category))
                                            },
                                        colors = CardDefaults.cardColors(containerColor = colors[colorIndex]),
                                        shape = RoundedCornerShape(24.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, iconColors[colorIndex].copy(alpha = 0.1f))
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
                                                    imageVector = icons[colorIndex],
                                                    contentDescription = null,
                                                    tint = iconColors[colorIndex],
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            }
                                            
                                            Spacer(Modifier.height(12.dp))
                                            
                                            Text(
                                                text = category.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = SlatePrimary
                                            )
                                            
                                            Text(
                                                text = "${category.children?.size ?: 0} 个子项",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = Gray400
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        val subCats = uiState.catPath.lastOrNull()?.children ?: emptyList()
                        if (subCats.isEmpty()) {
                            EmptyHint("该分类下暂无子项")
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(subCats) { item ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.navigateTo(2, uiState.catPath + item)
                                            },
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        shape = RoundedCornerShape(20.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(20.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = item.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = Slate700,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "${item.children?.size ?: 0} 组主题",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                    color = Gray400
                                                )
                                                Spacer(Modifier.width(8.dp))
                                                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(12.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    2 -> {
                        val leafCats = uiState.catPath.lastOrNull()?.children ?: emptyList()
                        if (leafCats.isEmpty()) {
                            EmptyHint("该主题下暂无具体分类")
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(leafCats) { item ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)
                                            .clickable {
                                                viewModel.navigateTo(3, uiState.catPath + item)
                                            },
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        shape = RoundedCornerShape(24.dp),
                                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFF8FAFC))
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            Text(
                                                text = item.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = SlatePrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    3 -> {
                        if (uiState.isWordsLoading) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = OrangePrimary)
                            }
                        } else if (uiState.words.isEmpty()) {
                            EmptyHint("该分类下暂无词语")
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(uiState.words) { word ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
                                        shape = RoundedCornerShape(20.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = word.word,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SlatePrimary
                                                )
                                                word.pinyin?.let {
                                                    Text(
                                                        text = it,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontStyle = FontStyle.Italic,
                                                        color = Gray400
                                                    )
                                                }
                                            }
                                            
                                            word.definition?.let {
                                                Spacer(Modifier.height(8.dp))
                                                Text(
                                                    text = it,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Slate700
                                                )
                                            }

                                            word.example?.let {
                                                Spacer(Modifier.height(4.dp))
                                                Text(
                                                    text = "例：$it",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic),
                                                    color = Gray500
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
