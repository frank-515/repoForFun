use std::error::Error;
use std::future::Future;
use reqwest::{Client, Response};
use serde::{Deserialize, Serialize};
use crate::config::Config;
#[derive(Debug, Clone)]
pub struct  HttpClient {
    http_client: Client,
    server_addr: String,
    url_base: String
}

#[derive(Deserialize, Serialize, Debug)]
pub struct VersionMetadata {
    pub version: String,
    file_type: String,
}

#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct FileMetadata {
    pub path: String,
    pub digest: String,
    pub filename: String
}
#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct FileList {
    pub files: Vec<FileMetadata>,
}

impl HttpClient {

    pub fn new() -> Self {
        let config = Config::default();
        HttpClient {
            http_client: Client::new(),
            server_addr: config.get_ipv4port(),
            url_base: format!("http://{}/check", config.get_ipv4port())
        }
    }

    fn build(config: Config) -> Self {
        HttpClient {
            http_client: Client::new(),
            server_addr: config.get_ipv4port(),
            url_base: format!("http://{}/check", config.get_ipv4port())
        }
    }
    fn url(&self, location: &str) -> String {
        format!("{}{}", self.url_base, location)
    }

    pub async fn get_latest_version(&self) -> Result<VersionMetadata, Box<dyn Error>> {
        let api_url = self.url("/version");
        let res = self.http_client.get(api_url).send().await.unwrap();
        let latest_version = res.json::<VersionMetadata>().await?;
        Ok(latest_version)
    }

    pub async fn get_version_file_list(&self, version: &VersionMetadata) -> Result<FileList, Box<dyn Error>> {
        // let api_url = format!("http://{}/file-list/{}", self.server_addr, version.version);
        let api_url = self.url(format!("/file-list/{}", version.version).as_str());
        let res = self.http_client.get(api_url).send().await.unwrap();
        let file_list = res.json::<FileList>().await?;
        Ok(file_list)
    }

    pub fn get_file<T: AsRef<str>>(&self, loc: T)  -> impl Future<Output = Result<Response, reqwest::Error>> {
        let request_url = format!("http://{}{}", self.server_addr, loc.as_ref());
        self.http_client.get(request_url).send()
    }

}

#[cfg(test)]
mod tests {
    use super::*;
    #[tokio::test]
    async fn get_latest_version_test() {
        let client = HttpClient::new();
        let result = client.get_latest_version().await;
        assert!(result.is_ok());
        dbg!(result.unwrap());
    }

    #[tokio::test]
    async fn get_file_list() {
        let client = HttpClient::new();
        let version = VersionMetadata{ version: "0.34".to_string(), file_type: "archive".to_string() };
        let result = client.get_version_file_list(&version).await;
        assert!(result.is_ok());
    }
}