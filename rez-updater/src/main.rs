use reqwest::Client;

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let client = Client::new();
    let response = client.get("https://localhost/get").send().await?;
    println!("{}", response.text().await?);
    Ok(())
}
