use std::error::Error;
use std::fs;
use std::path::{Path, PathBuf};
use tokio::fs::File;
use tokio::io::{AsyncReadExt, AsyncWriteExt};
use crate::config::Config;

#[derive(Debug, Clone)]
pub struct TempFolder {
    base_url: String,
}

impl Default for TempFolder {
    fn default() -> Self {
        let config = Config::default();
        TempFolder {
            base_url: config.tmp_dir
        }
    }
}
impl TempFolder {

    pub fn exist<P: AsRef<Path>>(&self, path: P) -> bool {
        let path = path.as_ref();
        PathBuf::from(&self.base_url).join(path).exists()
    }

    pub async fn get_file<P: AsRef<Path>>(&self, path_: P) -> Result<File, Box<dyn Error>> {
        let path = path_.as_ref();
        let mut full_path = PathBuf::from(&self.base_url);
        // 处理根目录情况
        if path == Path::new("/") || path == Path::new("\\") {
            return Err("Invalid path: Root path not supported".into());
        }

        // 处理Windows路径的情况
        let path_str = path.to_string_lossy();
        if path_str.contains('\\') {
            let path_components: Vec<&str> = path_str.split('\\').collect();
            for component in path_components {
                full_path = full_path.join(component);
            }
        } else {
            // 处理其他情况的路径
            let path_components: Vec<&str> = path_str.split('/').collect();
            for component in path_components {
                full_path = full_path.join(component);
            }
        }
        // 如果full_path是目录则返回错误
        if full_path.is_dir() {
            return Err("Invalid path: Cannot open directory as a file".into());
        }

        // 继续实现FileManager::open_file函数并返回结果
        FileManager::open_file(&full_path.to_str().unwrap()).await
    }

    pub async fn put<P: AsRef<Path>>(&self, path: P, data: &[u8]) -> Result<(), Box<dyn Error>> {
        let mut file = self.get_file(path).await?;
        file.write_all(data).await?;
        Ok(())
    }

    pub async fn get<P: AsRef<Path>>(&self, path: P) -> Result<Box<[u8]>, Box<dyn Error>> {
        let mut file = self.get_file(path).await?;

        let mut data = Vec::new();
        file.read_to_end(&mut data).await?;

        Ok(data.into_boxed_slice())
    }

    pub async fn remove<P: AsRef<Path>>(&self, path: P) -> Result<(), Box<dyn Error>> {
        Self::delete_path_recursively(PathBuf::from(&self.base_url).join(path))?;
        Ok(())
    }

    fn delete_path_recursively<P: AsRef<Path>>(path: P) -> Result<(), Box<dyn Error>> {
        let path = path.as_ref();
        if path.exists() {
            if path.is_file() {
                std::fs::remove_file(path)?;
            } else if path.is_dir() {
                std::fs::remove_dir_all(path).unwrap();
                // // 删除路径下的所有文件和文件夹
                // for entry in std::fs::read_dir(path)? {
                //     let entry = entry?;
                //     let entry_path = entry.path();
                //
                //     // 递归删除子文件夹或文件
                //     Self::delete_path_recursively(&entry_path)?;
                // }
                //
                // // 删除文件夹本身
                // std::fs::remove_dir(path)?;
            }
        }

        if path != Path::new("./") {
            Self::remove_empty_dir_recursively(path)?;
        }

        Ok(())
    }

    fn remove_empty_dir_recursively<P : AsRef<Path>>(path_: P) -> Result<(), Box<dyn Error>> {
        let path = path_.as_ref();
        if path == Path::new("./") {
            return Ok(());
        }
        // 检查父文件夹为空并递归删除
        if let Some(parent) = path.parent() {
            if parent.exists() && parent.is_dir() {
                if parent.read_dir()?.next().is_none() {
                    std::fs::remove_dir(parent)?;
                    Self::remove_empty_dir_recursively(parent)?;
                }
            }
        }
        Ok(())
    }

    pub async fn get_as_string<P: AsRef<Path>>(&self, path: P) -> Result<String, Box<dyn Error>> {
        let mut file = self.get_file(path).await?;
        let mut buf = String::new();
        file.read_to_string(&mut buf).await?;
        Ok(buf)
    }
}

struct FileManager;
impl FileManager {
    async fn open_file<T: AsRef<[u8]> + std::convert::AsRef<std::ffi::OsStr>>(path: T) -> Result<File, Box<dyn Error>> {
        let path_buf = PathBuf::from(&path);

        let parent_dir = path_buf.parent().ok_or("Invalid path")?;
        if !parent_dir.exists() {
            fs::create_dir_all(&parent_dir)?;
        }
        let file = fs::OpenOptions::new()
            .read(true)
            .write(true)
            .create(true)
            .open(path_buf).unwrap();
        Ok(tokio::fs::File::from_std(file))
    }

    async fn open_exist_file<T: AsRef<[u8]> + std::convert::AsRef<std::ffi::OsStr>>(path: T) -> Result<File, Box<dyn Error>> {
        let path_buf = PathBuf::from(&path);
        let file = fs::OpenOptions::new()
            .read(true)
            .write(true)
            .open(path_buf)?;
        Ok(tokio::fs::File::from_std(file))
    }
}

#[cfg(test)]
mod tests {
    use std::io::SeekFrom;
    use tokio::io::{AsyncReadExt, AsyncSeekExt, AsyncWriteExt};
    use super::*;

    #[tokio::test]
    async fn write_and_read() {
        let mut buf = String::new();

        let mut file = TempFolder::default().get_file(PathBuf::from("/a.txt").as_path()).await.unwrap();
        file.write_all("frank515s".as_bytes()).await.unwrap();
        file.seek(SeekFrom::Start(0)).await.unwrap();
        file.read_to_string(&mut buf).await.unwrap();
        assert_eq!(buf.as_str(), "frank515s");
    }

    #[tokio::test]
    async fn put_and_get() {
        let temp = TempFolder::default();
        temp.put("b.txt", "data".as_bytes()).await.unwrap();
        let data = temp.get(".\\b.txt").await.unwrap();
        assert_eq!(data.as_ref(), "data".as_bytes());
    }

    #[tokio::test]
    async fn read_as_string() {
        let temp = TempFolder::default();
        temp.put("b.txt", "data".as_bytes()).await.unwrap();
        let data = temp.get_as_string(".\\b.txt").await.unwrap();
        // assert_eq!(data.as_ref(), String::from("data"));
        assert_eq!(data.as_str(), "data");
    }

    #[tokio::test]
    async fn delete_dir() {
        let temp = TempFolder::default();
        temp.put("./a/a/a/a.txt", "data".as_bytes()).await.unwrap();
        temp.put("./b/a/a/a.txt", "data".as_bytes()).await.unwrap();
        temp.put("./b/a/a/b.txt", "data".as_bytes()).await.unwrap();
        temp.remove("a/a\\a\\a.txt").await.unwrap();
        temp.remove("./a").await.unwrap();
        assert_eq!(temp.exist("./a"), false);
        assert_eq!(temp.exist("./b/a/a/a.txt"), true);
        temp.remove("./b/a/a/a.txt").await.unwrap();
    }

    #[tokio::test]
    async fn absolute_path() {
        let temp = TempFolder::default();
        temp.put("/a.txt", "frank515".as_bytes()).await.unwrap();
        assert!(temp.exist("a.txt"));
    }

}
