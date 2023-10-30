use std::error::Error;
use crate::client::http_client::{FileList, HttpClient, VersionMetadata};

#[derive(Debug)]
struct FileDownloader {
    http_client: HttpClient,
    file_list: FileList,
    version: VersionMetadata
}

impl FileDownloader {
    async fn init() -> Result<Self, Box<dyn Error>> {
        let client = HttpClient::new();
        let version = client.get_latest_version().await?;
        let file_list = client.get_version_file_list(&version).await?;
        Ok(FileDownloader {
            http_client: client,
            file_list,
            version
        })
    }


    fn fetch_data(&self) -> Result<(), Box<dyn Error>> {
        for file in &self.file_list.files {

        };
        todo!()
    }
}