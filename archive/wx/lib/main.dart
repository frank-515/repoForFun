import 'package:flutter/cupertino.dart';
import 'package:sync_music_player/src/localDatabase/database.dart';

import 'src/app.dart';

import 'src/services/testData.dart';

void main() async {
  // Run the app and pass in the SettingsController. The app listens to the
  // SettingsController for changes, then passes it further down to the
  // SettingsView.
  runApp(MyApp());

  await LocalDatabase().init();
  TestData().importLocalTestData();
}
