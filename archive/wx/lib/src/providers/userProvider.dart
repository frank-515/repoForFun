import 'package:flutter/material.dart';
import '../model.dart';

class UserProfileNotifier extends ChangeNotifier {
  static final UserProfileNotifier _instance = UserProfileNotifier._internal();

  factory UserProfileNotifier() {
    return _instance;
  }

  UserProfileNotifier._internal() {
    // 初始化UserProfile实例
    _userProfile = UserProfile(name: 'Default User', avatarUrl: '', id: '1');
  }

  UserProfile? _userProfile;

  UserProfile? get userProfile => _userProfile;

  void setUserProfile(UserProfile userProfile) {
    _userProfile = userProfile;
    notifyListeners();
  }
}
