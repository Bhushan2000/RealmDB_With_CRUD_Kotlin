
# RealmDB & Retrofit With CRUD Kotlin
<img src="https://github.com/user-attachments/assets/cd72be14-7b32-4120-979c-e2c42b907bc4" height="150" width="150" />

The project focus on building a simple todo list app that utilizes RealmDB for local data storage and network calls with retrofit.
  
## Project Details:
- Architecture: MVVM
- Data Storage: RealmDB
- Networking: Retrofit with Coroutines
- UI Management: ViewModel and LiveData
  
## Features:
- Add new Todo
- Edit existing Todo
- Delete Todo
- View a list of Todo
- Persist data locally using RealmDB
  
## Features Implemented:
- **RealmDB Integration**: Setup and configured RealmDB for local data storage.
- **Retrofit Configuration**: Implemented Retrofit for network operations, including GET , POST, DELETE, and PUT requests.
- **MVVM Architecture**: Designed the project following the MVVM pattern for better separation of concerns and maintainability.
- **Coroutines**: Utilized Kotlin Coroutines for asynchronous operations.
- **LiveData and ViewModel**: Managed UI-related data in a lifecycle-conscious way using LiveData and ViewModel.
- **Network Connectivity**: Implmented Network connectivy.
- **Offline Mode**: View your todos in offline mode.


## Screenshots

<img src="https://github.com/user-attachments/assets/7efa8221-b435-4064-8346-9a46e5ca23bf" height="1000" width="500" />

## Important Note :  Due to the use of Free Rest-API's - 
- Adding a new todo will not add it into the server.
- It will simulate a POST request and will return the new created todo with a new id
- Updating a todo will not update it into the server.
- It will simulate a PUT/PATCH request and will return updated todo with modified data
- Deleting a todo will not delete it into the server.
- It will simulate a DELETE request and will return deleted todo with isDeleted & deletedOn keys

## How to Run the Project:
- Clone the repository from <a href="https://github.com/Bhushan2000/RealmDB_With_CRUD_Kotlin.git">Click Here</a>
- Open the project in Android Studio.
- Sync the project with Gradle files.
- Run the project on an emulator or a physical device with a minimum SDK version of 21.
