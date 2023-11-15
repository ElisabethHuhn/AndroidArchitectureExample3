# AndroidArchitectureExample3

# Introduction
This project is an example of State of the Art Android Architecture circa 2023. It uses:
* Kotlin 100%
* Koin for Dependency Injection
  * For now, the only classes that are injected are the ViewModel and Repository
* Compose for building UI
  * Also uses Compose NavGraph navigation
* MVI to
  * persist UI local cache across orientation changes
  * business logic to calculate values for UI display
  * business logic for actions in response to UI events
  * UI state variables governing UI compose
  * User Events (The I in MVI "intents") to execute actions in response to user triggers
* Repository to separate business logic from Data Source
  * Examples of both localDataSource and remoteDataSource
  * localDataSource is ROOM DB
  * remoteDataSource is RetroFit2
* Use Flows to move lists back to UI compose (via the ViewModel) from Repository
* Coroutines for both serial and parallel structured concurrency
* Automated Testing

# Requirements
Build a sample app using State of the Art Architectural components.

*Functional Requirements*

Rest API endpoint:
https://d49c3a78-a4f2-437d-bf72-569334dea17c.mock.pstmn.io/data

The endpoint returns two lists: Drivers and Routes
Create a local persistent data source of the remote data that can serve as a cache.

Two Screens: Driver and Route

Driver Screen:
* Displays list of driver names and ids
* FAB sort button. Sort based on last name
* When a particular driver is selected, Route Screen is displayed for that driver

Route Screen
* List of routes for a given driver depends on business rules. If the driverID:
  * is same as routeID => display the route
  * is divisible by 2 => display the first R type route
  * is divisible by 5 => display the second C type route
  * does not meet any of the above rules => display the last I type route

*Technical Requirements*
* MVVM architecture
* Repository that separates
  * data source from data usage
  * local data source from remote data source
* Koin DI
  * Repository
  * ViewModel
  * Others?
* Compose UI
* Retrofit remote 
* ROOM local
* Coroutines / flows
* LiveData
  * Actually, Id like to make the argument that flows should replace LiveData

# Architecture Discussion
I initially wrote this app with an MVVM architecture, as that was the direct requirement. But as I learned more about MVI I rewrote it. And the experience has taught me that MVI is clearly superior. One file, ScreenContract.kt, tells you everything you need to know to unit test the Screen.

Writing and maintaining automated testing is clearly costly. But the potential for payback ROI is obvious. Regression testing is performed consistently and repeatedly. The earlier you find a bug, the cheaper it is to fix. Thus, a bug found by a developer is cheaper than that same bug found in QA. Automated testing finds bugs earlier in the process, and this savings more than covers the cost of creating and maintaining automated testing.

MVI allows for a greater separation of concerns, and thus an easier isolation of code, leading to higher quality testing. MVI allows for the tracking of user event occurrence resulting in UI state change.  

# Punch List of things to do and bugs to fix
* Figure out best way to test MVI
* Figure out testing
  * Test UI
  * Test ViewModel
  * Test Repository
    * local data source (ROOM)
    * remote data source (Retrofit)

* Go through the TODOs and decide whether to address them or not.
* Fill out the sections of this README document

# Next Steps
* Assure exception handling for coroutine loading of data sources
* Assure coroutine cancellation behaves properly
* Automated Testing driven by MVI architecture.
  * Unit Tests
    * Use Contract to unit test that each user event triggers the expected state change
    * Test Room
    * Test remote API calls
  * Use Instrumentation testing to test:
    * UI properly reflects state
    * Automate UI test scenarios

# Structured Concurrency Hierarchy
![DataSourceExceptions.jpg](DataSourceExceptions.jpg)

# Near Exhaustive list of Testing Types
* Functional - Does it do what the requirements say it should do
* Non-functional - Performance, Capacity, Throughput, Power consumption, network reception
* Android UI
  * component visibility
  * user interaction (event detection and response actions conform to requirements)
* Device Compatibility
  * OS version
  * Device Model
  * Screen Size
  * Screen Resolution
  * Network Connectivity
* Integration Testing
* Network Testing
  * Connectivity
  * Intermittent reception
  * Field testing with Mobile Data Network
* Installation Testing
* Security Testing
* Bucket or A/B testing

# Android Tests
* Unit Tests
  * JUnit
  * Robolectric
  * Mockito
* Integration Tests
* UI Tests
  * Espresso
  * UI Automator