import 'package:flutter/material.dart';
import 'package:sync_music_player/src/model.dart';
import 'package:sync_music_player/src/widgets/chatMessageHeader.dart';

class ContactListWidget extends StatefulWidget {
  const ContactListWidget({super.key});

  @override
  _ContactListWidgetState createState() => _ContactListWidgetState();
}

class _ContactListWidgetState extends State<ContactListWidget> {
  List<ContactHeader> contactHeaders = [];

  void updateContactList() {
    // 硬编码的实例数据
    var testContactHeaders = [
      ContactHeader(
        id: '1',
        avatarUrl: 'https://example.com/avatar1.jpg',
        recentMessage: '你好，最近怎么样？',
        time: DateTime.now().subtract(Duration(hours: 2)),
        unreadCount: 2,
      ),
      ContactHeader(
        id: '2',
        avatarUrl: 'https://example.com/avatar2.jpg',
        recentMessage: '明天一起吃饭吗？',
        time: DateTime.now().subtract(Duration(days: 1)),
        unreadCount: 0,
      ),
      ContactHeader(
        id: '3',
        avatarUrl: 'https://example.com/avatar3.jpg',
        recentMessage: '记得回复我的消息哦',
        time: DateTime.now().subtract(Duration(days: 3)),
        unreadCount: 1,
      ),
    ];
    setState(() {
      contactHeaders = testContactHeaders;
    });
  }

  @override
  Widget build(BuildContext context) {
    updateContactList(); //暂时先获取数据
    return ListView.builder(
      itemCount: contactHeaders.length,
      itemBuilder: (BuildContext context, int index) {
        return SizedMessageHeader(
          contactHeader: contactHeaders[index],
        );
      },
    );
  }
}

class SizedMessageHeader extends StatelessWidget {
  final ContactHeader contactHeader;
  const SizedMessageHeader({
    super.key,
    required this.contactHeader,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 70,
      child: Padding(
          padding: const EdgeInsets.only(left: 8, right: 8),
          child: chatMessageHeader(
            avatarUrl: contactHeader.avatarUrl,
            id: contactHeader.id,
            recentMessage: contactHeader.recentMessage,
            time: formatTime(contactHeader.time),
            unreadCount: contactHeader.unreadCount,
          )),
    );
  }
}

String formatTime(DateTime time) {
  final now = DateTime.now();
  final diff = now.difference(time);
  if (diff.inDays > 5) {
    return '${time.year} / ${time.month} / ${time.day}';
  } else if (diff.inDays > 0) {
    return '${diff.inDays}天前';
  } else if (diff.inHours > 0) {
    return '${diff.inHours}小时前';
  } else if (diff.inMinutes > 0) {
    return '${diff.inMinutes}分钟前';
  } else {
    return '刚刚';
  }
}
