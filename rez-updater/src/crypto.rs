use std::fs::File;
use std::io::Read;
use std::path::PathBuf;
use sha2::{Sha256, Digest}; // 使用 crate `sha2`

#[derive(Debug, Clone)]
pub struct Sha256Calculator {
    data: Vec<u8>,
}

impl Sha256Calculator {
    // 从文件创建一个 Sha256Calculator
    pub fn from_file(path: &str) -> Result<Self, std::io::Error> {
        let native_path = PathBuf::from(path);
        let mut file = File::open(native_path.to_str().unwrap())?;
        let mut buffer = vec![];
        file.read_to_end(&mut buffer)?;
        Ok(Sha256Calculator { data: buffer })
    }

    // 从数据创建一个 Sha256Calculator
    pub fn from_data<T: AsRef<[u8]>>(data: T) -> Self {
        Sha256Calculator { data: data.as_ref().iter().cloned().collect() }
    }



    // 计算 SHA256 哈希值
    pub fn calculate_hash(&self) -> String {
        let mut hasher = Sha256::new();
        hasher.update(&self.data);
        let result = hasher.finalize();
        format!("{:x}", result)
    }
}
#[cfg(test)]
mod tests {
    use std::path::PathBuf;
    use super::*;
    #[test]
    fn calc_hash_from_string() {
        let digest = Sha256Calculator::from_data("frank515").calculate_hash();
        assert_eq!(digest.as_str(), "376f5f11f154c8c15cfb9dc57278a66686f653fefc3722cd3db795cca785dd5e")
    }

    #[test]
    fn calc_hash_from_file() {
        let digest = Sha256Calculator::from_file("./Cargo.toml").unwrap().calculate_hash();
        assert_ne!(digest.len(), 0);
        assert_ne!(digest.as_str(), "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        //Which is the sha256 of "" in UTF-8
    }

}