## Sample usage of MockWebServer path dispatcher.

[MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) is a library for mocking server responses. [MockwebServer path dispatcher](https://github.com/DroidsOnRoids/mockwebserver-path-dispatcher) is a helper library that helps write tests in more concise and readable way. Some ideas were taken from [MockWebServer+](https://github.com/orhanobut/mockwebserverplus) - library that uses yaml files to store responses in resources package.


### Setup
First thing to do is to change your BASE_API_URL. Easiest way to do that is to create new buildType or productFlavor that contains changed API URL to localhost.

```java
productFlavors {
        letswift {
            applicationIdSuffix ".letswift"
            versionNameSuffix "-letswift"
            buildConfigField 'String', 'BASE_API_URL', '"https://api.letswift.pl/api/v1/"'
            buildConfigField 'String', 'BASE_IMAGES_URL', '"https://api.letswift.pl"'
        }
        mockUiTest {
            applicationIdSuffix ".letswift"
            versionNameSuffix "-letswift"
            buildConfigField 'String', 'BASE_API_URL', '"http://localhost:12345"'
            buildConfigField 'String', 'BASE_IMAGES_URL', '"https://api.letswift.pl"'
        }
        // TODO add flavor for toast api when available
    }
```

Second thing is to add mockWebServer-path-dispatcher to your test dependencies.

```java

androidTestImplementation "pl.droidsonroids.testing:mockwebserver-path-dispatcher:1.0.1"
```

Next you need to create resources/fixtures directory. And put there your responses.

```
src
├── test
│   ├── java
│   ├── resources
│   │   ├── fixtures
│   │   │   ├── events_200.yaml
│   │   │   ├── event17_200.yaml
```

Saple response in yaml file:

```yaml
statusCode : 200
headers:
- 'Content-Type: application/json; charset=UTF-8'
body: >
    {
      "events": "body"
    }
```

### Implementation
Last step is to implement it into your test class.

```java
@JvmField
    @Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false) //launchActivity should be set on false because you want to execute your mockWebServer code before activity started.

    val mockWebServer = MockWebServer()

    private fun pathCondition() {
            val dispatcher = FixtureDispatcher()
            val condition = PathQueryConditionFactory("pathPrefix") // pathPrefix is optional you can put empty string here if your paths does not have common part.
            dispatcher.putResponse(condition.withPathInfix("pathInfix"), "yaml_file_response")
            dispatcher.putResponse(condition.withPathInfix("events"), "events_200")
            dispatcher.putResponse(condition.withPathInfix("/events/17"), "event17_200")
            mockWebServer.setDispatcher(dispatcher)
        }

    @Before
    fun setup(){
        pathCondition() //Set your dispatcher before server is started
        mockWebServer.start(12345) //Start mockWebServer using port you set in API URL
        activityRule.launchActivity(null) //Launch activity
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown() //Shutdown server after tests
    }
```

Clone project to test it in action. Remember to delete @Ignore annotations.

### Documentation
MockWebServer path dispatcher have also ability to match your responses by pathAndQueryParameter. Check detailed documentation for more info -> [HERE](https://github.com/DroidsOnRoids/mockwebserver-path-dispatcher)
