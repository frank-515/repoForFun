use std::fmt::format;
use std::path::PathBuf;
use std::process::Output;
use std::sync::{Arc, Weak};
use std::thread;
use std::time::Duration;
use crate::config::Config;
use crate::ui::UI;
slint::include_modules!();
mod client;
mod config;
mod crypto;
mod fs;
mod ui;
mod archive;

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let worker_handle = tokio::spawn(work_thread());
    worker_handle.await.unwrap();
    Ok(())
}

async fn work_thread() {
    let temp = fs::TempFolder::default();
    let mut downloader = client::fetch::FileDownloader::init().await.unwrap();
    if downloader.need_update() {
        println!("Fetch data...");
        downloader.fetch_data_sequential().await.unwrap();
        downloader.update_config();
    }
    let data_dir = format!("{}/data/versions/{}/data", Config::default().tmp_dir, downloader.version.version);
    let mut output = None;;
    match std::env::consts::OS {
        "windows" => {
            let script_path = temp.tmp_path("./data/versions").join(downloader.version.version).join("install.cmd").canonicalize().unwrap();
            dbg!(&script_path);
            output = Some(std::process::Command::new(script_path.to_str().unwrap())
                // .arg(script_path)
                .env("DATA_DIR", data_dir)
                .output());
        }
        "linux" => {
            let script_path = temp.tmp_path("./data/versions").join(downloader.version.version).join("install.sh").canonicalize().unwrap();
            output = Some(std::process::Command::new("sh")
                .arg(script_path)
                .env("DATA_DIR", data_dir)
                .output());
        }
        "macos" => {

        }
        _ => {
            println!("Unknown os:{}", std::env::consts::OS);
        }
    }

    match output.unwrap() {
        Ok(output) => {
            println!("{}",output.status);
            match String::from_utf8(output.stdout.to_vec()) {
                Ok(s) => {
                    println!("{}", &s);
                }
                Err(e) => {
                    println!("{:?}", &e);
                }
            }
        }
        Err(err) => {
            println!("{}", err);
        }
    }

}