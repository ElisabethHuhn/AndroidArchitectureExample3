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
* Unit Testing

# Requirements
Build a sample app using State of the Art Architectural components

# Architecture Discussion
* some words around what the architecture is, what the alternatives were, and why I made the choices I did

# Next Steps

# Punch List of things to do and bugs to fix
* Convert to MVI
  * Create Contract file for each screen
  * convert navigation to Route/Screen where Route gathers the nav paths/state/user events/actions and passes to screen
  * convert state to mutableStateFlow from LiveData
  * Convert user triggers to intents
* Convert remote calls to coroutines from callbacks
* Figure out testing 
  * Test UI
  * Test ViewModel
  * Test Repository
    * local data source (ROOM)
    * remote data source (Retrofit)
* Figure out objects eg DBDriver vs Driver
* Go through the TODOs and decide whether to address them or not.
* Fill out the sections of this README document

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