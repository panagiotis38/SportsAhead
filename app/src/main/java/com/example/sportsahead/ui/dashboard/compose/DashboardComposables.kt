package com.example.sportsahead.ui.dashboard.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.example.data.model.SportId
import com.example.sportsahead.R
import com.example.sportsahead.ui.ErrorView
import com.example.sportsahead.ui.ExpandableView
import com.example.sportsahead.ui.GenericLoader
import com.example.sportsahead.ui.TopBar
import com.example.sportsahead.ui.dashboard.DashboardViewModel
import com.example.sportsahead.ui.model.ErrorUiModel
import com.example.sportsahead.ui.model.dashboard.EventUiModel
import com.example.sportsahead.ui.model.dashboard.SportUiModel
import com.example.sportsahead.ui.theme.CharlestonGreen
import com.example.sportsahead.ui.theme.DarkJungleGreen

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    val uiModel by viewModel.uiFlow.collectAsState()

    Scaffold(
        topBar = { TopBar() }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkJungleGreen)
        ) {
            when {
                uiModel.isLoading -> {
                    GenericLoader()
                }
                uiModel.error.show -> {
                    ErrorView(errorModel = uiModel.error)
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
    Column {
        Spacer(modifier = Modifier.height(20.dp))
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
}


@Composable
fun SportHeaderView(sportModel: SportUiModel, onItemClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .background(CharlestonGreen)
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
                painter = painterResource(getSportIconById(sportId = sportModel.id)),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = sportModel.name,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )
        }
        Image(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(
                id = if (sportModel.isExpanded.value) {
                    R.drawable.arrow_up
                } else {
                    R.drawable.arrow_down
                }
            ),
            contentDescription = null
        )
    }
}

@DrawableRes
private fun getSportIconById(sportId: String): Int {
    return when (sportId) {
        SportId.SOCCER.value -> {
            R.drawable.ic_soccer
        }
        SportId.BASKETBALL.value -> {
            R.drawable.ic_basketball
        }
        SportId.TENNIS.value -> {
            R.drawable.ic_tennis
        }
        SportId.TABLE_TENNIS.value -> {
            R.drawable.ic_table_tennis
        }
        SportId.E_SPORTS.value -> {
            R.drawable.ic_esports
        }
        SportId.HANDBALL.value -> {
            R.drawable.ic_handball
        }
        SportId.BEACH_VOLLEY.value -> {
            R.drawable.ic_volleyball
        }
        else -> {
            R.drawable.ic_sport_default
        }
    }
}

@Composable
fun EventView(eventModel: EventUiModel, onFavouriteClicked: () -> Unit) {

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(10.dp)
            .width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = Modifier
                .border(
                    border = BorderStroke(1.dp, Color.White),
                    shape = RoundedCornerShape(20)
                )
                .padding(2.dp),
            text = eventModel.formattedTimeUntilStart.value,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
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
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = eventModel.secondContestant,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
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
            sportModel = sportModel,
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
    SportHeaderView(generateSportModel("1")) {}
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