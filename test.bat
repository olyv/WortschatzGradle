echo UNLOCK SCREEN
adb shell input keyevent 82 
echo UNLOCKED

echo START TESTS
./gradlew connectedAndroidTest