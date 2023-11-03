slint::include_modules!();
mod client;
mod config;
mod crypto;
mod fs;
mod ui;
#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    Ok(())
}

