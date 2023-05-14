package com.example.sportsahead.ui.dashboard.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sportsahead.R
import com.example.sportsahead.ui.ErrorScreen
import com.example.sportsahead.ui.ExpandableView
import com.example.sportsahead.ui.GenericLoader
import com.example.sportsahead.ui.TopBar
import com.example.sportsahead.ui.dashboard.DashboardViewModel
import com.example.sportsahead.ui.model.ErrorUiModel
import com.example.sportsahead.ui.model.dashboard.EventUiModel
import com.example.sportsahead.ui.model.dashboard.SportUiModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    val uiModel by viewModel.uiFlow.collectAsState()

    Scaffold(
        topBar = { TopBar() }
    ) { padding ->
        Box(modifier = Modifier.background(Color.DarkGray)) {
            when {
                uiModel.isLoading -> {
                    GenericLoader()
                }
                uiModel.error.show -> {
                    ErrorScreen(errorModel = uiModel.error)
                }
                uiModel.content.upcomingEvents.isNotEmpty() -> {
                    ContentScreen(
                        modifier = Modifier.padding(padding),
                        upcomingEvents = uiModel.content.upcomingEvents,
                        onFavouriteClicked = { sportId, eventId ->
                            viewModel.onFavouriteClicked(sportId, eventId)
                        },
                        onSectionClicked = { sportId ->
                            viewModel.onSectionClicked(sportId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ContentScreen(
    upcomingEvents: List<SportUiModel>,
    modifier: Modifier = Modifier,
    onFavouriteClicked: (String, String) -> Unit,
    onSectionClicked: (String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(upcomingEvents) { item ->
            SportView(
                sportModel = item,
                onFavouriteClicked = onFavouriteClicked,
                onSectionClicked = onSectionClicked
            )
        }
    }

}


@Composable
fun SportHeaderView(text: String, onItemClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .background(Color.Gray)
            .clickable(
                indication = null, // Removes the ripple effect on tap
                interactionSource = remember { MutableInteractionSource() }, // Removes the ripple effect on tap
                onClick = onItemClicked
            )
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .size(30.dp),
                painter = painterResource(R.drawable.ic_launcher_foreground), //TODO: add diferent srawable per sport
                contentDescription = null
            )
            //TODO: handle fonts
            //TODO: handle icons
            Text(
                // modifier = Modifier.padding(horizontal = 5.dp),
                text = text,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            )
        }
        Image(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(R.drawable.arrow_down),
            contentDescription = null
        )
    }
}

@Composable
fun EventView(eventModel: EventUiModel, onFavouriteClicked: () -> Unit) {

    Column(
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(10.dp)
            .width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        //TODO: handle fonts
        //TODO: handle icons
        //TODO: handle favourites
        Text(
            modifier = Modifier
                .border(
                    border = BorderStroke(1.dp, Color.White),
                    shape = RoundedCornerShape(20)
                )
                .padding(2.dp),
            text = "10:06:34",
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
        Image(
            modifier = Modifier
                .size(30.dp)
                .clickable(
                    indication = null, // Removes the ripple effect on tap
                    interactionSource = remember { MutableInteractionSource() }, // Removes the ripple effect on tap
                    onClick = {
                        onFavouriteClicked()
                    }
                ),
            painter = painterResource(
                id = if (eventModel.isFavourite.value) {
                    R.drawable.ic_favourite_selected
                } else {
                    R.drawable.ic_favourite_unselected
                }
            ),
            contentDescription = null
        )
        Text(
            text = eventModel.firstContestant,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = eventModel.secondContestant,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SportView(
    sportModel: SportUiModel,
    onSectionClicked: (String) -> Unit,
    onFavouriteClicked: (String, String) -> Unit = { _, _ -> }
) {
    Column {
        SportHeaderView(
            text = sportModel.name,
            onItemClicked = {
                onSectionClicked(sportModel.id)
            }
        )
        ExpandableView(isExpanded = sportModel.isExpanded.value) {
            LazyRow(
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(items = sportModel.events) {
                    EventView(
                        eventModel = it,
                        onFavouriteClicked = {
                            onFavouriteClicked(sportModel.id, it.id)
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 320)
@Composable
fun SportHeaderViewPreview() {
    SportHeaderView("Basketball") {}
}

@Preview(showBackground = true, widthDp = 320, backgroundColor = 0x000000)
@Composable
fun EventViewPreview() {
    EventView(
        eventModel = generateEvent("1", "1"),
        onFavouriteClicked = {}
    )
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun SportViewPreview() {
    SportView(
        sportModel = generateSportModel("1"),
        onSectionClicked = {}
    )
}

@Preview
@Composable
fun ErrorScreenPreview() {
    ErrorScreen(
        errorModel = ErrorUiModel(
            show = true,
            title = "Something has gone wrong!",
            message = "Please try again later"
        )
    )
}

fun generateSportModel(id: String): SportUiModel = SportUiModel(
    id = id,
    name = "Sport $id",
    events = listOf(
        generateEvent(id, "1"),
        generateEvent(id, "2"),
        generateEvent(id, "3"),
        generateEvent(id, "4"),
        generateEvent(id, "5"),
        generateEvent(id, "6"),
        generateEvent(id, "7"),
        generateEvent(id, "8"),
        generateEvent(id, "9"),
        generateEvent(id, "10"),
    )
)

fun generateEvent(sportId: String, eventId: String) = EventUiModel(
    id = eventId,
    firstContestant = "Contestant ${sportId}_${eventId}_1",
    secondContestant = "Contestant ${sportId}_${eventId}_2"
)