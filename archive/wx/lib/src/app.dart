import 'package:flutter/cupertino.dart';
import 'package:sync_music_player/src/widgets/bubble.dart';
import 'widgets/chatMessageHeader.dart';
import 'widgets/chatDialog.dart';
import 'widgets/contactListWidget.dart'; // 导入新的widget

/// The Widget that configures your application.
class MyApp extends StatelessWidget {
  const MyApp({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return CupertinoApp(
      theme: CupertinoThemeData(
        brightness: Brightness.light, // 主题
      ),
      home: MainPage(), // 指定根路由
    );
  }
}

class MainPage extends StatefulWidget {
  const MainPage({super.key});

  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  double contactListWidth = 240.0;

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(
        middle: Text("MainPage"),
      ),
      child: SafeArea(
        child: LayoutBuilder(
          builder: (BuildContext context, BoxConstraints constraints) {
            return Row(
              children: [
                SizedBox(
                  width: contactListWidth,
                  child: ContactListWidget(), // 使用新的widget
                ),
                GestureDetector(
                  onPanUpdate: (details) {
                    setState(() {
                      contactListWidth += details.delta.dx;
                      contactListWidth =
                          contactListWidth.clamp(0, constraints.maxWidth);
                    });
                  },
                  child: MouseRegion(
                    cursor: SystemMouseCursors.resizeLeftRight,
                    child: Container(
                      width: 2,
                      color: CupertinoColors.systemGrey,
                    ),
                  ),
                ),
                Expanded(child: ChatDialog()),
              ],
            );
          },
        ),
      ),
    );
  }
}
