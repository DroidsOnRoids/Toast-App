package pl.droidsonroids.toast.mock.test

import android.support.test.rule.ActivityTestRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import pl.droidsonroids.testing.mockwebserver.FixtureDispatcher
import pl.droidsonroids.testing.mockwebserver.condition.PathQueryConditionFactory
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.function.getString
import pl.droidsonroids.toast.robot.SpeakersRobot

class MockSpeakersScreenTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        setPathDispatcher()
        mockWebServer.start(12345)
        activityRule.launchActivity(null)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun goToSpeakersScreen() {
        with(SpeakersRobot()) {
            performClickOnElementWithId(R.id.actionSpeakers)
        }
    }

    private fun goToSearchScreen() {
        goToSpeakersScreen()
        with(SpeakersRobot()) {
            performClickOnElementWithId(R.id.searchImageButton)
        }
    }

    @Test
    fun isToolbarDisplayed() {
        val toolbarTitle = getString(R.string.speakers_title)
        goToSpeakersScreen()
        with(SpeakersRobot()) {
            checkIfToolbarWithTitleIsDisplayed(toolbarTitle, R.id.toolbar)
        }
    }

    @Test
    fun isSearchIconDisplayed() {
        goToSpeakersScreen()
        with(SpeakersRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.searchImageButton)
        }
    }

    @Test
    fun isSpeakerSelectedOnSpeakersScreen() {
        goToSpeakersScreen()
        with(SpeakersRobot()) {
            performClickOnRecyclerViewElement(R.id.speakersRecyclerView, 0)
        }
    }

    @Test
    fun isSpeakerSelectedOnSearchScreen() {
        goToSearchScreen()
        with(SpeakersRobot()) {
            performTyping("a", R.id.searchBox)
            checkIfSearchIsPerformed()
            performClickOnRecyclerViewElement(R.id.speakersSearchRecyclerView, 0)
        }
    }

    @Test
    fun isEveryElementOnSearchScreenDisplayed() {
        goToSearchScreen()
        with(SpeakersRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.searchBox)
            checkIfHomeButtonIsDisplayed()
            checkIfHintIsDisplayed(R.id.searchBox, getString(R.string.search_hint))
        }
    }

    @Test
    fun isSpeakersScreenDisplayedAfterClickingOnBackButton() {
        goToSearchScreen()
        with(SpeakersRobot()) {
            performNavigateUp()
            checkIfElementWithIdIsDisplayed(R.id.searchImageButton)
        }
    }

    @Test
    fun isSortingBarDisplayed() {
        goToSpeakersScreen()
        with(SpeakersRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.sortingBarLayout)
            checkIfElementWithIdIsDisplayed(R.id.arrowDownImage)
        }
    }

    @Test
    fun isSortingBarExpanded() {
        goToSpeakersScreen()
        with(SpeakersRobot()) {
            performClickOnElementWithId(R.id.titleSortingLayout)
            checkIfElementWithIdIsDisplayed(R.id.arrowUpImage)
            checkIfElementWithIdIsDisplayed(R.id.alphabeticalDivider)
            checkIfElementWithIdIsDisplayed(R.id.alphabeticalSortImage)
            checkIfTextIsCorrect(getString(R.string.alphabetical), R.id.alphabeticalText)
            checkIfElementWithIdIsDisplayed(R.id.dateDivider)
            checkIfElementWithIdIsDisplayed(R.id.dateSortImage)
            checkIfTextIsCorrect(getString(R.string.date), R.id.dateText)
        }
    }

    private fun setPathDispatcher() {
        val dispatcher = FixtureDispatcher()
        val factory = PathQueryConditionFactory("")
        dispatcher.putResponse(factory.withPathInfix("/events"), "events17_200")
        dispatcher.putResponse(factory.withPathInfix("/events/17"), "event17_200")
        dispatcher.putResponse(factory.withPathInfix("/speakers"), "speakers_200")
        dispatcher.putResponse(factory.withPathInfix("/speakers/16"), "speakers16_200")
        mockWebServer.setDispatcher(dispatcher)
    }
}
