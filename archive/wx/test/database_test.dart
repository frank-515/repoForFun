import 'package:flutter_test/flutter_test.dart';
import 'package:sync_music_player/src/localDatabase/database.dart';
import 'package:path_provider_platform_interface/path_provider_platform_interface.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();
  group('general', () {
    LocalDatabase database = LocalDatabase();
    setUpAll(() async {
      PathProviderPlatform.instance = FakePathProviderPlatform();
      await database.init();
      database.execute('''
CREATE TABLE IF NOT EXISTS friends_table (
    -- 好友的唯一标识
    friend_id TEXT PRIMARY KEY,
    -- 好友的显示名称
    friend_name TEXT,
    -- 头像
    avatar_url TEXT,
    -- 与该好友的最后一次消息交互时间，使用时间戳类型
    last_message_time TIMESTAMP
);
''', []);
    });
    test('basic_insert&query', () {
      database.execute(
          'INSERT INTO friends_table (friend_name, avatar_url, last_message_time) VALUES ("frank515", "", 1683712000)',
          []);
      var result = database.query(
          'SELECT * FROM friends_table WHERE friend_name = "frank515"', []);
      expect(result[0]['friend_name'], 'frank515');
    });
    test('delete', () {
      database.execute(
          'INSERT INTO friends_table (friend_name, avatar_url, last_message_time) VALUES ("frank515", "", 1683712000)',
          []);
      database.execute(
          'DELETE FROM friends_table WHERE friend_name = "frank515"', []);
      var result = database.query(
          'SELECT * FROM friends_table WHERE friend_name = "frank515"', []);
      expect(result.length, 0);
    });

    test('update', () {
      database.execute(
          'INSERT INTO friends_table (friend_name, avatar_url, last_message_time) VALUES ("frank515", "", 1683712000)',
          []);
      database.execute(
          'UPDATE friends_table SET last_message_time = 1683712001 WHERE friend_name = "frank515"',
          []);
      var result = database.query(
          'SELECT * FROM friends_table WHERE friend_name = "frank515"', []);
      expect(result[0]['last_message_time'], 1683712001);
    });

    tearDownAll(() {
      // 清理测试数据
      database.execute('DELETE FROM friends_table', []);
      database.execute('DROP TABLE friends_table', []);
    });
  });
}

// 添加 FakePathProviderPlatform 类的定义
class FakePathProviderPlatform extends Fake
    with MockPlatformInterfaceMixin
    implements PathProviderPlatform {
  @override
  Future<String?> getTemporaryPath() async {
    return 'temporaryPath';
  }

  @override
  Future<String?> getApplicationSupportPath() async {
    return 'applicationSupportPath';
  }

  @override
  Future<String?> getLibraryPath() async {
    return 'libraryPath';
  }

  @override
  Future<String?> getApplicationDocumentsPath() async {
    return 'applicationDocumentsPath';
  }

  @override
  Future<String?> getExternalStoragePath() async {
    return 'externalStoragePath';
  }

  @override
  Future<List<String>?> getExternalCachePaths() async {
    return <String>['externalCachePath'];
  }

  @override
  Future<List<String>?> getExternalStoragePaths({
    StorageDirectory? type,
  }) async {
    return <String>['externalStoragePath'];
  }

  @override
  Future<String?> getDownloadsPath() async {
    return 'downloadsPath';
  }
}
