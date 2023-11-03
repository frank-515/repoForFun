
use slint::SharedString;
slint::include_modules!();
pub fn display_ui() {
    let demo_ui = Demo::new().unwrap();

    // let ui_handle = demo_ui.as_weak();
    // demo_ui.on_onBack(move || {
    //     let ui = ui_handle.unwrap();
    //     let title = ui.get_title_str();
    //     ui.set_title_str(SharedString::from("ss"));
    // });
    demo_ui.run().unwrap();
}
#[cfg(test)]
mod tests {
    use crate::ui::display_ui;

    #[test]
    fn foo() {
        display_ui();
    }
}