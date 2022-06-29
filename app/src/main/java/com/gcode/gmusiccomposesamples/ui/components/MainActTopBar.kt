/*
 * MIT License
 *
 * Copyright (c) 2022 VastGui
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        modifier,
        backgroundColor
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
    backgroundColor: Color,
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