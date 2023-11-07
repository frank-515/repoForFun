use std::error::Error;
use std::fs;
use std::path::{Path, PathBuf};
use std::fs::File;
use std::io::Read;

use serde::{Deserialize, Serialize};
use crate::config;

#[derive(Debug, Deserialize, Clone, Serialize)]
pub struct Config {
    pub server_addr: String,
    pub server_port: String,
    pub tmp_dir: String,
    pub current_version: String,
    pub execution: String
}

impl Config {
    fn build(path: PathBuf) -> Result<Self, Box<dyn Error>> {
        let current_dir = std::env::current_dir()?;
        let mut config_path;

        if path.is_absolute() {
            config_path = PathBuf::from(path);
        } else {
            config_path = current_dir.join(path)
        }

        let mut config_file = File::open(config_path)?;
        let mut contents = String::new();
        config_file.read_to_string(&mut contents)?;

        let data: Config = serde_json::from_str(&contents)?;
        Ok(data)
    }

    pub fn get_ipv4port(&self) -> String {
        format!("{}:{}", self.server_addr, self.server_port)
    }
    pub fn set_server_addr(&mut self, server_addr: String) {
        self.server_addr = server_addr;
        self.write_to_file();
    }

    pub fn set_server_port(&mut self, server_port: String) {
        self.server_port = server_port;
        self.write_to_file();
    }

    pub fn set_tmp_dir(&mut self, tmp_dir: String) {
        self.tmp_dir = tmp_dir;
        self.write_to_file();
    }

    pub fn set_current_version(&mut self, current_version: String) {
        self.current_version = current_version;
        self.write_to_file();
    }

    pub fn set_execution(&mut self, execution: String) {
        self.execution = execution;
        self.write_to_file();
    }

    fn write_to_file(&self) {
        let current_dir = std::env::current_dir().expect("Unable to get current directory");
        let config_path = current_dir.join("config.json");

        // Serialize the configuration to JSON string
        let contents = serde_json::to_string(&self).expect("Failed to serialize configuration");

        // Write the contents to the config file
        fs::write(&config_path, &contents).expect("Failed to write configuration to file");
    }
}

impl Default for Config {
    fn default() -> Self {
        Config::build(Path::new("").join("config.json")).expect("Unable to find default config.")
    }
}


#[cfg(test)]
mod tests {
    use std::path::Path;
    use crate::config::Config;

    #[test]
    fn get_config_json() {

        let result = Config::build(Path::new("").join("config.json"));
        assert!(result.is_ok());
        let config = result.unwrap();
        assert_eq!(config.server_port.as_str(), "3000");
        assert_eq!(config.server_addr.as_str(), "localhost");
    }

    #[test]
    fn get_ipv4_port() {
        let result = Config::build(Path::new("").join("config.json")).unwrap();
        assert_eq!(result.get_ipv4port(), String::from("localhost:3000"))
    }

    #[test]
    fn set_version() {
        let mut result = Config::default();
        result.set_execution(String::from("a"));
    }
}