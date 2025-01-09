import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

/// 聊天消息头部组件，显示头像、ID、最近消息、时间和未读消息数量。
class chatMessageHeader extends StatelessWidget {
  /// 头像的URL。
  final String avatarUrl;

  /// 用户的ID。
  final String id;

  /// 最近的消息内容。
  final String recentMessage;

  /// 消息的时间。
  final String time;

  /// 未读消息的数量。
  final int unreadCount;

  /// 创建一个 [chatMessageHeader] 实例。
  ///
  /// [avatarUrl] 是头像的URL，必须提供。
  /// [id] 是用户的ID，必须提供。
  /// [recentMessage] 是最近的消息内容，必须提供。
  /// [time] 是消息的时间，必须提供。
  /// [unreadCount] 是未读消息的数量，默认为0。
  const chatMessageHeader({
    super.key,
    required this.avatarUrl,
    required this.id,
    required this.recentMessage,
    required this.time,
    required this.unreadCount,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        SizedBox(
          width: 60,
          height: 60,
          child: Stack(
            children: [
              // 头像
              Center(
                child: SizedBox(
                  width: 48, // 设置头像宽度
                  height: 48, // 设置头像高度
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(10.0), // 设置圆角半径
                    child: FadeInImage.assetNetwork(
                      placeholder: 'assets/images/avatar.jpg',
                      image: avatarUrl,
                      width: 128, // 设置图片宽度
                      height: 128, // 设置图片高度
                      fit: BoxFit.cover, // 保持宽高比并填充
                      imageErrorBuilder: (context, error, stackTrace) {
                        return Image.asset('assets/images/avatar.jpg',
                            width: 128, height: 128, fit: BoxFit.cover);
                      },
                    ),
                  ),
                ),
              ),

              if (unreadCount > 0)
                Positioned(
                  top: -1,
                  right: 0,
                  child: Container(
                    padding: EdgeInsets.all(4),
                    decoration: BoxDecoration(
                      color: CupertinoColors.systemRed,
                      shape: BoxShape.circle,
                    ),
                    child: unreadCount > 9
                        ? Text(
                            unreadCount.toString(),
                            style: TextStyle(
                              color: CupertinoColors.systemRed,
                              fontSize: 10,
                            ),
                          )
                        : Text(
                            unreadCount.toString(),
                            style: TextStyle(
                              color: CupertinoColors.white,
                              fontSize: 10,
                            ),
                          ),
                  ),
                ),
            ],
          ),
        ),

        SizedBox(width: 8),
        // 右侧两列
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // 第一行：ID 和时间
              Padding(
                padding: const EdgeInsets.only(top: 7),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      id,
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 14,
                      ),
                    ),
                    Text(
                      time,
                      style: TextStyle(
                        color: CupertinoColors.systemGrey,
                        fontSize: 10,
                      ),
                    ),
                  ],
                ),
              ),
              // 第二行：消息摘要
              Text(
                recentMessage,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                style:
                    TextStyle(color: CupertinoColors.systemGrey, fontSize: 12),
              ),
            ],
          ),
        ),
      ],
    );
  }
}
