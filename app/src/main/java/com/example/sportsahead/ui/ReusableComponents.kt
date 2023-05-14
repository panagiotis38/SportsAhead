package com.example.sportsahead.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sportsahead.R
import com.example.sportsahead.ui.model.ErrorUiModel

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        backgroundColor = Color.DarkGray,
        //TODO: Add primary colors etc: backgroundColor = colorResource(id = R.color.colorPrimary),
        contentColor = Color.White
    )
}

@Composable
fun GenericLoader() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.White
            )
        }
    }
}

@Composable
fun ErrorScreen(
    errorModel: ErrorUiModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = Modifier
                        .background(
                            color = Color.Red,
                            shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp)
                        )
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    text = errorModel.title,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp)
                        )
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 40.dp,
                            bottom = 55.dp
                        ),
                    text = errorModel.message,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(35.dp))
            }
            Button(
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                onClick = {
                    errorModel.onTryAgain()
                }
            ) {
                Text(
                    text = "Try again",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
            }
        }
    }

}

@Composable
fun ExpandableView(
    isExpanded: Boolean,
    expandableContent: @Composable () -> Unit
) {
    // Opening Animation
    val expandTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeIn(
            animationSpec = tween(300)
        )
    }

    // Closing Animation
    val collapseTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        expandableContent()
    }
}

@Preview(showBackground = false)
@Composable
fun TopBarPreview() {
    TopBar()
}