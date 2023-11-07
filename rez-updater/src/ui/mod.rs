use std::cell::RefCell;
use std::sync::{Arc, Weak};
use slint::SharedString;
slint::include_modules!();


pub struct UI {
    pub slint_ui: Arc::<RefCell<Demo>>,

}

impl UI {
    pub fn init() -> Result<Self, slint::PlatformError> {
        let ui = UI {
            slint_ui: Arc::new(RefCell::new(Demo::new()?)),
        };

        ui.slint_ui.borrow().set_current_page(SharedString::from("Checking"));

        let ui_handle = Arc::downgrade(&ui.slint_ui);
        ui.slint_ui.borrow().on_confirm_update(move || {
            let ui = ui_handle.upgrade().unwrap();
            ui.borrow().set_current_page(SharedString::from("Downloading"));
        });

        let ui_handle = Arc::downgrade(&ui.slint_ui);
        ui.slint_ui.borrow().on_exit(move || {
            let ui = ui_handle.upgrade().unwrap();
            ui.borrow().hide().unwrap();
        });
        Ok(ui)
    }

    pub fn run(&self) -> Result<(), slint::PlatformError>{
        self.slint_ui.borrow().run()?;
        Ok(())
    }

    pub fn display_error<S: AsRef<str>>(&self, message: S) {
        let ui = self.slint_ui.borrow();
        ui.set_error_message(SharedString::from(message.as_ref()));
        ui.set_current_page(SharedString::from("Error"));
    }

    pub fn display_confirm_update_dialog(&self) {
        let ui = self.slint_ui.borrow();
        ui.set_current_page(SharedString::from("UpdateDialog"));
    }

    pub fn display_fetch_metadata(&self) {
        let ui = self.slint_ui.borrow();
        ui.set_current_page(SharedString::from("FetchMetadata"));
    }

    pub fn display_download_progress(&self, percentage: f32) {
        let ui = self.slint_ui.borrow();
        if ui.get_current_page().as_str() != "Downloading" {
            ui.set_current_page(SharedString::from("Downloading"));
        }
        ui.set_download_percentage(percentage);
    }

    pub fn display_install(&self) {
        let ui = self.slint_ui.borrow();
        ui.set_current_page(SharedString::from("Installing"));
    }

    pub fn display_finished(&self) {
        let ui = self.slint_ui.borrow();
        ui.set_current_page(SharedString::from("Finished"));
    }
}
#[cfg(test)]
mod tests {
    use std::thread;
    use super::*;

    #[test]
    fn ui_logic() {
        let ui = UI::init().unwrap();
        let ui_handle = ui.slint_ui.borrow().as_weak();
        let thread = thread::spawn(move || {
            let ui = ui_handle.clone();
            slint::invoke_from_event_loop(move || ui.unwrap().set_current_page("Finished".into())).unwrap();
        });
        ui.run().unwrap();
    }

    // #[tokio::test]
    // async fn ui_logic_tokio() {
    //     let ui = UI::init().unwrap();
    //     let slint = ui.slint_ui.as_ptr();
    //     let thread = tokio::spawn(async {
    //         unsafe {
    //             let ui = &*slint.clone();
    //             slint::invoke_from_event_loop(move || ui.set_current_page("Finished".into())).unwrap();
    //         }
    //     });
    //     ui.run().unwrap();
    // }
}