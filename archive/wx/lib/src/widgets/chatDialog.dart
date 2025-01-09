import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sync_music_player/src/model.dart';
import 'package:sync_music_player/src/widgets/bubble.dart';
import '../services/testData.dart';

class ChatDialog extends StatefulWidget {
  @override
  _ChatDialogState createState() => _ChatDialogState();
}

class _ChatDialogState extends State<ChatDialog> {
  List<Message> chatRecords = [];
  final TextEditingController inputController = TextEditingController();

  void addMessage(Message message, bool isSender) {
    setState(() {
      chatRecords = MessageService().getMessagesById('user1', 'user2');
    });
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Stack(
        children: [
          ListView(
            children: getRecordsView(chatRecords),
          ),
          Positioned(
            bottom: 0,
            left: 0,
            right: 0,
            child: Container(
              margin: EdgeInsets.all(16.0),
              padding: EdgeInsets.all(4.0),
              decoration: BoxDecoration(
                borderRadius: BorderRadius.all(Radius.circular(4.0)),
              ),
              child: CupertinoTextField(
                controller: inputController, // 这里正确地配置了 TextEditingController
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.all(Radius.circular(24.0)),
                  border: Border.all(color: CupertinoColors.activeBlue),
                ),
                suffix: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: CupertinoButton.filled(
                    sizeStyle: CupertinoButtonSize.small,
                    child: Icon(Icons.arrow_upward, size: 12.0),
                    onPressed: () {
                      // 在这里添加点击箭头按钮时的逻辑
                      debugPrint('输入框内容: ${inputController.text}');
                      inputController.text = '';
                    },
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  // 处理消息列表. 显示时间间隔
  List<Widget> getRecordsView(List<Message> chatRecords) {
    List<Widget> widgets = [];
    for (int i = 0; i < chatRecords.length; i++) {
      if (i == 0) {
        // 第一条信息显示时间，并居中
        widgets.add(Center(
          child: Text(
            formatTime(chatRecords[i].sendTime),
            style: TextStyle(color: Colors.grey, fontSize: 12),
          ),
        ));
      }
      widgets.add(Bubble(
        isSender: chatRecords[i].senderId == 'user1', // 根据实际情况判断是否为发送者
        message: chatRecords[i].content,
      ));
      if (i < chatRecords.length - 1) {
        Duration difference =
            chatRecords[i + 1].sendTime.difference(chatRecords[i].sendTime);
        if (difference.inMinutes > 10) {
          // 大于5分钟则显示时间，并居中
          widgets.add(Center(
            child: Text(
              formatTime(chatRecords[i + 1].sendTime),
              style: TextStyle(color: Colors.grey, fontSize: 12),
            ),
          ));
        }
      }
    }
    // 添加一个空白区域，高度为120px，避免被键盘遮挡
    widgets.add(SizedBox(height: 60));
    return widgets;
  }
}

String formatTime(DateTime time) {
  final now = DateTime.now();
  final difference = now.difference(time);

  if (difference.inDays == 0) {
    return '今天 ${time.hour}:${time.minute.toString().padLeft(2, '0')}';
  } else if (difference.inDays == 1) {
    return '昨天 ${time.hour}:${time.minute.toString().padLeft(2, '0')}';
  } else if (difference.inDays == 2) {
    return '前天 ${time.hour}:${time.minute.toString().padLeft(2, '0')}';
  } else if (difference.inDays < 365) {
    return '${time.year}-${time.month}-${time.day} ${time.hour}:${time.minute.toString().padLeft(2, '0')}';
  } else {
    return '更早以前';
  }
}
