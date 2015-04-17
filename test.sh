echo UNLOCK SCREEN
adb shell input keyevent 82 # unlock
echo UNLOCKED

echo START TESTS
./gradlew connectedAndroidTest