# Food4U

## Compilation from command line
### Windows 

1. Clone the repository.
2. Download and install [android SDK command line tools](https://developer.android.com/studio#downloads) (Command line tools only).
3. Set environment variable `ANDROID_SDK_ROOT` to the path to the installed SDK tools.
4. In the project directory run:
   
* Powershell:
   ```
    ./gradlew.bat assembleDebug
    ``` 
* CMD:
   ```
    gradlew assembleDebug
    ``` 

5. Install the app-debug.apk file on your phone.
    

### Linux

1. Clone the repository.
2. Download and install [android SDK command line tools](https://developer.android.com/studio#downloads) (Command line tools only).
3. Set environment variable `ANDROID_SDK_ROOT` to the path to the installed SDK tools.
4. In the project directory run:
    
    ```
    ./gradlew.sh assembleDebug
    ``` 

5. Install the app-debug.apk file on your phone.


    