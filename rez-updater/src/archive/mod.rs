

use std::path::{Path, PathBuf};

use zip::ZipArchive;
use crate::fs::TempFolder;


struct Decoder {
    temp: TempFolder
}

impl Default for Decoder {
    fn default() -> Self {
        Decoder {
            temp: TempFolder::default()
        }
    }
}
impl Decoder {
    // TODO: add more archive type
    async fn extract_zip<P: AsRef<Path>>(&self, path: P) -> zip::result::ZipResult<()> {
        let path = path.as_ref();
        let file = self.temp.get_file(&path).await.unwrap().into_std().await;
        let mut archive = ZipArchive::new(file)?;
        let target_dir = path.parent().unwrap().join(path.file_stem().unwrap());
        self.temp.create_directory(&target_dir).unwrap();
        archive.extract(self.temp.tmp_path(&target_dir))
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    #[tokio::test]
    async fn foo() {
        let decoder = Decoder::default();
        decoder.extract_zip("./ui.zip").await.unwrap();
    }
}