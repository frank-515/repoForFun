use std::error::Error;
use std::path::{Path, PathBuf};
use std::fs::File;
use std::io::Read;

use serde::{Deserialize, Serialize};
use crate::config;

#[derive(Debug, Deserialize, Clone)]
pub struct Config {
    pub server_addr: String,
    pub server_port: String,
    pub tmp_dir: String
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
}