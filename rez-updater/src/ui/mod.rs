use std::cell::RefCell;
use std::sync::Arc;
use slint::SharedString;
slint::include_modules!();
struct UI {
    slint_ui: Arc::<RefCell<Demo>>,
}

impl UI {
    fn init() -> Result<Self, slint::PlatformError> {
        let ui = UI {
            slint_ui: Arc::new(RefCell::new(Demo::new()?)),
        };

        ui.slint_ui.borrow().set_current_page(SharedString::from("UpdateDialog"));

        let ui_handle = Arc::downgrade(&ui.slint_ui);
        ui.slint_ui.borrow().on_confirm_update(move || {
            let ui = ui_handle.upgrade().unwrap();
            ui.borrow().set_current_page(SharedString::from("Downloading"));
        });

        let ui_handle = Arc::downgrade(&ui.slint_ui);
        ui.slint_ui.borrow().on_refuse_update(move || {
            let ui = ui_handle.upgrade().unwrap();
            ui.borrow().hide().unwrap();
        });
        Ok(ui)
    }

    fn run(&self) -> Result<(), slint::PlatformError>{
        self.slint_ui.borrow().run()?;
        Ok(())
    }
}
#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn foo() {
        let ui = UI::init().unwrap();
        ui.run().unwrap();
    }
}