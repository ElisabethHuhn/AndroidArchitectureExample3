# AndroidArchitectureExample3

# Introduction
This project is an example of State of the Art Android Architecture circa 2023. It uses:
* Kotlin 100%
* Koin for Dependency Injection
  * For now, the only classes that are injected are the ViewModel and Repository
* Compose for building UI
  * Also uses Compose NavGraph navigation
* MVVM to
  * persist UI local cache across orientation changes
  * business logic to calculate values for UI display
  * business logic for actions in response to UI events
  * UI state variables governing UI compose
* Repository to separate business logic from Data Source
  * Examples of both localDataSource and remoteDataSource
  * localDataSource is ROOM DB
  * remoteDataSource is RetroFit2
* Use Flows to move lists back to UI compose (via the ViewModel) from Repository
* Unit Testing


# Requirements
Build a sample app using State of the Art Architectural components

# Architecture Discussion

# Next Steps

# Punch List of things to do and bugs to fix
* Finish getting rid of drivers/routes list and use flows only
* Finish UI compose functions
  * Need to fix the VM functions that provide the data, but Compose itself seems to work
* Get Room working
* When the VM comes up, 
  * When the Repository comes up, Retrofit is called to get the list of drivers and Routes
  * when the response returns, create the DB and fill it
  * getDrivers queries the DB for the drivers
    * if it doesn't get any, query retrofit for the remote list
* Once Repository is in place, fix VM methods
* Figure out testing (using the VM functions to cheat Repository)
