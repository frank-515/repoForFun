import 'dart:io';
import 'package:path/path.dart';
import 'package:sqlite3/sqlite3.dart';
import 'package:path_provider/path_provider.dart';
import 'package:flutter/foundation.dart';

abstract class LocalDatabaseInterface {
  List<Map<String, dynamic>> query(String sql, List<dynamic> args);
  void execute(String sql, List<dynamic> args);
}

class LocalDatabase implements LocalDatabaseInterface {
  static LocalDatabase? _instance;

  // 数据库文件路径
  late String _databasePath;

  factory LocalDatabase() {
    _instance ??= LocalDatabase._internal();
    return _instance!;
  }

  LocalDatabase._internal();

  // 异步初始化数据库函数
  Future<void> init() async {
    debugPrint('初始化数据库');
    final directory = await getApplicationDocumentsDirectory();
    _databasePath = join(directory.path, 'chat.db');

    final file = File(_databasePath);
    if (!file.existsSync()) {
      // 如果文件不存在，则创建一个新的数据库
      debugPrint('数据库文件不存在，创建新的数据库');
      // 递归创建目录
      final dbDirectory = Directory(dirname(_databasePath));
      await dbDirectory.create(recursive: true);
      final db = sqlite3.open(_databasePath);
      // 创建 friends_table
      _createFriendsTable(db);
      // 创建 messages_table
      _createMessagesTable(db);
      db.dispose();
    } else {
      // 如果文件存在，则直接打开它
      debugPrint('数据库文件已存在，打开数据库\n$_databasePath');
      final db = sqlite3.open(_databasePath);
      // 可以在这里执行其他初始化操作
      db.dispose();
    }
    return Future<void>.value();
  }

  // 创建 friends_table
  void _createFriendsTable(Database db) {
    db.execute('''
      CREATE TABLE IF NOT EXISTS friends_table (
        friend_id TEXT PRIMARY KEY,
        friend_name TEXT,
        avatar_url TEXT,
        last_message_time TIMESTAMP
      );
    ''');
    debugPrint('创建 friends_table');
  }

  // 创建 messages_table
  void _createMessagesTable(Database db) {
    db.execute('''
      CREATE TABLE IF NOT EXISTS messages_table (
        message_id TEXT PRIMARY KEY,
        sender_id TEXT,
        receiver_id TEXT,
        message_content TEXT,
        send_time TIMESTAMP,
        message_type TEXT DEFAULT 'text',
        FOREIGN KEY (sender_id) REFERENCES friends_table (friend_id),
        FOREIGN KEY (receiver_id) REFERENCES friends_table (friend_id)
      );
    ''');
    debugPrint('创建 messages_table');
  }

  @override
  List<Map<String, dynamic>> query(String sql, List<dynamic> args) {
    final db = sqlite3.open(_databasePath);

    try {
      final ResultSet resultSet = db.select(sql, args.toList());
      final List<Map<String, dynamic>> result = [];

      for (final Row row in resultSet) {
        result.add(
            row.map<String, dynamic>((key, value) => MapEntry(key, value)));
      }
      debugPrint('执行查询语句: $sql, 查询到记录数: ${result.length}');
      return result;
    } finally {
      db.dispose();
    }
  }

  @override
  void execute(String sql, List<dynamic> args) async {
    final db = sqlite3.open(_databasePath);

    try {
      db.execute(sql, args.toList());
      debugPrint('执行语句: $sql');
    } finally {
      db.dispose();
    }
  }
}
