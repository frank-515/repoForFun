import 'package:sync_music_player/src/model.dart';

import '../localDatabase/database.dart';

class TestData {
  void importLocalTestData() {
    final FriendService friendService = FriendService();
    final MessageService messageService = MessageService();
    friendService.insertFriend(Friend(
        id: 'user1',
        name: 'Frank515',
        avatarUrl: '',
        lastMessageTime: DateTime.now()));
    friendService.insertFriend(Friend(
        id: 'user2',
        name: '515knarF',
        avatarUrl: '',
        lastMessageTime: DateTime.now()));

    // 插入消息
    messageService.insertMessageById(Message(
      id: '1',
      senderId: 'user1',
      receiverId: 'user2',
      content: "Allô?",
      sendTime: DateTime.now().subtract(Duration(minutes: 70)),
      type: 'text',
    ));
    messageService.insertMessageById(Message(
      id: '2',
      senderId: 'user1',
      receiverId: 'user2',
      content: "Bonjour? quel est ton nom?",
      sendTime: DateTime.now().subtract(Duration(minutes: 50)),
      type: 'text',
    ));
    messageService.insertMessageById(Message(
      id: '3',
      senderId: 'user2',
      receiverId: 'user1',
      content: "I am frank, and I am here to help you!",
      sendTime: DateTime.now(),
      type: 'text',
    ));
    messageService.insertMessageById(Message(
      id: '4',
      senderId: 'user1',
      receiverId: 'user2',
      content: 'Alors. mais je ne sais pas que parle anglais',
      sendTime: DateTime.now(),
      type: 'text',
    ));
    messageService.insertMessageById(Message(
      id: '5',
      senderId: 'user2',
      receiverId: 'user1',
      content:
          'Desolé... mais je vous connais et je peux vous aider en français!',
      sendTime: DateTime.now().add(Duration(minutes: 10)),
      type: 'text',
    ));
  }
}

class RecentContactService {
  final db = LocalDatabase();

  List<ContactHeader> getRecentContacts() {
    // 从数据库中获取最近联系人
    // 这里假设你已经实现了从数据库中获取最近联系人的逻辑
    // 并返回一个 List<ContactHeader>
    return [];
  }
}

class MessageService {
  final db = LocalDatabase();

  List<Message> getMessagesById(String senderId, String receiverId) {
    var result = db.query(
        'SELECT * FROM messages_table WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY send_time ASC',
        [senderId, receiverId, receiverId, senderId]);
    return result.map((e) => Message.fromMap(e)).toList();
  }

  void insertMessageById(Message message) {
    // 将DateTime对象转换为时间戳（毫秒）
    int sendTimeInMilliseconds = message.sendTime.millisecondsSinceEpoch;
    db.execute(
        'INSERT INTO messages_table (message_id, sender_id, receiver_id, message_content, send_time, message_type) VALUES (?,?,?,?,?,?)',
        [
          message.id,
          message.senderId,
          message.receiverId,
          message.content,
          sendTimeInMilliseconds, // 使用时间戳插入
          'text'
        ]);
  }
}

class FriendService {
  final db = LocalDatabase();
  List<Friend> getFriends() {
    var result = db.query('SELECT * FROM friends_table', []);
    return result.map((e) => Friend.fromMap(e)).toList();
  }

  void insertFriend(Friend friend) {
    db.execute(
        'INSERT INTO friends_table (friend_id, friend_name, avatar_url, last_message_time) VALUES (?, ?, ?, ?)',
        [friend.id, friend.name, friend.avatarUrl, friend.lastMessageTime]);
  }
}

class UserProfileService {
  final db = LocalDatabase();
  UserProfile getUserProfile() {
    // 从数据库中获取指定用户的用户资料
    // 这里假设你已经实现了从数据库中获取用户资料的逻辑
    // 并返回一个 UserProfile
    return UserProfile(name: 'Default User', avatarUrl: '', id: 'user1');
  }
}


// -- 创建好友列表表
// CREATE TABLE IF NOT EXISTS friends_table (
//     -- 好友的唯一标识
//     friend_id TEXT PRIMARY KEY,
//     -- 好友的显示名称
//     friend_name TEXT,
//     -- 头像
//     avatar_url TEXT,
//     -- 与该好友的最后一次消息交互时间，使用时间戳类型
//     last_message_time TIMESTAMP,
// );

// -- 创建消息记录表
// CREATE TABLE IF NOT EXISTS messages_table (
//     -- 消息的唯一标识符，文本类型，由服务端生成并同步过来，设置为主键确保每条消息有唯一标识
//     message_id TEXT PRIMARY KEY,
//     -- 发送者的好友ID
//     sender_id TEXT,
//     -- 接收者的好友ID
//     receiver_id TEXT,
//     -- 实际的消息内容
//     message_content TEXT,
//     -- 消息发送的具体时间
//     send_time TIMESTAMP,
//     -- 用于区分不同类型消息的字段，如'text'表示文本消息、'image'表示图片，'voice'表示语音，'video'表示视频，'file'表示文件
//     message_type TEXT DEFAULT 'text',
//     -- 设置外键约束，确保sender_id和receiver_id在friends_table中存在对应的friend_id，保证数据的关联性和完整性
//     FOREIGN KEY (sender_id) REFERENCES friends_table (friend_id),
//     FOREIGN KEY (receiver_id) REFERENCES friends_table (friend_id)
// );