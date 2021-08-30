# Keep it up

<i>Keep it up</i> checks network services periodically by sending a ping, connecting to a specific port or downloading a site. It can also be used to ensure a connection with a timeout will be kept alive.

<b>Features</b>

- Create multiple separate network tasks
- One task monitors one network service at a specified interval and can be enabled or disabled
- A task sends a specified number of pings, performs a specified number of connections attempts or downloads a file or a site
- Successful and unsuccessful attempts are logged
- Downloaded files can automatically be deleted after a successful download or be kept
- Notifications can be sent on failure or unsuccessful attempt
- A network task can be restricted to WiFi connection, i.e. it does not perform any action on mobile networks with potentially limited download data
- The configuration can be exported and imported as JSON data file

<b>Permissions</b>

- <i>RECEIVE_BOOT_COMPLETED</i> for restarting running networks tasks on device boot
- <i>ACCESS_NETWORK_STATE</i> for checking network accessibility
- <i>INTERNET</i> for accessing the internet
- <i>WAKE_LOCK</i> to keep the device awake while executing a task
- <i>FOREGROUND_SERVICE</i> to start the foreground service for running tasks

<b>Note</b>

The app works best if you disable battery optimization. If battery optimization is active, networks tasks execution may be unreliable especially for short intervals, i.e. they may trigger less often and the trigger time may not be exact. There is a link in the app leading to the Android battery settings for the app. Of course, with disabled battery optimization power consumption may be higher.

## Installation

The app requires Android 5.0 (API level 21) and should run with all subsequent versions, however it's not tested with all versions and devices. Please download one of the provided apk files. You have to allow the installation from unknown sources. With older Android versions (7.x and lower) you have to allow this globally in the security settings. With recent versions of Android this can be configured per app and you have to allow it for the file manager or browser that is used for installation.

Two apk files are provided:

`keepitup-debug.apk` and `keepitup-release.apk`

The debug version contains debug information and provides some logging features in the system settings. The release version is optimized and runs faster. It is recommended to use this version aside from development purposes.

## Signature

The apk files released here on Github are self signed. To verfiy the signature you can use the `apksigner` tool that comes with Android SDK:

`apksigner verify --verbose --print-certs ./keepitup-debug.apk`

`apksigner verify --verbose --print-certs ./keepitup-release.apk`

The output should include

```
Number of signers: 1
Signer #1 certificate DN: CN=Alwin Ibba, O=ibbaa
Signer #1 certificate SHA-256 digest: 8d006bf85e3d2d823939a107378358944178ed39e5ab534dbfa028f7cce1e3af
Signer #1 certificate SHA-1 digest: d65a046585d434c785fda70216cc0d892893dd3d
Signer #1 certificate MD5 digest: 711a19fc541a1c6c898bc866cd8d09e1
Signer #1 key algorithm: RSA
Signer #1 key size (bits): 2048
Signer #1 public key SHA-256 digest: 2acba358e06962a9cdb106a1b788f131a5cb8ab06180a9078e62a1be66fa0d65
Signer #1 public key SHA-1 digest: d8eeb46370f1ff2f0548d075319e07e90763117e
Signer #1 public key MD5 digest: 0020b7336f5edb8b3a82d62d7c239842
```

## Build

### Local

For the local build you need the Android SDK installed on your machine. It is recommended to install Android Studio. Gradle takes the SDK location from a file `local.properties` with the property `sdk.dir=mylocation` pointing to the location.

Call `./gradlew assemble` to trigger the build.

The two output apk files are located under

`./keepitupmain/build/outputs/apk/debug` for the debug build variant and `./keepitupmain/build/outputs/apk/release` for the release build variant.

### Debug

For the docker build you don't need any Android tools. Simply call `./docker_build.sh`. The output directories are the same as for the local build. 

The `docker_build.sh` script works for Linux. Docker must be installed of course. There is not script for Windows at the moment but it should not be difficult to create one.

### Signing

You have to provide your own signing keys if you build *Keep it up* by yourself.

You can create a keystore for *Keep it up* with the following command:

`keytool -genkey -v -keystore keepitup.jks -alias keepitupkey -keyalg RSA -keysize 2048 -validity 20000`

