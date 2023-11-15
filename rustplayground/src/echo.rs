use std::sync::{Arc, Mutex};
use std::time::Duration;
use tokio::io::{AsyncReadExt, AsyncWriteExt};

fn run() {
    // 创建Tokio运行时实例
    let rt = tokio::runtime::Builder::new_multi_thread()
        .enable_all()
        .worker_threads(2)
        .build()
        .unwrap();

    let mut handle_list = Vec::new();

    // 创建共享变量buf
    let buf = Arc::new(Mutex::new(String::new()));

    // 复制buf的Arc指针
    let buf_ = Arc::clone(&buf);

    // 在Tokio运行时中生成一个异步任务，用于从buf读取并输出到控制台
    handle_list.push(rt.spawn(async move {
        let mut last = String::new();
        loop {
            let input = loop {
                // 尝试获得buf的锁，如果获取成功，则需要退出循环继续进行后续操作
                match buf_.lock() {
                    Ok(mut buf) => {
                        if *buf == last {
                            *buf = String::new();
                            break None;
                        }
                        last = buf.clone();
                        break Some(buf.clone());
                    }
                    Err(e) => {
                        eprintln!("{:?}", e);
                        // 锁获取失败，继续重试
                    }
                }
            };
            // 输出到控制台
            match input {
                None => {}
                Some(input ) => {
                    tokio::io::stdout().write_all(input.as_bytes()).await.unwrap();
                }
            }

        }
    }));

    // 复制buf的Arc指针
    let buf_ = Arc::clone(&buf);

    // 在Tokio运行时中生成一个异步任务，用于读取控制台输入并写入到buf
    handle_list.push(rt.spawn(async move {
        loop {
            let mut input_buf = String::new();
            std::io::stdin().read_line(&mut input_buf).unwrap();
            loop {
                // 尝试获得buf的锁，如果获取成功，则需要将输入写入到buf，并退出循环继续进行后续操作
                match buf_.lock() {
                    Ok(mut buf) => {
                        *buf = input_buf.clone();
                        break;
                    }
                    Err(e) => {
                        eprintln!("{:?}", e);
                        // 锁获取失败，继续重试
                    }
                }
            }
        }
    }));

    loop {
        if handle_list.iter().all(|handle| {handle.is_finished()}) {
            std::process::exit(0);
        }
        std::thread::sleep(Duration::from_secs(1));
    }

}

#[cfg(test)]
mod tests {
    use super::run;
    // Run by `cargo run echo`
    #[test]
    fn echo() {
        run();
    }
}