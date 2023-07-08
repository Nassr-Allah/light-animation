package com.nassrou.lightanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nassrou.lightanimation.ui.LightItemDetails
import com.nassrou.lightanimation.ui.theme.DarkGrey
import com.nassrou.lightanimation.ui.theme.Gray
import com.nassrou.lightanimation.ui.theme.LightAnimationTheme
import com.nassrou.lightanimation.ui.theme.LightGray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val items = listOf(
                LightItemDetails(icon = R.drawable.ic_android, color = Color.Green),
                LightItemDetails(icon = R.drawable.ic_heart, color = Color.Red),
                LightItemDetails(icon = R.drawable.ic_lightbulb, color = Color.Yellow),
                LightItemDetails(icon = R.drawable.ic_mail, color = Color.Blue),
                LightItemDetails(icon = R.drawable.ic_comment, color = Color.White),
            )

            LightAnimationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkGrey
                ) {
                    LightItemsList(list = items)
                }
            }
        }
    }
}

@Composable
fun LightItemsList(list: List<LightItemDetails>) {
    
    var selectedItem by remember {
        mutableStateOf(-1)
    }
    
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround) {
        list.forEachIndexed { index, lightItemDetails ->
            LightItem(
                color = lightItemDetails.color,
                isSelected = selectedItem == index,
                icon = lightItemDetails.icon
            ) {
                selectedItem = index
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LightItem(color: Color, isSelected: Boolean, icon: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            LightSource(color = color, isSelected = isSelected)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 90.dp), contentAlignment = Alignment.Center) {
            CustomButton(color = color, icon = icon, isSelected = isSelected) {
                onClick()
            }
        }
    }
}

@Composable
fun CustomButton(isSelected: Boolean = false, color: Color, icon: Int, onClick: () -> Unit) {
    val contentColor = animateColorAsState(
        if (isSelected) color else LightGray
    )
    OutlinedButton(
        modifier = Modifier.width(120.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor.value
        ),
        border = BorderStroke(width = 2.dp, color = contentColor.value),
        onClick = { onClick() }
    ) {
        Icon(painter = painterResource(icon), contentDescription = null)
    }
}

@ExperimentalAnimationApi
@Composable
fun LightSource(color: Color, isSelected: Boolean) {

    val circleColor = animateColorAsState(
        if (isSelected) color else LightGray
    )
    val lightColor = animateColorAsState(
        if (isSelected) color else Color.Transparent
    )
    val brush = Brush.horizontalGradient(listOf(lightColor.value, Color.Transparent))

    Box(
        modifier = Modifier
            .fillMaxWidth(0.65f)
            .height(100.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(modifier = Modifier
            .size(15.dp)
            .clip(CircleShape)
            .background(circleColor.value)
            .blur(20.dp)
        )
        AnimatedVisibility(
            visible = isSelected,
            enter =  expandHorizontally() + fadeIn(
                animationSpec = tween(
                    durationMillis = 850
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 1000
                )
            )
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .blur(4.dp)
                    .drawWithCache {
                        val path = Path()
                        path.moveTo(0f, size.height / 2f)
                        path.lineTo(size.width, 0f)
                        path.lineTo(size.width, size.height)
                        path.lineTo(0f, size.height / 2f)
                        path.close()
                        onDrawBehind {
                            drawPath(path = path, brush = brush)
                        }
                    }
            )
        }
    }
}

