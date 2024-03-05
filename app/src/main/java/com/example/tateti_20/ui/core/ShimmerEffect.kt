package com.example.tateti_20.ui.core

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.tateti_20.ui.theme.Background
import com.example.tateti_20.ui.theme.Orange1
import com.example.tateti_20.ui.theme.OrangeShimmerBase
import com.example.tateti_20.ui.theme.OrangeShimmerTrasition

@Composable
fun ShimmerLogin(
    isLoading:Boolean,
    modifier: Modifier = Modifier,
    contentAfterLoading: @Composable () -> Unit,
){
    if (isLoading){
        Column ( modifier = modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 8.dp)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 8.dp)
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(42.dp))

            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)){
                Box(modifier = Modifier
                    .height(12.dp)
                    .width(80.dp)
                    .shimmerEffect())
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier
                    .height(12.dp)
                    .width(80.dp)
                    .shimmerEffect())
            }

            Spacer(modifier = Modifier.height(42.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 8.dp)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 8.dp)
                    .shimmerEffect()
            )
        }
    } else {
        contentAfterLoading()
    }
}

@Composable
fun ShimmerModalDrawer(
    isLoading:Boolean,
    modifier: Modifier = Modifier,
    contentAfterLoading: @Composable () -> Unit
){
    if (isLoading){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Background)
        ) {
            Box(
                modifier = modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = modifier
                    .width(50.dp)
                    .height(12.dp)
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = modifier
                    .width(50.dp)
                    .height(12.dp)
                    .shimmerEffect()
            )
        }
    } else contentAfterLoading()
}
@Composable
fun ShimmerHome(
    isLoading:Boolean,
    modifier: Modifier = Modifier,
    contentAfterLoading: @Composable () -> Unit
){
    if (isLoading){
        Column ( modifier = modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            Divider(modifier = Modifier.fillMaxWidth())
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(50.dp)
                    .width(150.dp)
                    .shimmerEffect()
            )
            Divider(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(130.dp))
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .shimmerEffect()
                )

            }

            Spacer(modifier = Modifier.height(50.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 12.dp)
                    .shimmerEffect()
            )
        }
    } else {
        contentAfterLoading()
    }
}


fun Modifier.shimmerEffect(
    baseColor: Color = OrangeShimmerBase,
    transitionColor: Color = OrangeShimmerTrasition
): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                baseColor,
                transitionColor,
                baseColor,
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned { size = it.size }
}