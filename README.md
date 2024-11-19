Steps on how to publish the app to firebase:
- dev: 
    - run gradle task `releaseDevToFirebase`
- stage:
    - run gradle task `releaseStageToFirebase`

Notes:
- Create the tester group in firebase
- Make sure to have the correct `google-services.json` for connecting with the firebase app
- Make sure to have the correct `firebase-creds.json` for firebase distribution authentication (in the root directory)
- Change the firebase app ids specified in the `build.gradle` file
