import 'package:flutter/cupertino.dart';
import '../model.dart';

/// 聊天对话框提供者类，用于管理聊天相关的数据和状态。
class Chatdialogprovider extends ChangeNotifier {
  // 存储好友列表
  final List<Friend> _friends = [];
  // 存储当前对话对象ID
  final String _currentFriendId = '';
  final String _currentFriendName = '';
  // 存储每个好友的聊天历史记录
  final Map<String, List<Message>> _chatHistory = {};
  // 存储最近联系人列表
  final List<ContactHeader> _contactHeaders = [];

  List<Friend> get friends => _friends;
  String get currentFriendId => _currentFriendId;
  String get currentFriendName => _currentFriendName;
  Map<String, List<Message>> get chatHistory => _chatHistory;
  List<Message> getChatHistoryByFriendId(String friendId) {
    return _chatHistory[friendId] ?? [];
  }
  List<ContactHeader> get contactHeaders => _contactHeaders;

  void setContactHeaders(List<ContactHeader> contactHeaders) {
    _contactHeaders.clear();
    _contactHeaders.addAll(contactHeaders);
    notifyListeners();
  }

  /// 从好友列表中移除指定ID的好友。
  ///
  /// @param friendId 要移除的好友的ID。
  void removeFriend(String friendId) {
    _friends.removeWhere((friend) => friend.id == friendId);
    notifyListeners();
  }

  /// 清除指定好友的聊天历史记录。
  ///
  /// @param friendId 要清除聊天历史记录的好友的ID。
  void clearChatHistory(String friendId) {
    _chatHistory.remove(friendId);
    notifyListeners();
  }

  /// 直接设置好友列表。
  /// 
  /// @param friends 要设置的好友列表。
  void setFriends(List<Friend> friends) {
    _friends.clear();
    _friends.addAll(friends);
    notifyListeners();
  }

  /// 直接设置所有聊天历史记录。
  /// 
  /// @param chatHistory 要设置的聊天历史记录映射。
  void setChatHistory(Map<String, List<Message>> chatHistory) {
    _chatHistory.clear();
    _chatHistory.addAll(chatHistory);
    notifyListeners();
  }

  void setChartHistoryByFriendId(String friendId, List<Message> messages) {
    _chatHistory[friendId] = messages;
  }
}
