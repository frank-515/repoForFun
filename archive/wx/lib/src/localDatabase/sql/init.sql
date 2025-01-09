-- 创建好友列表表
CREATE TABLE IF NOT EXISTS friends_table (
    -- 好友的唯一标识
    friend_id TEXT PRIMARY KEY,
    -- 好友的显示名称
    friend_name TEXT,
    -- 头像
    avatar_url TEXT,
    -- 与该好友的最后一次消息交互时间，使用时间戳类型
    last_message_time TIMESTAMP,
);

-- 创建消息记录表
CREATE TABLE IF NOT EXISTS messages_table (
    -- 消息的唯一标识符，文本类型，由服务端生成并同步过来，设置为主键确保每条消息有唯一标识
    message_id TEXT PRIMARY KEY,
    -- 发送者的好友ID
    sender_id TEXT,
    -- 接收者的好友ID
    receiver_id TEXT,
    -- 实际的消息内容
    message_content TEXT,
    -- 消息发送的具体时间
    send_time TIMESTAMP,
    -- 用于区分不同类型消息的字段，如'text'表示文本消息、'image'表示图片，'voice'表示语音，'video'表示视频，'file'表示文件
    message_type TEXT DEFAULT 'text',
    -- 设置外键约束，确保sender_id和receiver_id在friends_table中存在对应的friend_id，保证数据的关联性和完整性
    FOREIGN KEY (sender_id) REFERENCES friends_table (friend_id),
    FOREIGN KEY (receiver_id) REFERENCES friends_table (friend_id)
);
