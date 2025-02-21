In this assigment I have approached from the following steps:
1. Making a clean code, divided the project to different packages.
2. Used Dagegr hilt for dependency injections for creating dependency graph and to make it easier to test.
3. After MainActivity, added the MainActivity screen with approach of jetpack compose.
4. Added class in package data.dto called "ItemsList" which was converted from json (Using Postman) to regular data class. 
5. In MainActivity screen I injected MainActivityViewModel (MVVM).
6. Main activity viewmodel used repository for performing local or remote calls.
7. For the remote calls added ApiClient for also using loggin interceptor to the track data.
8. For locals calls used RoomDB.
9. Used try and catch for not making program crash in case of exception in local/remote callks.
10. Added unit tests with high percentage of code coverage.
