import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Bubble extends StatelessWidget {
  final bool isSender;
  final String message;

  Bubble({required this.isSender, required this.message});

  @override
  Widget build(BuildContext context) {
    return Align(
      alignment: isSender ? Alignment.centerRight : Alignment.centerLeft,
      child: Container(
        margin: EdgeInsets.symmetric(horizontal: 24.0, vertical: 8.0),
        padding: EdgeInsets.all(6.0),
        decoration: BoxDecoration(
          color: isSender
              ? CupertinoColors.activeBlue
              : CupertinoColors.systemGrey,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(12),
            topRight: Radius.circular(12),
            bottomLeft: isSender ? Radius.circular(12) : Radius.circular(0),
            bottomRight: isSender ? Radius.circular(0) : Radius.circular(12),
          ),
        ),
        child: Text(
          message,
          style: TextStyle(color: Colors.white, fontSize: 14),
        ),
      ),
    );
  }
}
