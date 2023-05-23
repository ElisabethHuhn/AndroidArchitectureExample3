# AndroidArchitectureExample3

# Introduction
This project is an example of State of the Art Android Architecture circa 2023. It uses:
* Compose for building UI
* MVVM to
  * persist UI local cache across orientation changes
  * business logic to calculate values for UI display
  * business logic for actions in response to UI events
  * UI state variables governing UI compose
* Repository to separate business logic from Data Source
* RetroFit2 for REST calls
* ROOM for local DB Persistence

# Punch List of things to do and bugs to fix
* Finish UI compose functions
  * assure navigation works
  * Floating Action Button not showing up
* Figure out testing using the VM functions to cheat Repository
* Implement Repository
  * pass calls from VM to Repository
* Get Retrofit working
* Get ROOM working
