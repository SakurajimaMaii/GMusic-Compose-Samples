package com.gcode.gmusiccomposesamples.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 主界面topBar
 * @param modifier Modifier
 * @param title [@androidx.compose.runtime.Composable] Function0<Unit>
 * @param icon [@androidx.compose.runtime.Composable] Function0<Unit>?
 * @param more [@androidx.compose.runtime.Composable] Function0<Unit>? 更多操作,一般建议在3个左右
 * @param backgroundColor Color
 * @param contentColor Color
 * @param contentPadding PaddingValues
 * @param elevation Dp
 */
@Composable
fun MainActTopBar(
    modifier: Modifier = Modifier,
    title:@Composable () -> Unit = {},
    icon:@Composable (() -> Unit)? = null,
    more:@Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    contentPadding:PaddingValues = AppBarDefaults.ContentPadding,
    elevation: Dp = AppBarDefaults.TopAppBarElevation
){
    TopBar(
        contentColor,
        contentPadding,
        elevation,
        modifier
    ){
        if (icon == null) {
            Spacer(TitleInsetWithoutIcon)
        } else {
            Row(TitleIconModifier, verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                    content = icon
                )
            }
        }

        Row(
            Modifier.fillMaxHeight().weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.h6) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                    content = title
                )
            }
        }

        if(more!=null){
            Row(
                Modifier.fillMaxHeight().weight(1f).wrapContentWidth(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                    content = more
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    contentColor: Color,
    contentPadding:PaddingValues,
    elevation: Dp,
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
){
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        shape = androidx.compose.ui.graphics.RectangleShape,
        modifier = modifier
    ) {
        Row(Modifier.fillMaxWidth()
            .padding(contentPadding)
            .height(AppBarHeight),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            content = content)
    }
}

private val AppBarHeight = 56.dp
private val AppBarHorizontalPadding = 4.dp
private val TitleInsetWithoutIcon = Modifier.width(16.dp - AppBarHorizontalPadding)
private val TitleIconModifier = Modifier.fillMaxHeight()
    .width(72.dp - AppBarHorizontalPadding)