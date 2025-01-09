class Friend {
  final String id;
  final String name;
  final String avatarUrl;
  final DateTime lastMessageTime;

  Friend({
    required this.id,
    required this.name,
    required this.avatarUrl,
    required this.lastMessageTime,
  });

  factory Friend.fromMap(Map<String, dynamic> map) {
    return Friend(
      id: map['friend_id'],
      name: map['friend_name'],
      avatarUrl: map['avatar_url'],
      lastMessageTime: DateTime.parse(map['last_message_time']),
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'friend_id': id,
      'friend_name': name,
      'avatar_url': avatarUrl,
      'last_message_time': lastMessageTime.toIso8601String(),
    };
  }
}

class Message {
  final String id;
  final String senderId;
  final String receiverId;
  final String content;
  final DateTime sendTime;
  final String type;

  Message({
    required this.id,
    required this.senderId,
    required this.receiverId,
    required this.content,
    required this.sendTime,
    required this.type,
  });

  // 从数据库查询结果映射到Message对象
  factory Message.fromMap(Map<String, dynamic> map) {
    // 将时间戳（毫秒）转换为DateTime对象
    DateTime sendTime = DateTime.fromMillisecondsSinceEpoch(map['send_time']);
    return Message(
      id: map['message_id'],
      senderId: map['sender_id'],
      receiverId: map['receiver_id'],
      content: map['message_content'],
      sendTime: sendTime,
      type: map['message_type'],
    );
  }


  Map<String, dynamic> toMap() {
    return {
      'message_id': id,
      'sender_id': senderId,
      'receiver_id': receiverId,
      'message_content': content,
      'send_time': sendTime.toIso8601String(),
      'message_type': type,
    };
  }
}

class ContactHeader {
  final String id;
  final String recentMessage;
  final DateTime time;
  final String avatarUrl;
  final int unreadCount;

  ContactHeader({
    required this.id,
    required this.recentMessage,
    required this.time,
    required this.avatarUrl,
    required this.unreadCount
  });
}

class UserProfile {
  final String id;
  final String name;
  final String avatarUrl;
  UserProfile({
    required this.id,
    required this.name,
    required this.avatarUrl,
  });
}