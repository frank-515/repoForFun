use std::error::Error;
use std::fmt::{Display};
use crate::client::http_client::{FileList, HttpClient, VersionMetadata};
use crate::config::Config;
use crate::fs::TempFolder;
use crate::crypto::Sha256Calculator;
#[derive(Debug)]
pub struct FileDownloader {
    http_client: HttpClient,
    file_list: FileList,
    pub version: VersionMetadata,
    concurrency: usize,
    temp: TempFolder,
    config: Config
}

impl FileDownloader {
    pub async fn init() -> Result<Self, Box<dyn Error>> {
        let client = HttpClient::new();
        let version = client.get_latest_version().await?;
        let file_list = client.get_version_file_list(&version).await?;
        Ok(FileDownloader {
            http_client: client,
            file_list,
            version,
            concurrency: 1,
            temp: TempFolder::default(),
            config: Config::default()
        })
    }

    pub fn need_update(&self) -> bool {
        self.config.current_version != self.version.version
    }
    
    pub fn update_config(&mut self) {
        &self.config.set_current_version(String::from(&self.version.version));
    }


    pub async fn fetch_data(&self) -> Result<(), Box<dyn Error>> {
        // let save = Arc::new(Mutex::new(Vec::new()));
        // let thread_pool = tokio::runtime::Builder::new_multi_thread()
        //     .worker_threads(self.concurrency)
        //     .build()?;
        //
        // let download_futures = self.file_list.files.iter().map(|file| {
        //     let save_clone = Arc::clone(&save);
        //
        //     let download_task = async move {
        //         let save_clone = Arc::clone(&save_clone);
        //         let file_clone = file.clone();
        //
        //         thread_pool.spawn(async move {
        //             let downloaded_data = self.http_client.get_file(&file_clone.path).await?;
        //
        //             let mut save = save_clone.lock().await;
        //             save.push(downloaded_data);
        //
        //             Ok::<(), E>(())
        //         })
        //     };
        //
        //     download_task
        // });
        //
        // thread_pool.block_on(join_all(download_futures));
        //
        // Ok(())
        todo!()
    }

    pub async fn fetch_data_sequential(&self) -> Result<(), Box<dyn Error>> {

        for file_metadata in &self.file_list.files {
            let response = self.http_client.get_file(&file_metadata.path).await?;
            let data = &*response.bytes().await?;

            // Check file sha-256 value
            self.temp.put(&file_metadata.path, data).await?;
            // let file_local_path = PathBuf::from(&self.config.tmp_dir).join(format!(".{}", &file_metadata.path));
            let file_local_path = format!("{}{}", self.config.tmp_dir, &file_metadata.path);
            let file_digest = Sha256Calculator::from_file(file_local_path.as_str())?.calculate_hash();
            if file_digest != file_metadata.digest {
                dbg!(&file_digest, &file_metadata.digest);
                return Err(Box::new(NetError {
                    message: format!("Digest not match:{} at {}", file_metadata.path, &file_local_path)
                }));
            }
        }
        Ok(())
    }

}

#[derive(Debug)]
struct NetError{
    message: String,
}

impl Display for NetError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "{}", self.message)
    }
}

impl Error for NetError {}

#[cfg(test)]
mod tests {
    use super::*;
    #[tokio::test]
    async fn fetch_from_server() {
        let downloader = FileDownloader::init().await.unwrap();
        downloader.fetch_data_sequential().await.unwrap();
    }
}